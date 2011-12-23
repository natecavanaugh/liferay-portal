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

package com.liferay.util.ant;

import org.apache.tools.ant.Project;

/**
 * @author Brian Wing Shun Chan
 */
public class AntUtil {

	public static Project getProject() {
		Project project = new Project();

		SystemLogger logger = new SystemLogger();

		logger.setMessageOutputLevel(Project.MSG_INFO);
		logger.setOutputPrintStream(System.out);
		logger.setErrorPrintStream(System.err);

		project.addBuildListener(logger);

		return project;
	}

}