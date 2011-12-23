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

package com.liferay.portal.service;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.util.MethodCache;
import com.liferay.portal.kernel.util.ReferenceRegistry;

/**
 * The utility for the resource remote service. This utility wraps {@link com.liferay.portal.service.impl.ResourceServiceImpl} and is the primary access point for service operations in application layer code running on a remote server.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ResourceService
 * @see com.liferay.portal.service.base.ResourceServiceBaseImpl
 * @see com.liferay.portal.service.impl.ResourceServiceImpl
 * @generated
 */
public class ResourceServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.portal.service.impl.ResourceServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.model.Resource getResource(
		long companyId, java.lang.String name, int scope,
		java.lang.String primKey)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getResource(companyId, name, scope, primKey);
	}

	public static ResourceService getService() {
		if (_service == null) {
			_service = (ResourceService)PortalBeanLocatorUtil.locate(ResourceService.class.getName());

			ReferenceRegistry.registerReference(ResourceServiceUtil.class,
				"_service");
			MethodCache.remove(ResourceService.class);
		}

		return _service;
	}

	public void setService(ResourceService service) {
		MethodCache.remove(ResourceService.class);

		_service = service;

		ReferenceRegistry.registerReference(ResourceServiceUtil.class,
			"_service");
		MethodCache.remove(ResourceService.class);
	}

	private static ResourceService _service;
}