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

package com.liferay.portlet.messageboards.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.MethodCache;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * The utility for the message boards ban remote service. This utility wraps {@link com.liferay.portlet.messageboards.service.impl.MBBanServiceImpl} and is the primary access point for service operations in application layer code running on a remote server.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see MBBanService
 * @see com.liferay.portlet.messageboards.service.base.MBBanServiceBaseImpl
 * @see com.liferay.portlet.messageboards.service.impl.MBBanServiceImpl
 * @generated
 */
public class MBBanServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portlet.messageboards.service.impl.MBBanServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portlet.messageboards.model.MBBan addBan(
		long banUserId, com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().addBan(banUserId, serviceContext);
	}

	public static void deleteBan(long banUserId,
		com.liferay.portal.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService().deleteBan(banUserId, serviceContext);
	}

	public static MBBanService getService() {
		if (_service == null) {
			_service = (MBBanService)PortalBeanLocatorUtil.locate(MBBanService.class.getName());

			ReferenceRegistry.registerReference(MBBanServiceUtil.class,
				"_service");
			MethodCache.remove(MBBanService.class);
		}

		return _service;
	}

	public void setService(MBBanService service) {
		MethodCache.remove(MBBanService.class);

		_service = service;

		ReferenceRegistry.registerReference(MBBanServiceUtil.class, "_service");
		MethodCache.remove(MBBanService.class);
	}

	private static MBBanService _service;
}