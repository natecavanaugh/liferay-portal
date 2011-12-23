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

package com.liferay.portal.security.permission.comparator;

import com.liferay.portal.model.Permission;

import java.io.Serializable;

import java.util.Comparator;

/**
 * @author Brian Wing Shun Chan
 */
public class PermissionActionIdComparator
	implements Comparator<Object>, Serializable {

	public int compare(Object obj1, Object obj2) {
		String actionId1 = null;

		if (obj1 instanceof String) {
			actionId1 = (String)obj1;
		}
		else {
			Permission permission1 = (Permission)obj1;

			actionId1 = permission1.getActionId();
		}

		String actionId2 = null;

		if (obj2 instanceof String) {
			actionId2 = (String)obj2;
		}
		else {
			Permission permission2 = (Permission)obj2;

			actionId2 = permission2.getActionId();
		}

		return actionId1.compareTo(actionId2);
	}

}