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

package com.liferay.portlet.social.model.impl;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portlet.social.model.SocialActivityCounterDefinition;
import com.liferay.portlet.social.util.SocialStatsUtil;

/**
 * @author Zsolt Berentey
 */
public class SocialActivityLimitImpl extends SocialActivityLimitBaseImpl {

	public int getCount(int limitPeriod) {
		int activityDay = SocialStatsUtil.getActivityDay();
		String[] parts = StringUtil.split(getValue(), StringPool.SLASH);
		int count = GetterUtil.getInteger(parts[parts.length-1], 0);

		if (limitPeriod !=
				SocialActivityCounterDefinition.LIMIT_PERIOD_LIFETIME &&
			parts.length < 2) {

			return 0;
		}

		if (limitPeriod ==
				SocialActivityCounterDefinition.LIMIT_PERIOD_LIFETIME) {

			return count;
		}
		else if (limitPeriod ==
				SocialActivityCounterDefinition.LIMIT_PERIOD_PERIOD) {

			String[] periodBounds = StringUtil.split(parts[0], StringPool.DASH);

			int periodStart = GetterUtil.getInteger(periodBounds[0], 0);
			int periodEnd = GetterUtil.getInteger(periodBounds[1], 0);

			if (activityDay >= periodStart && activityDay <= periodEnd) {
				return count;
			}
		}
		else if (limitPeriod ==
				SocialActivityCounterDefinition.LIMIT_PERIOD_DAY) {

			if (GetterUtil.getInteger(parts[0], 0) == activityDay) {
				return count;
			}
		}

		return 0;
	}

	public void setCount(int limitPeriod, int count) {
		if (limitPeriod == SocialActivityCounterDefinition.LIMIT_PERIOD_DAY) {
			setValue(
				String.valueOf(SocialStatsUtil.getActivityDay()) +
					StringPool.SLASH + String.valueOf(count));
		}
		else if (limitPeriod ==
					SocialActivityCounterDefinition.LIMIT_PERIOD_PERIOD) {

			setValue(
				String.valueOf(SocialStatsUtil.getCurrentStartPeriod()) +
					StringPool.DASH +
					String.valueOf(SocialStatsUtil.getCurrentEndPeriod()) +
					StringPool.SLASH + String.valueOf(count));
		}
		else if (limitPeriod ==
				SocialActivityCounterDefinition.LIMIT_PERIOD_LIFETIME) {

			setValue(String.valueOf(count));
		}
	}

}