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

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ContextPathUtil;

import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Shuyang Zhou
 */
public class DirectServletContext implements ServletContext {

	public DirectServletContext(ServletContext servletContext) {
		_servletContext = servletContext;

		_contextPath = ContextPathUtil.getContextPath(servletContext);
	}

	public Object getAttribute(String name) {
		return _servletContext.getAttribute(name);
	}

	public Enumeration<String> getAttributeNames() {
		return _servletContext.getAttributeNames();
	}

	public ServletContext getContext(String uriPath) {
		return _servletContext.getContext(uriPath);
	}

	public String getContextPath() {
		return _contextPath;
	}

	public String getInitParameter(String name) {
		return _servletContext.getInitParameter(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return _servletContext.getInitParameterNames();
	}

	public int getMajorVersion() {
		return _servletContext.getMajorVersion();
	}

	public String getMimeType(String file) {
		return _servletContext.getMimeType(file);
	}

	public int getMinorVersion() {
		return _servletContext.getMinorVersion();
	}

	public RequestDispatcher getNamedDispatcher(String name) {
		return _servletContext.getNamedDispatcher(name);
	}

	public String getRealPath(String path) {
		return _servletContext.getRealPath(path);
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		String fullPath = _contextPath.concat(path);

		Servlet servlet = DirectServletRegistry.getServlet(fullPath);

		if (servlet == null) {
			if (_log.isDebugEnabled()) {
				_log.debug("No servlet found for " + fullPath);
			}

			return _servletContext.getRequestDispatcher(path);
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug("Servlet found for " + fullPath);
			}

			return new DirectRequestDispatcher(servlet);
		}
	}

	public URL getResource(String path) throws MalformedURLException {
		return _servletContext.getResource(path);
	}

	public InputStream getResourceAsStream(String path) {
		return _servletContext.getResourceAsStream(path);
	}

	public Set<String> getResourcePaths(String path) {
		return _servletContext.getResourcePaths(path);
	}

	public String getServerInfo() {
		return _servletContext.getServerInfo();
	}

	/**
	 * @deprecated
	 */
	public Servlet getServlet(String name) throws ServletException {
		return _servletContext.getServlet(name);
	}

	public String getServletContextName() {
		return _servletContext.getServletContextName();
	}

	/**
	 * @deprecated
	 */
	public Enumeration<String> getServletNames() {
		return _servletContext.getServletNames();
	}

	/**
	 * @deprecated
	 */
	public Enumeration<Servlet> getServlets() {
		return _servletContext.getServlets();
	}

	/**
	 * @deprecated
	 */
	public void log(Exception exception, String message) {
		_servletContext.log(exception, message);
	}

	public void log(String message) {
		_servletContext.log(message);
	}

	public void log(String message, Throwable t) {
		_servletContext.log(message, t);
	}

	public void removeAttribute(String name) {
		_servletContext.removeAttribute(name);
	}

	public void setAttribute(String name, Object value) {
		_servletContext.setAttribute(name, value);
	}

	private static Log _log = LogFactoryUtil.getLog(DirectServletContext.class);

	private String _contextPath;
	private ServletContext _servletContext;

}