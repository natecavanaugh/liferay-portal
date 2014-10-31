<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/rss/init.jsp" %>

<%
String redirect = ParamUtil.getString(request, "redirect");

String typeSelection = ParamUtil.getString(request, "typeSelection");

int assetOrder = ParamUtil.getInteger(request, "assetOrder", -1);
%>

<liferay-portlet:renderURL portletConfiguration="true" var="configurationURL" />

<%
PortletURL configurationActionURL = renderResponse.createActionURL();

configurationActionURL.setParameter("struts_action", "/portlet_configuration/edit_configuration");
configurationActionURL.setParameter("redirect", configurationURL.toString());
configurationActionURL.setParameter("backURL", redirect);
configurationActionURL.setParameter("portletResource", portletResource);

PortletURL configurationRenderURL = renderResponse.createRenderURL();

configurationRenderURL.setParameter("struts_action", "/portlet_configuration/edit_configuration");
configurationRenderURL.setParameter("redirect", configurationURL.toString());
configurationRenderURL.setParameter("backURL", redirect);
configurationRenderURL.setParameter("portletResource", portletResource);
%>

<aui:form action="<%= configurationURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationURL.toString() %>" />
	<aui:input name="typeSelection" type="hidden" value="<%= typeSelection %>" />
	<aui:input name="articleGroupId" type="hidden" />
	<aui:input name="articleId" type="hidden" />
	<aui:input name="assetOrder" type="hidden" />

	<c:choose>
		<c:when test="<%= typeSelection.equals(StringPool.BLANK) %>">
			<liferay-ui:panel-container extended="<%= true %>" id="rssFeedsSettingsPanelContainer" persistState="<%= true %>">
				<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="rssFeedsSettingsPanel" persistState="<%= true %>" title="feeds">
					<liferay-ui:error exception="<%= ValidatorException.class %>">

					<%
					ValidatorException ve = (ValidatorException)errorException;
					%>

					<liferay-ui:message key="the-following-are-invalid-urls" />

					<%
					Enumeration enu = ve.getFailedKeys();

					while (enu.hasMoreElements()) {
						String url = (String)enu.nextElement();
					%>

						<strong><%= HtmlUtil.escape(url) %></strong><%= (enu.hasMoreElements()) ? ", " : "." %>

					<%
					}
					%>

					</liferay-ui:error>

					<aui:fieldset cssClass="subscriptions">

						<%
						if (urls.length == 0) {
							urls = new String[1];
							urls [0] = StringPool.BLANK;
						}

						for (int i = 0; i < urls.length; i++) {
							String title = StringPool.BLANK;

							if (i < titles.length) {
								title = titles[i];
							}
						%>

							<div class="lfr-form-row lfr-form-row-inline">
								<div class="row-fields">
									<aui:input cssClass="lfr-input-text-container" label="title" name='<%= "title" + i %>' value="<%= title %>" />

									<aui:input cssClass="lfr-input-text-container" label="url" name='<%= "url" + i %>' value="<%= urls[i] %>" />
								</div>
							</div>

						<%
						}
						%>

					</aui:fieldset>
				</liferay-ui:panel>

				<liferay-ui:panel collapsible="<%= true %>" extended="<%= true %>" id="rssFeedsDisplaySettingsPanel" persistState="<%= true %>" title="display-settings">
					<aui:fieldset>
						<aui:input name="preferences--showFeedTitle--" type="checkbox" value="<%= showFeedTitle %>" />

						<aui:input name="preferences--showFeedPublishedDate--" type="checkbox" value="<%= showFeedPublishedDate %>" />

						<aui:input name="preferences--showFeedDescription--" type="checkbox" value="<%= showFeedDescription %>" />

						<%
						String taglibShowFeedImageOnClick = "if (this.checked) {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "feedImageAlignment.disabled = '';} else {document." + renderResponse.getNamespace() + "fm." + renderResponse.getNamespace() + "feedImageAlignment.disabled = 'disabled';}";
						%>

						<aui:input name="preferences--showFeedImage--" onClick="<%= taglibShowFeedImageOnClick %>" type="checkbox" value="<%= showFeedImage %>" />

						<aui:input name="preferences--showFeedItemAuthor--" type="checkbox" value="<%= showFeedItemAuthor %>" />

						<aui:select label="num-of-entries-per-feed" name="preferences--entriesPerFeed--">

							<%
							for (int i = 1; i < 10; i++) {
							%>

								<aui:option label="<%= i %>" selected="<%= i == entriesPerFeed %>" />

							<%
							}
							%>

						</aui:select>

						<aui:select label="num-of-expanded-entries-per-feed" name="preferences--expandedEntriesPerFeed--">

							<%
							for (int i = 0; i < 10; i++) {
							%>

								<aui:option label="<%= i %>" selected="<%= i == expandedEntriesPerFeed %>" />

							<%
							}
							%>

						</aui:select>

						<aui:select disabled="<%= !showFeedImage %>" name="preferences--feedImageAlignment--">
							<aui:option label="left" selected='<%= feedImageAlignment.equals("left") %>' />
							<aui:option label="right" selected='<%= feedImageAlignment.equals("right") %>' />
						</aui:select>

						<div class="form-group">

							<%
							JournalArticle headerArticle = null;

							if (Validator.isNotNull(headerArticleId)) {
								try {
									headerArticle = JournalArticleLocalServiceUtil.getArticle(headerArticleGroupId, headerArticleId);
								}
								catch (NoSuchArticleException nsae) {
								}
							}
							%>

							<aui:input name="headerWebContent" type="resource" value="<%= (headerArticle != null) ? headerArticle.getTitle(locale) : StringPool.BLANK %>" />

							<aui:button name="selectButton" onClick='<%= renderResponse.getNamespace() + "selectionForHeader();" %>' value="select" />

							<aui:button name="removeButton" onClick='<%= renderResponse.getNamespace() + "removeSelectionForHeader();" %>' value="remove" />
						</div>

						<div class="form-group">

							<%
							JournalArticle footerArticle = null;

							if (Validator.isNotNull(footerArticleId)) {
								try {
									footerArticle = JournalArticleLocalServiceUtil.getArticle(footerArticleGroupId, footerArticleId);
								}
								catch (NoSuchArticleException nsae) {
								}
							}
							%>

							<aui:input name="footerWebContent" type="resource" value="<%= (footerArticle != null) ? footerArticle.getTitle(locale) : StringPool.BLANK %>" />

							<aui:button name="selectButton" onClick='<%= renderResponse.getNamespace() + "selectionForFooter();" %>' value="select" />

							<aui:button name="removeButton" onClick='<%= renderResponse.getNamespace() + "removeSelectionForFooter();" %>' value="remove" />
						</div>
					</aui:fieldset>
				</liferay-ui:panel>
			</liferay-ui:panel-container>

			<aui:button-row>
				<aui:button onClick='<%= renderResponse.getNamespace() + "saveSettings();" %>' type="submit" />
			</aui:button-row>

			<aui:script use="aui-base,liferay-auto-fields">
				var subscriptionsTable = A.one('#<portlet:namespace />subscriptions');

				if (subscriptionsTable) {
					subscriptionsTable.delegate(
						'click',
						function(event) {
							event.currentTarget.get('parentNode.parentNode').remove();
						},
						'.remove-subscription'
					);
				}

				new Liferay.AutoFields(
					{
						contentBox: 'fieldset.subscriptions',
						fieldIndexes: '<portlet:namespace />subscriptionIndexes',
						namespace: '<portlet:namespace />'
					}
				).render();
			</aui:script>
		</c:when>
		<c:when test="<%= typeSelection.equals(JournalArticle.class.getName()) %>">
			<aui:input name="assetType" type="hidden" value="<%= JournalArticle.class.getName() %>" />

			<liferay-ui:message key="select" />: <%= ResourceActionsUtil.getModelResource(locale, JournalArticle.class.getName()) %>

			<br /><br />

			<%@ include file="/html/portlet/rss/select_journal_article.jspf" %>
		</c:when>
	</c:choose>
