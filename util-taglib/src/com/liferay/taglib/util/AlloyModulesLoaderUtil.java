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

package com.liferay.taglib.util;

import com.caucho.quercus.lib.UnserializeReader;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.util.ContentUtil;

import java.util.*;

/**
 * @author Iliyan Peychev
 * @author Miguel Pastor
 */
public class AlloyModulesLoaderUtil {

	public static Set<String> expandModulesList(
			Set<String> modules, boolean includeDependent,
				boolean includeSkinModules, boolean includeLangModules) {

		JSONObject moduleDependencies =
			AlloyModulesLoaderUtil.getModuleDependencies();

		Set<String> expandedSet = new TreeSet<String>();

		modules = filterRequiredModules(modules);

		for (String module : modules) {
			expandedSet.add(module);

			JSONObject moduleObj = moduleDependencies.getJSONObject(module);

			if (moduleObj == null || (!includeDependent && moduleObj.has("condition"))) {
				continue;
			}

			JSONArray requiredModules = moduleObj.getJSONArray("expanded");

			if (requiredModules != null && requiredModules.length() > 0) {
				for (int i = 0; i < requiredModules.length(); i++) {
					String requiredModule = requiredModules.getString(i);

					if (!includeSkinModules &&
						requiredModule.startsWith("skin-")) {
						continue;
					}
					else if (!includeLangModules &&
						requiredModule.startsWith("lang/")) {
						continue;
					}

					expandedSet.add(requiredModule);
				}
			}
		}

		return expandedSet;
	}

	public static Set<String> filterRequiredModules(Set<String> requiredModules) {
		Set<String> c = new TreeSet<String>();

		if (requiredModules != null) {
			for (String module : requiredModules) {
				JSONObject moduleDependencies = AlloyModulesLoaderUtil.getModuleDependencies();

				JSONObject mod = moduleDependencies.getJSONObject(module);

				JSONArray supersededes = null;

				if (mod != null) {
					supersededes = mod.getJSONArray("supersedes");
				}

				if (supersededes != null && supersededes.length() > 0) {
					for (int o = 0; o < supersededes.length(); o++) {
						//Must walk the other modules in case a module is a rollup of rollups (datatype)
						JSONObject m = moduleDependencies.getJSONObject(supersededes.getString(o));

						JSONArray mSupersededes = null;

						if (m != null) {
							mSupersededes = m.getJSONArray("supersedes");
						}

						if (mSupersededes != null && mSupersededes.length() > 0) {
							Set<String> tmp = new TreeSet<String>();

							for (int k = 0; k < mSupersededes.length(); k++) {
								tmp.add(mSupersededes.getString(k));
							}

							c.addAll(filterRequiredModules(tmp));
						} else {
							c.add(supersededes.getString(o));
						}
					}
				} else {
					c.add(module);
				}
			}

			requiredModules = c;
		}

		return requiredModules;
	}

	public static List<String> resolveRequirements(Set<String> requirements) {
		if (requirements == null) {
			return new ArrayList<String>();
		}

		List<String> listRequirements = new ArrayList<String>(requirements);

		int l, j, k, p = 0;

		String a, b;

		String doneKey;

		Map<String, Boolean> done = new HashMap<String, Boolean>();

		boolean moved = false;

		// keep going until we make a pass without moving anything
		for (;;) {
			l = listRequirements.size();
			moved = false;

			// start the loop after items that are already sorted
			for (j = p; j < l; j++) {

				// check the next module on the list to see if its
				// dependencies have been met
				a = listRequirements.get(j);

				// check everything below current item and move if we
				// find a requirement for the current item
				for (k = j + 1; k < l; k++) {
					doneKey = a + listRequirements.get(k);

					if (!done.containsKey(doneKey) && _requires(listRequirements, a, listRequirements.get(k))) {

						// extract the dependency so we can move it up
						b = listRequirements.remove(k);

						// insert the dependency above the item that
						// requires it
						listRequirements.add(j, b);

						// only swap two dependencies once to short circut
						// circular dependencies
						done.put(doneKey, true);

						// keep working
						moved = true;

						break;
					}
				}

				// jump out of loop if we moved something
				if (moved) {
					break;
				// this item is sorted, move our pointer and keep going
				} else {
					p++;
				}
			}

			// when we make it here and moved is false, we are
			// finished sorting
			if (!moved) {
				break;
			}
		}

		return listRequirements;
	}

