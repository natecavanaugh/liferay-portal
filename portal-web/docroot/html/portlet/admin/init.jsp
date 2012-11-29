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

<%@ include file="/html/portlet/init.jsp" %>

<%@ page import="com.liferay.portal.CompanyMxException" %><%@
page import="com.liferay.portal.CompanyVirtualHostException" %><%@
page import="com.liferay.portal.CompanyWebIdException" %><%@
page import="com.liferay.portal.captcha.recaptcha.ReCaptchaImpl" %><%@
page import="com.liferay.portal.convert.ConvertProcess" %><%@
page import="com.liferay.portal.dao.shard.ManualShardSelector" %><%@
page import="com.liferay.portal.kernel.dao.shard.ShardUtil" %><%@
page import="com.liferay.portal.kernel.image.ImageMagickUtil" %><%@
page import="com.liferay.portal.kernel.plugin.PluginPackage" %><%@
page import="com.liferay.portal.kernel.scripting.ScriptingUtil" %><%@
page import="com.liferay.portal.kernel.util.InstancePool" %><%@
page import="com.liferay.portal.kernel.util.OSDetector" %><%@
page import="com.liferay.portal.kernel.xuggler.XugglerUtil" %><%@
page import="com.liferay.portal.upload.LiferayFileUpload" %><%@
page import="com.liferay.portal.util.PortalInstances" %><%@
page import="com.liferay.portlet.documentlibrary.model.DLFileEntry" %><%@
page import="com.liferay.portlet.documentlibrary.model.DLFileVersion" %><%@
page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %><%@
page import="com.liferay.portlet.expando.model.ExpandoBridge" %><%@
page import="com.liferay.portlet.expando.model.ExpandoColumnConstants" %>

<%@ page import="org.apache.log4j.Level" %><%@
page import="org.apache.log4j.LogManager" %><%@
page import="org.apache.log4j.Logger" %>

<%
boolean showShardSelector = false;

if (PropsValues.SHARD_SELECTOR.equals(ManualShardSelector.class.getName()) && (ShardUtil.getAvailableShardNames().length > 1)) {
	showShardSelector = true;
}

Runtime runtime = Runtime.getRuntime();

long memoryMax = runtime.maxMemory();
long memoryTotal = runtime.totalMemory();
long memoryTotalFree = runtime.freeMemory();

long memoryTotalUsed = memoryTotal - memoryTotalFree;
long memoryMaxFree = memoryMax - memoryTotalUsed;
long memoryAllMaxFree = memoryMax - memoryTotalUsed - memoryTotalFree;

memoryAllMaxFree = convertBytesToMB(memoryAllMaxFree);
memoryMax = convertBytesToMB(memoryMax);
memoryMaxFree = convertBytesToMB(memoryMaxFree);
memoryTotal = convertBytesToMB(memoryTotal);
memoryTotalFree = convertBytesToMB(memoryTotalFree);
memoryTotalUsed = convertBytesToMB(memoryTotalUsed);

memoryAllMaxFree = chartZeroValueFix(memoryAllMaxFree);
memoryMaxFree = chartZeroValueFix(memoryMaxFree);
memoryTotalFree = chartZeroValueFix(memoryTotalFree);
memoryTotalUsed = chartZeroValueFix(memoryTotalUsed);
%>

<%!
private long convertBytesToMB(long value) {
	long binaryPrefix = 1024;

	value = (value / binaryPrefix) / binaryPrefix;

	return value;
}

private long chartZeroValueFix(long value) {
	if (value == 0) {
		value = 1;
	}

	return value;
}
%>

<%@ include file="/html/portlet/admin/init-ext.jsp" %>