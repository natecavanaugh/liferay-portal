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

package com.liferay.portal.model;

import com.liferay.portal.service.persistence.LayoutRevisionUtil;
import com.liferay.portal.service.persistence.LayoutUtil;
import com.liferay.portal.servlet.filters.cache.CacheUtil;

/**
 * @author Alexander Chow
 * @author Raymond Augé
 */
public class PortletPreferencesListener
	extends BaseModelListener<PortletPreferences> {

	@Override
	public void onAfterRemove(PortletPreferences portletPreferences) {
		clearCache(portletPreferences);
	}

	@Override
	public void onAfterUpdate(PortletPreferences portletPreferences) {
		clearCache(portletPreferences);
	}

	protected void clearCache(PortletPreferences portletPreferences) {
		try {
			long companyId = 0;

			Layout layout = LayoutUtil.fetchByPrimaryKey(
				portletPreferences.getPlid());

			if ((layout != null) && !layout.isPrivateLayout()) {
				companyId = layout.getCompanyId();
			}
			else {
				LayoutRevision layoutRevision =
					LayoutRevisionUtil.fetchByPrimaryKey(
						portletPreferences.getPlid());

				if ((layoutRevision != null) &&
					!layoutRevision.isPrivateLayout()) {

					companyId = layoutRevision.getCompanyId();
				}
			}

			if (companyId > 0) {
				CacheUtil.clearCache(companyId);
			}
		}
		catch (Exception e) {
			CacheUtil.clearCache();
		}
	}

}