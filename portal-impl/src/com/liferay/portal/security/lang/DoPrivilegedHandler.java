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

package com.liferay.portal.security.lang;

import com.liferay.portal.kernel.security.pacl.NotPrivileged;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * @author Raymond Augé
 */
public class DoPrivilegedHandler
	implements DoPrivilegedBean, InvocationHandler {

	public DoPrivilegedHandler(Object bean) {
		_bean = bean;
	}

	public Object getActualBean() {
		return _bean;
	}

	public Object invoke(Object object, Method method, Object[] arguments)
		throws Throwable {

		Class<?> methodDeclaringClass = method.getDeclaringClass();
		String methodName = method.getName();

		if (methodDeclaringClass.equals(DoPrivilegedBean.class) &&
			methodName.equals("getActualBean")) {

			return getActualBean();
		}
		else if (_isNotPrivileged(_bean.getClass(), method)) {
			return method.invoke(_bean, arguments);
		}

		try {
			return AccessController.doPrivileged(
				new InvokePrivilegedExceptionAction(_bean, method, arguments));
		}
		catch (PrivilegedActionException pae) {
			Exception e = pae.getException();

			if (e instanceof InvocationTargetException) {
				InvocationTargetException ite = (InvocationTargetException)e;

				throw ite.getTargetException();
			}

			throw e;
		}
	}

	public class InvokePrivilegedExceptionAction
		implements PrivilegedExceptionAction<Object> {

		public InvokePrivilegedExceptionAction(
			Object bean, Method method, Object[] arguments) {

			_bean = bean;
			_method = method;
			_arguments = arguments;
		}

		public Object run() throws Exception {
			return _method.invoke(_bean, _arguments);
		}

		private Object[] _arguments;
		private Object _bean;
		private Method _method;

	}

	private boolean _isNotPrivileged(Class<?> clazz, Method method)
		throws SecurityException {

		NotPrivileged notPrivileged = method.getAnnotation(NotPrivileged.class);

		while ((notPrivileged == null) && (clazz != null)) {
			try {
				method = clazz.getMethod(
					method.getName(), method.getParameterTypes());
			}
			catch (NoSuchMethodException nsme) {
				break;
			}

			notPrivileged = method.getAnnotation(NotPrivileged.class);

			if (notPrivileged == null) {
				clazz = clazz.getSuperclass();
			}
		}

		if (notPrivileged != null) {
			return true;
		}

		return false;
	}

	private Object _bean;

}