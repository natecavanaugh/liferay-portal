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

package com.liferay.portal.kernel.util;

import java.io.IOException;

/**
 * @author Shuyang Zhou
 */
public class HeapUtil {

	public static void heapDump(boolean live, boolean binary, String file) {
		int processId = ProcessUtil.getProcessId();

		StringBundler sb = new StringBundler(7);

		sb.append("jmap -dump:");

		if (live) {
			sb.append("live,");
		}

		if (binary) {
			sb.append("format=b,");
		}

		sb.append("file=");
		sb.append(file);
		sb.append(StringPool.SPACE);
		sb.append(processId);

		try {
			Runtime runtime = Runtime.getRuntime();

			runtime.exec(sb.toString());
		}
		catch (IOException ioe) {
			throw new RuntimeException("Unable to perform heap dump", ioe);
		}
	}

}