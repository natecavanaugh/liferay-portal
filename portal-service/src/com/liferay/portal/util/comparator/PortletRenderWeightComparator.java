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

package com.liferay.portal.util.comparator;

import com.liferay.portal.model.Portlet;

import java.io.Serializable;

import java.util.Comparator;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletRenderWeightComparator
	implements Comparator<Portlet>, Serializable {

	public int compare(Portlet portlet1, Portlet portlet2) {
		int renderWeight1 = portlet1.getRenderWeight();
		int renderWeight2 = portlet2.getRenderWeight();

		if (renderWeight1 < renderWeight2) {
			return 1;
		}
		else if (renderWeight1 > renderWeight2) {
			return -1;
		}
		else {
			return portlet1.getPortletId().compareTo(portlet2.getPortletId());
		}
	}

}