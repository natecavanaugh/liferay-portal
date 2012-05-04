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

<%@ include file="/html/portlet/trash/init.jsp" %>

<div class="asset-content">

	<%
	long entryId = ParamUtil.getLong(request, "entryId");

	TrashEntry trashEntry = TrashEntryLocalServiceUtil.getEntry(entryId);

	TrashHandler trashHandler = TrashHandlerRegistryUtil.getTrashHandler(trashEntry.getClassName());

	TrashRenderer trashRenderer = trashHandler.getTrashRenderer(trashEntry.getClassPK());

	String template = null;

	if (trashRenderer instanceof AssetRenderer) {
		template = AssetRenderer.TEMPLATE_FULL_CONTENT;
	}
	else {
		template = TrashRenderer.TEMPLATE_DEFAULT;
	}

	String path = trashRenderer.render(renderRequest, renderResponse, template);
	String redirect = ParamUtil.getString(request, "redirect");
	String title = trashRenderer.getTitle(locale);
	%>

	<c:if test="<%= !(trashRenderer instanceof AssetRenderer) %>">
		<liferay-ui:header
			backURL="<%= redirect %>"
			localizeTitle="<%= false %>"
			title="<%= title %>"
		/>
	</c:if>

	<c:if test="<%= Validator.isNotNull(path) %>">
		<liferay-util:include page="<%= path %>" portletId="<%= trashRenderer.getPortletId() %>" />
	</c:if>
</div>