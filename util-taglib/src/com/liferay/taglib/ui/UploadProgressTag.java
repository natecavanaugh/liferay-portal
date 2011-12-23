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

package com.liferay.taglib.ui;

import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Keith R. Davis
 */
public class UploadProgressTag extends IncludeTag {

	public void setId(String id) {
		_id = id;
	}

	public void setIframeSrc(String iframeSrc) {
		_iframeSrc = iframeSrc;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public void setRedirect(String redirect) {
		_redirect = redirect;
	}

	@Override
	protected void cleanUp() {
		_id = null;
		_iframeSrc = null;
		_message = null;
		_redirect = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		request.setAttribute("liferay-ui:upload-progress:id", _id);
		request.setAttribute(
			"liferay-ui:upload-progress:iframe-src", _iframeSrc);
		request.setAttribute("liferay-ui:upload-progress:message", _message);
		request.setAttribute("liferay-ui:upload-progress:redirect", _redirect);
	}

	private static final String _PAGE =
		"/html/taglib/ui/upload_progress/page.jsp";

	private String _id;
	private String _iframeSrc;
	private String _message;
	private String _redirect;

}