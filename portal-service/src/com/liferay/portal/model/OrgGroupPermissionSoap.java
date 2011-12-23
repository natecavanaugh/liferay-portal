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

package com.liferay.portal.model;

import com.liferay.portal.service.persistence.OrgGroupPermissionPK;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author    Brian Wing Shun Chan
 * @generated
 */
public class OrgGroupPermissionSoap implements Serializable {
	public static OrgGroupPermissionSoap toSoapModel(OrgGroupPermission model) {
		OrgGroupPermissionSoap soapModel = new OrgGroupPermissionSoap();

		soapModel.setOrganizationId(model.getOrganizationId());
		soapModel.setGroupId(model.getGroupId());
		soapModel.setPermissionId(model.getPermissionId());

		return soapModel;
	}

	public static OrgGroupPermissionSoap[] toSoapModels(
		OrgGroupPermission[] models) {
		OrgGroupPermissionSoap[] soapModels = new OrgGroupPermissionSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static OrgGroupPermissionSoap[][] toSoapModels(
		OrgGroupPermission[][] models) {
		OrgGroupPermissionSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new OrgGroupPermissionSoap[models.length][models[0].length];
		}
		else {
			soapModels = new OrgGroupPermissionSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static OrgGroupPermissionSoap[] toSoapModels(
		List<OrgGroupPermission> models) {
		List<OrgGroupPermissionSoap> soapModels = new ArrayList<OrgGroupPermissionSoap>(models.size());

		for (OrgGroupPermission model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new OrgGroupPermissionSoap[soapModels.size()]);
	}

	public OrgGroupPermissionSoap() {
	}

	public OrgGroupPermissionPK getPrimaryKey() {
		return new OrgGroupPermissionPK(_organizationId, _groupId, _permissionId);
	}

	public void setPrimaryKey(OrgGroupPermissionPK pk) {
		setOrganizationId(pk.organizationId);
		setGroupId(pk.groupId);
		setPermissionId(pk.permissionId);
	}

	public long getOrganizationId() {
		return _organizationId;
	}

	public void setOrganizationId(long organizationId) {
		_organizationId = organizationId;
	}

	public long getGroupId() {
		return _groupId;
	}

	public void setGroupId(long groupId) {
		_groupId = groupId;
	}

	public long getPermissionId() {
		return _permissionId;
	}

	public void setPermissionId(long permissionId) {
		_permissionId = permissionId;
	}

	private long _organizationId;
	private long _groupId;
	private long _permissionId;
}