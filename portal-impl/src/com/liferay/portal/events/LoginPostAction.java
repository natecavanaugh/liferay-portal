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

package com.liferay.portal.events;

import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsValues;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

/**
 * @author Brian Wing Shun Chan
 */
public class LoginPostAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			if (_log.isDebugEnabled()) {
				_log.debug("Running " + request.getRemoteUser());
			}

			HttpSession session = request.getSession();

			long companyId = PortalUtil.getCompanyId(request);
			long userId = 0;

			// Language

			session.removeAttribute(Globals.LOCALE_KEY);

			// Live users

			if (PropsValues.LIVE_USERS_ENABLED) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				ClusterNode clusterNode =
					ClusterExecutorUtil.getLocalClusterNode();

				if (clusterNode != null) {
					jsonObject.put(
						"clusterNodeId", clusterNode.getClusterNodeId());
				}

				jsonObject.put("command", "signIn");
				jsonObject.put("companyId", companyId);
				jsonObject.put("remoteAddr", request.getRemoteAddr());
				jsonObject.put("remoteHost", request.getRemoteHost());
				jsonObject.put("sessionId", session.getId());

				String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

				jsonObject.put("userAgent", userAgent);

				userId = PortalUtil.getUserId(request);

				jsonObject.put("userId", userId);

				MessageBusUtil.sendMessage(
					DestinationNames.LIVE_USERS, jsonObject.toString());
			}

			if (PrefsPropsUtil.getBoolean(
					companyId, PropsKeys.ADMIN_SYNC_DEFAULT_ASSOCIATIONS)) {

				if (userId == 0) {
					userId = PortalUtil.getUserId(request);
				}

				UserLocalServiceUtil.addDefaultGroups(userId);
				UserLocalServiceUtil.addDefaultRoles(userId);
				UserLocalServiceUtil.addDefaultUserGroups(userId);
			}

			User user = PortalUtil.getUser(request);

			PasswordPolicy passwordPolicy = user.getPasswordPolicy();

			if ((passwordPolicy != null) && passwordPolicy.isExpireable() &&
				(passwordPolicy.getWarningTime() > 0)) {

				int expireInXDays = _passwordRemainingDays(
					passwordPolicy, user);

				if (expireInXDays >= 0) {
					SessionMessages.add(
						request, "passwordExpireInXDays", expireInXDays);
				}
				else if (expireInXDays == -1) {
					int graceLoginRemaining =
						passwordPolicy.getGraceLimit() -
							user.getGraceLoginCount();

					SessionMessages.add(
						request, "graceLoginRemaining", graceLoginRemaining);
				}
			}
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	private int _passwordRemainingDays(PasswordPolicy passwordPolicy, User user)
		throws PortalException {

		Date now = new Date();

		if (user.getPasswordModifiedDate() == null) {
			user.setPasswordModifiedDate(now);

			UserLocalServiceUtil.updateUser(user);
		}

		long timeModified = user.getPasswordModifiedDate().getTime();

		long passwordExpiresOn =
			(passwordPolicy.getMaxAge() * 1000) + timeModified;

		long timeStartWarning =
			passwordExpiresOn - (passwordPolicy.getWarningTime() * 1000);

		if (now.getTime() > timeStartWarning) {
			long dayTime = 24 * 60 * 60 * 1000;

			int remainingDays =
				(int)((passwordExpiresOn - now.getTime()) / dayTime);

			return (remainingDays > -1) ? remainingDays : -1;
		}

		return -2;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoginPostAction.class);

}