</aui:form>

<aui:script>
	function <portlet:namespace />removeSelectionForFooter() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('remove-footer-article');

		submitForm(form, '<%= configurationActionURL.toString() %>');
	}

	function <portlet:namespace />removeSelectionForHeader() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('remove-header-article');

		submitForm(form, '<%= configurationActionURL.toString() %>');
	}

	function <portlet:namespace />selectAsset(articleGroupId, articleId, assetOrder) {
		var form = AUI.$(document.<portlet:namespace />fm);

		if (assetOrder == 1) {
			form.fm('<%= Constants.CMD %>').val('set-footer-article');
		}
		else {
			form.fm('<%= Constants.CMD %>').val('set-header-article');
		}

		form.fm('articleGroupId').val(articleGroupId);
		form.fm('articleId').val(articleId);
		form.fm('typeSelection').val('');

		submitForm(form, '<%= configurationActionURL.toString() %>');
	}

	function <portlet:namespace />saveSettings() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('<%= Constants.UPDATE %>');
		form.fm('typeSelection').val('');

		submitForm(form, '<%= configurationActionURL.toString() %>');
	}

	function <portlet:namespace />selectionForHeader() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('typeSelection').val('<%= JournalArticle.class.getName() %>');
		form.fm('assetOrder').val(0);

		submitForm(form);
	}

	function <portlet:namespace />selectionForFooter() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('typeSelection').val('<%= JournalArticle.class.getName() %>');
		form.fm('assetOrder').val(1);

		submitForm(form);
	}
</aui:script>