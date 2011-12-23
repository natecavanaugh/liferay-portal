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

package com.liferay.portalweb.portal.controlpanel.organizations;

import com.liferay.portalweb.portal.BaseTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organization.OrganizationTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationaddress.OrganizationAddressTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationcomment.OrganizationCommentTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationemailaddress.OrganizationEmailAddressTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationpage.OrganizationPageTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationphonenumber.OrganizationPhoneNumberTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationservice.OrganizationServiceTests;
import com.liferay.portalweb.portal.controlpanel.organizations.organizationwebsite.OrganizationWebsiteTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Brian Wing Shun Chan
 */
public class OrganizationsTests extends BaseTests {

	public static Test suite() {
		TestSuite testSuite = new TestSuite();

		testSuite.addTest(OrganizationTests.suite());
		testSuite.addTest(OrganizationCommentTests.suite());
		testSuite.addTest(OrganizationAddressTests.suite());
		testSuite.addTest(OrganizationEmailAddressTests.suite());
		testSuite.addTest(OrganizationPageTests.suite());
		testSuite.addTest(OrganizationPhoneNumberTests.suite());
		testSuite.addTest(OrganizationServiceTests.suite());
		testSuite.addTest(OrganizationWebsiteTests.suite());

		return testSuite;
	}

}