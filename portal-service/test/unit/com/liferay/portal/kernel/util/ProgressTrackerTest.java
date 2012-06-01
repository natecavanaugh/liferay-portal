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

package com.liferay.portal.kernel.util;

import com.liferay.portal.upload.LiferayFileUpload;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

/**
 * @author Sergio González
 */
public class ProgressTrackerTest {

	@Test
	public void testGetMessage() throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession();

		MockInstallProcess process = new MockInstallProcess(mockHttpSession);

		process.initialize();

		ProgressTracker progressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertEquals(
			StringPool.BLANK, progressTracker.getMessage());

		process.download();

		progressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertEquals(
			"downloading", progressTracker.getMessage());

		process.finish();
	}

	public void testGetPercent() throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession();

		MockInstallProcess process = new MockInstallProcess(mockHttpSession);

		process.initialize();

		ProgressTracker sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertEquals(
			0, sessionProgressTracker.getPercent());

		process.download();
		process.copy();

		sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertEquals(
			sessionProgressTracker.getPercent(), 25);

		process.finish();
	}

	@Test
	public void testGetStatus() throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession();

		MockInstallProcess process = new MockInstallProcess(mockHttpSession);

		process.initialize();

		ProgressTracker sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		process.download();
		process.copy();

		Assert.assertEquals(
			ProgressStatusConstants.COPYING,
			sessionProgressTracker.getStatus());

		process.finish();
	}

	@Test
	public void testInitializeAndFinish() throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession();

		MockInstallProcess process = new MockInstallProcess(mockHttpSession);

		process.initialize();
		
		ProgressTracker sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertNotNull(sessionProgressTracker);

		process.finish();

		sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertNull(sessionProgressTracker);

		process.finish();
	}

	@Test
	public void testInitialStatus() throws Exception {
		MockHttpSession mockHttpSession = new MockHttpSession();

		MockInstallProcess process = new MockInstallProcess(mockHttpSession);

		process.initialize();

		ProgressTracker sessionProgressTracker =
			(ProgressTracker)mockHttpSession.getAttribute(
				LiferayFileUpload.PERCENT + _progressId);

		Assert.assertEquals(
			ProgressStatusConstants.PREPARED,
			sessionProgressTracker.getStatus());
		Assert.assertEquals(
			StringPool.BLANK, sessionProgressTracker.getMessage());
		Assert.assertEquals(
			0, sessionProgressTracker.getPercent());

		process.finish();
	}

	private class MockInstallProcess {

		private ProgressTracker _progressTracker;

		public MockInstallProcess(MockHttpSession mockHttpSession) {
			ProgressTracker progressTracker =
				new ProgressTracker(mockHttpSession, _progressId);

			progressTracker.addProgress(
				ProgressStatusConstants.DOWNLOADING, 25, "downloading");
			progressTracker.addProgress(
				ProgressStatusConstants.COPYING, 50, "copying");

			_progressTracker = progressTracker;
		}

		public void copy() {
			_progressTracker.setStatus(ProgressStatusConstants.COPYING);
		}

		public void download() {
			_progressTracker.setStatus(ProgressStatusConstants.DOWNLOADING);
		}

		public void finish() {
			_progressTracker.finish();
		}

		public void initialize() {
			_progressTracker.initialize();
		}

	}

	private static final String _progressId = "TestId";
}