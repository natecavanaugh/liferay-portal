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

package com.liferay.portal.kernel.messaging.proxy;

import com.liferay.portal.kernel.messaging.sender.SingleDestinationMessageSender;
import com.liferay.portal.kernel.messaging.sender.SingleDestinationSynchronousMessageSender;

/**
 * @author Micha Kiener
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public abstract class BaseProxyBean {

	public final SingleDestinationMessageSender
		getSingleDestinationMessageSender() {

		return _singleDestinationMessageSender;
	}

	public final SingleDestinationSynchronousMessageSender
		getSingleDestinationSynchronousMessageSender() {

		return _singleDestinationSynchronousMessageSender;
	}

	public final void setSingleDestinationMessageSender(
		SingleDestinationMessageSender singleDestinationMessageSender) {

		_singleDestinationMessageSender = singleDestinationMessageSender;
	}

	public final void setSingleDestinationSynchronousMessageSender(
		SingleDestinationSynchronousMessageSender
		singleDestinationSynchronousMessageSender) {

		_singleDestinationSynchronousMessageSender =
			singleDestinationSynchronousMessageSender;
	}

	private SingleDestinationMessageSender _singleDestinationMessageSender;
	private SingleDestinationSynchronousMessageSender
		_singleDestinationSynchronousMessageSender;

}