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

package com.liferay.portal.kernel.poller.comet;

/**
 * @author Edward Han
 * @author Brian Wing Shun Chan
 */
public abstract class BaseCometSession implements CometSession {

	public void close() throws CometException {
		try {
			doClose();

			_cometResponse.close();
		}
		catch (CometException ce) {
			throw ce;
		}
		catch (Exception e) {
			throw new CometException(e);
		}
	}

	public CometRequest getCometRequest() {
		return _cometRequest;
	}

	public CometResponse getCometResponse() {
		return _cometResponse;
	}

	public String getSessionId() {
		return _sessionId;
	}

	public void setCometRequest(CometRequest cometRequest) {
		_cometRequest = cometRequest;
	}

	public void setCometResponse(CometResponse cometResponse) {
		_cometResponse = cometResponse;
	}

	public void setSessionId(String sessionId) {
		_sessionId = sessionId;
	}

	protected abstract void doClose() throws Exception;

	private CometRequest _cometRequest;
	private CometResponse _cometResponse;
	private String _sessionId;

}