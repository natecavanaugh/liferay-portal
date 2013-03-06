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

package com.liferay.portal.words.service;

import com.liferay.portal.service.ServiceWrapper;

/**
 * <p>
 * This class is a wrapper for {@link WordsLocalService}.
 * </p>
 *
 * @author    Brian Wing Shun Chan
 * @see       WordsLocalService
 * @generated
 */
public class WordsLocalServiceWrapper implements WordsLocalService,
	ServiceWrapper<WordsLocalService> {
	public WordsLocalServiceWrapper(WordsLocalService wordsLocalService) {
		_wordsLocalService = wordsLocalService;
	}

	/**
	* Returns the Spring bean ID for this bean.
	*
	* @return the Spring bean ID for this bean
	*/
	public java.lang.String getBeanIdentifier() {
		return _wordsLocalService.getBeanIdentifier();
	}

	/**
	* Sets the Spring bean ID for this bean.
	*
	* @param beanIdentifier the Spring bean ID for this bean
	*/
	public void setBeanIdentifier(java.lang.String beanIdentifier) {
		_wordsLocalService.setBeanIdentifier(beanIdentifier);
	}

	/**
	* Checks the spelling of a block of text.
	*
	* <p>
	* This method handles spell checking of text. It takes a block of text
	* as input,and returns a list of any incorrectly spelled words found.
	* </p>
	*
	* @param text the block of text to be spell checked.
	* @return the mis-spelled words
	*/
	public java.util.List<java.lang.String> checkSpelling(java.lang.String text) {
		return _wordsLocalService.checkSpelling(text);
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
	* @param word the misspelled word.
	* @return the suggested corrections.
	*/
	public java.util.List<java.lang.String> getSuggestions(
		java.lang.String word) {
		return _wordsLocalService.getSuggestions(word);
	}

	/**
	 * @deprecated Renamed to {@link #getWrappedService}
	 */
	public WordsLocalService getWrappedWordsLocalService() {
		return _wordsLocalService;
	}

	/**
	 * @deprecated Renamed to {@link #setWrappedService}
	 */
	public void setWrappedWordsLocalService(WordsLocalService wordsLocalService) {
		_wordsLocalService = wordsLocalService;
	}

	public WordsLocalService getWrappedService() {
		return _wordsLocalService;
	}

	public void setWrappedService(WordsLocalService wordsLocalService) {
		_wordsLocalService = wordsLocalService;
	}

	private WordsLocalService _wordsLocalService;
}