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

package com.liferay.blogs.toolbar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributor;
import com.liferay.portal.kernel.portlet.toolbar.contributor.PortletToolbarContributorTemplateRenderer;
import com.liferay.portal.kernel.servlet.taglib.ui.MenuItem;
import com.liferay.portal.kernel.servlet.taglib.ui.URLMenuItem;
import com.liferay.portal.kernel.util.StreamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.security.permission.ResourcePermissionChecker;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.portlet.blogs.BlogsPortletInstanceSettings;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Sergio González
 */
@Component(property = {"javax.portlet.name=33", "struts.action=-"})
public class BlogsPortletToolbarContributor
	implements PortletToolbarContributor {

	@Override
	public List<MenuItem> getContentAdditionMenuItems(
		PortletRequest portletRequest) {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		if (!_resourcePermissionChecker.checkResource(
				themeDisplay.getPermissionChecker(),
				themeDisplay.getScopeGroupId(), ActionKeys.ADD_ENTRY)) {

			return Collections.<MenuItem>emptyList();
		}

		List<MenuItem> menuItems = new ArrayList<>();

		menuItems.add(getAddEntryMenuItem(portletRequest, themeDisplay));

		return menuItems;
	}

	@Override
	public String getHTML(
			PortletRequest portletRequest, PortletResponse portletResponse)
		throws PortalException {

		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String script = _get(getClass(), "dependencies/blogs.ftl");

		Map<String, Object> contextObjects = new HashMap<>();

		PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();

		BlogsPortletInstanceSettings blogsPortletInstanceSettings =
			BlogsPortletInstanceSettings.getInstance(
				themeDisplay.getLayout(), portletDisplay.getPortletName());

		contextObjects.put(
			"blogsPortletInstanceSettings", blogsPortletInstanceSettings);

		return _portletToolbarContributorTemplateRenderer.render(
			portletRequest, portletResponse, script, contextObjects);
	}

	protected URLMenuItem getAddEntryMenuItem(
		PortletRequest portletRequest, ThemeDisplay themeDisplay) {

		URLMenuItem urlMenuItem = new URLMenuItem();

		urlMenuItem.setLabel(
			LanguageUtil.get(themeDisplay.getLocale(), "add-entry"));

		PortletURL portletURL = PortletURLFactoryUtil.create(
			portletRequest, PortletKeys.BLOGS, themeDisplay.getPlid(),
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("struts_action", "/blogs/edit_entry");

		String currentURL = PortalUtil.getCurrentURL(portletRequest);

		portletURL.setParameter("redirect", currentURL);
		portletURL.setParameter("backURL", currentURL);

		urlMenuItem.setURL(portletURL.toString());

		return urlMenuItem;
	}

	@Reference(target = "(template.language=ftl)", unbind = "-")
	protected void setPortletToolbarContributorTemplateRenderer(
		PortletToolbarContributorTemplateRenderer
			portletToolbarContributorTemplateRenderer) {

		_portletToolbarContributorTemplateRenderer =
			portletToolbarContributorTemplateRenderer;
	}

	@Reference(
		target = "(resource.name=com.liferay.portlet.blogs)", unbind = "-"
	)
	protected void setResourcePermissionChecker(
		ResourcePermissionChecker resourcePermissionChecker) {

		_resourcePermissionChecker = resourcePermissionChecker;
	}

	private String _get(Class<?> clazz, String resource) {
		InputStream inputStream = null;

		Bundle bundle = FrameworkUtil.getBundle(clazz);

		Package pkg = clazz.getPackage();

		String packageName = pkg.getName();

		String path = packageName.replaceAll("\\.", "/") + "/" + resource;

		try {
			URL url = bundle.getEntry(path);

			inputStream = url.openStream();

			return StringUtil.read(inputStream);
		}
		catch (IOException ioe) {
			throw new SystemException(
				"Unable to read " + path + " in bundle " +
					bundle.getSymbolicName(),
				ioe);
		}
		finally {
			StreamUtil.cleanUp(inputStream);
		}
	}

	private PortletToolbarContributorTemplateRenderer
		_portletToolbarContributorTemplateRenderer;
	private ResourcePermissionChecker _resourcePermissionChecker;

}