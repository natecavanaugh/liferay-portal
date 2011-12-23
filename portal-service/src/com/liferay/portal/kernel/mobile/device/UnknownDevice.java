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

package com.liferay.portal.kernel.mobile.device;

import java.util.Collections;
import java.util.Map;

/**
 * Class represents unknown device
 *
 * @author Milen Dyankov
 */
public class UnknownDevice extends AbstractDevice {

	public static UnknownDevice getInstance() {
		return _instance;
	}

	public String getBrand() {
		return VersionableName.UNKNOWN.getName();
	}

	public String getBrowser() {
		return VersionableName.UNKNOWN.getName();
	}

	public String getBrowserVersion() {
		return VersionableName.UNKNOWN.getName();
	}

	public Map<String, Capability> getCapabilities() {
		return Collections.emptyMap();
	}

	public String getCapability(String name) {
		return null;
	}

	public String getModel() {
		return VersionableName.UNKNOWN.getName();
	}

	public String getOS() {
		return VersionableName.UNKNOWN.getName();
	}

	public String getOSVersion() {
		return VersionableName.UNKNOWN.getName();
	}

	public String getPointingMethod() {
		return VersionableName.UNKNOWN.getName();
	}

	public Dimensions getScreenSize() {
		return Dimensions.UNKNOWN;
	}

	public boolean hasQwertyKeyboard() {
		return true;
	}

	public boolean isTablet() {
		return false;
	}

	private UnknownDevice() {
	}

	private static UnknownDevice _instance = new UnknownDevice();

}