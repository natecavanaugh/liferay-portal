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

package com.liferay.httpservice.internal.http;

import javax.servlet.Filter;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class FilterTracker implements ServiceTrackerCustomizer<Filter, Filter> {

	public FilterTracker(HttpSupport httpSupport) {
	}

	public Filter addingService(ServiceReference<Filter> serviceReference) {
		return null;
	}

	public void modifiedService(
		ServiceReference<Filter> serviceReference, Filter filter) {
	}

	public void removedService(
		ServiceReference<Filter> serviceReference, Filter filter) {
	}

}