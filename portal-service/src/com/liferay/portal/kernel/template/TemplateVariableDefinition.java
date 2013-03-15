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

package com.liferay.portal.kernel.template;

/**
 * @author Jorge Ferrer
 */
public class TemplateVariableDefinition {

	public TemplateVariableDefinition(
		String label, Class<?> clazz, String variableName) {

		_label = label;
		_clazz = clazz;
		_name = variableName;
	}

	public TemplateVariableDefinition(
		String label, Class<?> clazz, String variableName,
		TemplateVariableDefinition itemTemplateVariableDefinition) {

		this(label, clazz, variableName);

		_itemTemplateVariableDefinition = itemTemplateVariableDefinition;
	}

	public Class<?> getClazz() {
		return _clazz;
	}

	public TemplateVariableDefinition getItemTemplateVariableDefinition() {
		return _itemTemplateVariableDefinition;
	}

	public String getLabel() {
		return _label;
	}

	public String getName() {
		return _name;
	}

	public boolean isCollection() {
		if (_itemTemplateVariableDefinition != null) {
			return true;
		}

		return false;
	}

	private Class<?> _clazz;
	private TemplateVariableDefinition _itemTemplateVariableDefinition;
	private String _label;
	private String _name;

}