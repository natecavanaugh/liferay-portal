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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.util.StringPool;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * See http://issues.liferay.com/browse/LEP-2297.
 * </p>
 *
 * @author Olaf Fricke
 * @author Brian Wing Shun Chan
 */
public class PortalDelegatorServlet extends HttpServlet {

	public static void addDelegate(String subContext, HttpServlet delegate) {
		if (subContext == null) {
			throw new IllegalArgumentException();
		}

		if (delegate == null) {
			throw new IllegalArgumentException();
		}

		_delegates.put(subContext, delegate);
	}

	public static void removeDelegate(String subContext) {
		if (subContext == null) {
			throw new IllegalArgumentException();
		}

		_delegates.remove(subContext);
	}

	@Override
	public void service(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {

		String uri = request.getPathInfo();

		if ((uri == null) || (uri.length() == 0)) {
			throw new ServletException("Path information is not specified");
		}

		String[] paths = uri.split(StringPool.SLASH);

		if (paths.length < 2) {
			throw new ServletException("Path " + uri + " is invalid");
		}

		HttpServlet delegate = _delegates.get(paths[1]);

		if (delegate == null) {
			throw new ServletException(
				"No servlet registred for context " + paths[1]);
		}

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		try {
			ClassLoader delegateClassLoader =
				delegate.getClass().getClassLoader();

			currentThread.setContextClassLoader(delegateClassLoader);

			delegate.service(request, response);
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);
		}
	}

	private static Map<String, HttpServlet> _delegates =
		new HashMap<String, HttpServlet>();

}