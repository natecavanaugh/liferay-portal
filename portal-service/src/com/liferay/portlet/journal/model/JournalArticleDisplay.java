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

package com.liferay.portlet.journal.model;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public interface JournalArticleDisplay extends Serializable {

	public long getCompanyId();

	public long getId();

	public long getResourcePrimKey();

	public long getGroupId();

	public long getUserId();

	public String getArticleId();

	public double getVersion();

	public String getTitle();

	public String getUrlTitle();

	public String getDescription();

	public String[] getAvailableLocales();

	public String getContent();

	public void setContent(String content);

	public String getType();

	public String getStructureId();

	public void setStructureId(String structureId);

	public String getTemplateId();

	public void setTemplateId(String templateId);

	public boolean isSmallImage();

	public void setSmallImage(boolean smallImage);

	public long getSmallImageId();

	public void setSmallImageId(long smallImageId);

	public String getSmallImageURL();

	public void setSmallImageURL(String smallImageURL);

	public int getNumberOfPages();

	public void setNumberOfPages(int numberOfPages);

	public int getCurrentPage();

	public void setCurrentPage(int currentPage);

	public boolean isPaginate();

	public void setPaginate(boolean paginate);

	public boolean isCacheable();

	public void setCacheable(boolean cacheable);

}