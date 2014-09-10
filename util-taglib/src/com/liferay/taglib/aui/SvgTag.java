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

package com.liferay.taglib.aui;

import com.liferay.portal.kernel.servlet.taglib.aui.SvgData;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.Node;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import com.liferay.portal.kernel.xml.XPath;
import com.liferay.taglib.aui.base.BaseSvgTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * @author Eduardo Lundgren
 * @author Bruno Basto
 * @author Nathan Cavanaugh
 * @author Julio Camarero
 */
public class SvgTag extends BaseSvgTag {

	public static void flushSvgData(PageContext pageContext)
		throws Exception {

		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		SvgData svgData = (SvgData)request.getAttribute(WebKeys.SVG_DATA);

		if (svgData == null) {
			return;
		}

		request.removeAttribute(WebKeys.SVG_DATA);

		svgData.writeTo(request, pageContext.getOut());
	}

	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request =
			(HttpServletRequest)pageContext.getRequest();

		JspWriter jspWriter = pageContext.getOut();

		try {
			if (getUseReference()) {
				if (Validator.isNotNull(_file)) {
					_file = HttpUtil.URLtoString(_file);
					Document document = SAXReaderUtil.read(_file);

					if (Validator.isNull(_element)) {
						_element = _getElementId();
					}

					if (Validator.isNull(_viewBox)) {
						_viewBox = _getViewBox();
					}

					SvgData svgData = (SvgData)request.getAttribute(WebKeys.SVG_DATA);

					if (svgData == null) {
						svgData = new SvgData();

						request.setAttribute(WebKeys.SVG_DATA, svgData);
					}

					svgData.append(_file);
				}

				jspWriter.write("<svg class=\"taglib-svg");

				if (Validator.isNotNull(_cssClass)) {
					jspWriter.write(" ");
					jspWriter.write(_cssClass);
				}

				if (Validator.isNotNull(_id)) {
					jspWriter.write("\" id=\"");
					jspWriter.write(_id);
				}

				if (Validator.isNotNull(_viewBox)) {
					jspWriter.write("\" viewBox=\"");
					jspWriter.write(_viewBox);
				}

				jspWriter.write("\">");
				jspWriter.write("<use xlink:href=\"#");
				jspWriter.write(_element);
				jspWriter.write("\">");
				jspWriter.write("</svg>");
			}
			else {
				if (Validator.isNotNull(_file)) {
					_file = HttpUtil.URLtoString(_file);
					jspWriter.write(_file);
				}
			}

			return EVAL_PAGE;
		}
		catch (Exception e) {
			throw new JspException(e);
		}
		finally {
			if (!ServerDetector.isResin()) {
				super.cleanUp();

				_document = null;
			}
		}
	}

	@Override
	public int doStartTag() throws JspException {
		_cssClass = getCssClass();
		_element = getElement();
		_file = getFile();
		_id = getId();
		_viewBox = getViewBox();

		return super.doStartTag();
	}

	private String _getElementId() throws Exception {
		XPath xPath = SAXReaderUtil.createXPath("//*[@id]");

		_document = (_document == null) ? SAXReaderUtil.read(_file) : _document;
		Node node = xPath.selectSingleNode(_document);

		if (node == null) {
			return "";
		}

		return node.valueOf("@id");
	}

	private String _getViewBox() throws Exception {
		XPath xPath = SAXReaderUtil.createXPath("//*[@viewBox]");
		
		_document = (_document == null) ? SAXReaderUtil.read(_file) : _document;
		Node node = xPath.selectSingleNode(_document);

		if (node == null) {
			return "";
		}

		return node.valueOf("@viewBox");
	}

	private Document _document = null;
	private String _cssClass = null;
	private String _element = null;
	private String _file = null;
	private String _id = null;
	private String _viewBox = null;
}