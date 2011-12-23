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

package com.liferay.util.bridges.jsf.sun;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Brian Myunghun Kim
 */
public class WriterWrapper extends Writer {

	public WriterWrapper(Writer writer) {
		_writer = writer;
	}

	@Override
	public void close() throws IOException {
		_writer.close();
	}

	@Override
	public void flush() {
	}

	public void write(char cbuf) throws IOException {
		_writer.write(cbuf);
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		StringBuilder sb = new StringBuilder(len);

		sb.append(cbuf, off, len);

		_writer.write(sb.toString());
	}

	@Override
	public void write(int c) throws IOException {
		_writer.write(c);
	}

	@Override
	public void write(String str) throws IOException {
		_writer.write(str);
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		_writer.write(str, off, len);
	}

	private Writer _writer;

}