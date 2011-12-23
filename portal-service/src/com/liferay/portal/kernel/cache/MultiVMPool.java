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

package com.liferay.portal.kernel.cache;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 */
public interface MultiVMPool {

	public void clear();

	public void clear(String name);

	/**
	 * @deprecated
	 */
	public Object get(PortalCache portalCache, String key);

	public Object get(String name, String key);

	public PortalCache getCache(String name);

	public PortalCache getCache(String name, boolean blocking);

	/**
	 * @deprecated
	 */
	public void put(PortalCache portalCache, String key, Object value);

	/**
	 * @deprecated
	 */
	public void put(PortalCache portalCache, String key, Serializable value);

	public void put(String name, String key, Object value);

	public void put(String name, String key, Serializable value);

	/**
	 * @deprecated
	 */
	public void remove(PortalCache portalCache, String key);

	public void remove(String name, String key);

	public void removeCache(String name);

}