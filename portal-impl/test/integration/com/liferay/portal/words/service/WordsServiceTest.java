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

package integration.com.liferay.portal.words.service;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.test.ExecutionTestListeners;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.words.service.WordsServiceUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Ken Boyer
 */

@ExecutionTestListeners(listeners = {EnvironmentExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class WordsServiceTest {

	@Test
	public void testCheckSpelling() throws Exception {
		List<TestCondition> testConditions = new ArrayList<TestCondition>();

		testConditions.add(
				new TestCondition(
						StringPool.BLANK, 0, "Invalid result for empty input"));

		testConditions.add(
				new TestCondition(
						StringPool.SPACE, 0, "Invalid result for space input"));

		testConditions.add(
				new TestCondition(
						"acceptible catagory definitly experiance imediately " +
				"lisence personel recieve tommorrow youre", 10,
				"Undetected missspellings"));

		testConditions.add(
				new TestCondition(
						"acceptable category definitely experience " +
				"immediately license personnel receive tomorrow you're",
						0,"Invalid misspellings returned"));

		for (TestCondition tc : testConditions) {
			JSONObject json = WordsServiceUtil.checkSpelling(tc.getWords());

			Assert.assertEquals(
					tc.getMessage(), tc.getLength(),
					json.getJSONArray(_DATA).getJSONArray(0).length());
		}
	}

	@Test
	public void testCheckSpellingStructure() throws Exception {
		JSONObject json = WordsServiceUtil.checkSpelling(StringPool.BLANK);

		Assert.assertEquals(
				"Valid \"outcome\" object not found", _SUCCESS,
				json.getString(_OUTCOME));

		Assert.assertEquals(
				"Valid \"data\" object not found", _EMPTY_RESULT,
				json.getJSONArray(_DATA).getString(0));
	}

	@Test
	public void testGetNoSuggestions() throws Exception {
		List<TestCondition> testConditions = new ArrayList<TestCondition>();

		testConditions.add(
				new TestCondition(
						StringPool.BLANK, 0, "Invalid result for empty input"));

		testConditions.add(
				new TestCondition(
						StringPool.SPACE, 0, "Invalid result for space input"));

		testConditions.add(
				new TestCondition(
						_GOOD_APPLE, 0,
						"Suggestions returned for correctly spelled word"));

		for (TestCondition tc : testConditions) {
			JSONObject json = WordsServiceUtil.checkSpelling(tc.getWords());
			Assert.assertEquals(
					tc.getMessage(), tc.getLength(),
					WordsServiceUtil.getSuggestions(tc.getWords()).size());
		}
	}

	@Test
	public void testGetSuggestions() throws Exception {
		TestCondition testCondition = new TestCondition(
				_BAD_APPLE, 0,
				"Suggestions not returned for incorrectly spelled word");

		Assert.assertTrue(
				testCondition.getMessage(),
				WordsServiceUtil.getSuggestions(
						testCondition.getWords()).size() > 0);
	}

	private static final String _BAD_APPLE = "Aple";
	private static final String _DATA = "data";
	private static final String _EMPTY_RESULT ="[]";
	private static final String _GOOD_APPLE = "Apple";
	private static final String _OUTCOME = "outcome";
	private static final String _SUCCESS = "success";

}