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
import javax.servlet.jsp.tagext.BodyTag;

/**
 * @author Brian Wing Shun Chan
 */
public class SuccessTag extends IncludeTag implements BodyTag {

	@Override
	public int doStartTag() {
		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		request.setAttribute("liferay-ui:success:key", _key);
		request.setAttribute("liferay-ui:success:message", _message);
		request.setAttribute(
			"liferay-ui:success:translateMessage",
			String.valueOf(_translateMessage));

		return EVAL_BODY_BUFFERED;
	}

	public void setKey(String key) {
		_key = key;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public void setTranslateMessage(boolean translateMessage) {
		_translateMessage = translateMessage;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	private static final String _PAGE = "/html/taglib/ui/success/page.jsp";

	private String _key;
	private String _message;
	private boolean _translateMessage = true;

}