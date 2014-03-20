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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * @author Eduardo Lundgren
 * @author Bruno Basto
 * @author Nathan Cavanaugh
 * @author Julio Camarero
 * @generated
 */
public class BaseButtonRowTag extends com.liferay.taglib.util.IncludeTag {

	@Override
	public int doStartTag() throws JspException {
		setAttributeNamespace(_ATTRIBUTE_NAMESPACE);

		return super.doStartTag();
	}

	public java.lang.String getCollapseLabel() {
		return _collapseLabel;
	}

	public boolean getCollapsible() {
		return _collapsible;
	}

	public java.lang.String getCssClass() {
		return _cssClass;
	}

	public java.lang.String getId() {
		return _id;
	}

	public void setCollapseLabel(java.lang.String collapseLabel) {
		_collapseLabel = collapseLabel;

		setScopedAttribute("collapseLabel", collapseLabel);
	}

	public void setCollapsible(boolean collapsible) {
		_collapsible = collapsible;

		setScopedAttribute("collapsible", collapsible);
	}

	public void setCssClass(java.lang.String cssClass) {
		_cssClass = cssClass;

		setScopedAttribute("cssClass", cssClass);
	}

	public void setId(java.lang.String id) {
		_id = id;

		setScopedAttribute("id", id);
	}

	@Override
	protected void cleanUp() {
		_collapseLabel = "actions";
		_collapsible = false;
		_cssClass = null;
		_id = null;
	}

	@Override
	protected String getEndPage() {
		return _END_PAGE;
	}

	@Override
	protected String getStartPage() {
		return _START_PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest request) {
		setNamespacedAttribute(request, "collapseLabel", _collapseLabel);
		setNamespacedAttribute(request, "collapsible", _collapsible);
		setNamespacedAttribute(request, "cssClass", _cssClass);
		setNamespacedAttribute(request, "id", _id);
	}

	protected static final String _ATTRIBUTE_NAMESPACE = "aui:button-row:";

	private static final String _END_PAGE =
		"/html/taglib/aui/button_row/end.jsp";

	private static final String _START_PAGE =
		"/html/taglib/aui/button_row/start.jsp";

	private java.lang.String _collapseLabel = "actions";
	private boolean _collapsible = false;
	private java.lang.String _cssClass = null;
	private java.lang.String _id = null;

}