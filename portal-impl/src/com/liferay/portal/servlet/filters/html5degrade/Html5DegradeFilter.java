/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.servlet.filters.html5degrade;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.StringServletResponse;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.KMPSearch;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PropsValues;

import java.io.IOException;
import java.io.Writer;

import java.nio.CharBuffer;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Minhchau Dang
 */
public class Html5DegradeFilter extends BasePortalFilter {

	public static final String SKIP_FILTER =
		Html5DegradeFilter.class.getName() + "SKIP_FILTER";

	@Override
	public void init(FilterConfig filterConfig) {

		super.init(filterConfig);

		for (String ignorePath : PropsValues.HTML5_DEGRADE_IGNORE_PATHS) {
			_ignorePaths.add(ignorePath);
		}
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (isDegrade(request) && !isInclude(request) &&
			!isAlreadyFiltered(request)) {

			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isAlreadyFiltered(HttpServletRequest request) {

		if (request.getAttribute(SKIP_FILTER) != null) {
			return true;
		}
		else {
			return false;
		}
	}

	protected boolean isInclude(HttpServletRequest request) {

		String uri =
			(String) request.getAttribute(JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI);

		if (uri == null) {
			return false;
		}
		else {
			return true;
		}
	}

	protected boolean isDegrade(HttpServletRequest request) {

		String path = request.getPathInfo();

		if (_ignorePaths.contains(path)) {
			if (_log.isDebugEnabled()) {
				_log.debug("Ignore path " + path);
			}

			return false;
		}

		// Modifying binary content through a servlet filter under certain
		// conditions is bad on performance the user will not start downloading
		// the content until the entire content is modified.

		String lifecycle = ParamUtil.getString(request, "p_p_lifecycle");

		if ((lifecycle.equals("1") && LiferayWindowState.isExclusive(request)) ||
			lifecycle.equals("2")) {

			return false;
		}
		else {
			return true;
		}
	}

	protected void degrade(StringServletResponse stringResponse, Writer writer)
		throws IOException {

		CharBuffer charBuffer = CharBuffer.wrap(stringResponse.getString());

		while (charBuffer.hasRemaining()) {
			char c = charBuffer.get();

			writer.write(c);

			if (c == CharPool.LESS_THAN) {
				processTag(charBuffer, writer);
			}
		}
	}

	protected String extractContent(CharBuffer charBuffer, int length) {

		CharBuffer duplicateCharBuffer = charBuffer.duplicate();

		int position = duplicateCharBuffer.position() + length;

		String content = duplicateCharBuffer.limit(position).toString();

		charBuffer.position(position);

		return content;
	}

	protected void processCommentTag(CharBuffer charBuffer, Writer writer)
		throws IOException {

		int remaining = charBuffer.remaining();

		for (int i = 2; i < remaining; i++) {
			if ((charBuffer.charAt(i) == CharPool.GREATER_THAN) &&
				(charBuffer.charAt(i - 1) == CharPool.DASH) &&
				(charBuffer.charAt(i - 2) == CharPool.DASH)) {

				String comment = extractContent(charBuffer, i);

				writer.write(comment);

				return;
			}
		}
	}

	@Override
	protected void processFilter(
		HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain)
		throws Exception {

		if (_log.isDebugEnabled()) {
			String completeURL = HttpUtil.getCompleteURL(request);

			_log.debug("Degrading HTML5 to HTML4 for " + completeURL);
		}

		request.setAttribute(SKIP_FILTER, Boolean.TRUE);

		StringServletResponse stringResponse =
			new StringServletResponse(response);

		processFilter(
			Html5DegradeFilter.class, request, stringResponse, filterChain);

		String contentType =
			GetterUtil.getString(stringResponse.getContentType()).toLowerCase();

		response.setContentType(contentType);

		if (contentType.startsWith(ContentTypes.TEXT_HTML)) {
			degrade(stringResponse, response.getWriter());
		}
		else {
			ServletResponseUtil.write(response, stringResponse);
		}
	}

	protected void processRegularTag(CharBuffer charBuffer, Writer writer)
		throws IOException {

		int remaining = charBuffer.remaining();

		String tagName = StringPool.BLANK;

		for (int i = 1; i < remaining; i++) {
			char c = charBuffer.charAt(i);

			if (c == CharPool.GREATER_THAN) {
				tagName = extractContent(charBuffer, i);

				break;
			}

			if (Character.isWhitespace(c)) {
				tagName = extractContent(charBuffer, i);

				break;
			}
		}

		String lowerCaseTagName = tagName.toLowerCase();

		boolean openTag = true;

		if (lowerCaseTagName.charAt(0) == CharPool.SLASH) {
			lowerCaseTagName = lowerCaseTagName.substring(1);

			openTag = false;
		}

		if (_html5BlockTags.contains(lowerCaseTagName)) {
			if (!openTag) {
				writer.write(CharPool.SLASH);
			}

			writer.write(_tagNameDiv);

			writer.write(" html5=\"");
			writer.write(tagName);
			writer.write(CharPool.QUOTE);
		}
		else if (_html5InlineTags.contains(lowerCaseTagName)) {
			if (!openTag) {
				writer.write(CharPool.SLASH);
			}

			writer.write(_tagNameSpan);

			if (openTag) {
				writer.write(" html5=\"");
				writer.write(tagName);
				writer.write(CharPool.QUOTE);
			}
		}
		else {
			writer.write(tagName);
		}

		int length =
			scanUntilUnquotedCharacter(charBuffer, CharPool.GREATER_THAN);

		if (length == -1) {
			length = remaining;
		}

		String content = extractContent(charBuffer, length);

		writer.write(content);

		if (openTag && (length > 1) &&
			(content.charAt(length - 1) == CharPool.SLASH)) {

			openTag = false;
		}

		if (openTag) {
			String marker = null;
			int[] nexts = null;

			if (lowerCaseTagName.equals(_tagNameScript)) {
				marker = _markerScriptClose;
				nexts = _markerScriptCloseNexts;
			}
			else if (lowerCaseTagName.equals(_tagNameStyle)) {
				marker = _markerStyleClose;
				nexts = _markerStyleCloseNexts;
			}

			if (marker != null) {
				length = KMPSearch.search(charBuffer, marker, nexts);

				if (length == -1) {
					length = charBuffer.remaining();
				}
				else {
					length += marker.length();
				}

				content = extractContent(charBuffer, length);

				writer.write(content);
			}
		}
	}

	protected void processTag(CharBuffer charBuffer, Writer writer)
		throws IOException {

		int remaining = charBuffer.remaining();

		if (remaining == 0) {
			return;
		}

		char c = charBuffer.charAt(0);

		if (c == CharPool.EXCLAMATION) {
			if ((remaining > 3) && (charBuffer.charAt(1) == CharPool.DASH) &&
				(charBuffer.charAt(2) == CharPool.DASH)) {

				processCommentTag(charBuffer, writer);
			}
			else {
				int length =
					scanUntilCharacter(charBuffer, CharPool.GREATER_THAN);

				if (length == -1) {
					length = remaining;
				}

				String content = extractContent(charBuffer, length);

				writer.write(content);
			}
		}
		else {
			processRegularTag(charBuffer, writer);
		}
	}

	protected int scanUntilCharacter(CharBuffer charBuffer, char stopChar) {

		int remaining = charBuffer.remaining();

		for (int i = 0; i < remaining; i++) {
			char c = charBuffer.charAt(i);

			if (c == stopChar) {
				return i;
			}
		}

		return -1;
	}

	protected int scanUntilUnquotedCharacter(
		CharBuffer charBuffer, char stopChar) {

		char quoteChar = _nullChar;
		boolean escapedChar = false;

		int remaining = charBuffer.remaining();

		for (int i = 0; i < remaining; i++) {
			char c = charBuffer.charAt(i);

			if (escapedChar) {
				escapedChar = false;
			}
			else if (c == CharPool.BACK_SLASH) {
				escapedChar = true;
			}
			else if (c == quoteChar) {
				quoteChar = _nullChar;
			}
			else if (quoteChar != _nullChar) {

			}
			else if ((c == CharPool.APOSTROPHE) || (c == CharPool.QUOTE)) {
				quoteChar = c;
			}
			else if ((quoteChar == _nullChar) && (c == stopChar)) {
				return i;
			}
		}

		return -1;
	}

	private static final Set<String> _html5BlockTags =
		SetUtil.fromArray(new String[] {
			"article", "aside", "audio", "canvas", "details", "figcaption",
			"figure", "footer", "header", "hgroup", "nav", "output",
			"progress", "section", "summary", "video"
		});

	private static final Set<String> _html5InlineTags =
		SetUtil.fromArray(new String[] {
			"abbr", "mark", "meter", "time"
		});

	private static Log _log = LogFactoryUtil.getLog(Html5DegradeFilter.class);

	private static final String _markerScriptClose = "</script>";

	private static final int[] _markerScriptCloseNexts =
		KMPSearch.generateNexts(_markerScriptClose);

	private static final String _markerStyleClose = "</style>";

	private static final int[] _markerStyleCloseNexts =
		KMPSearch.generateNexts(_markerStyleClose);

	private static final char _nullChar = '\0';

	private static final String _tagNameDiv = "div";

	private static final String _tagNameScript = "script";

	private static final String _tagNameSpan = "span";

	private static final String _tagNameStyle = "style";

	private Set<String> _ignorePaths = new HashSet<String>();

}
