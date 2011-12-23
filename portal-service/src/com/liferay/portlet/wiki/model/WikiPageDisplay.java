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

package com.liferay.portlet.wiki.model;

import java.io.Serializable;

/**
 * @author Jorge Ferrer
 */
public interface WikiPageDisplay extends Serializable {

	public long getUserId();

	public void setUserId(long userId);

	public long getNodeId();

	public void setNodeId(long nodeId);

	public String getTitle();

	public void setTitle(String title);

	public double getVersion();

	public void setVersion(double version);

	public String getContent();

	public void setContent(String content);

	public String getFormattedContent();

	public void setFormattedContent(String formattedContent);

	public String getFormat();

	public void setFormat(String format);

	public boolean getHead();

	public boolean isHead();

	public void setHead(boolean head);

	public String[] getAttachments();

	public void setAttachments(String[] attachments);

}