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

package com.liferay.portal.server.capabilities;

import java.lang.reflect.Field;

import java.util.List;

import javax.servlet.ServletContext;

/**
 * @author Brian Wing Shun Chan
 * @author Igor Spasic
 */
public class JettyServerCapabilities implements ServerCapabilities {

	public void determine(ServletContext servletContext) throws Exception {
		determineSupportsHotDeploy(servletContext);
	}

	public boolean isSupportsHotDeploy() {
		return _supportsHotDeploy;
	}

	protected void determineSupportsHotDeploy(ServletContext servletContext)
		throws Exception {

		// org.eclipse.jetty.webapp.WebAppContext

		Class<?> servletContextClass = servletContext.getClass();

		Field outerClassField = servletContextClass.getDeclaredField("this$0");

		outerClassField.setAccessible(true);

		Object webAppContext = outerClassField.get(servletContext);

		// org.eclipse.jetty.server.handler.AbstractHandler

		Class<?> abstractHandlerClass = webAppContext.getClass();

		for (int i = 0; i < 6; i++) {
			abstractHandlerClass = abstractHandlerClass.getSuperclass();
		}

		// org.eclipse.jetty.server.Server

		Field serverField = abstractHandlerClass.getDeclaredField("_server");

		serverField.setAccessible(true);

		Object server = serverField.get(webAppContext);

		// org.eclipse.jetty.util.component.AggregateLifeCycle

		Class<?> aggregateLifeCycleClass = server.getClass();

		for (int i = 0; i < 4; i++) {
			aggregateLifeCycleClass = aggregateLifeCycleClass.getSuperclass();
		}

		// org.eclipse.jetty.deploy.DeploymentManager

		Field beansField = aggregateLifeCycleClass.getDeclaredField("_beans");

		beansField.setAccessible(true);

		Object deploymentManager = null;

		List<?> aggregateLifeCycleBeans = (List<?>)beansField.get(server);

		for (Object aggregateLifeCycleBean : aggregateLifeCycleBeans) {

			// org.eclipse.jetty.util.component.AggregateLifeCycle$Bean

			Class<?> aggregateLifeCycleBeanClass =
				aggregateLifeCycleBean.getClass();

			Field beanField = aggregateLifeCycleBeanClass.getDeclaredField(
				"_bean");

			beanField.setAccessible(true);

			Object bean = beanField.get(aggregateLifeCycleBean);

			Class<?> beanClass = bean.getClass();

			String beanClassName = beanClass.getName();

			if (beanClassName.equals(
					"org.eclipse.jetty.deploy.DeploymentManager")) {

				deploymentManager = bean;

				break;
			}
		}

		if (deploymentManager == null) {
			throw new Exception("DeploymentManager not found");
		}

		// org.eclipse.jetty.deploy.providers.ScanningAppProvider

		Class<?> deploymentManagerClass = deploymentManager.getClass();

		Field providersField = deploymentManagerClass.getDeclaredField(
			"_providers");

		providersField.setAccessible(true);

		List<?> providers = (List<?>)providersField.get(deploymentManager);

		boolean hotDeploySupported = false;

		for (Object provider : providers) {
			Class<?> providerClass = provider.getClass();

			String providerClassName = providerClass.getName();

			if (!providerClassName.equals(
					"org.eclipse.jetty.deploy.providers.ContextProvider")) {

				continue;
			}

			Class<?> scanningAppProviderClass = providerClass.getSuperclass();

			Field scanIntervalField = scanningAppProviderClass.getDeclaredField(
				"_scanInterval");

			scanIntervalField.setAccessible(true);

			Integer scanInterval = (Integer)scanIntervalField.get(provider);

			if ((scanInterval != null) && (scanInterval.intValue() > 0)) {
				hotDeploySupported = true;

				break;
			}
		}

		_supportsHotDeploy = hotDeploySupported;
	}

	private boolean _supportsHotDeploy;

}