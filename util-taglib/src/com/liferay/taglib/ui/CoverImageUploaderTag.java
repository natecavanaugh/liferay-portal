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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.taglib.aui.FormTag;
import com.liferay.taglib.util.IncludeTag;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergio González
 */
public class CoverImageUploaderTag extends IncludeTag {

	public void setCoverImageId(long coverImageId) {
		_coverImageId = coverImageId;
	}

	@Override
	protected void cleanUp() {
		_coverImageId = 0;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		String formName = StringPool.BLANK;

		FormTag formTag = (FormTag)findAncestorWithClass(this, FormTag.class);

		if (formTag != null) {
			formName = formTag.getName();
		}

		request.setAttribute(
			"liferay-ui:cover-image-uploader:coverImageId",
			String.valueOf(_coverImageId));
		request.setAttribute(
			"liferay-ui:cover-image-uploader:formName", formName);
	}

	private static final String _PAGE =
		"/html/taglib/ui/cover_image_uploader/page.jsp";

	private long _coverImageId;

}