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
List<String> editorProperties = new ArrayList(EditorUtil.getAvailableEditorProperties().stringPropertyNames());

ListUtil.sort(editorProperties);
%>

<h3><liferay-ui:message key="preferred-editors" /></h3>

<aui:fieldset>

	<%
	for (String editorProperty : editorProperties) {
		if (StringUtil.endsWith(editorProperty, ".available")) {
			continue;
		}

		String systemDefaultEditor = PropsUtil.get(editorProperty);
		String selectedEditor = EditorUtil.getUserEditorValue(editorProperty, user);

		StringBuilder labelStringBuilder = new StringBuilder();
		StringBuilder defaultOptionLabelStringBuilder = new StringBuilder();

		List<String> langKeys = ListUtil.fromString(StringUtil.replace(editorProperty, _OLD_STRINGS, _REPLACEMENTS), StringPool.PERIOD);

		if (langKeys.size() > 1) {
			Collections.swap(langKeys, 0, 1);
		}

		for (String langKey : langKeys) {
			labelStringBuilder.append(LanguageUtil.get(request, langKey));
			labelStringBuilder.append(StringPool.SPACE);
		}

		labelStringBuilder.append(LanguageUtil.get(request, "editor"));

		defaultOptionLabelStringBuilder.append(LanguageUtil.get(request, systemDefaultEditor));
		defaultOptionLabelStringBuilder.append(StringPool.SPACE);
		defaultOptionLabelStringBuilder.append(StringPool.OPEN_PARENTHESIS);
		defaultOptionLabelStringBuilder.append(LanguageUtil.get(request, "system-default"));
		defaultOptionLabelStringBuilder.append(StringPool.CLOSE_PARENTHESIS);
	%>

		<aui:select label="<%= labelStringBuilder.toString() %>" name="<%= CamelCaseUtil.toCamelCase(editorProperty, _DELIMITERS) %>">

			<aui:option label="<%= defaultOptionLabelStringBuilder.toString() %>" selected='<%= selectedEditor.equals("default") || selectedEditor == null %>' value="default" />

			<%
			for (String editor : EditorUtil.getAvailableEditors(editorProperty + ".available")) {
			%>

				<c:if test="<%= !editor.equals(systemDefaultEditor) %>">
					<aui:option label="<%= editor %>" selected="<%= editor.equals(selectedEditor) %>" value="<%= editor %>" />
				</c:if>

			<%
			}
			%>

		</aui:select>

	<%
	}
	%>

</aui:fieldset>

<%!
private static final char[] _DELIMITERS = {CharPool.UNDERLINE, CharPool.DASH, CharPool.PERIOD};
private static final String[] _OLD_STRINGS = {"editor.wysiwyg.", "portal-web.docroot.html.", ".jsp", StringPool.UNDERLINE};
private static final String[] _REPLACEMENTS = {StringPool.BLANK, StringPool.BLANK, StringPool.BLANK, StringPool.DASH};
%>