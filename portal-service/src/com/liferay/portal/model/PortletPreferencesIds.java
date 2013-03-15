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

package com.liferay.portal.model;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 */
public class PortletPreferencesIds implements Serializable {

	public PortletPreferencesIds() {
	}

	public PortletPreferencesIds(
		long companyId, long ownerId, int ownerType, long plid,
		String portletId) {

		_companyId = companyId;
		_ownerId = ownerId;
		_ownerType = ownerType;
		_plid = plid;
		_portletId = portletId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public long getOwnerId() {
		return _ownerId;
	}

	public int getOwnerType() {
		return _ownerType;
	}

	public long getPlid() {
		return _plid;
	}

	public String getPortletId() {
		return _portletId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public void setOwnerId(long ownerId) {
		_ownerId = ownerId;
	}

	public void setOwnerType(int ownerType) {
		_ownerType = ownerType;
	}

	public void setPlid(long plid) {
		_plid = plid;
	}

	public void setPortletId(String portletId) {
		_portletId = portletId;
	}

	private long _companyId;
	private long _ownerId;
	private int _ownerType;
	private long _plid;
	private String _portletId;

}