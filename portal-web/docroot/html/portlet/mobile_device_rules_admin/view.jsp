<%--
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
--%>

<%@ include file="/html/portlet/mobile_device_rules_admin/init.jsp" %>

<%
String chooseCallback = ParamUtil.getString(request, "chooseCallback");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("struts_action", "/mobile_device_rules_admin/view");
portletURL.setParameter("chooseCallback", chooseCallback);
portletURL.setParameter("groupId", String.valueOf(groupId));

request.setAttribute("view.jsp-portletURL", portletURL);

pageContext.setAttribute("portletURL", portletURL);

String portletURLString = portletURL.toString();
%>

<c:if test="<%= Validator.isNotNull(chooseCallback) %>">
	<liferay-ui:header
		title="rule-groups"
	/>
</c:if>

<liferay-portlet:renderURL varImpl="addURL">
	<portlet:param name="struts_action" value="/mobile_device_rules_admin/edit_rule_group"/>
	<portlet:param name="groupId" value="<%= String.valueOf(groupId) %>"/>
	<portlet:param name="redirect" value="<%= currentURL %>"/>
</liferay-portlet:renderURL>

<liferay-util:include page="/html/portlet/mobile_device_rules_admin/toolbar.jsp">
	<liferay-util:param name="toolbarItem" value="view"/>
	<liferay-util:param name="addURL" value="<%=addURL.toString()%>"/>
</liferay-util:include>

<%
RuleGroupSearch ruleGroupSearch = null;

if (renderRequest != null) {
	ruleGroupSearch = new RuleGroupSearch(renderRequest, portletURL);
}
else {
	ruleGroupSearch = new RuleGroupSearch(resourceRequest, portletURL);
}

%>
<aui:form action="<%= portletURLString %>" method="post" name="fm">

	<liferay-ui:search-container searchContainer="<%= ruleGroupSearch %>">

		<%
		RuleGroupDisplayTerms displayTerms = (RuleGroupDisplayTerms) searchContainer.getDisplayTerms();
		RuleGroupSearchTerms searchTerms = (RuleGroupSearchTerms) searchContainer.getSearchTerms();
		%>

		<liferay-ui:search-form page="/html/portlet/mobile_device_rules_admin/device_rule_group_search.jsp" />

		<liferay-ui:search-container-results>
			<%@ include file="/html/portlet/mobile_device_rules_admin/device_rule_group_search_results.jspf" %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
				className="com.liferay.portlet.mobiledevicerules.model.MDRRuleGroup"
				escapedModel="<%= true %>"
				keyProperty="ruleGroupId"
				modelVar="ruleGroup"
		>
			<%
			String rowURL;

			if (Validator.isNotNull(chooseCallback)) {
				StringBundler sb = new StringBundler(7);

				sb.append("javascript:Liferay.Util.getOpener().");
				sb.append(chooseCallback);
				sb.append("('");
				sb.append(ruleGroup.getRuleGroupId());
				sb.append("', Liferay.Util.getWindow());");

				rowURL = sb.toString();
			}
			else {
			%>
				<liferay-portlet:renderURL varImpl="editURL">
					<portlet:param name="struts_action" value="/mobile_device_rules_admin/edit_rule_group"/>
					<portlet:param name="redirect" value="<%= portletURLString %>"/>
					<portlet:param name="ruleGroupId" value="<%= String.valueOf(ruleGroup.getRuleGroupId()) %>"/>
				</liferay-portlet:renderURL>
			<%
				rowURL = editURL.toString();
			}
			%>

			<%@ include file="/html/portlet/mobile_device_rules_admin/device_rule_group_columns.jspf" %>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator type="more"/>
	</liferay-ui:search-container>
</aui:form>