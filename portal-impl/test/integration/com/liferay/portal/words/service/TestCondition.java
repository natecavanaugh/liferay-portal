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

package com.liferay.portal.words.service;

/**
 * @author Ken Boyer
 */

public class TestCondition {

	TestCondition(String words, int length, String message) {
		this._words = words;
		this._length = length;
		this._message = message;
	}

	public int getLength() {
		return _length;
	}

	public String getMessage() {
		return _message;
	}

	public String getWords() {
		return _words;
	}

	private final int _length;
	private final String _message;
	private final String _words;

}