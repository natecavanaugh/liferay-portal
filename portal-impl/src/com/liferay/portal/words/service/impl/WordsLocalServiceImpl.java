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

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.PublicRenderParameter;
import com.liferay.portal.words.service.base.WordsLocalServiceBaseImpl;
import com.liferay.portal.words.WordsUtil;
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
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.words.service.base.WordsLocalServiceBaseImpl
 * @see com.liferay.portal.words.service.WordsLocalServiceUtil
 */
public class WordsLocalServiceImpl extends WordsLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.liferay.portal.words.service.WordsLocalServiceUtil} to access the words local service.
	 */
    public List<String> checkSpelling(String text) {
        List<String> invalid = new ArrayList<String>();
        List<InvalidWord> invalidWords = WordsUtil.checkSpelling(text);

        for (InvalidWord invalidWord : invalidWords) {
            System.out.println("Invalid Word:  " + invalidWord.getInvalidWord());
            invalid.add(invalidWord.getInvalidWord());
        }
        return invalid;
    }

    public List<String> getSuggestions(String word) {
        List<String> suggestions;
        List<InvalidWord> invalidWords = WordsUtil.checkSpelling(word);

        if (!invalidWords.isEmpty() && !invalidWords.get(0).getSuggestions().isEmpty()) {
            suggestions = invalidWords.get(0).getSuggestions();
        }
        else {
            suggestions  = new ArrayList<String>();
        }
        return suggestions;
    }
}