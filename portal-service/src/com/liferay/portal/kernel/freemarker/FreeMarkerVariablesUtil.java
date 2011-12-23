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

package com.liferay.portal.kernel.freemarker;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
public class FreeMarkerVariablesUtil {

	public static FreeMarkerVariables getFreeMarkerVariables() {
		return _freeMarkerVariables;
	}

	public static void insertHelperUtilities(
		FreeMarkerContext freeMarkerContext, String[] restrictedVariables) {

		getFreeMarkerVariables().insertHelperUtilities(
			freeMarkerContext, restrictedVariables);
	}

	public static void insertVariables(
			FreeMarkerContext freeMarkerContext, HttpServletRequest request)
		throws Exception {

		getFreeMarkerVariables().insertVariables(freeMarkerContext, request);
	}

	public void setFreeMarkerVariables(
		FreeMarkerVariables freeMarkerVariables) {

		_freeMarkerVariables = freeMarkerVariables;
	}

	private static FreeMarkerVariables _freeMarkerVariables;

}