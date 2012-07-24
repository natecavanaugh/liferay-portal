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

<div class="document-display-style display-icon aui-helper-hidden" data-draggable="true" data-title="{title}" id="invisible_icon">
	<liferay-util:include page="/html/portlet/document_library/invisible_entry_action.jsp" />

	<a class="document-link" data-folder="<%= Boolean.FALSE.toString() %>" href="<%= tempRowURL.toString() %>" title="{title}">
		<span class="document-thumbnail">
			<img alt="" border="no" src="<%= thumbnailSrc %>" style="<%= thumbnailStyle %>" />
		</span>

		<span class="entry-title">
			{title}
		</span>
	</a>
</div>