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

import com.sun.faces.RIConstants;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.ConfigureListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

/**
 * @author Brian Myunghun Kim
 */
public class LiferayConfigureListener extends ConfigureListener {

	public static final String ASSOCIATE_KEY =
		RIConstants.FACES_PREFIX + "ApplicationAssociate";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();

		super.contextInitialized(sce);

		ApplicationAssociate associate = ApplicationAssociate.getInstance(
			new LiferayServletContextAdapter(sc));

		if (associate != null) {
			sc.setAttribute(ASSOCIATE_KEY, associate);
		}
	}

}