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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.words.service.base.WordsServiceBaseImpl;
import java.util.List;

/**
 * The implementation of the words remote service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portal.words.service.WordsService} interface.
 *
 * <p>
 * This is a remote service. Methods of this service are expected to have security checks based on the propagated JAAS credentials because this service can be accessed remotely.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.words.service.base.WordsServiceBaseImpl
 * @see com.liferay.portal.words.service.WordsServiceUtil
 */
public class WordsServiceImpl extends WordsServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link com.liferay.portal.words.service.WordsServiceUtil} to access the words remote service.
	 */
    public JSONObject checkSpelling(String text) {
        JSONArray inner = JSONFactoryUtil.createJSONArray();
        for (String s : wordsLocalService.checkSpelling(text))
            inner.put(s);

        JSONObject jsonObj = JSONFactoryUtil.createJSONObject();
        jsonObj.put(OUTCOME, SUCCESS);
        jsonObj.put(DATA, JSONFactoryUtil.createJSONArray().put(inner));

        return jsonObj;
    }

    public List<String> getSuggestions(String word) {
        return wordsLocalService.getSuggestions(word);
    }

    private static final String OUTCOME = "outcome";
    private static final String SUCCESS = "success";
    private static final String DATA = "data";

}