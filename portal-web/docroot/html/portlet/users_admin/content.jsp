<%--
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
--%>

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "browse");

String viewUsersRedirect = ParamUtil.getString(request, "viewUsersRedirect");

int status = ParamUtil.getInteger(request, "status", WorkflowConstants.STATUS_APPROVED);

String backURL = ParamUtil.getString(request, "backURL", viewUsersRedirect);

String usersListView = ParamUtil.get(request, "usersListView", UserConstants.LIST_VIEW_TREE);

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/users_admin/view_users");
portletURL.setParameter("usersListView", usersListView);

if (Validator.isNotNull(viewUsersRedirect)) {
	portletURL.setParameter("viewUsersRedirect", viewUsersRedirect);
}

String portletURLString = portletURL.toString();
%>

<liferay-ui:error exception="<%= CompanyMaxUsersException.class %>" message="unable-to-activate-user-because-that-would-exceed-the-maximum-number-of-users-allowed" />
<liferay-ui:error exception="<%= RequiredOrganizationException.class %>" message="you-cannot-delete-organizations-that-have-suborganizations-or-users" />
<liferay-ui:error exception="<%= RequiredUserException.class %>" message="you-cannot-delete-or-deactivate-yourself" />

<aui:form action="<%= portletURLString %>" method="get" name="fm">
	<liferay-portlet:renderURLParams varImpl="portletURL" />
	<aui:input name="<%= Constants.CMD %>" type="hidden" />
	<aui:input name="toolbarItem" type="hidden" value="<%= toolbarItem %>" />
	<aui:input name="redirect" type="hidden" value="<%= portletURLString %>" />

	<%
	long organizationGroupId = 0;

	int inactiveUsersCount = 0;
	int usersCount = 0;
	%>

	<c:if test="<%= portletName.equals(PortletKeys.USERS_ADMIN) %>">
		<liferay-util:include page="/html/portlet/users_admin/toolbar.jsp" />

		<c:if test="<%= usersListView.equals(UserConstants.LIST_VIEW_FLAT_ORGANIZATIONS) || usersListView.equals(UserConstants.LIST_VIEW_FLAT_USERS) %>">
			<portlet:renderURL var="headerBackURL">
				<portlet:param name="struts_action" value="/users_admin/view_users" />
			</portlet:renderURL>

			<liferay-ui:header
				backLabel="users-and-organizations-home"
				backURL="<%= headerBackURL.toString() %>"
				title='<%= usersListView.equals(UserConstants.LIST_VIEW_FLAT_ORGANIZATIONS) ? "organizations" : "users" %>'
			/>
		</c:if>
	</c:if>

	<c:choose>
		<c:when test="<%= usersListView.equals(UserConstants.LIST_VIEW_FLAT_ORGANIZATIONS) %>">
			<%@ include file="/html/portlet/users_admin/view_flat_organizations.jspf" %>
		</c:when>
		<c:when test="<%= usersListView.equals(UserConstants.LIST_VIEW_FLAT_USERS) %>">

			<%
			boolean organizationContextView = false;
			%>

			<%@ include file="/html/portlet/users_admin/view_flat_users.jspf" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/html/portlet/users_admin/view_tree.jspf" %>
		</c:otherwise>
	</c:choose>
</aui:form>