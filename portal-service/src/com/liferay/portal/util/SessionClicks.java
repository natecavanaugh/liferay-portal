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

package com.liferay.portal.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public class SessionClicks {

	public static String get(
		HttpServletRequest request, String key, String defaultValue) {

		return get(request, _DEFAULT_NAMESPACE, key, defaultValue);
	}

	public static String get(
		HttpServletRequest request, String namespace, String key,
		String defaultValue) {

		try {
			PortalPreferences preferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(request);

			return preferences.getValue(namespace, key, defaultValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			return null;
		}
	}

	public static void put(
		HttpServletRequest request, String key, String value) {

		put(request, _DEFAULT_NAMESPACE, key, value);
	}

	public static void put(
		HttpServletRequest request, String namespace, String key,
		String value) {

		try {
			PortalPreferences preferences =
				PortletPreferencesFactoryUtil.getPortalPreferences(request);

			preferences.setValue(namespace, key, value);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private static final String _DEFAULT_NAMESPACE =
		SessionClicks.class.getName();

	private static Log _log = LogFactoryUtil.getLog(SessionClicks.class);

}