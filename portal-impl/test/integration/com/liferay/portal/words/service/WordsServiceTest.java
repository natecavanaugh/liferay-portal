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
import com.liferay.portal.test.EnvironmentExecutionTestListener;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.words.service.WordsServiceUtil;

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
    public void testCheckSpellingStructure() throws Exception {
        JSONObject json = WordsServiceUtil.checkSpelling("");
        Assert.assertTrue("Invalid number of objects", json.length() == 2);

        Assert.assertTrue("\"outcome\" object not found", json.has(OUTCOME));
        Assert.assertTrue(
                "\"outcome\" object has no value", !json.isNull(OUTCOME));
        Assert.assertTrue(
                "Invalid value for \"outcome\"",
                SUCCESS.equals(json.getString(OUTCOME)));

        Assert.assertTrue("\"data\" object not found", json.has(DATA));
        Assert.assertTrue("\"data\" object has no value", !json.isNull(DATA));
        Assert.assertTrue("\"data\" object has no value", !json.isNull(DATA));

        Assert.assertTrue(
                "\"data\" object is not an array of arrays",
                json.getJSONArray(DATA).length() == 1);
        Assert.assertTrue(
                "\"data\" object inner array is null element",
                json.getJSONArray(DATA).getJSONArray(0).isNull(0));
    }

    @Test
    public void testCheckSpellingNoInput() throws Exception {
        JSONObject json = WordsServiceUtil.checkSpelling("");
        Assert.assertTrue(
                "Output not empty when no input",
                json.getJSONArray(DATA).getJSONArray(0).length() == 0);
    }

    @Test
    public void testCheckSpellingSpaceInput() throws Exception {
        JSONObject json = WordsServiceUtil.checkSpelling(" ");
        Assert.assertTrue(
                "Output not empty when space input",
                json.getJSONArray(DATA).getJSONArray(0).length() == 0);
    }

	@Test
	public void testCheckSpellingMisspellings() throws Exception {
		JSONObject json = WordsServiceUtil.checkSpelling(
				"acceptible catagory definitly experiance imediately " +
				"lisence personel recieve tommorrow youre");
		Assert.assertTrue(
				"Undetected missspellings",
				json.getJSONArray(DATA).getJSONArray(0).length() == 10);
	}


	@Test
	public void testCheckSpellingNoMisspellings() throws Exception {
		JSONObject json = WordsServiceUtil.checkSpelling(
				"acceptable category definitely experience immediately " +
				"license personnel receive tomorrow you're");
		Assert.assertTrue(
				"Invalid detection of misspelling",
				json.getJSONArray(DATA).getJSONArray(0).length() == 0);
	}

	@Test
	public void testGetSuggestionsCorrectSpelling() throws Exception {

		Assert.assertTrue(
				"Correct spelling not detected",
				WordsServiceUtil.getSuggestions("Apple").isEmpty());
	}

	@Test
	public void testGetSuggestionsMisSpelling() throws Exception {
		Assert.assertTrue(
				"Misspelling not found",
				WordsServiceUtil.getSuggestions("Aple").contains("apple"));
	}

	@Test
	public void testGetSuggestionsNoInput() throws Exception {
		Assert.assertTrue(
				"Empty input returned non-empty result",
				WordsServiceUtil.getSuggestions("").isEmpty());
	}

	@Test
	public void testGetSuggestionsSpaceInput() throws Exception {
		Assert.assertTrue(
				"Space input returned non-empty result",
				WordsServiceUtil.getSuggestions(" ").isEmpty());
	}

	private static final String DATA = "data";
	private static final String OUTCOME = "outcome";
	private static final String SUCCESS = "success";

}