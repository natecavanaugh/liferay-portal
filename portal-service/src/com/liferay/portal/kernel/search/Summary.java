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

package com.liferay.portal.kernel.search;

import javax.portlet.PortletURL;

/**
 * @author Brian Wing Shun Chan
 * @author Ryan Park
 */
public class Summary {

	public Summary(String title, String content, PortletURL portletURL) {
		_title = title;
		_content = content;
		_portletURL = portletURL;
	}

	public String getContent() {
		return _content;
	}

	public PortletURL getPortletURL() {
		return _portletURL;
	}

	public String getTitle() {
		return _title;
	}

	public void setContent(String content) {
		_content = content;
	}

	public void setPortletURL(PortletURL portletURL) {
		_portletURL = portletURL;
	}

	public void setTitle(String title) {
		_title = title;
	}

	private String _content;
	private PortletURL _portletURL;
	private String _title;

}