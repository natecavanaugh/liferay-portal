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

package com.liferay.util.bridges.jsf.common;

import com.liferay.portal.kernel.language.LanguageUtil;

import java.util.Locale;

import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * <p>
 * This class provides static convenience methods for creating FacesMessage
 * objects from locale-specific values in the Liferay portal.properties file,
 * and adding them to the FacesContext either globally, or to individual
 * components.
 * </p>
 *
 * @author Neil Griffin
 */
public class FacesMessageUtil {

	public static void error(FacesContext facesContext, String key) {
		error(null, facesContext, key);
	}

	public static void error(
		FacesContext facesContext, String key, Object argument) {

		error(null, facesContext, key, argument);
	}

	public static void error(
		FacesContext facesContext, String key, Object[] arguments) {

		error(null, facesContext, key, arguments);
	}

	public static void error(
		String clientId, FacesContext facesContext, String key) {

		_addMessage(clientId, facesContext, FacesMessage.SEVERITY_ERROR, key);
	}

	public static void error(
		String clientId, FacesContext facesContext, String key,
		Object argument) {

		_addMessage(
			clientId, facesContext, FacesMessage.SEVERITY_ERROR, key, argument);
	}

	public static void error(
		String clientId, FacesContext facesContext, String key,
		Object[] arguments) {

		_addMessage(
			clientId, facesContext, FacesMessage.SEVERITY_ERROR, key,
			arguments);
	}

	public static void info(FacesContext facesContext, String key) {
		info(null, facesContext, key);
	}

	public static void info(
		FacesContext facesContext, String key, Object argument) {

		info(null, facesContext, key, argument);
	}

	public static void info(
		FacesContext facesContext, String key, Object[] arguments) {

		info(null, facesContext, key, arguments);
	}

	public static void info(
		String clientId, FacesContext facesContext, String key) {

		_addMessage(clientId, facesContext, FacesMessage.SEVERITY_INFO, key);
	}

	public static void info(
		String clientId, FacesContext facesContext, String key,
		Object argument) {

		_addMessage(
			clientId, facesContext, FacesMessage.SEVERITY_INFO, key, argument);
	}

	public static void info(
		String clientId, FacesContext facesContext, String key,
		Object[] arguments) {

		_addMessage(
			clientId, facesContext, FacesMessage.SEVERITY_INFO, key, arguments);
	}

	private static void _addMessage(
		String clientId, FacesContext facesContext, Severity severity,
		String key) {

		Locale locale = JSFPortletUtil.getLocale(facesContext);

		String message = LanguageUtil.get(locale, key);

		FacesMessage facesMessage = new FacesMessage(severity, message, null);

		facesContext.addMessage(clientId, facesMessage);
	}

	private static void _addMessage(
		String clientId, FacesContext facesContext, Severity severity,
		String key, Object argument) {

		Locale locale = JSFPortletUtil.getLocale(facesContext);

		String message = LanguageUtil.format(locale, key, argument);

		FacesMessage facesMessage = new FacesMessage(severity, message, null);

		facesContext.addMessage(clientId, facesMessage);
	}

	private static void _addMessage(
		String clientId, FacesContext facesContext, Severity severity,
		String key, Object[] arguments) {

		Locale locale = JSFPortletUtil.getLocale(facesContext);

		String message = LanguageUtil.format(locale, key, arguments);

		FacesMessage facesMessage = new FacesMessage(severity, message, null);

		facesContext.addMessage(clientId, facesMessage);
	}

}