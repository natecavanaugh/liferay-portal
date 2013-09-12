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
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.util.PortletKeys;
import com.liferay.taglib.util.VelocityTaglib;

import java.util.Locale;

/**
 * @author Tina Tian
 */
public class LiferayTemplateMacro implements TemplateMacro {

	public void breadcrumbs(String type) throws Exception {
		Object theme = _template.get("theme");

		if ((theme == null) || !(theme instanceof VelocityTaglib)) {
			return;
		}

		VelocityTaglib velocityTaglib = (VelocityTaglib)theme;

		if ((type != null) && type.equals(LayoutConstants.TYPE_CONTROL_PANEL)) {
			velocityTaglib.breadcrumb(null, false, false, true, true);
		}
		else {
			velocityTaglib.breadcrumb();
		}
	}

	public void dockbar() throws Exception {
		Object theme = _template.get("theme");

		if ((theme == null) || !(theme instanceof VelocityTaglib)) {
			return;
		}

		VelocityTaglib velocityTaglib = (VelocityTaglib)theme;

		velocityTaglib.runtime(PortletKeys.DOCKBAR);
	}

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