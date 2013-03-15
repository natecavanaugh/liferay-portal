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

package com.liferay.portal.kernel.transaction;

/**
 * @author Michael Young
 */
public enum Isolation {

	COUNTER(TransactionDefinition.ISOLATION_COUNTER),
	DEFAULT(TransactionDefinition.ISOLATION_DEFAULT),
	PORTAL(TransactionDefinition.ISOLATION_PORTAL),
	READ_COMMITTED(TransactionDefinition.ISOLATION_READ_COMMITTED),
	READ_UNCOMMITTED(TransactionDefinition.ISOLATION_READ_UNCOMMITTED),
	REPEATABLE_READ(TransactionDefinition.ISOLATION_REPEATABLE_READ),
	SERIALIZABLE(TransactionDefinition.ISOLATION_SERIALIZABLE);

	Isolation(int value) {
		_value = value;
	}

	public int value() {
		return _value;
	}

	private int _value;

}