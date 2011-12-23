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

package com.liferay.portal.kernel.search;

import java.io.Serializable;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
public interface Hits extends Serializable {

	public Document doc(int n);

	public Document[] getDocs();

	public int getLength();

	public Query getQuery();

	public String[] getQueryTerms();

	public float[] getScores();

	public float getSearchTime();

	public String[] getSnippets();

	public long getStart();

	public float score(int n);

	public void setDocs(Document[] docs);

	public void setLength(int length);

	public void setQuery(Query query);

	public void setQueryTerms(String[] queryTerms);

	public void setScores(float[] scores);

	public void setScores(Float[] scores);

	public void setSearchTime(float time);

	public void setSnippets(String[] snippets);

	public void setStart(long start);

	public String snippet(int n);

	public List<Document> toList();

}