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
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.address.Address606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.announcements.Announcements606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.announcementsdelivery.AnnouncementsDelivery606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.bookmarks.Bookmarks606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.calendar.Calendar606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.community.Community606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.documentlibrary.DocumentLibrary606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.expando.Expando606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.groups.Groups606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.login.LoginTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.organizations.Organizations606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.portletpermissions.PortletPermissions606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.shopping.Shopping606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.social.Social606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.stagingcommunity.StagingCommunity606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.stagingorganization.StagingOrganization606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.tags.Tags606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.webcontent.WebContent606LatestTests;
import com.liferay.portalweb.portal.dbupgrade.viewsampledatalatest.wiki.Wiki606LatestTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewSampleData606LatestTests extends BaseTests {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();

		testSuite.addTest(LoginTests.suite());
		testSuite.addTest(Address606LatestTests.suite());
		testSuite.addTest(Announcements606LatestTests.suite());
		testSuite.addTest(AnnouncementsDelivery606LatestTests.suite());
		testSuite.addTest(Bookmarks606LatestTests.suite());
		testSuite.addTest(Calendar606LatestTests.suite());
		testSuite.addTest(Community606LatestTests.suite());
		testSuite.addTest(DocumentLibrary606LatestTests.suite());
		testSuite.addTest(Expando606LatestTests.suite());
		testSuite.addTest(Groups606LatestTests.suite());
		testSuite.addTest(Organizations606LatestTests.suite());
		testSuite.addTest(PortletPermissions606LatestTests.suite());
		testSuite.addTest(Shopping606LatestTests.suite());
		testSuite.addTest(Social606LatestTests.suite());
		testSuite.addTest(StagingCommunity606LatestTests.suite());
		testSuite.addTest(StagingOrganization606LatestTests.suite());
		testSuite.addTest(Tags606LatestTests.suite());
		testSuite.addTest(WebContent606LatestTests.suite());
		testSuite.addTest(Wiki606LatestTests.suite());

		return testSuite;
	}

}