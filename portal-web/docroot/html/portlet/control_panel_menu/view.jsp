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

<%@ include file="/html/portlet/control_panel_menu/init.jsp" %>

<%
	String type = ParamUtil.getString(request, "type", "contentPanel");
	int length = ParamUtil.getInteger(request, "intLength", 19);

	String sitesMenu = "";

	String ppid = GetterUtil.getString((String)request.getAttribute("control_panel.jsp-ppid"), layoutTypePortlet.getStateMaxPortletId());

	String category = PortalUtil.getControlPanelCategory(ppid, themeDisplay);

	String[] allCategories = PortletCategoryKeys.ALL;

	String controlPanelCategory = HttpUtil.getParameter(PortalUtil.getCurrentURL(request), "controlPanelCategory", false);

	if (Validator.isNotNull(controlPanelCategory)) {
		allCategories = new String[] {controlPanelCategory};
	}
%>

<h1 class="user-greeting">
	<liferay-ui:message key="control-panel" />
</h1>

<div class="portal-add-content">
	<div class="control-panel-tools">
		<aui:input cssClass="search-panels" inputCssClass="search-panels-input" label="" name="searchPanel" />
	</div>

	<liferay-ui:panel-container extended="<%= true %>" id="controlPanelMenuAddContentPanelContainer" persistState="<%= true %>">
		<%
			type = "contentPanel";
			length = 19;
		%>

		<%@ include file="/html/portlet/control_panel_menu/view_add_content.jspf" %>
	</liferay-ui:panel-container>
</div>

<aui:select cssClass="select-navigation-wrapper" inputCssClass="select-navigation-input" label="" name="selectNavigation" onChange="location.href=this.value;">
	<%
		type = "selectNavigation";
		length = 21;
	%>

	<%@ include file="/html/portlet/control_panel_menu/view_add_content.jspf" %>
</aui:select>

<aui:select cssClass="select-site-wrapper" inputCssClass="select-site-input" label="" name="siteSelect" onChange="location.href=this.value;">
	<%= sitesMenu %>
</aui:select>

<span class="select-site-arrow"></span>

<aui:script use="liferay-panel">
	var trigger = A.one('#<portlet:namespace />groupSelector a');

	var panelContainer = Liferay.Panel.get('controlPanelMenuAddContentPanelContainer');

	if (trigger && panelContainer) {
		trigger.on(
			'click',
			function(event) {
				panelContainer.once(
					'collapse',
					function(event) {
						event.preventDefault();
					}
				);
			}
		);
	}
</aui:script>