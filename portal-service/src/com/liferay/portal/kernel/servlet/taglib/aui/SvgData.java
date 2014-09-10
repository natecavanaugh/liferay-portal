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

package com.liferay.portal.kernel.servlet.taglib.aui;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

public class SvgData implements Serializable {

	public void append(String data) {
		_svgSet.add(data);
	}

	public void writeTo(HttpServletRequest request, Writer writer)
		throws IOException {

		writer.write("<svg class=\"svg-sprite\"");
		writer.write("display=\"none\">");
		
		for (String svg : _svgSet) {
			writer.write(svg);
		}

		writer.write("</svg>");
	}

	private static final long serialVersionUID = 1L;

	private Set<String> _svgSet = new TreeSet<String>();
}