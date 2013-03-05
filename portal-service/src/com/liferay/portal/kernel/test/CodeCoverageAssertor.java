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

package com.liferay.portal.kernel.test;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Shuyang Zhou
 */
public class CodeCoverageAssertor implements TestRule {

	public CodeCoverageAssertor() {
		this(null, null, true);
	}

	public CodeCoverageAssertor(
		String[] includes, String[] excludes, boolean includeInnerClasses) {

		_includes = includes;
		_excludes = excludes;
		_includeInnerClasses = includeInnerClasses;
	}

	public void appendAssertClasses(List<Class<?>> assertClasses) {
	}

	public Statement apply(
		final Statement statement, final Description description) {

		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				_dynamicallyInstrumentMethod.invoke(null, _includes, _excludes);

				try {
					statement.evaluate();
				}
				finally {
					List<Class<?>> assertClasses = new ArrayList<Class<?>>();

					String className = description.getClassName();

					if (className.endsWith("Test")) {
						className = className.substring(
							0, className.length() - 4);

						ClassLoader classLoader = getClassLoader();

						Class<?> clazz = classLoader.loadClass(className);

						assertClasses.add(clazz);
					}

					appendAssertClasses(assertClasses);

					_assertCoverageMethod.invoke(
						null, _includeInnerClasses,
						assertClasses.toArray(
							new Class<?>[assertClasses.size()]));
				}
			}
		};
	}

	protected ClassLoader getClassLoader() {
		Class<?> clazz = getClass();

		return clazz.getClassLoader();
	}

	private static final Method _assertCoverageMethod;
	private static final Method _dynamicallyInstrumentMethod;

	private final String[] _excludes;
	private final boolean _includeInnerClasses;
	private final String[] _includes;

	static {
		ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();

		try {
			Class<?> instrumentationAgentClass = systemClassLoader.loadClass(
				"net.sourceforge.cobertura.instrument.InstrumentationAgent");

			_assertCoverageMethod = instrumentationAgentClass.getMethod(
				"assertCoverage", boolean.class, Class[].class);
			_dynamicallyInstrumentMethod = instrumentationAgentClass.getMethod(
				"dynamicallyInstrument", String[].class, String[].class);
		}
		catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}

	}

}