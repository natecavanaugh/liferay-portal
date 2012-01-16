<%--
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
--%>

<%
String contextPath = request.getParameter("contextPath");

if (contextPath == null) {
	contextPath = ContextPathUtil.getContextPath(application);
}

List<JSONWebServiceActionMapping> jsonWebServiceActionMappings = JSONWebServiceActionsManagerUtil.getJSONWebServiceActionMappings(contextPath);
%>

<p>
	Context Path: <b><%= Validator.isNull(contextPath) ? StringPool.SLASH : contextPath %></b><br />
	Total Methods: <b><%= jsonWebServiceActionMappings.size() %></b>
</p>

<aui:input cssClass="lfr-service-search" label="" name="serviceSearch" placeholder="search" type="search" />

<div class="services" id="services">
	<%
	String previousActionClassName = null;
	int i = 0;

	for (JSONWebServiceActionMapping jsonWebServiceActionMapping : jsonWebServiceActionMappings) {
		i += 1;

		Class<?> actionClass = jsonWebServiceActionMapping.getActionClass();

		String actionClassName = actionClass.getSimpleName();

		if (actionClassName.endsWith("ServiceUtil")) {
			actionClassName = actionClassName.substring(0, actionClassName.length() - 11);
		}

		if (!actionClassName.equals(previousActionClassName)) {
			previousActionClassName = actionClassName;
	%>

			<%
			if (i != 1) {
			%>

				</div>

			<%
			}
			%>

			<div class="api-section service">
				<h3 class="service-class-name service-result">
					<%= actionClassName %>
				</h3>

	<%
	}

	String path = jsonWebServiceActionMapping.getPath();

	int pos = path.lastIndexOf(CharPool.SLASH);

	path = path.substring(pos + 1);
	%>

		<div class="api-signature">
			<a class="method-name service-result" data-metaData="<%= previousActionClassName %>" href="?signature=<%= jsonWebServiceActionMapping.getSignature() %>">
				<%= path %>
			</a>

			<span class="params">
				<%= ArrayUtil.toString(jsonWebServiceActionMapping.getMethodParameters(), "name", StringPool.COMMA_AND_SPACE) %>
			</span>
		</div>

	<%
	}

	%>
	</div>

</div>

<div class="no-matches aui-helper-hidden" id="noMatches"><liferay-ui:message key="there-are-no-services-matching-that-phrase" /></div>

<script type="text/javascript">
	AUI().use(
		'aui-base',
		'autocomplete-base',
		'autocomplete-filters',
		'autocomplete-highlighters',
		// 'aui-template',
		function(A) {
			// A.one('#serviceSearch').plug()

			var ServiceFilter = A.Component.create(
				{
					NAME: 'servicefilter',
					EXTENDS: A.Base,
					AUGMENTS: [A.AutoCompleteBase],
					prototype: {
						initializer: function() {
							var instance = this;

							instance._bindUIACBase();
							instance._syncUIACBase();
						}
					}
				}
			);

			var services = A.one('#services');
			var servicesClone = services.clone();
			var noMatchMessage = A.one('#noMatches');

			var results = [];

			var nodes = servicesClone.all('.service-result').each(
				function(item, index, collection) {
					results.push(
						{
							node: item,
							text: A.Lang.trim(item.text())
						}
					);
				}
			);

			var AArray = A.Array;
			var replaceRE = new RegExp('[-_\\s\\W]', 'g');
			var hasString = function(text, query) {
				return text.toLowerCase().replace(replaceRE, '').indexOf(query) !== -1;
			};

			var filter = new ServiceFilter(
				{
					inputNode: '#serviceSearch',
					minQueryLength: 0,
					queryDelay: 0,
					source: results,
					resultTextLocator: 'text',
					resultFilters: function(query, results) {
						query = query.toLowerCase();
						return AArray.filter(
							results,
							function(item, index, collection) {
								return hasString(item.raw.node.attr('data-metaData') + '/' + item.text, query);
							}
						);
					},
					resultHighlighter: function(query, results) {
						var queryChars = AArray.unique(query.toLowerCase().split(''));
						var queryCharsLength = queryChars.length;

						while (queryCharsLength--) {
							var queryChar = queryChars[queryCharsLength];

							queryChars.push(
								'-' + queryChar,
								queryChar + '-'
							);
						}

						return AArray.map(
							results,
							function(item, index, collection) {
								return A.Highlight.all(item.text, queryChars);
							}
						);
					}
				}
			);

			filter.on(
				'results',
				A.debounce(
					function(event) {
						var query = event.query;
						var results = event.results;

						var resultsLength = results.length;

						servicesClone.remove();
						servicesClone.all('.service, .api-signature').hide();

						services.toggle(!query);

						A.Array.each(
							results,
							function(item, index, collection) {
								var raw = item.raw;
								var node = raw.node;
								var serviceNode = raw.serviceNode;

								if (!serviceNode) {
									serviceNode = node.ancestor('.service');

									raw.serviceNode = serviceNode;
								}

								if (node.hasClass('method-name')) {
									var signatureNode = raw.signatureNode;

									if (!signatureNode) {
										signatureNode = node.ancestor('.api-signature');

										raw.signatureNode = signatureNode;
									}

									signatureNode.show();

									node.html(item.highlighted);
								}

								node.show();
								serviceNode.show();
							}
						);

						noMatchMessage.toggle(!resultsLength);

						if (query) {
							services.placeBefore(servicesClone);
						}
					},
					50
				)
			);
		}
	);
</script>