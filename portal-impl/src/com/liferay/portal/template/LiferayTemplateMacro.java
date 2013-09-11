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

package com.liferay.portal.template;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.template.Template;
import com.liferay.portal.kernel.template.TemplateMacro;

import java.util.Locale;

/**
 * @author Tina Tian
 */
public class LiferayTemplateMacro implements TemplateMacro {

	public String language(String languageKey) {
		Object locale = _template.get("locale");

		if ((locale == null) || !(locale instanceof Locale)) {
			return languageKey;
		}

		return LanguageUtil.get((Locale)locale, languageKey);
	}

	@Override
	public void setTemplate(Template template) {
		_template = template;
	}

	private Template _template;

}