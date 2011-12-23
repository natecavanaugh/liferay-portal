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

package com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest;

import com.liferay.portalweb.portal.BaseTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.address.Address523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.announcements.Announcements523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.announcementsdelivery.AnnouncementsDelivery523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.bookmarks.Bookmarks523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.calendar.Calendar523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.community.Community523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.documentlibrary.DocumentLibrary523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.expando.Expando523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.groups.Groups523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.login.LoginTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.organizations.Organizations523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.portletpermissions.PortletPermissions523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.shopping.Shopping523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.social.Social523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.stagingcommunity.StagingCommunity523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.stagingorganization.StagingOrganization523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.tags.Tags523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.webcontent.WebContent523LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.wiki.Wiki523LatestTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewSampleData523LatestTests extends BaseTests {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();

		testSuite.addTest(LoginTests.suite());
		testSuite.addTest(Address523LatestTests.suite());
		testSuite.addTest(Announcements523LatestTests.suite());
		testSuite.addTest(AnnouncementsDelivery523LatestTests.suite());
		testSuite.addTest(Bookmarks523LatestTests.suite());
		testSuite.addTest(Calendar523LatestTests.suite());
		testSuite.addTest(Community523LatestTests.suite());
		testSuite.addTest(DocumentLibrary523LatestTests.suite());
		testSuite.addTest(Expando523LatestTests.suite());
		testSuite.addTest(Groups523LatestTests.suite());
		testSuite.addTest(Organizations523LatestTests.suite());
		testSuite.addTest(PortletPermissions523LatestTests.suite());
		testSuite.addTest(Shopping523LatestTests.suite());
		testSuite.addTest(Social523LatestTests.suite());
		testSuite.addTest(StagingCommunity523LatestTests.suite());
		testSuite.addTest(StagingOrganization523LatestTests.suite());
		testSuite.addTest(Tags523LatestTests.suite());
		testSuite.addTest(WebContent523LatestTests.suite());
		testSuite.addTest(Wiki523LatestTests.suite());

		return testSuite;
	}

}