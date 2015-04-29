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

<%@ include file="/html/portlet/language/init.jsp" %>

<%
Locale[] locales = LocaleUtil.fromLanguageIds(languageIds);

String portletId = portletDisplay.getId();
%>

<liferay-ui:ddm-template-renderer displayStyle="<%= displayStyle %>" displayStyleGroupId="<%= displayStyleGroupId %>" entries="<%= ListUtil.fromArray(locales) %>">
	<liferay-ui:language displayCurrentLocale="<%= displayCurrentLocale %>" displayStyle="<%= displayStyle %>" languageIds="<%= languageIds %>" />
</liferay-ui:ddm-template-renderer>

<aui:script>
	require('html/js/liferay/address', function(main) {
		main.log('Hello World!');

		var portletBody = $('#p_p_id_<%= portletId %>_ .portlet-body');

		portletBody.before('<div><strong>Hello World from a ES6 module!</strong></div>');
	}, function(error) {
		console.error(error);
	});
</aui:script>
