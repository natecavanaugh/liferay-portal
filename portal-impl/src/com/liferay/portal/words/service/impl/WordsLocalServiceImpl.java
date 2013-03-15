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

package com.liferay.portal.words.service.impl;

import com.liferay.portal.words.WordsUtil;
import com.liferay.portal.words.service.base.WordsLocalServiceBaseImpl;
import com.liferay.util.jazzy.InvalidWord;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of the words local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portal.words.service.WordsLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Ken Boyer
 * @see com.liferay.portal.words.service.base.WordsLocalServiceBaseImpl
 * @see com.liferay.portal.words.service.WordsLocalServiceUtil
 */
public class WordsLocalServiceImpl extends WordsLocalServiceBaseImpl {

	/**
	 * Checks the spelling of a block of text.
	 *
	 * <p>
	 * This method handles spell checking of text. It takes a block of text
	 * as input,and returns a list of any incorrectly spelled words found.
	 * </p>
	 *
	 * @param  text the block of text to be spell checked.
	 * @return the mis-spelled words
	 */
	public List<String> checkSpelling(String text) {
		List<String> invalid = new ArrayList<String>();
		List<InvalidWord> invalidWords = WordsUtil.checkSpelling(text);

		for (InvalidWord invalidWord : invalidWords) {
			invalid.add(invalidWord.getInvalidWord());
		}

		return invalid;
	}

	/**
	 * Finds suggestions for a misspelled word.
	 *
	 * <p>
	 * This method finds suggested spellings for a misspelled word.
	 * It takes a misspelled word as input,and returns a list of
	 * suggested correctly spelled words.
	 * </p>
	 *
	 * @param  word the misspelled word.
	 * @return the suggested corrections.
	 */
	public List<String> getSuggestions(String word) {
		List<InvalidWord> invalidWords = WordsUtil.checkSpelling(word);
		List<String> suggestions = new ArrayList<String>();

		if (!invalidWords.isEmpty()) {
			suggestions = invalidWords.get(0).getSuggestions();
		}

		return suggestions;
	}

}