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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.social.NoSuchActivityCounterException;
import com.liferay.portlet.social.model.SocialAchievement;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.model.SocialActivityCounter;
import com.liferay.portlet.social.model.SocialActivityCounterConstants;
import com.liferay.portlet.social.model.SocialActivityCounterDefinition;
import com.liferay.portlet.social.model.SocialActivityDefinition;
import com.liferay.portlet.social.model.SocialActivityLimit;
import com.liferay.portlet.social.model.SocialActivityUserStats;
import com.liferay.portlet.social.service.base.SocialActivityCounterLocalServiceBaseImpl;
import com.liferay.portlet.social.service.persistence.SocialActivityCounterFinderUtil;
import com.liferay.portlet.social.util.SocialStatsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zsolt Berentey
 */
public class SocialActivityCounterLocalServiceImpl
	extends SocialActivityCounterLocalServiceBaseImpl {

	public void addActivityStats(SocialActivity activity)
		throws PortalException, SystemException {

		if (!socialActivitySettingLocalService.isModelEnabled(
				activity.getGroupId(), activity.getClassNameId())) {

			return;
		}

		User user = userPersistence.findByPrimaryKey(activity.getUserId());

		SocialActivityDefinition activityDefinition =
			socialActivitySettingLocalService.getActivityDefinition(
				activity.getGroupId(), activity.getClassName(),
				activity.getType());

		if (activityDefinition == null ||
			!activityDefinition.isCounterEnabled()) {

			return;
		}

		// Activity Handler

		if (activityDefinition.getActivityProcessor() != null) {
			activityDefinition.getActivityProcessor().processActivity(activity);
		}

		// Counters

		for (SocialActivityCounterDefinition activityCounterDefinition :
				activityDefinition.getActivityCounterDefinitions()) {

			if (activityCounterDefinition.getIncrement() != 0) {
				if (checkLimit(user, activity, activityCounterDefinition)) {
					incrementCounter(
						activity.getGroupId(), user, activity.getAssetEntry(),
						activityCounterDefinition);
				}
			}
		}

		// Achievements

		List<SocialAchievement> achievements =
			activityDefinition.getAchievements();

		for (SocialAchievement achievement : achievements) {
			achievement.processActivity(activity);
		}

		// Activity counter

		incrementCounter(
			activity.getGroupId(), activity.getClassNameId(),
			activity.getClassPK(), SocialActivityCounterConstants.TYPE_ASSET,
			SocialActivityCounterConstants.NAME_ASSET_ACTIVITY, 1);

		incrementCounter(
			activity.getGroupId(),
			PortalUtil.getClassNameId(User.class.getName()),
			activity.getUserId(), SocialActivityCounterConstants.TYPE_ACTOR,
			SocialActivityCounterConstants.NAME_USER_ACTIVITY, 1);
	}

	@Transactional(
		rollbackFor = SystemException.class,
		propagation = Propagation.REQUIRES_NEW)
	public SocialActivityCounter addActivityCounter(
			long companyId, long groupId, long classNameId, long classPK,
			int ownerType, String statName, int currentValue, int totalValue)
		throws SystemException {

		long id = counterLocalService.increment();

		SocialActivityCounter newActivityCounter =
			socialActivityCounterPersistence.create(id);

		newActivityCounter.setCompanyId(companyId);
		newActivityCounter.setGroupId(groupId);
		newActivityCounter.setClassPK(classPK);
		newActivityCounter.setClassNameId(classNameId);
		newActivityCounter.setOwnerType(ownerType);
		newActivityCounter.setName(statName);
		newActivityCounter.setCurrentValue(currentValue);
		newActivityCounter.setTotalValue(totalValue);
		newActivityCounter.setStartPeriod(
			SocialStatsUtil.getCurrentStartPeriod());
		newActivityCounter.setEndPeriod(-1);

		socialActivityCounterPersistence.update(
			newActivityCounter, false);

		return newActivityCounter;
	}

	public void deleteActivityCounters(AssetEntry assetEntry)
		throws SystemException {

		if (assetEntry != null) {
			socialActivityLimitPersistence.removeByC_C(
				assetEntry.getClassNameId(), assetEntry.getClassPK());

			SocialActivityCounter lastAssetCounter = fetchLastCounter(
				assetEntry.getGroupId(),
				assetEntry.getClassNameId(),
				assetEntry.getClassPK(),
				SocialActivityCounterConstants.TYPE_ASSET,
				SocialActivityCounterConstants.NAME_POPULARITY);

			SocialActivityCounter lastContributionEntry = fetchLastCounter(
				assetEntry.getGroupId(),
				PortalUtil.getClassNameId(User.class.getName()),
				assetEntry.getUserId(),
				SocialActivityCounterConstants.TYPE_CREATOR,
				SocialActivityCounterConstants.NAME_CONTRIBUTION);

			if (lastContributionEntry != null && lastAssetCounter != null) {
				if (lastContributionEntry.getStartPeriod()
						!= SocialStatsUtil.getCurrentStartPeriod()) {

					lastContributionEntry = createNewPeriod(
						lastContributionEntry);
				}

				lastContributionEntry.setTotalValue(
					lastContributionEntry.getTotalValue() -
						lastAssetCounter.getTotalValue());

				if (lastAssetCounter.getStartPeriod() ==
						SocialStatsUtil.getCurrentStartPeriod()) {

					lastContributionEntry.setCurrentValue(
						lastContributionEntry.getCurrentValue() -
							lastAssetCounter.getCurrentValue());
				}

				socialActivityCounterPersistence.update(
					lastContributionEntry, false);
			}
		}

		deleteActivityCounters(
			assetEntry.getClassNameId(), assetEntry.getClassPK());
	}

	public void deleteActivityCounters(long classNameId, long classPK)
		throws SystemException {

		socialActivityCounterPersistence.removeByC_C(classNameId, classPK);
	}

	public SocialActivityCounter fetchLastCounter(
			long groupId, long classNameId, long classPK, int ownerType,
			String counterName)
		throws SystemException {

		return socialActivityCounterPersistence.fetchByG_C_C_N_O_E(
			groupId, classNameId, classPK, counterName, ownerType, -1);
	}

	public SocialActivityCounter findLastCounter(
			long groupId, long classNameId, long classPK, int ownerType,
			String counterName)
		throws NoSuchActivityCounterException, SystemException {

		return socialActivityCounterPersistence.findByG_C_C_N_O_E(
			groupId, classNameId, classPK, counterName, ownerType, -1);
	}

	public List<SocialActivityCounter> getCounterDistribution(
			long groupId, String statName, int lastPeriods,
			boolean includeCurrent)
		throws SystemException {

		if (includeCurrent) {
			lastPeriods = lastPeriods - 1;
		}

		int start = SocialStatsUtil.getStartPeriod(-lastPeriods);
		int end = -1;

		if (!includeCurrent) {
			end = SocialStatsUtil.getCurrentStartPeriod() - 1;
		}

		return getCounterDistribution(groupId, statName, start, end);
	}

	public List<SocialActivityCounter> getCounterDistribution(
			long groupId, String statName, int statPeriodStart,
			int statPeriodEnd)
		throws SystemException {

		List<SocialActivityCounter> entries =
			socialActivityCounterFinder.findCounterDistributionByNameAndPeriod(
				groupId, statName, statPeriodStart, statPeriodEnd);

		return entries;
	}

	public List<SocialActivityCounter> getCounters(
			long groupId, String statName, int lastPeriods,
			boolean includeCurrent)
		throws SystemException {

		if (includeCurrent) {
			lastPeriods = lastPeriods - 1;
		}

		int start = SocialStatsUtil.getStartPeriod(-lastPeriods);
		int end = -1;

		if (!includeCurrent) {
			end = SocialStatsUtil.getCurrentStartPeriod() - 1;
		}

		return getCounters(groupId, statName, start, end);
	}

	public List<SocialActivityCounter> getCounters(
			long groupId, String statName, int statPeriodStart,
			int statPeriodEnd)
		throws SystemException {

		List<SocialActivityCounter> entries =
			new ArrayList<SocialActivityCounter>();

		int currentPeriodStart = SocialStatsUtil.getCurrentStartPeriod();
		int start = statPeriodStart;

		if (start > currentPeriodStart) {
			start = currentPeriodStart;
		}

		if (statPeriodEnd >= currentPeriodStart || statPeriodEnd == -1) {
			if (start < currentPeriodStart) {
				entries.addAll(
					socialActivityCounterFinder.findCountersByNameAndPeriod(
						groupId, statName, start, currentPeriodStart - 1));

				start = currentPeriodStart;
			}
		}

		entries.addAll(socialActivityCounterFinder.findCountersByNameAndPeriod(
			groupId, statName, start, statPeriodEnd));

		return entries;
	}

	public List<SocialActivityCounter> getTagsByCounter(
			long groupId, String statName, int lastPeriods,
			boolean includeCurrent)
		throws SystemException {

		if (includeCurrent) {
			lastPeriods = lastPeriods - 1;
		}

		int start = SocialStatsUtil.getStartPeriod(-lastPeriods);
		int end = -1;

		if (!includeCurrent) {
			end = SocialStatsUtil.getCurrentStartPeriod() - 1;
		}

		return getTagsByCounter(groupId, statName, start, end);
	}

	public List<SocialActivityCounter> getTagsByCounter(
			long groupId, String statName, int statPeriodStart,
			int statPeriodEnd)
		throws SystemException {

		List<SocialActivityCounter> entries =
			socialActivityCounterFinder.findTagsByCounter(
				groupId, statName, statPeriodStart, statPeriodEnd);

		return entries;
	}

	public List<SocialActivityUserStats> getUserStatsByCounters(
			long groupId, String[] rankingCounters, String[] selectedCounters,
			int start, int end)
		throws SystemException {

		List<SocialActivityUserStats> userStatsList =
			new ArrayList<SocialActivityUserStats>();

		List<Long> userIds =
			SocialActivityCounterFinderUtil.findTopUsersByCounters(
				groupId, rankingCounters, start, end);

		if (userIds.size() == 0) {
			return userStatsList;
		}

		List<SocialActivityCounter> statsEntries =
			SocialActivityCounterFinderUtil.findUserStatsByCounters(
				groupId, userIds, selectedCounters, -1, -1);

		long userId = 0;
		SocialActivityUserStats userStats = null;

		for (SocialActivityCounter counter : statsEntries) {
			if (counter.getClassPK() != userId) {
				userId = counter.getClassPK();
				userStats = new SocialActivityUserStats();

				userStats.setUserId(userId);

				userStatsList.add(userStats);
			}

			userStats.addStat(
				new KeyValuePair(
					counter.getName(),
					String.valueOf(counter.getCurrentValue())));
		}

		return userStatsList;
	}

	public int getUserStatsByCountersCount(
			long groupId, String[] rankingCounters)
		throws SystemException {

		return SocialActivityCounterFinderUtil.countTopUsersByCounters(
			groupId, rankingCounters);
	}

	public void incrementUserAchievementsCounter(long groupId, long userId)
		throws PortalException, SystemException {

		incrementCounter(
			groupId, PortalUtil.getClassNameId(User.class.getName()), userId,
			SocialActivityCounterConstants.TYPE_ACTOR,
			SocialActivityCounterConstants.NAME_USER_ACHIEVEMENT, 1);
	}

	protected boolean checkLimit(
			User user, SocialActivity activity,
			SocialActivityCounterDefinition counter)
		throws PortalException, SystemException {

		if (counter.getLimitValue() == 0) {
			return true;
		}

		SocialActivityLimit activityLimit =
			socialActivityLimitPersistence.fetchByG_U_C_C_A_A(
				activity.getGroupId(), user.getUserId(),
				activity.getClassNameId(), activity.getClassPK(),
				activity.getType(), counter.getName());

		if (activityLimit == null) {
			try {
				Group group = groupPersistence.findByPrimaryKey(
					activity.getGroupId());

				activityLimit =
					socialActivityLimitLocalService.addActivityLimit(
						group.getCompanyId(), activity.getGroupId(),
						user.getUserId(), activity.getClassNameId(),
						activity.getClassPK(), activity.getType(),
						counter.getName(), counter.getLimitPeriod());
			}
			catch (SystemException se) {
				activityLimit =
					socialActivityLimitPersistence.fetchByG_U_C_C_A_A(
						activity.getGroupId(), user.getUserId(),
						activity.getClassNameId(), activity.getClassPK(),
						activity.getType(), counter.getName());
			}
		}

		int actionCount = activityLimit.getCount(counter.getLimitPeriod());

		if (actionCount >= counter.getLimitValue()) {
			return false;
		}

		activityLimit.setCount(counter.getLimitPeriod(), actionCount + 1);

		socialActivityLimitPersistence.update(activityLimit, false);

		return true;
	}

	protected String createActionKey(String className, int activityKey) {
		return className + StringPool.UNDERLINE + activityKey;
	}

	protected SocialActivityCounter createActivityCounter(
			final long companyId, final long groupId, final long classNameId,
			final long classPK, final int classType, final String statName,
			final int overallValue)
		throws SystemException {

		SocialActivityCounter newEntry = null;

		try {
			newEntry =
				getSocialActivityCounterLocalService().addActivityCounter(
					companyId, groupId, classNameId, classPK, classType,
					statName, 0, overallValue);
		}
		catch (SystemException se) {
			newEntry = fetchLastCounter(
				groupId, classNameId, classPK, classType, statName);
		}

		return newEntry;
	}

	protected SocialActivityCounter createNewPeriod(
			SocialActivityCounter oldEntry)
		throws SystemException {

		int statPeriodStart = SocialStatsUtil.getCurrentStartPeriod();

		if (oldEntry != null) {
			oldEntry.setEndPeriod(statPeriodStart - 1);

			socialActivityCounterPersistence.update(oldEntry, false);

			return createActivityCounter(
				oldEntry.getCompanyId(), oldEntry.getGroupId(),
				oldEntry.getClassNameId(), oldEntry.getClassPK(),
				oldEntry.getOwnerType(), oldEntry.getName(),
				oldEntry.getTotalValue());
		}

		return null;
	}

	protected void incrementCounter(
			long groupId, User actor, AssetEntry assetEntry,
			SocialActivityCounterDefinition counter)
		throws PortalException, SystemException {

		int classType = counter.getOwnerType();
		long userClassNameId = PortalUtil.getClassNameId(User.class.getName());

		if (classType == SocialActivityCounterConstants.TYPE_ACTOR) {
			incrementCounter(
				groupId, userClassNameId, actor.getUserId(), classType,
				counter.getName(), counter.getIncrement());
		}
		else if (classType == SocialActivityCounterConstants.TYPE_ASSET) {
			incrementCounter(
				groupId, assetEntry.getClassNameId(), assetEntry.getClassPK(),
				classType, counter.getName(), counter.getIncrement());
		}
		else {
			incrementCounter(
				groupId, userClassNameId, assetEntry.getUserId(), classType,
				counter.getName(), counter.getIncrement());
		}
	}

	protected void incrementCounter(
			long groupId, long classNameId, long classPK, int ownerType,
			String counterName, int increment)
		throws PortalException, SystemException {

		SocialActivityCounter counter = fetchLastCounter(
			groupId, classNameId, classPK, ownerType, counterName);

		if (counter == null) {
			Group group = groupPersistence.findByPrimaryKey(groupId);

			counter = createActivityCounter(
				group.getCompanyId(), groupId, classNameId, classPK, ownerType,
				counterName, 0);
		}

		if (!counter.isPeriodActive()) {
			counter = createNewPeriod(counter);
		}

		counter.setCurrentValue(counter.getCurrentValue() + increment);
		counter.setTotalValue(counter.getTotalValue() + increment);

		socialActivityCounterPersistence.update(counter, false);
	}

}