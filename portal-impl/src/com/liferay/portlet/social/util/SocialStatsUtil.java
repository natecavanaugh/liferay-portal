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

package com.liferay.portlet.social.util;

import com.liferay.ibm.icu.util.Calendar;
import com.liferay.ibm.icu.util.GregorianCalendar;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.PropsValues;

import java.util.Date;

/**
 * @author Zsolt Berentey
 */
public class SocialStatsUtil {

	public static int getActivityDay() {
		return getActivityDay(System.currentTimeMillis());
	}

	public static int getActivityDay(long timeInMillis) {
		long currentTimeInDays = timeInMillis / _DAYS_IN_MILLIS;

		return (int)(currentTimeInDays - _baseTimeInDays);
	}

	public static int getCurrentStartPeriod() {
		if (!isWithinPeriod(
				_currentStartPeriod, _currentEndPeriod, getActivityDay())) {

			if (PropsValues.SOCIAL_ACTIVITY_COUNTER_PERIOD_LENGTH.equals(
					"month")) {

				Calendar calendar = new GregorianCalendar();

				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				_currentStartPeriod = getActivityDay(
					calendar.getTime().getTime());
			}
			else {
				int currentDay = getActivityDay();

				_currentStartPeriod = (int)(currentDay /
					getCounterPeriodLength()) * getCounterPeriodLength();
			}
		}

		return _currentStartPeriod;
	}

	public static int getCurrentEndPeriod() {
		if (!isWithinPeriod(
				_currentStartPeriod, _currentEndPeriod, getActivityDay())) {

			_currentEndPeriod = getCurrentStartPeriod() + getCounterPeriodLength()
				- 1;
		}

		return _currentEndPeriod;
	}

	public static Date getDate(int activityDay) {
		long timeInMillis = activityDay * _DAYS_IN_MILLIS;

		return new Date(_baseActivityCalendar.getTime().getTime() +
			timeInMillis);
	}

	public static int getFirstAtivityDayOfYear() {
		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return getActivityDay(calendar.getTime().getTime());
	}

	public static int getEndPeriod(int periodModifier) {
		if (PropsValues.SOCIAL_ACTIVITY_COUNTER_PERIOD_LENGTH.equals(
				"month")) {

			Calendar calendar = new GregorianCalendar();

			calendar.set(Calendar.DATE, 1);
			calendar.add(Calendar.MONTH, periodModifier + 1);
			calendar.add(Calendar.DATE, -1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			return getActivityDay(calendar.getTime().getTime());
		}
		else {
			return getCurrentEndPeriod() -
				(periodModifier * getCounterPeriodLength());
		}
	}

	public static int getCounterPeriodLength() {
		if (PropsValues.SOCIAL_ACTIVITY_COUNTER_PERIOD_LENGTH.equals("month")) {
			if (!isWithinPeriod(
					_currentStartPeriod, _currentEndPeriod, getActivityDay())) {

				Calendar calendar = new GregorianCalendar();

				calendar.set(Calendar.DATE, 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);

				_currentStartPeriod = getActivityDay(
					calendar.getTime().getTime());
				_currentPeriodLength =	calendar.getActualMaximum(
					Calendar.DAY_OF_MONTH);
				_currentEndPeriod = _currentStartPeriod + _currentPeriodLength
					- 1;
			}
		}
		else {
			if (_currentPeriodLength == 0) {
				_currentPeriodLength = GetterUtil.getInteger(
					PropsValues.SOCIAL_ACTIVITY_COUNTER_PERIOD_LENGTH);
			}
		}

		return _currentPeriodLength;
	}

	public static int getStartPeriod(int periodModifier) {
		if (PropsValues.SOCIAL_ACTIVITY_COUNTER_PERIOD_LENGTH.equals(
				"month")) {

			Calendar calendar = new GregorianCalendar();

			calendar.set(Calendar.DATE, 1);
			calendar.add(Calendar.MONTH, periodModifier);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			return getActivityDay(calendar.getTime().getTime());
		}
		else {
			return getCurrentStartPeriod() -
				(periodModifier * getCounterPeriodLength());
		}
	}

	public static boolean isWithinPeriod() {
		return isWithinPeriod(
			getCurrentStartPeriod(), getCurrentEndPeriod(), getActivityDay());
	}

	public static boolean isWithinPeriod(int start, int end, int value) {
		return (value >= start && value <= end);
	}

	private static final long _DAYS_IN_MILLIS = 1000 * 60 * 60 * 24;

	private static Calendar _baseActivityCalendar =
		new GregorianCalendar(2010, Calendar.JANUARY, 1);
	private static long _baseTimeInDays =
		_baseActivityCalendar.getTime().getTime() / _DAYS_IN_MILLIS;
	private static int _currentEndPeriod = 0;
	private static int _currentPeriodLength = -1;
	private static int _currentStartPeriod = 0;

}