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

package com.liferay.util.servlet;

import java.io.IOException;

import javax.servlet.ServletInputStream;

/**
 * @author Brian Wing Shun Chan
 */
public class ServletInputStreamWrapper extends ServletInputStream {

	public ServletInputStreamWrapper(ServletInputStream is) {
		_is = is;
	}

	@Override
	public int available() throws IOException {
		return _is.available();
	}

	@Override
	public void close() throws IOException {
		_is.close();
	}

	@Override
	public void mark(int readlimit) {
		_is.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return _is.markSupported();
	}

	@Override
	public int read() throws IOException {
		return _is.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return _is.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return _is.read(b, off, len);
	}

	@Override
	public int readLine(byte[] b, int off, int len) throws IOException {
		return _is.readLine(b, off, len);
	}

	@Override
	public void reset() throws IOException {
		_is.reset();
	}

	@Override
	public long skip(long n) throws IOException {
		return _is.skip(n);
	}

	private ServletInputStream _is;

}