<%@ page import="com.liferay.portlet.trash.util.TrashUtil" %>
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

<%@ include file="/html/taglib/init.jsp" %>

<%
String portletURL = (String)request.getAttribute("liferay-ui:trash-empty:portletURL");

String emptyMessage = (String)request.getAttribute("liferay-ui:trash-empty:emptyMessage");
String confirmMessage = (String)request.getAttribute("liferay-ui:trash-empty:confirmMessage");

int totalEntries = GetterUtil.getInteger(request.getAttribute("liferay-ui:trash-empty:totalEntries"));

Group group = themeDisplay.getScopeGroup();
%>

<aui:form action="<%= portletURL %>" cssClass="trash-empty-form" method="post" name="emptyForm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.EMPTY_TRASH %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
</aui:form>

<div class="lfr-message-info">
	<liferay-ui:message arguments="<%= TrashUtil.getMaxAge(group) %>" key="entries-that-have-been-in-recycle-bin-for-more-than-x-days-will-be-automatically-deleted" />

	<c:if test="<%= totalEntries > 0 %>">
		<a href="javascript:;" onClick="<portlet:namespace />emptyTrash();"><liferay-ui:message key="<%= emptyMessage %>" /></a>
	</c:if>
</div>

<aui:script use="aui-base">
	Liferay.provide(
		window,
		'<portlet:namespace />emptyTrash',
		function() {
			if (confirm('<%= UnicodeLanguageUtil.get(pageContext, confirmMessage) %>')) {
				submitForm(document.<portlet:namespace />emptyForm);
			}
		}
	);
</aui:script>