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

package com.liferay.portal.kernel.struts;

import com.liferay.portal.kernel.util.PortalClassInvoker;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletActionInvoker {

	public static void processAction(
			String className, PortletConfig portletConfig,
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortalClassInvoker.invoke(
			true, className, "processAction",
			new String[] {
				"org.apache.struts.action.ActionMapping",
				"org.apache.struts.action.ActionForm",
				PortletConfig.class.getName(), ActionRequest.class.getName(),
				ActionResponse.class.getName()
			},
			null, null, portletConfig, actionRequest, actionResponse);
	}

}