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

package com.liferay.portal.kernel.portlet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletBagPool {

	public static PortletBag get(String portletId) {
		return _instance._get(portletId);
	}

	public static void put(String portletId, PortletBag portletBag) {
		_instance._put(portletId, portletBag);
	}

	public static PortletBag remove(String portletId) {
		return _instance._remove(portletId);
	}

	public static void reset() {
		_instance._reset();
	}

	private PortletBagPool() {
		_portletBagPool = new ConcurrentHashMap<String, PortletBag>();
	}

	private PortletBag _get(String portletId) {
		return _portletBagPool.get(portletId);
	}

	private void _put(String portletId, PortletBag portletBag) {
		_portletBagPool.put(portletId, portletBag);
	}

	private PortletBag _remove(String portletId) {
		return _portletBagPool.remove(portletId);
	}

	private void _reset() {
		_portletBagPool.clear();
	}

	private static PortletBagPool _instance = new PortletBagPool();

	private Map<String, PortletBag> _portletBagPool;

}