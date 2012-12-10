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

package com.liferay.portal.json;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONSerializer;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.HitsImpl;

import junit.framework.TestCase;

/**
 * @author Igor Spasic
 */
public class JSONSerializerTest extends TestCase {

	public void testSerializeHits() {
		JSONSerializer jsonSerializer = JSONFactoryUtil.createJSONSerializer();

		Hits hits = new HitsImpl();

		String json = jsonSerializer.serialize(hits);

		assertTrue(json.contains("\"docs\":null"));
		assertFalse(json.contains("\"query\""));
		assertTrue(json.contains("\"queryTerms\":null"));
		assertTrue(json.contains("\"scores\":[]"));
		assertTrue(json.contains("\"snippets\":[]"));
		assertTrue(json.contains("\"start\":0"));
		assertTrue(json.contains("\"length\":0"));
	}

}