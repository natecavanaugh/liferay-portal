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

package com.liferay.portal.language;

import com.liferay.portal.kernel.util.EnumerationUtil;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Carlos Sierra Andrés
 */
public class CompositeResourceBundle extends ResourceBundle {

	public CompositeResourceBundle(ResourceBundle ... delegateResourceBundles) {
		_delegateResourceBundles = delegateResourceBundles;
	}

	@Override
	public Enumeration<String> getKeys() {
		if (_enumerations == null) {
			_enumerations = new Enumeration[_delegateResourceBundles.length];

			for (int i = 0; i < _delegateResourceBundles.length; i++) {
				_enumerations[i] = _delegateResourceBundles[i].getKeys();
			}
		}

		return EnumerationUtil.<String>compose(_enumerations);
	}

	@Override
	protected Object handleGetObject(String key) {
		for (ResourceBundle delegateResourceBundle : _delegateResourceBundles) {
			Object object = null;

			try {
				object = delegateResourceBundle.getObject(key);
			}
			catch (MissingResourceException e) {
				continue;
			}

			if (object != null) {
				return object;
			}
		}

		throw new MissingResourceException(
			"Could not find resource", CompositeResourceBundle.class.getName(),
			key);
	}

	private ResourceBundle[] _delegateResourceBundles;
	private Enumeration<String>[] _enumerations;

}