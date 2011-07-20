/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.servlet.taglib.FileAvailabilityUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.taglib.util.PositionTagSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

/**
 * @author Scott Lee
 * @author Shuyang Zhou
 */
public class IconHelpTag extends IconTag {

	@Override
	protected String getPage() {
		if (FileAvailabilityUtil.isAvailable(getServletContext(), _PAGE)) {
			return _PAGE;
		}
		else {
			return null;
		}
	}

	@Override
	protected int processEndTag() throws Exception {
		HttpServletRequest request = getServletRequest();

		ThemeDisplay themeDisplay = (ThemeDisplay)pageContext.getAttribute(
			"themeDisplay");

		JspWriter jspWriter = pageContext.getOut();

		boolean jsLoaded = GetterUtil.getBoolean(
			(String)request.getAttribute("taglib-icon-help"));

		if (!jsLoaded) {
			StringBundler script = new StringBundler();

			script.append("<script type=\"text/javascript\">");
			script.append("Liferay.Portal.ToolTip.addShowListeners();");
			script.append("</script>");

			String position = PositionTagSupport.getPosition(
				request, StringPool.BLANK);

			if (position.equals(PositionTagSupport.POSITION_INLINE)) {
				jspWriter.write(script.toString());
			}
			else {
				StringBundler sb = (StringBundler)request.getAttribute(
					WebKeys.PAGE_TOP);

				if (sb == null) {
					sb = new StringBundler();

					request.setAttribute(WebKeys.PAGE_TOP, sb);
				}

				sb.append(script.toString());
			}

			request.setAttribute("taglib-icon-help", String.valueOf(true));

		}

		jspWriter.write("<span class=\"taglib-icon-help\">");
		jspWriter.write("<img tabindex=\"0\" alt=\"\" ");
		jspWriter.write("class=\"tooltip-target\" ");
		jspWriter.write("src=\"");
		jspWriter.write(themeDisplay.getPathThemeImages());
		jspWriter.write("/portlet/help.png\" />");
		jspWriter.write("<span class=\"aui-helper-hidden-accessible ");
		jspWriter.write("tooltip-container\" >");
		jspWriter.write(LanguageUtil.get(pageContext, getMessage()));
		jspWriter.write("</span></span>");

		return EVAL_PAGE;
	}

	private static final String _PAGE = "/html/taglib/ui/icon_help/page.jsp";

}