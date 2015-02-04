/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.editor;

import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.CamelCaseUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import com.liferay.portal.model.User;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Julio Camarero
 */
public class EditorUtil {

	public static Properties getAvailableEditorProperties() {
		return PropsUtil.getProperties("editor.wysiwyg", false);
	}

	public static String[] getAvailableEditors(String editorImpl) {
		String[] availableEditors = StringUtil.split(
			PropsUtil.get(editorImpl), CharPool.COMMA);

		return availableEditors;
	}

	public static String getDefaultEditorValue(String editorImpl) {
		return PropsUtil.get(editorImpl);
	}

	public static String getEditorMode(String langType) {
		String editorMode = "php";

		if (langType.equals("css")) {
			editorMode = "css";
		}
		else if (langType.equals("xml") ||
				 langType.equals("xsl") ||
				 langType.equals("xsd")) {

			editorMode = "xml";
		}

		return editorMode;
	}

	public static Map<String, Serializable> getEditorPropertiesMap() {
		Map<String, Serializable> propMap = new HashMap<>();

		Properties editorProperties = getAvailableEditorProperties();

		for (String property : editorProperties.stringPropertyNames()) {
			if (StringUtil.endsWith(property, "jsp") || StringUtil.endsWith(property, "default")) {
				property = CamelCaseUtil.toCamelCase(property, _DELIMITERS);
			}

			propMap.put(property, StringPool.BLANK);
		}

		return propMap;
	}

	public static String getUserEditorValue(
		HttpServletRequest request, String editorImpl, User user) {
		Map<String, Serializable> preferredEditors = user.getPreferredEditors();

		if (Validator.isNotNull(preferredEditors)) {
			String camelizedEditorImpl = CamelCaseUtil.toCamelCase(
					editorImpl, _DELIMITERS);

			String editorPreference = GetterUtil.getString(
				preferredEditors.get(camelizedEditorImpl));

			if (Validator.isNotNull(editorPreference)) {
				editorImpl = editorPreference;
			}
		}

		return editorImpl;
	}

	public static String getEditorValue(
		HttpServletRequest request, String editorImpl, User user) {
		String originalEditorImpl = editorImpl;

		if (Validator.isNotNull(editorImpl)) {
			editorImpl = getUserEditorValue(request, editorImpl, user);
			String temp = PropsUtil.get(originalEditorImpl);

			if (editorImpl.equals("default") ||
				(editorImpl.equals(originalEditorImpl) &&
				Validator.isNotNull(temp))) {
					editorImpl = PropsUtil.get(originalEditorImpl);
			}
		}

		if (!BrowserSnifferUtil.isRtf(request)) {
			editorImpl = "simple";
		}

		if (Validator.isNull(editorImpl)) {
			editorImpl = _EDITOR_WYSIWYG_DEFAULT;
		}

		return editorImpl;
	}

	private static final String _EDITOR_WYSIWYG_DEFAULT = PropsUtil.get(
		PropsKeys.EDITOR_WYSIWYG_DEFAULT);

	private static final char[] _DELIMITERS = {
		CharPool.UNDERLINE,
		CharPool.DASH,
		CharPool.PERIOD
	};

}