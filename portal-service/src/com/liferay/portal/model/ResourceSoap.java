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

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.portal.service.http.ResourceServiceSoap}.
 *
 * @author    Brian Wing Shun Chan
 * @see       com.liferay.portal.service.http.ResourceServiceSoap
 * @generated
 */
public class ResourceSoap implements Serializable {
	public static ResourceSoap toSoapModel(Resource model) {
		ResourceSoap soapModel = new ResourceSoap();

		soapModel.setResourceId(model.getResourceId());
		soapModel.setCodeId(model.getCodeId());
		soapModel.setPrimKey(model.getPrimKey());

		return soapModel;
	}

	public static ResourceSoap[] toSoapModels(Resource[] models) {
		ResourceSoap[] soapModels = new ResourceSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ResourceSoap[][] toSoapModels(Resource[][] models) {
		ResourceSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ResourceSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ResourceSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ResourceSoap[] toSoapModels(List<Resource> models) {
		List<ResourceSoap> soapModels = new ArrayList<ResourceSoap>(models.size());

		for (Resource model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ResourceSoap[soapModels.size()]);
	}

	public ResourceSoap() {
	}

	public long getPrimaryKey() {
		return _resourceId;
	}

	public void setPrimaryKey(long pk) {
		setResourceId(pk);
	}

	public long getResourceId() {
		return _resourceId;
	}

	public void setResourceId(long resourceId) {
		_resourceId = resourceId;
	}

	public long getCodeId() {
		return _codeId;
	}

	public void setCodeId(long codeId) {
		_codeId = codeId;
	}

	public String getPrimKey() {
		return _primKey;
	}

	public void setPrimKey(String primKey) {
		_primKey = primKey;
	}

	private long _resourceId;
	private long _codeId;
	private String _primKey;
}