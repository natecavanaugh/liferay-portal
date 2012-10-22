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

package com.liferay.portal.kernel.lock;

import com.liferay.portal.kernel.security.pacl.permission.PortalRuntimePermission;

/**
 * @author Alexander Chow
 */
public class LockListenerRegistryUtil {

	public static LockListener getLockListener(String className) {
		return getLockListenerRegistry().getLockListener(className);
	}

	public static LockListenerRegistry getLockListenerRegistry() {
		PortalRuntimePermission.checkGetBeanProperty(
			LockListenerRegistryUtil.class);

		return _lockListenerRegistry;
	}

	public static void register(LockListener lockListener) {
		getLockListenerRegistry().register(lockListener);
	}

	public static void unregister(LockListener lockListener) {
		getLockListenerRegistry().unregister(lockListener);
	}

	public void setLockListenerRegistry(
		LockListenerRegistry lockListenerRegistry) {

		PortalRuntimePermission.checkSetBeanProperty(getClass());

		_lockListenerRegistry = lockListenerRegistry;
	}

	private static LockListenerRegistry _lockListenerRegistry;

}