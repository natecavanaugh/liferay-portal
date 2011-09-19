<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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
<%@ include file="/html/common/init.jsp" %>

<%@ page import="com.liferay.portal.setup.SetupWizardUtil" %>
<%@ page import="com.liferay.taglib.aui.ScriptTag" %>

<%
UnicodeProperties properties = (UnicodeProperties)request.getAttribute(WebKeys.SETUP_WIZARD_PROPERTIES);

boolean propertiesFileCreated = GetterUtil.getBoolean((String)request.getAttribute(WebKeys.SETUP_WIZARD_PROPERTIES_UPDATED));

boolean setupWizardFinished = GetterUtil.getBoolean((Boolean)session.getServletContext().getAttribute(WebKeys.SETUP_WIZARD_FINISHED));
%>

<html>
	<head>
		<title><liferay-ui:message key="welcome-to-liferay" /></title>

		<%@ include file="/html/common/themes/top_js.jspf" %>

		<style>
			<liferay-util:include page="/html/themes/classic/css/application.css" />
			<liferay-util:include page="/html/portal/css.jsp" />
			<liferay-util:include page="/html/portal/css/portal/setup_wizard.jspf" />
		</style>
	</head>
	<body>
		<div id="wrapper" style="padding-left: 2%; padding-right: 2%">
			<div id="content">
				<div id="setup-wrapper">
					<header id="banner" role="banner">
						<hgroup id="heading" style="display: table; width: 100%">
							<div style="float: left; width: 50%">
								<h1 class="company-title">
									<img src="/html/icons/company_logo.png" title="Liferay" alt="Liferay" />
								</h1>
							</div>
							<div id="setup-title">
								<h1>
									<liferay-ui:message key="basic-configuration" />
								</h1>
							</div>
						</hgroup>
					</header>

					<c:choose>
						<c:when test="<%= properties== null || !setupWizardFinished %>">
							<div>
								<aui:form action="/c/portal/setup_wizard" name="fm" method="post">
									<aui:input type="hidden" name="<%= Constants.CMD %>" value="<%= Constants.UPDATE %>" />
							
									<aui:fieldset cssClass="fieldset-header" label="portal">
										<aui:input label="portal-name" name='<%= "properties--" + PropsKeys.COMPANY_DEFAULT_WEB_ID + "--" %>' value="<%= PropsValues.COMPANY_DEFAULT_WEB_ID %>" />
							
										<%
										String domain = " liferay.com";
										%>
										<span class="tooltip"><liferay-ui:message key="for-example-x" arguments="<%= domain %>" /></span>
									</aui:fieldset>
							
									<aui:fieldset cssClass="fieldset-header" label="administrator-user">
										<aui:input label="first-name" name='<%= "properties--" + PropsKeys.DEFAULT_ADMIN_FIRST_NAME + "--" %>' value="<%= PropsValues.DEFAULT_ADMIN_FIRST_NAME %>" />
							
										<aui:input label="last-name" name='<%= "properties--" + PropsKeys.DEFAULT_ADMIN_LAST_NAME + "--" %>' value="<%= PropsValues.DEFAULT_ADMIN_LAST_NAME %>" />
							
										<aui:input label="email" name='<%= "properties--" + PropsKeys.ADMIN_EMAIL_FROM_ADDRESS + "--" %>' value="<%= PropsUtil.get(PropsKeys.ADMIN_EMAIL_FROM_ADDRESS) %>" />
									</aui:fieldset>
							
									<aui:fieldset cssClass="fieldset-header" label="database">
							
										<%
										String[] drivers = PropsUtil.getArray(PropsKeys.SETUP_DATABASE_DRIVERS_LIST);
							
										String jdbcDefaultUrl = PropsUtil.get(PropsKeys.JDBC_DEFAULT_URL);
										%>
							
										<aui:input name="defaultDatabase" type="hidden" value='<%= jdbcDefaultUrl.indexOf("hsqldb") != -1 %>' />
										<div id="defaultDatabaseOptions">
											<strong><liferay-ui:message key="default-database-hypersonic" /></strong>. <liferay-ui:message key="this-database-is-useful-for-development" />
							
											<a href="javascript;" id="changeDataBaseLink">(<liferay-ui:message key="change" />)</a>
							              </div>
							
										<div class="aui-helper-hidden" id="customDatabaseOptions">
											<a href="javascript;" id="defaultDatabaseOptionsLink">&laquo; <liferay-ui:message key="use-default-database" /></a>
							
											<aui:select name="databaseType">
											<%
											for (String driver : drivers) {
											%>
												<aui:option selected="<%= jdbcDefaultUrl.indexOf(driver) != -1 %>" label="<%= driver %>" />
											<%
											}
											%>
											</aui:select>
							
											<span id="tooltip" class="aui-helper-hidden tooltip"><liferay-ui:message key="in-order-to-use-this-database" /></span>
							
											<aui:input name="databaseName" value="lportal" />
							
											<aui:input label="user-name" name='<%= "properties--" + PropsKeys.JDBC_DEFAULT_USERNAME + "--" %>' value="<%= PropsUtil.get(PropsKeys.JDBC_DEFAULT_USERNAME) %>" />
							
											<aui:input label="password" name='<%= "properties--" + PropsKeys.JDBC_DEFAULT_PASSWORD + "--" %>' value="<%= PropsUtil.get(PropsKeys.JDBC_DEFAULT_PASSWORD) %>" />
							           	</div>
									</aui:fieldset>
							
									<aui:button-row>
										<aui:button type="submit" value="finish-configuration" />
									</aui:button-row>
								</aui:form>
							</div>
							
							<aui:script use="aui-base">
								var databaseSelector = A.one('#databaseType');
								var defaultDatabaseOptions = A.one('#defaultDatabaseOptions');
								var customDatabaseOptions = A.one('#customDatabaseOptions');
								var tooltip = A.one('#tooltip');
								var changeDataBaseLink = A.one('#changeDataBaseLink');
								var defaultDatabaseOptionsLink = A.one('#defaultDatabaseOptionsLink');
								var defaultDatabase = A.one('#defaultDatabase');
							
								if (databaseSelector.get('value') != 'hsqldb') {
									defaultDatabaseOptions.hide();
							
									customDatabaseOptions.show();
								}
							
								changeDataBaseLink.on(
									'click',
								        function(event) {
											event.preventDefault();
							
											defaultDatabaseOptions.hide();
							
											customDatabaseOptions.show();
							
											defaultDatabase.val('false');
								        }
								);
							
								defaultDatabaseOptionsLink.on(
									'click',
										function(event) {
											event.preventDefault();
							
											defaultDatabaseOptions.show();
							
											customDatabaseOptions.hide();
							
											defaultDatabase.val('true');
								        }
								);
							
								var onChangeDatabaseSelector = function() {
									var value = databaseSelector.get('value');
							
								    	if ((value == 'hsqldb') || (value == 'mysql') || (value == 'postgresql')) {
											tooltip.hide();
								        }
								        else {
											tooltip.show();
								        }
								}
							
								onChangeDatabaseSelector();
							
								databaseSelector.on('change', onChangeDatabaseSelector);
							</aui:script>
							
							<%
								ScriptTag.flushScriptData(pageContext);
							%>
						</c:when>
						<c:otherwise>
							<liferay-util:buffer var="credentials">
								<p>
									<liferay-ui:message key="after-restarting-you-will-be-able-to-login" />
							
									<dl class="credentials">
										<dt><liferay-ui:message key="email-address" />:</dt>
										<dd><%= properties.get(PropsKeys.DEFAULT_ADMIN_EMAIL_ADDRESS) %></dd>
										<dt><liferay-ui:message key="password" />:</dt>
										<dd><%= PropsUtil.get("default.admin.password") %></dd>
									</dl>
								</p>
							</liferay-util:buffer>
							
							<div id="messages-wrapper">
								<c:choose>
									<c:when test="<%= propertiesFileCreated %>">
										<aui:fieldset cssClass="fieldset-header" label="congratulations">
											<p>
												<liferay-ui:message key="your-configuration-has-finished-sucessfully" />
												<br />
												<span class="tooltip">
													<liferay-ui:message key="this-configuration-has-been-saved-in" arguments='<%= "<span>" + PropsValues.LIFERAY_HOME + StringPool.SLASH + SetupWizardUtil._PROPERTIES_FILE_NAME + "</span>" %>'/>
												</span>
											</p>
											<p>
												<liferay-ui:message key="the-application-server-must-be-restarted-to-apply-changes" />
											</p>
							
											<%= credentials %>
							
										</aui:fieldset>
									</c:when>
									<c:otherwise>
										<aui:fieldset cssClass="fieldset-header" label="create-a-configuration-file">
											<p>
												<liferay-ui:message key="sorry-we-were-not-able-to-write-configuration-file" arguments='<%= "<span>" + PropsValues.LIFERAY_HOME + "</span>" %>'/>
											</p>
							
											<%= credentials %>
							
										</aui:fieldset>
							
										<aui:fieldset cssClass="fieldset-header" label="your-portal-ext-properties">
											<div class="wrapper last">
												<aui:input inputCssClass="properties-text" name="portal-ext" label="" type="textarea" value="<%= properties.toString() %>" wrap="soft" />
											</div>
										</aui:fieldset>
									</c:otherwise>
								</c:choose>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</body>
</html>