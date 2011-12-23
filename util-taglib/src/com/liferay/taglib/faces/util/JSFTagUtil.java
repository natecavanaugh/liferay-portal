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

package com.liferay.taglib.faces.util;

import com.liferay.portal.kernel.util.Validator;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * @author Neil Griffin
 */
public class JSFTagUtil {

	public static String eval(String expr) {
		if (Validator.isNotNull(expr) &&
			UIComponentTag.isValueReference(expr)) {

			FacesContext facesContext = FacesContext.getCurrentInstance();

			Application application = facesContext.getApplication();
			ValueBinding valueBinding = application.createValueBinding(expr);

			expr = String.valueOf(valueBinding.getValue(facesContext));
		}

		return expr;
	}

}