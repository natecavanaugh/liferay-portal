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

package com.liferay.taglib.aui.base;

import javax.servlet.jsp.JspException;

/**
 * @author Eduardo Lundgren
 * @author Bruno Basto
 * @author Nathan Cavanaugh
 * @author Julio Camarero
 * @generated
 */
public class BaseSvgTag extends com.liferay.taglib.util.IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public java.lang.String getCssClass() {
		return _cssClass;
	}

	public java.lang.String getElement() {
		return _element;
	}

	public java.lang.String getFile() {
		return _file;
	}

	public java.lang.String getId() {
		return _id;
	}

	public boolean getUseReference() {
		return _useReference;
	}

	public java.lang.String getViewBox() {
		return _viewBox;
	}

	public void setCssClass(java.lang.String cssClass) {
		_cssClass = cssClass;

		setScopedAttribute("cssClass", cssClass);
	}

	public void setElement(java.lang.String element) {
		_element = element;

		setScopedAttribute("element", element);
	}

	public void setFile(java.lang.String file) {
		_file = file;

		setScopedAttribute("file", file);
	}

	public void setId(java.lang.String id) {
		_id = id;

		setScopedAttribute("id", id);
	}

	public void setUseReference(boolean useReference) {
		_useReference = useReference;

		setScopedAttribute("useReference", useReference);
	}

	public void setViewBox(java.lang.String viewBox) {
		_viewBox = viewBox;

		setScopedAttribute("viewBox", viewBox);
	}

	@Override
	protected void cleanUp() {
		_cssClass = null;
		_element = null;
		_file = null;
		_id = null;
		_useReference = true;
		_viewBox = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	protected static final String _ATTRIBUTE_NAMESPACE = "aui:svg:";

	private static final String _PAGE =
		"/html/taglib/aui/svg/page.jsp";

	private java.lang.String _cssClass = null;
	private java.lang.String _element = null;
	private java.lang.String _file = null;
	private java.lang.String _id = null;
	private boolean _useReference = true;
	private java.lang.String _viewBox = null;

}