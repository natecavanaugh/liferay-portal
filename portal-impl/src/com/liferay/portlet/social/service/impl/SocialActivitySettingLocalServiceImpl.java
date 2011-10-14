/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.social.service.impl;

import com.liferay.portal.json.JSONObjectImpl;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.Group;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.social.model.SocialActivityCounterDefinition;
import com.liferay.portlet.social.model.SocialActivityDefinition;
import com.liferay.portlet.social.model.SocialActivitySetting;
import com.liferay.portlet.social.service.base.SocialActivitySettingLocalServiceBaseImpl;
import com.liferay.portlet.social.util.SocialConfigurationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class SocialActivitySettingLocalServiceImpl
	extends SocialActivitySettingLocalServiceBaseImpl {

	public SocialActivityDefinition getActivityDefinition(
			long groupId, String className, int activityKey)
		throws SystemException {

		String key = encodeKey(groupId, className, activityKey);

		SocialActivityDefinition activityDefinition =
			(SocialActivityDefinition)_portalCache.get(key);

		if (activityDefinition != null) {
			return activityDefinition;
		}

		SocialActivityDefinition defaultDefinition =
			SocialConfigurationUtil.getActivityDefinition(
				className, activityKey);

		if (defaultDefinition == null) {
			return null;
		}

		activityDefinition = getActivityDefinition(
			groupId, className, activityKey, defaultDefinition);

		_portalCache.put(key, activityDefinition);

		return activityDefinition;
	}

	public List<SocialActivityDefinition> getActivityDefinitions(
			long groupId, String className)
		throws SystemException {

		List<SocialActivityDefinition> activityDefinitions =
			new ArrayList<SocialActivityDefinition>();

		List<SocialActivityDefinition> defaults =
			SocialConfigurationUtil.getActivityDefinitions(className);

		for (SocialActivityDefinition defaultDefinition : defaults) {
			activityDefinitions.add(
				getActivityDefinition(
					groupId, className, defaultDefinition.getActivityType()));
		}

		return activityDefinitions;
	}

	public List<SocialActivitySetting> getGroupSettings(long groupId)
		throws SystemException {

		return socialActivitySettingPersistence.findByG_A(groupId, 0);
	}

	public boolean isModelEnabled(long groupId, long classNameId)
		throws SystemException {

		boolean enabled = false;

		SocialActivitySetting groupSetting =
			socialActivitySettingPersistence.fetchByG_C_A_N(
				groupId, classNameId, 0, "enabled");

		if (groupSetting != null) {
			enabled = GetterUtil.getBoolean(groupSetting.getValue(), false);
		}

		return enabled;
	}

	public void updateActivitySettings(
			long groupId, String className, int activityKey,
			List<SocialActivityCounterDefinition> counters)
		throws PortalException, SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		SocialActivityDefinition defaultActivityDefinition =
			SocialConfigurationUtil.getActivityDefinition(
				className, activityKey);

		for (SocialActivityCounterDefinition counter : counters) {
			SocialActivitySetting activitySetting =
				socialActivitySettingPersistence.fetchByG_C_A_N(
					groupId, classNameId, activityKey, counter.getName());

			if (activitySetting != null) {
				SocialActivityCounterDefinition activityDefinition =
					defaultActivityDefinition.getActivityCounterDefinition(
						counter.getName());

				if ((activityDefinition != null) &&
					(activityDefinition.equals(counter))) {

					socialActivitySettingPersistence.remove(activitySetting);

					continue;
				}
				else {
					activitySetting.setValue(createJSONSetting(counter));
				}
			}
			else {
				Group group = groupLocalService.getGroup(groupId);

				long activitySettingId = counterLocalService.increment();

				activitySetting = socialActivitySettingPersistence.create(
					activitySettingId);

				activitySetting.setActivityType(activityKey);
				activitySetting.setClassNameId(classNameId);
				activitySetting.setCompanyId(group.getCompanyId());
				activitySetting.setGroupId(groupId);
				activitySetting.setName(counter.getName());
				activitySetting.setValue(createJSONSetting(counter));
			}

			socialActivitySettingPersistence.update(activitySetting, false);
		}

		_portalCache.remove(encodeKey(groupId, className, activityKey));
	}

	public void updateGroupSetting(
			long groupId, String className, boolean enabled)
		throws PortalException, SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		SocialActivitySetting groupSetting =
			socialActivitySettingPersistence.fetchByG_C_A_N(
				groupId, classNameId, 0, "enabled");

		if (groupSetting == null) {
			Group group = groupLocalService.getGroup(groupId);

			long activitySettingId = counterLocalService.increment();

			groupSetting = socialActivitySettingPersistence.create(
				activitySettingId);

			groupSetting.setClassNameId(classNameId);
			groupSetting.setCompanyId(group.getCompanyId());
			groupSetting.setGroupId(groupId);
			groupSetting.setName("enabled");
		}

		groupSetting.setValue(String.valueOf(enabled));

		socialActivitySettingPersistence.update(groupSetting, false);
	}

	protected String createJSONSetting(
		SocialActivityCounterDefinition counter) {

		JSONObject settings = JSONFactoryUtil.createJSONObject();

		settings.put("ownerType", counter.getOwnerType());
		settings.put("value", counter.getIncrement());
		settings.put("limitValue", counter.getLimitValue());
		settings.put("limitPeriod", counter.getLimitPeriod());

		return settings.toString();
	}

	protected String encodeKey(
		long groupId, String className, int activityKey) {

		StringBundler sb = new StringBundler(100);

		sb.append(_CACHE_NAME);
		sb.append(StringPool.POUND);
		sb.append(StringUtil.toHexString(groupId));
		sb.append(StringPool.POUND);
		sb.append(className);
		sb.append(StringPool.POUND);
		sb.append(StringUtil.toHexString(activityKey));

		return sb.toString();
	}

	protected SocialActivityDefinition getActivityDefinition(
			long groupId, String className, int activityKey,
			SocialActivityDefinition defaultDefinition)
		throws SystemException {

		SocialActivityDefinition activityDefinition = null;

		List<SocialActivitySetting> activitySettings =
			getActivitySettings(
				groupId, className, defaultDefinition.getActivityType());

		activityDefinition = defaultDefinition.clone();

		for (SocialActivitySetting activitySetting : activitySettings) {
			if (activitySetting.getName().equals("logEnabled")) {
				activityDefinition.setLogActivity(
					GetterUtil.getBoolean(
						activitySetting.getValue(),
						defaultDefinition.isLogActivity()));
			}
			else if (activitySetting.getName().equals("statsEnabled")) {
				activityDefinition.setCounterEnabled(
					GetterUtil.getBoolean(
						activitySetting.getValue(),
						defaultDefinition.isCounterEnabled()));
			}
			else {
				JSONObject settings = null;

				try {
					settings = JSONFactoryUtil.createJSONObject(
						activitySetting.getValue());
 				}
				catch (JSONException je) {
					settings = new JSONObjectImpl();
				}

				SocialActivityCounterDefinition counter =
					activityDefinition.getActivityCounterDefinition(
						activitySetting.getName());

				if (counter == null) {
					counter = new SocialActivityCounterDefinition();

					counter.setName(activitySetting.getName());

					activityDefinition.addCounter(counter);
				}

				counter.setOwnerType(settings.getInt("ownerType"));
				counter.setIncrement(settings.getInt("value"));
				counter.setLimitValue(settings.getInt("limitValue"));
				counter.setLimitPeriod(settings.getInt("limitPeriod"));
			}
		}

		return activityDefinition;
	}

	protected List<SocialActivitySetting> getActivitySettings(
			long groupId, String className, int activityKey)
		throws SystemException {

		long classNameId = PortalUtil.getClassNameId(className);

		List<SocialActivitySetting> activitySettings =
			socialActivitySettingPersistence.findByG_C_A(
				groupId, classNameId, activityKey);

		return activitySettings;
	}

	private static final String _CACHE_NAME =
		SocialActivitySettingLocalServiceImpl.class.getName();

	private static PortalCache _portalCache = MultiVMPoolUtil.getCache(
		_CACHE_NAME);

}