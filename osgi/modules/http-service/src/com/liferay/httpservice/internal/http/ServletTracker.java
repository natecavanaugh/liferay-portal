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

import javax.servlet.Servlet;

import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class ServletTracker
	implements ServiceTrackerCustomizer<Servlet, Servlet> {

	public ServletTracker(HttpSupport httpSupport) {
	}

	public Servlet addingService(ServiceReference<Servlet> serviceReference) {
		return null;
	}

	public void modifiedService(
		ServiceReference<Servlet> serviceReference, Servlet servlet) {
	}

	public void removedService(
		ServiceReference<Servlet> serviceReference, Servlet servlet) {
	}

}