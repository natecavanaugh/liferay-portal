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

<%@ include file="/html/taglib/ui/panel_container/init.jsp" %>

</div>

<aui:script use="aui-toggler">
	new A.TogglerDelegate({
		animated: true,
		container: '#<%= id %>',
		content: '.aui-toggler-content',
		header: '.aui-toggler-header',
		on: {
			'toggler:expandedChange': function(event) {
				var header = event.target.get('header');

				var persistId = header.getData('persist-id');

				if (persistId) {
					var data = {};

					data[persistId] = event.newVal ? "open" : "closed";

					Liferay.Store(data);
				}
			}
		},
		transition: {
			duration: .3,
			easing: 'cubic-bezier'
		}
	});
</aui:script>