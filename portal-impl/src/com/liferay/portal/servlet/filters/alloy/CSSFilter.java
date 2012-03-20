/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet.filters.alloy;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletContextUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.portal.kernel.util.*;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.taglib.util.AlloyModulesLoaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @author Iliyan Peychev
 * @author Miguel Pastor
 */
public class CSSFilter extends BasePortalFilter {

	public static final String SKIP_FILTER =
		CSSFilter.class.getName() + "SKIP_FILTER";

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (isAlreadyFiltered(request)) {
			return false;
		}
		else {
			return true;
		}
	}

	protected boolean isAlreadyFiltered(HttpServletRequest request) {
		if (request.getAttribute(SKIP_FILTER) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		request.setAttribute(SKIP_FILTER, Boolean.TRUE);

		if (_log.isDebugEnabled()) {
			String completeURL = HttpUtil.getCompleteURL(request);

			_log.debug("Executing AlloyUI CSS filter on " + completeURL);
		}

		StringServletResponse stringServerResponse = new StringServletResponse(
			response);

		processFilter(
			CSSFilter.class, request, stringServerResponse, filterChain);

		String contentType = response.getContentType();

		if ((contentType != null) &&
			contentType.startsWith(ContentTypes.TEXT_HTML)) {

			String content = stringServerResponse.getString();

			Set<String> expandedUseSet = (Set<String>)request.getAttribute(
				WebKeys.EXPANDED_CSS_USAGES);

			List<String> requirements =
				AlloyModulesLoaderUtil.resolveRequirements(expandedUseSet);

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			ServletContext servletContext =
				(ServletContext)request.getAttribute(WebKeys.CTX);

			long javaScriptLastModified = ServletContextUtil.getLastModified(
				servletContext, "/html/js/", true);
			
			String comboURL = PortalUtil.getStaticResourceURL(
				request, themeDisplay.getCDNDynamicResourcesHost() +
				themeDisplay.getPathContext() + "/combo/", "minifierType=css",
				javaScriptLastModified);
			
			String auiPath = themeDisplay.getPathContext() + "/html/js/aui/";
			
			String componentsURL = AlloyModulesLoaderUtil.getURL(
				comboURL, auiPath, "m", null, requirements, "css", true);

			String url = StringPool.BLANK;

			if (!componentsURL.isEmpty()) {
				url = HtmlUtil.escapeAttribute(PortalUtil.getStaticResourceURL(
					request, componentsURL));

				url = "<link href=\"" + url + "\"" +
					" rel=\"stylesheet\" type=\"text/css\" />";
			}

			content = content.replaceFirst(
				WebKeys.MARKER_CSS_COMPONENTS_URL, url);

			ServletResponseUtil.write(response, content);
		}
		else {
			ServletResponseUtil.write(response, stringServerResponse);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(CSSFilter.class);

}