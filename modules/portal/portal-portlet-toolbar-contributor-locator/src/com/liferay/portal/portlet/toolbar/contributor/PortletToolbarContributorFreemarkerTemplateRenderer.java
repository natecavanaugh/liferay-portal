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

package com.liferay.portal.portlet.toolbar.contributor;

import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributorTemplateRenderer;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateManager;
import com.liferay.portal.kernel.template.TemplateManagerUtil;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.portletdisplaytemplate.util.PortletDisplayTemplateConstants;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Sergio González
 */
@Component(property = {"template.language=ftl"})
public class PortletToolbarContributorFreemarkerTemplateRenderer
	implements PortletToolbarContributorTemplateRenderer {

	public String render(
		PortletRequest portletRequest, PortletResponse portletResponse,
		String script, Map<String, Object> contextObjects) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		TemplateResource templateResource = new StringTemplateResource(
			"portlet_toolbar_contributor", script);

		try {
			Template template = TemplateManagerUtil.getTemplate(
				TemplateConstants.LANG_TYPE_FTL, templateResource, true);

			HttpServletRequest request = PortalUtil.getHttpServletRequest(
				portletRequest);

			if (contextObjects == null) {
				contextObjects = new HashMap<>();
			}

			contextObjects.put("themeDisplay", themeDisplay);
			contextObjects.put("request", request);

			TemplateManager templateManager =
				TemplateManagerUtil.getTemplateManager(
					TemplateConstants.LANG_TYPE_FTL);

			// FreeMarker servlet application

			templateManager.addTaglibApplication(
				contextObjects,
				PortletDisplayTemplateConstants.FREEMARKER_SERVLET_APPLICATION,
				request.getServletContext());

			HttpServletResponse response = PortalUtil.getHttpServletResponse(
				portletResponse);

			// FreeMarker servlet request

			templateManager.addTaglibRequest(
				contextObjects,
				PortletDisplayTemplateConstants.FREEMARKER_SERVLET_REQUEST,
				request, response);

			// Taglib Liferay hash

			templateManager.addTaglibFactory(
				contextObjects,
				PortletDisplayTemplateConstants.TAGLIB_LIFERAY_HASH,
				request.getServletContext());

			if (contextObjects != null) {
				for (String key : contextObjects.keySet()) {
					template.put(key, contextObjects.get(key));
				}
			}

			UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

			template.processTemplate(unsyncStringWriter);

			return unsyncStringWriter.toString();
		}
		catch (TemplateException te) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to parse template " + script);
			}

			return StringPool.BLANK;
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletToolbarContributorFreemarkerTemplateRenderer.class);

}