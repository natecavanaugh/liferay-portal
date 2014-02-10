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

<%@ include file="/html/portlet/dictionary/init.jsp" %>

<aui:nav-bar>
	<aui:form method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "getDefinition();" %>'>
		<aui:nav-bar-search>
			<div class="form-search">
				<liferay-ui:input-search name="word" autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>" placeholder='<%= LanguageUtil.get(locale, "Word") %>' />
			</div>

			<aui:select label="" name="type">
				<aui:option label="dictionary" value="http://dictionary.reference.com/search?q=" />
				<aui:option label="thesaurus" value="http://thesaurus.reference.com/search?q=" />
			</aui:select>
		</aui:nav-bar-search>
	</aui:form>
</aui:nav-bar>

<aui:script>
	function <portlet:namespace />getDefinition() {
		window.open(document.<portlet:namespace />fm.<portlet:namespace />type[document.<portlet:namespace />fm.<portlet:namespace />type.selectedIndex].value + encodeURIComponent(document.<portlet:namespace />fm.<portlet:namespace />word.value));
	}
</aui:script>
