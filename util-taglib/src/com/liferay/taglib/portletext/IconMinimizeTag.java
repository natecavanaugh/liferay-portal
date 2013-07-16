/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.taglib.portletext;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.taglib.FileAvailabilityUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import com.liferay.taglib.ui.IconTag;

import javax.portlet.PortletRequest;
import javax.portlet.WindowStateException;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class IconMinimizeTag extends IconTag {

	@Override
	protected String getPage() {
		if (FileAvailabilityUtil.isAvailable(servletContext, _PAGE)) {
			return _PAGE;
		}

		PortletDisplay portletDisplay =
			(PortletDisplay)pageContext.getAttribute("portletDisplay");

		if (!portletDisplay.isShowMinIcon()) {
			return null;
		}

		setCssClass("portlet-minimize portlet-minimize-icon");

		String image = null;

		if (portletDisplay.isStateMin()) {
			image = "restore";
		}
		else {
			image = "minimize";
		}

		setImage("../portlet/".concat(image));
		setMessage(image);

		String minimizeURL = createMinimizeURL(portletDisplay.getId());

		StringBundler onClickSB = new StringBundler(5);
		onClickSB.append("Liferay.Portlet.minimize('#p_p_id_");
		onClickSB.append(HtmlUtil.escapeJS(portletDisplay.getId()));
		onClickSB.append("_', this, {'minimizeURL':'");
		onClickSB.append(HtmlUtil.escapeJS(minimizeURL));
		onClickSB.append("'}); return false;");

		setOnClick(onClickSB.toString());

		setToolTip(false);
		setUrl(portletDisplay.getURLMin());

		return super.getPage();
	}

	private String createMinimizeURL(String portletId) {
		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			request, portletId, themeDisplay.getPlid(),
			PortletRequest.RENDER_PHASE);

		liferayPortletURL.setRenderPortletURL(true);

		try {
			liferayPortletURL.setWindowState(LiferayWindowState.EXCLUSIVE);
		}
		catch (WindowStateException e) {
			if (_log.isDebugEnabled()) {
				_log.debug("Unable to set EXCLUSIVE state", e);
			}
		}

		return liferayPortletURL.toString();
	}

	private static final String _PAGE =
		"/html/taglib/portlet/icon_minimize/page.jsp";

	private static Log _log = LogFactoryUtil.getLog(IconMinimizeTag.class);

}