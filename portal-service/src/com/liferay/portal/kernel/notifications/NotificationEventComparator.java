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

package com.liferay.portal.kernel.notifications;

import java.util.Comparator;

/**
 * @author Edward Han
 */
public class NotificationEventComparator
	implements Comparator<NotificationEvent> {

	public NotificationEventComparator() {
		this(true);
	}

	public NotificationEventComparator(boolean ascending) {
		_ascending = ascending;
	}

	public int compare(
		NotificationEvent notificationEvent1,
		NotificationEvent notificationEvent2) {

		long deliverBy1 = notificationEvent1.getDeliverBy();
		long deliverBy2 = notificationEvent2.getDeliverBy();

		int value = 0;

		if (deliverBy1 < deliverBy2) {
			value = -1;
		}
		else if (deliverBy1 > deliverBy2) {
			value = 1;
		}

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	private boolean _ascending;

}