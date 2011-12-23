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

package com.liferay.portal.kernel.webdav;

import com.liferay.portal.model.Lock;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Alexander Chow
 */
public interface WebDAVStorage {

	public int copyCollectionResource(
			WebDAVRequest webDavRequest, Resource resource, String destination,
			boolean overwrite, long depth)
		throws WebDAVException;

	public int copySimpleResource(
			WebDAVRequest webDavRequest, Resource resource, String destination,
			boolean overwrite)
		throws WebDAVException;

	public int deleteResource(WebDAVRequest webDavRequest)
		throws WebDAVException;

	public Resource getResource(WebDAVRequest webDavRequest)
		throws WebDAVException;

	public List<Resource> getResources(WebDAVRequest webDavRequest)
		throws WebDAVException;

	public String getRootPath();

	public String getToken();

	public boolean isAvailable(WebDAVRequest webDavRequest)
		throws WebDAVException;

	public boolean isSupportsClassTwo();

	public Status lockResource(
			WebDAVRequest webDavRequest, String owner, long timeout)
		throws WebDAVException;

	public Status makeCollection(WebDAVRequest webDavRequest)
		throws WebDAVException;

	public int moveCollectionResource(
			WebDAVRequest webDavRequest, Resource resource, String destination,
			boolean overwrite)
		throws WebDAVException;

	public int moveSimpleResource(
			WebDAVRequest webDavRequest, Resource resource, String destination,
			boolean overwrite)
		throws WebDAVException;

	public int putResource(WebDAVRequest webDavRequest) throws WebDAVException;

	public Lock refreshResourceLock(
			WebDAVRequest webDavRequest, String uuid, long timeout)
		throws WebDAVException;

	public void setRootPath(String rootPath);

	public void setToken(String token);

	public boolean unlockResource(WebDAVRequest webDavRequest, String token)
		throws WebDAVException;

}