	private static boolean _requires(List<String> requirements, String mod1, String mod2) {
		JSONObject moduleDependencies = getModuleDependencies();

		JSONObject m = moduleDependencies.getJSONObject(mod1);

		JSONObject other = moduleDependencies.getJSONObject(mod2);

		if (m == null || other == null) {
			return false;
		}

		JSONObject after_map = m.getJSONObject("after_map");

		// check if this module should be sorted after the other
		// do this first to short circut circular deps
		if (after_map != null && after_map.has(mod2)) {
			return true;
		}

		after_map = other.getJSONObject("after_map");

		// and vis-versa
		if (after_map != null && after_map.has(mod1)) {
			return false;
		}

		// check if this module requires one the other supersedes

		JSONObject tmp = moduleDependencies.getJSONObject(mod2);

		if (tmp!= null) {
			JSONArray s = tmp.getJSONArray("supersedes");

			if (s != null) {
				for (int i = 0; i < s.length(); i++) {
					if (_requires(requirements, mod1, s.getString(i))) {
						return true;
					}
				}
			}
		}

		tmp = moduleDependencies.getJSONObject(mod1);

		if (tmp != null) {
			JSONArray s = tmp.getJSONArray("supersedes");

			if (s != null) {
				for (int i = 0; i < s.length(); i++) {
					if (_requires(requirements, mod2, s.getString(i))) {
						return false;
					}
				}
			}
		}

		// check if this module requires the other directly
		// if (r && YArray.indexOf(r, mod2) > -1) {
		JSONObject rm = m.getJSONObject("expanded_map");

		if (rm != null && rm.has(mod2)) {
			return true;
		}

		// external css files should be sorted below yui css
		if (m.getBoolean("ext") && m.getString("type").equals("css") && !other.getBoolean("ext") && other.getString("type").equals("css")) {
			return true;
		}

		return false;
	}

	public static String getURL(
		String comboURL, String auiPath, String modulePrefix,
		Map<String, String> params, List<String> modules, String type,
		boolean includeDependent) {

		JSONObject moduleDependencies = getModuleDependencies();

		StringBuilder modulesBuffer = new StringBuilder();

		for (String moduleName : modules) {
			JSONObject module = moduleDependencies.getJSONObject(moduleName);

			if (module == null) {
				continue;
			}

			if (!includeDependent && module.getBoolean("condition")) {
				continue;
			}

			if (module.getString("type").equalsIgnoreCase(type)) {
				if (modulesBuffer.length() > 0) {
					modulesBuffer.append(StringPool.AMPERSAND);
				}

				modulesBuffer.append(modulePrefix);

				modulesBuffer.append(StringPool.EQUAL);

				modulesBuffer.append(auiPath);

				modulesBuffer.append(module.getString("path"));
			}
		}

		if (modulesBuffer.length() == 0) {
			return StringPool.BLANK;
		}

		StringBuilder urlBuffer = new StringBuilder();

		urlBuffer.append(comboURL);

		urlBuffer.append(StringPool.AMPERSAND);

		urlBuffer.append(modulesBuffer.toString());

		if (params != null) {
			for (String param : params.keySet()) {
				urlBuffer.append(StringPool.AMPERSAND);

				urlBuffer.append(param);

				urlBuffer.append(StringPool.EQUAL);

				urlBuffer.append(params.get(param));
			}
		}

		String result = urlBuffer.toString();

		return result;
	}

	public static JSONObject getModuleDependencies() {
		if (_moduleDependencies == null) {
			_moduleDependencies = _loadAlloyModuleDependencies("modules.json");
		}

		return _moduleDependencies;
	}

	private static JSONObject _loadAlloyModuleDependencies(String file) {
		JSONObject modulesDependencies = JSONFactoryUtil.createJSONObject();

		try
		{
			String fileContent = ContentUtil.get(file);

			modulesDependencies = JSONFactoryUtil.createJSONObject(fileContent);

		} catch (JSONException e) {
			_log.error(
				"An error occurred while converting " + file + " to JSON");
		}

		return modulesDependencies;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AlloyModulesLoaderUtil.class);

	private static JSONObject _moduleDependencies;

}