/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.backgroundtask.messaging;

import com.liferay.portal.kernel.messaging.BaseMessageListener;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.util.ClassLoaderUtil;
import com.liferay.portlet.backgroundtask.executor.BackgroundTaskExecutor;
import com.liferay.portlet.backgroundtask.model.BTEntry;
import com.liferay.portlet.backgroundtask.model.BTEntryConstants;
import com.liferay.portlet.backgroundtask.service.BTEntryLocalServiceUtil;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskMessageListener extends BaseMessageListener {

	@Override
	protected void doReceive(Message message) throws Exception {
		long entryId = (Long)message.get("entryId");

		ServiceContext serviceContext = new ServiceContext();

		BTEntryLocalServiceUtil.updateEntry(
			entryId, null, BTEntryConstants.STATUS_IN_PROGRESS, serviceContext);

		try {
			ClassLoader classLoader = ClassLoaderUtil.getPortalClassLoader();

			BTEntry entry = BTEntryLocalServiceUtil.getBTEntry(entryId);

			String servletContextNames = entry.getServletContextNames();

			if (Validator.isNotNull(servletContextNames)) {
				classLoader = ClassLoaderUtil.getAggregatePluginsClassLoader(
					StringUtil.split(servletContextNames), false);
			}

			BackgroundTaskExecutor backgroundTaskExecutor =
				(BackgroundTaskExecutor)InstanceFactory.newInstance(
					classLoader, entry.getTaskExecutorClassName());

			backgroundTaskExecutor.execute(entry, classLoader);

			BTEntryLocalServiceUtil.updateEntry(
				entryId, null, BTEntryConstants.STATUS_SUCCESSFUL,
				serviceContext);
		}
		catch (Exception e) {
			BTEntryLocalServiceUtil.updateEntry(
				entryId, null, BTEntryConstants.STATUS_FAILED, serviceContext);
		}
	}

}