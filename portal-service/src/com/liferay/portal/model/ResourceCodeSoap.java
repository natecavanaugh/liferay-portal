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
 * This class is used by SOAP remote services.
 *
 * @author    Brian Wing Shun Chan
 * @generated
 */
public class ResourceCodeSoap implements Serializable {
	public static ResourceCodeSoap toSoapModel(ResourceCode model) {
		ResourceCodeSoap soapModel = new ResourceCodeSoap();

		soapModel.setCodeId(model.getCodeId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setName(model.getName());
		soapModel.setScope(model.getScope());

		return soapModel;
	}

	public static ResourceCodeSoap[] toSoapModels(ResourceCode[] models) {
		ResourceCodeSoap[] soapModels = new ResourceCodeSoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ResourceCodeSoap[][] toSoapModels(ResourceCode[][] models) {
		ResourceCodeSoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ResourceCodeSoap[models.length][models[0].length];
		}
		else {
			soapModels = new ResourceCodeSoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ResourceCodeSoap[] toSoapModels(List<ResourceCode> models) {
		List<ResourceCodeSoap> soapModels = new ArrayList<ResourceCodeSoap>(models.size());

		for (ResourceCode model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ResourceCodeSoap[soapModels.size()]);
	}

	public ResourceCodeSoap() {
	}

	public long getPrimaryKey() {
		return _codeId;
	}

	public void setPrimaryKey(long pk) {
		setCodeId(pk);
	}

	public long getCodeId() {
		return _codeId;
	}

	public void setCodeId(long codeId) {
		_codeId = codeId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public int getScope() {
		return _scope;
	}

	public void setScope(int scope) {
		_scope = scope;
	}

	private long _codeId;
	private long _companyId;
	private String _name;
	private int _scope;
}