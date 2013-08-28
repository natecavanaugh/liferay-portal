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

<%@ include file="/html/portlet/trash/init.jsp" %>

<aui:nav-bar>
	<div class="navbar-search pull-right">
		<div class="form-search">
			<div class="input-append">
				<input autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>" class="search-query span9" id="<portlet:namespace/>keywords" name="<portlet:namespace/>keywords" placeholder="<liferay-ui:message key="keywords" />" type="text" />

				<aui:button primary="<%= false %>" type="submit" value="search" />
			</div>
		</div>
	</div>
</aui:nav-bar>