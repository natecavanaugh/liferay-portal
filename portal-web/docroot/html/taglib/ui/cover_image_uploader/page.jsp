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
long coverImageId = GetterUtil.getLong((String)request.getAttribute("liferay-ui:cover-image-uploader:coverImageId"));
String formName = GetterUtil.getString((String)request.getAttribute("liferay-ui:cover-image-uploader:formName"));

boolean includeForm = false;

if (Validator.isNull(formName)) {
	formName = "coverImageSelectorForm";

	includeForm = true;
}

String coverImageURL = StringPool.BLANK;

boolean hasCoverImage = false;

if (coverImageId > 0) {
	hasCoverImage = true;

	FileEntry coverImage = PortletFileRepositoryUtil.getPortletFileEntry(coverImageId);

	coverImageURL = PortletFileRepositoryUtil.getPortletFileEntryURL(themeDisplay, coverImage, StringPool.BLANK);
}

String uploadImageURL = themeDisplay.getPathMain() + "/portal/edit_cover_image?p_auth=" + AuthTokenUtil.getToken(request);
%>

<liferay-util:buffer var="coverImageInput">
	<aui:input id='<%= namespace + "coverImageFile" %>' label="" name="coverImageFile" size="50" type="file" useNamespace="<%= false %>" wrapperCssClass="cover-image-input-wrapper">
		<aui:validator name="acceptFiles">
			'<%= StringUtil.merge(PrefsPropsUtil.getStringArray(PropsKeys.DL_FILE_EXTENSIONS, StringPool.COMMA)) %>'
		</aui:validator>
	</aui:input>
</liferay-util:buffer>

<liferay-util:buffer var="coverImageUploader">
	<aui:input name="coverImageId" type="hidden" value="<%= coverImageId %>" />
	<aui:input name="coverImageCropRegion" type="hidden" />

	<aui:fieldset cssClass="cover-image-selector">
		<div class='<%= hasCoverImage ? "hide" : StringPool.BLANK %> cover-image-upload-wrapper' id="<portlet:namespace/>imageUploadWrapper">
			<aui:a cssClass="btn btn-primary btn-default" href="javascript:;">
				<liferay-ui:message key="upload" />
			</aui:a>

			<c:if test="<%= !hasCoverImage %>">
				<%= coverImageInput %>
			</c:if>
		</div>

		<div class="<%= hasCoverImage ? StringPool.BLANK : "hide" %> cover-image-preview-wrapper">
			<img class="cover-image cover-image-preview" id="<portlet:namespace/>coverImagePreview" src="<%= coverImageURL %>" />

			<div class="cover-image-preview-mask <%= hasCoverImage ? StringPool.BLANK : "hide" %>"></div>

			<div class="cover-image-action-buttons">
				<div class="cover-image-change-wrapper">
					<aui:button name="change" type="submit" value="change-image" />

					<c:if test="<%= hasCoverImage %>">
						<%= coverImageInput %>
					</c:if>
				</div>

				<aui:button cssClass='<%= hasCoverImage ? "hide" : StringPool.BLANK %>' name="cancelImage" value="cancel" />
				<aui:button cssClass='<%= hasCoverImage ? StringPool.BLANK : "hide" %>' name="deleteImage" value="delete-image" />
			</div>
		</div>
	</aui:fieldset>
</liferay-util:buffer>

<c:choose>
	<c:when test="<%= includeForm %>">
		<aui:form action="<%= uploadImageURL %>" enctype="multipart/form-data" method="post" name="<%= formName %>" onSubmit="event.preventDefault();">
			<%= coverImageUploader %>
		</aui:form>
	</c:when>
	<c:otherwise>
		<%= coverImageUploader %>
	</c:otherwise>
</c:choose>

<aui:script use="liferay-cover-image-uploader">
	new Liferay.CoverImageUploader(
		{
			fileEntry: {
				id: <%= coverImageId %>,
				url: '<%= coverImageURL %>'
			},
			form: '#<%= formName %>',
			namespace: '<portlet:namespace />',
			uploadImageURL: '<%= uploadImageURL %>'
		}
	);
</aui:script>

<aui:script use="aui-base">
	Liferay.on(
		'coverImageUpdated',
		function(event) {
			document.<portlet:namespace /><%= formName %>.<portlet:namespace />coverImageCropRegion.value = A.JSON.stringify(event.cropRegion);
			document.<portlet:namespace /><%= formName %>.<portlet:namespace />coverImageId.value = event.coverImageId;
		}
	);
</aui:script>