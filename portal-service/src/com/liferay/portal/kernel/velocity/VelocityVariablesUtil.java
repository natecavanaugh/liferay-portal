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

package com.liferay.portal.kernel.velocity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Raymond Augé
 */
public class VelocityVariablesUtil {

	public static VelocityVariables getVelocityVariables() {
		return _velocityVariables;
	}

	public static void insertHelperUtilities(
		VelocityContext velocityContext, String[] restrictedVariables) {

		getVelocityVariables().insertHelperUtilities(
			velocityContext, restrictedVariables);
	}

	public static void insertVariables(
			VelocityContext velocityContext, HttpServletRequest request)
		throws Exception {

		getVelocityVariables().insertVariables(velocityContext, request);
	}

	public void setVelocityVariables(VelocityVariables velocityVariables) {
		_velocityVariables = velocityVariables;
	}

	private static VelocityVariables _velocityVariables;

}