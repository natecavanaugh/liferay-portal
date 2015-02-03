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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
Properties editorProperties = EditorUtil.getAvailableEditorProperties();
%>

<h3><liferay-ui:message key="preferred-editor-settings" /></h3>

<aui:fieldset>

	<%
	for (String property : editorProperties.stringPropertyNames()) {
		if (!StringUtil.endsWith(property, ".available")) {
			continue;
		}

		String[] availableEditors = EditorUtil.getAvailableEditors(property);

		property = property.replace(".available", "");

		String selectedEditor = EditorUtil.getEditorValue(request, property, user);

		String camelizedProperty = CamelCaseUtil.toCamelCase(property.replace("-", "").replace("_",""), CharPool.PERIOD);
	%>

		<aui:select label="<%= camelizedProperty %>" name="<%= camelizedProperty %>">

			<aui:option label="default" selected='<%= selectedEditor.equals("default") || selectedEditor == null %>' value="default" />

			<%
			for (String editor : availableEditors) {
			%>

				<aui:option label="<%= editor %>" selected="<%= editor.equals(selectedEditor) %>" value="<%= editor %>" />

			<%
			}
			%>

		</aui:select>

	<%
	}

	%>
</aui:fieldset>