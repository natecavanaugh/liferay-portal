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

package com.liferay.portal.kernel.servlet.taglib.ui;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.image.ImageBag;
import com.liferay.portal.kernel.image.ImageToolUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.portletfilerepository.PortletFileRepositoryUtil;

import java.awt.image.RenderedImage;

/**
 * @author Sergio González
 */
public class CoverImage {

	public CoverImage(long coverImageId, String coverImageCropRegion) {
		_coverImageId = coverImageId;

		_coverImageCropRegion = coverImageCropRegion;
	}

	public byte[] getBytes() throws Exception {
		if ((_coverImageId == 0) || Validator.isNull(_coverImageCropRegion)) {
			return null;
		}

		FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
			_coverImageId);

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
			_coverImageCropRegion);

		int height = jsonObject.getInt("height");
		int width = jsonObject.getInt("width");
		int x = jsonObject.getInt("x");
		int y = jsonObject.getInt("y");

		if ((x > 0) || (y > 0) || (width > 0) || (height > 0)) {
			ImageBag imageBag = ImageToolUtil.read(
				fileEntry.getContentStream());

			RenderedImage renderedImage = imageBag.getRenderedImage();

			renderedImage = ImageToolUtil.crop(
				renderedImage, height, width, x, y);

			return ImageToolUtil.getBytes(renderedImage, imageBag.getType());
		}

		return null;
	}

	public String getCoverImageCropRegion() {
		return _coverImageCropRegion;
	}

	public long getCoverImageId() {
		return _coverImageId;
	}

	public String getMimeType() throws PortalException {
		FileEntry fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(
			_coverImageId);

		return fileEntry.getMimeType();
	}

	public void setCoverImageCropRegion(String coverImageCropRegion) {
		_coverImageCropRegion = coverImageCropRegion;
	}

	public void setCoverImageId(long coverImageId) {
		_coverImageId = coverImageId;
	}

	private String _coverImageCropRegion;
	private long _coverImageId;

}