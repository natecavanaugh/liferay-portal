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

<%@ include file="/html/taglib/aui/fieldset/init.jsp" %>

<div <%= Validator.isNotNull(label) ? "aria-labelledby=\"" + labelId + "\"" : StringPool.BLANK %> class="fieldset <%= collapsible ? "panel panel-default" : StringPool.BLANK %> <%= cssClass %>" <%= Validator.isNotNull(id) ? "id=\"" + namespace + id + "\"" : StringPool.BLANK %> <%= InlineUtil.buildDynamicAttributes(dynamicAttributes) %> <%= Validator.isNotNull(label) ? "role=\"group\"" : StringPool.BLANK %>>
	<c:if test="<%= Validator.isNotNull(label) %>">
		<liferay-util:buffer var="header">
			<liferay-ui:message key="<%= label %>" localizeKey="<%= localizeLabel %>" />

			<c:if test="<%= Validator.isNotNull(helpMessage) %>">
				<liferay-ui:icon-help message="<%= helpMessage %>" />
			</c:if>
		</liferay-util:buffer>

		<c:choose>
			<c:when test="<%= collapsible %>">
				<div class="fieldset-legend legend panel-heading" id="<%= labelId %>">
					<div class="panel-title">
						<a aria-controls="collapseOne" aria-expanded="<%= !collapsed %>" class="<%= !collapsed ? "collapsed" : StringPool.BLANK %>" data-toggle="collapse" href="#<%= panelId %>" role="button">
							<%= header %>
						</a>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div class="fieldset-legend legend" id="<%= labelId %>">
					<span class="legend-text">
						<%= header %>
					</span>
				</div>
			</c:otherwise>
		</c:choose>
	</c:if>

	<div class="<%= !collapsed ? "in" : StringPool.BLANK %> <%= collapsible ? "panel-collapse collapse" : StringPool.BLANK %> <%= column ? "row" : StringPool.BLANK %>" id="<%= panelId %>">