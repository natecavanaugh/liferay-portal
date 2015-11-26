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

<%@ include file="/html/taglib/init.jsp" %>

<%
Double animationTime = GetterUtil.getDouble((String)request.getAttribute("liferay-ui:portlet-alert:animationTime"));
boolean closeable = GetterUtil.getBoolean(String.valueOf(request.getAttribute("liferay-ui:portlet-alert:closeable")));
String content = GetterUtil.getString((String)request.getAttribute("liferay-ui:portlet-alert:content"));
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:portlet-alert:cssClass"));
boolean destroyOnHide = GetterUtil.getBoolean(String.valueOf(request.getAttribute("liferay-ui:portlet-alert:destroyOnHide")));
String targetContainer = GetterUtil.getString((String)request.getAttribute("liferay-ui:portlet-alert:targetContainer"));
Double timeout = GetterUtil.getDouble((String)request.getAttribute("liferay-ui:portlet-alert:timeout"));
String type = GetterUtil.getString((String)request.getAttribute("liferay-ui:portlet-alert:type"));
%>