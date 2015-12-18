<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1");
String tabs2 = ParamUtil.getString(request, "tabs2", "users");

String redirect = ParamUtil.getString(request, "redirect");

long passwordPolicyId = ParamUtil.getLong(request, "passwordPolicyId");

PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil.fetchPasswordPolicy(passwordPolicyId);

String displayStyle = ParamUtil.getString(request, "displayStyle", "list");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcPath", "/edit_password_policy_assignments.jsp");
portletURL.setParameter("tabs1", tabs1);
portletURL.setParameter("redirect", redirect);
portletURL.setParameter("passwordPolicyId", String.valueOf(passwordPolicy.getPasswordPolicyId()));

PortalUtil.addPortletBreadcrumbEntry(request, passwordPolicy.getName(), null);
PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "assign-members"), portletURL.toString());

portletURL.setParameter("tabs2", tabs2);

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, tabs2), portletURL.toString());

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(passwordPolicy.getName());

SearchContainer searchContainer = new SearchContainer();

if (tabs2.equals("users")) {
	searchContainer = new UserSearch(renderRequest, portletURL);
}
else if (tabs2.equals("organizations")) {
	searchContainer = new OrganizationSearch(renderRequest, portletURL);
}
%>

<aui:nav-bar cssClass="collapse-basic-search" markupView="lexicon">
	<aui:nav cssClass="navbar-nav">

		<%
		PortletURL usersURL = PortletURLUtil.clone(portletURL, renderResponse);

		usersURL.setParameter("tabs2", "users");
		%>

		<aui:nav-item href="<%= usersURL.toString() %>" label="users" selected='<%= tabs2.equals("users") %>' />

		<%
		PortletURL userGroupsURL = PortletURLUtil.clone(portletURL, renderResponse);

		userGroupsURL.setParameter("tabs2", "organizations");
		%>

		<aui:nav-item href="<%= userGroupsURL.toString() %>" label="organizations" selected='<%= tabs2.equals("organizations") %>' />
	</aui:nav>

	<aui:nav-bar-search>
		<aui:form action="<%= portletURL.toString() %>" name="searchFm">
			<liferay-ui:input-search markupView="lexicon" />
		</aui:form>
	</aui:nav-bar-search>
</aui:nav-bar>

<liferay-frontend:management-bar
	includeCheckBox="<%= true %>"
	searchContainerId="passwordPolicyMembers"
>
	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-navigation
			navigationKeys='<%= new String[] {"all"} %>'
			portletURL="<%= renderResponse.createRenderURL() %>"
		/>
	</liferay-frontend:management-bar-filters>

	<liferay-frontend:management-bar-buttons>
		<liferay-frontend:management-bar-display-buttons
			displayViews='<%= new String[] {"list"} %>'
			portletURL="<%= renderResponse.createRenderURL() %>"
			selectedDisplayStyle="<%= displayStyle %>"
		/>
	</liferay-frontend:management-bar-buttons>

	<liferay-frontend:management-bar-filters>
		<liferay-frontend:management-bar-sort
			orderByCol="<%= searchContainer.getOrderByCol() %>"
			orderByType="<%= searchContainer.getOrderByType() %>"
			orderColumns='<%= new String[] {"name"} %>'
			portletURL="<%= portletURL %>"
		/>
	</liferay-frontend:management-bar-filters>
</liferay-frontend:management-bar>

<portlet:actionURL name="editPasswordPolicyAssignments" var="editPasswordPolicyAssignmentsURL" />

<aui:form action="<%= editPasswordPolicyAssignmentsURL %>" cssClass="container-fluid-1280" method="post" name="fm">
	<aui:input name="tabs1" type="hidden" value="<%= tabs1 %>" />
	<aui:input name="tabs2" type="hidden" value="<%= tabs2 %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="passwordPolicyId" type="hidden" value="<%= String.valueOf(passwordPolicy.getPasswordPolicyId()) %>" />

	<c:choose>
		<c:when test='<%= tabs2.equals("users") %>'>
			<aui:input name="addUserIds" type="hidden" />
			<aui:input name="removeUserIds" type="hidden" />

			<liferay-ui:search-container
				id="passwordPolicyMembers"
				rowChecker="<%= new UserPasswordPolicyChecker(renderResponse, passwordPolicy) %>"
				searchContainer="<%= searchContainer %>"
				var="userSearchContainer"
			>

				<%
				UserSearchTerms searchTerms = (UserSearchTerms)userSearchContainer.getSearchTerms();

				LinkedHashMap<String, Object> userParams = new LinkedHashMap<String, Object>();

				userParams.put("usersPasswordPolicies", Long.valueOf(passwordPolicy.getPasswordPolicyId()));
				%>

				<%@ include file="/user_search_columns.jspf" %>

				<liferay-ui:search-iterator markupView="lexicon" />
			</liferay-ui:search-container>
		</c:when>
		<c:when test='<%= tabs2.equals("organizations") %>'>
			<aui:input name="addOrganizationIds" type="hidden" />
			<aui:input name="removeOrganizationIds" type="hidden" />

			<liferay-ui:search-container
				id="passwordPolicyMembers"
				rowChecker="<%= new OrganizationPasswordPolicyChecker(renderResponse, passwordPolicy) %>"
				searchContainer="<%= searchContainer %>"
				var="organizationSearchContainer"
			>

				<%
				OrganizationSearchTerms searchTerms = (OrganizationSearchTerms)organizationSearchContainer.getSearchTerms();

				LinkedHashMap<String, Object> organizationParams = new LinkedHashMap<String, Object>();

				organizationParams.put("organizationsPasswordPolicies", Long.valueOf(passwordPolicy.getPasswordPolicyId()));
				%>

				<%@ include file="/organization_search_columns.jspf" %>

				<liferay-ui:search-iterator markupView="lexicon" />
			</liferay-ui:search-container>
		</c:when>
	</c:choose>
</aui:form>

<liferay-frontend:add-menu>
	<liferay-frontend:add-menu-item id="addMembers" title='<%= LanguageUtil.get(request, "add-members") %>' url="javascript:;" />
</liferay-frontend:add-menu>

<aui:script use="liferay-item-selector-dialog">
	var Util = Liferay.Util;

	var form = $(document.<portlet:namespace />fm);

	<portlet:renderURL var="selectMembersURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcPath" value="/select_members.jsp" />
		<portlet:param name="tabs1" value="<%= tabs1 %>" />
		<portlet:param name="tabs2" value="<%= tabs2 %>" />
		<portlet:param name="passwordPolicyId" value="<%= String.valueOf(passwordPolicyId) %>" />
	</portlet:renderURL>

	$('#<portlet:namespace />addMembers').on(
		'click',
		function(event) {
			event.preventDefault();

			var itemSelectorDialog = new A.LiferayItemSelectorDialog(
				{
					eventName: '<portlet:namespace />selectMember',
					on: {
						selectedItemChange: function(event) {
							var result = event.newVal;

							if (result && result.item) {
								if (result.memberType == 'users') {
									form.fm('addUserIds').val(result.item);
								}
								else if (result.memberType == 'organizations') {
									form.fm('addOrganizationIds').val(result.item);
								}

								submitForm(form);
							}
						}
					},
					title: '<liferay-ui:message arguments="<%= passwordPolicy.getName() %>" key="add-members-to-x" />',
					url: '<%= selectMembersURL %>'
				}
			);

			itemSelectorDialog.open();
		}
	);
</aui:script>