<%--
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
--%>

<%@ include file="/html/portlet/journal/init.jsp" %>

<%
String keywords = ParamUtil.getString(request, "keywords");

String search = ParamUtil.getString(request, ArticleDisplayTerms.ADVANCED_SEARCH, null);

long folderId = GetterUtil.getLong((String)request.getAttribute("view.jsp-folderId"));

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/journal/view");
portletURL.setParameter("folderId", String.valueOf(folderId));
%>

<div class="lfr-search-combobox search-button-container" id="<portlet:namespace />articlesSearchContainer">
	<aui:form action="<%= portletURL.toString() %>" method="post" name="fm1">
		<aui:layout>
			<aui:column>
				<aui:input cssClass="keywords lfr-search-combobox-item" id="keywords" label="" name="keywords" type="text" value="<%= keywords %>" />
			</aui:column>

			<aui:column>
				<aui:button cssClass="search lfr-search-combobox-item" name="search" type="submit" value="search" />
			</aui:column>

			<aui:column cssClass="advanced-search-column">

				<%
				String taglibOnClick = renderResponse.getNamespace() + "openAdvancedSearch();";
				%>

				<aui:button cssClass="lfr-search-combobox-item article-advanced-search-icon" inputCssClass="<%= (search != null) ? "close-advanced-search" : "" %>" name="showAdvancedSearch" onClick="<%= taglibOnClick %>" type="button" />
			</aui:column>
		</aui:layout>
	</aui:form>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />openAdvancedSearch',
		function() {
			var A = AUI();

			var advancedSearch = A.one('#<portlet:namespace/>advancedSearch');

			if (advancedSearch) {
				var showAdvancedSearch = A.one('#<portlet:namespace/>showAdvancedSearch');

				if (showAdvancedSearch.hasClass('close-advanced-search')) {
					showAdvancedSearch.removeClass('close-advanced-search');

					advancedSearch.hide();
				}
				else {
					showAdvancedSearch.addClass('close-advanced-search');

					advancedSearch.show();
				}
			}
		}
	);
</aui:script>