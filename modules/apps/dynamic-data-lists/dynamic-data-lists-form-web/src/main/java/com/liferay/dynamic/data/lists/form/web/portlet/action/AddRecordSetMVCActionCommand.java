/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.dynamic.data.lists.form.web.portlet.action;

import com.liferay.dynamic.data.lists.form.web.constants.DDLFormPortletKeys;
import com.liferay.dynamic.data.lists.model.DDLRecordSet;
import com.liferay.dynamic.data.lists.model.DDLRecordSetConstants;
import com.liferay.dynamic.data.lists.service.DDLRecordSetService;
import com.liferay.dynamic.data.mapping.exception.StructureDefinitionException;
import com.liferay.dynamic.data.mapping.exception.StructureLayoutException;
import com.liferay.dynamic.data.mapping.io.DDMFormJSONDeserializer;
import com.liferay.dynamic.data.mapping.io.DDMFormLayoutJSONDeserializer;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormLayout;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureService;
import com.liferay.dynamic.data.mapping.storage.StorageType;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseTransactionalMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PropertiesParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Basto
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + DDLFormPortletKeys.DYNAMIC_DATA_LISTS_FORM_ADMIN,
		"mvc.command.name=addRecordSet"
	},
	service = MVCActionCommand.class
)
public class AddRecordSetMVCActionCommand
	extends BaseTransactionalMVCActionCommand {

	protected DDMStructure addDDMStructure(ActionRequest actionRequest)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		String structureKey = ParamUtil.getString(
			actionRequest, "structureKey");
		String storageType = ParamUtil.getString(
			actionRequest, "storageType", StorageType.JSON.toString());
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");
		DDMForm ddmForm = getDDMForm(actionRequest);
		DDMFormLayout ddmFormLayout = getDDMFormLayout(actionRequest);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDMStructure.class.getName(), actionRequest);

		return _ddmStructureService.addStructure(
			groupId, DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			PortalUtil.getClassNameId(DDLRecordSet.class), structureKey,
			getLocalizedMap(themeDisplay.getLocale(), name),
			getLocalizedMap(themeDisplay.getLocale(), description), ddmForm,
			ddmFormLayout, storageType, DDMStructureConstants.TYPE_AUTO,
			serviceContext);
	}

	protected DDLRecordSet addRecordSet(
			ActionRequest actionRequest, long ddmStructureId)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long groupId = ParamUtil.getLong(actionRequest, "groupId");
		String recordSetKey = ParamUtil.getString(
			actionRequest, "recordSetKey");
		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			DDLRecordSet.class.getName(), actionRequest);

		return _ddlRecordSetService.addRecordSet(
			groupId, ddmStructureId, recordSetKey,
			getLocalizedMap(themeDisplay.getLocale(), name),
			getLocalizedMap(themeDisplay.getLocale(), description),
			DDLRecordSetConstants.MIN_DISPLAY_ROWS_DEFAULT,
			DDLRecordSetConstants.SCOPE_FORMS, serviceContext);
	}

	@Override
	protected void doTransactionalCommand(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		DDMStructure ddmStructure = addDDMStructure(actionRequest);

		DDLRecordSet recordSet = addRecordSet(
			actionRequest, ddmStructure.getStructureId());

		boolean publish = GetterUtil.getBoolean(
			ParamUtil.getString(actionRequest, "publish"));

		if (publish) {
			String url = publish(
				actionRequest, actionResponse, recordSet.getRecordSetId());

			_ddlRecordSetService.updateRecordSet(
				recordSet.getRecordSetId(), url);
		}
	}

	protected DDMForm getDDMForm(ActionRequest actionRequest)
		throws PortalException {

		try {
			String definition = ParamUtil.getString(
				actionRequest, "definition");

			return _ddmFormJSONDeserializer.deserialize(definition);
		}
		catch (PortalException pe) {
			throw new StructureDefinitionException(pe);
		}
	}

	protected DDMFormLayout getDDMFormLayout(ActionRequest actionRequest)
		throws PortalException {

		try {
			String layout = ParamUtil.getString(actionRequest, "layout");

			return _ddmFormLayoutJSONDeserializer.deserialize(layout);
		}
		catch (PortalException pe) {
			throw new StructureLayoutException(pe);
		}
	}

	protected Map<Locale, String> getLocalizedMap(Locale locale, String value) {
		Map<Locale, String> localizedMap = new HashMap<>();

		localizedMap.put(locale, value);

		return localizedMap;
	}

	protected String publish(
			ActionRequest actionRequest, ActionResponse actionResponse,
			long recordSetId)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getSiteGroupId();

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			Layout.class.getName(), actionRequest);

		String name = ParamUtil.getString(actionRequest, "name");
		String description = ParamUtil.getString(actionRequest, "description");

		Map<Locale, String> friendlyURLMap =
			LocalizationUtil.getLocalizationMap(actionRequest, "friendlyURL");
		Map<Locale, String> titleMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "title");
		Map<Locale, String> keywordsMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "keywords");
		Map<Locale, String> robotsMap = LocalizationUtil.getLocalizationMap(
			actionRequest, "robots");

		UnicodeProperties typeSettingsProperties =
			PropertiesParamUtil.getProperties(
				actionRequest, "TypeSettingsProperties--");

		String portletId = typeSettingsProperties.getProperty(
			"singlePortletApplication");

		serviceContext.setAttribute("layout.instanceable.allowed", true);

		Layout layout = LayoutServiceUtil.addLayout(
			groupId, false, 0, getLocalizedMap(themeDisplay.getLocale(), name),
			titleMap, getLocalizedMap(themeDisplay.getLocale(), description),
			keywordsMap, robotsMap, "single_portlet_application",
			typeSettingsProperties.toString(), true, friendlyURLMap,
			serviceContext);

		LayoutTypePortlet layoutTypePortlet =
			(LayoutTypePortlet)layout.getLayoutType();

		layoutTypePortlet.setLayoutTemplateId(
			themeDisplay.getUserId(), PropsValues.DEFAULT_LAYOUT_TEMPLATE_ID);

		LayoutServiceUtil.updateLayout(
			groupId, false, layout.getLayoutId(), layout.getTypeSettings());

		storePortletPreferences(layout, portletId, recordSetId);

		StringBundler sb = new StringBundler(4);

		Group group = themeDisplay.getSiteGroup();

		sb.append(themeDisplay.getPortalURL());
		sb.append(group.getPathFriendlyURL(false, themeDisplay));
		sb.append(group.getFriendlyURL());
		sb.append(layout.getFriendlyURL());

		return sb.toString();
	}

	@Reference
	protected void setDDLRecordSetService(
		DDLRecordSetService ddlRecordSetService) {

		_ddlRecordSetService = ddlRecordSetService;
	}

	@Reference
	protected void setDDMFormJSONDeserializer(
		DDMFormJSONDeserializer ddmFormJSONDeserializer) {

		_ddmFormJSONDeserializer = ddmFormJSONDeserializer;
	}

	@Reference
	protected void setDDMFormLayoutJSONDeserializer(
		DDMFormLayoutJSONDeserializer ddmFormLayoutJSONDeserializer) {

		_ddmFormLayoutJSONDeserializer = ddmFormLayoutJSONDeserializer;
	}

	@Reference
	protected void setDDMStructureService(
		DDMStructureService ddmStructureService) {

		_ddmStructureService = ddmStructureService;
	}

	protected void storePortletPreferences(
			Layout layout, String portletId, long recordSetId)
		throws Exception {

		PortletPreferences portletPreferences =
			PortletPreferencesFactoryUtil.getStrictPortletSetup(
				layout, portletId);

		portletPreferences.setValue("recordSetId", String.valueOf(recordSetId));

		portletPreferences.store();
	}

	private DDLRecordSetService _ddlRecordSetService;
	private DDMFormJSONDeserializer _ddmFormJSONDeserializer;
	private DDMFormLayoutJSONDeserializer _ddmFormLayoutJSONDeserializer;
	private DDMStructureService _ddmStructureService;

}