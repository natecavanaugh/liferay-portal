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

<%@ include file="/html/portlet/users_admin/init.jsp" %>

<%
User selUser = PortalUtil.getSelectedUser(request);
%>

<c:choose>
	<c:when test='<%= SessionMessages.contains(renderRequest, "request_processed") %>'>
		<aui:script>
			window.close();
			opener.<portlet:namespace />changeLogo('<%= selUser.getPortraitURL(themeDisplay) %>');
		</aui:script>
	</c:when>
	<c:otherwise>
		<portlet:actionURL var="editUserPortraitURL">
			<portlet:param name="struts_action" value="/users_admin/edit_user_portrait" />
		</portlet:actionURL>

		<aui:form action="<%= editUserPortraitURL %>" enctype="multipart/form-data" method="post" name="fm">
			<aui:input name="p_u_i_d" type="hidden" value="<%= selUser.getUserId() %>" />
			<aui:input name="cropRegion" type="hidden" />

			<liferay-ui:error exception="<%= UploadException.class %>" message="an-unexpected-error-occurred-while-uploading-your-file" />

			<liferay-ui:error exception="<%= UserPortraitSizeException.class %>">

				<%
				long imageMaxSize = PrefsPropsUtil.getLong(PropsKeys.USERS_IMAGE_MAX_SIZE) / 1024;
				%>

				<liferay-ui:message arguments="<%= imageMaxSize %>" key="please-enter-a-file-with-a-valid-file-size-no-larger-than-x" />
			</liferay-ui:error>

			<liferay-ui:error exception="<%= UserPortraitTypeException.class %>" message="please-enter-a-file-with-a-valid-file-type" />

			<aui:fieldset>
				<aui:input label='<%= LanguageUtil.format(pageContext, "upload-images-no-larger-than-x-k", PrefsPropsUtil.getLong(PropsKeys.UPLOAD_SERVLET_REQUEST_IMPL_MAX_SIZE)/1024, false) %>' name="fileName" size="50" type="file" />

				<div id="<%= portletDisplay.getNamespace() %>portrait-preview-wrapper">
					<img id="<%= portletDisplay.getNamespace() %>portrait-preview" style="display: none" />
				</div>

				<aui:button-row>
					<aui:button name="submitButton" type="submit" />

					<aui:button onClick="window.close();" type="cancel" value="close" />
				</aui:button-row>
			</aui:fieldset>
		</aui:form>

		<aui:script use="aui-io,json,aui-image-cropper,aui-loading-mask">
			<c:if test="<%= windowState.equals(WindowState.MAXIMIZED) %>">
				Liferay.Util.focusFormField(document.<portlet:namespace />fm.<portlet:namespace />fileName);
			</c:if>

			var imageCropper;

			var cropRegionNode = A.one('#<portlet:namespace />cropRegion');
			var fileNameNode = A.one('#<portlet:namespace />fileName');
			var formNode = A.one('#<portlet:namespace />fm');
			var imageNode = A.one('#<portlet:namespace />portrait-preview');
			var imageWrapperNode = A.one('#<portlet:namespace />portrait-preview-wrapper');
			var submitButton = A.one('#<portlet:namespace />submitButton');

			imageNode.on('load', imageLoadHandler);
			fileNameNode.on('change', fileNameChangeHandler);
			formNode.on('submit', submitHandler);

			function imageLoadHandler(event) {
				imageWrapperNode.setStyle('width', 'auto');
				imageWrapperNode.setStyle('height', 'auto');

				imageWrapperNode.loadingmask.hide();

				imageNode.setStyle('display', 'inline');

				if (imageCropper) {
					imageCropper.afterImageChange();
				} else {
					imageCropper = new A.ImageCropper({
						srcNode: imageNode
					}).render();
				}

				submitButton.attr('disabled', false);
				submitButton.ancestor('.aui-button').removeClass('aui-button-disabled');
			}

			function fileNameChangeHandler(event) {
				var uploadURL = '<portlet:actionURL><portlet:param name="struts_action" value="/users_admin/edit_user_portrait" /><portlet:param name="<%= Constants.CMD %>" value="<%= Constants.ADD_TEMP %>" /></portlet:actionURL>';

				var previewURL = '<portlet:resourceURL><portlet:param name="struts_action" value="/users_admin/edit_user_portrait" /><portlet:param name="<%= Constants.CMD %>" value="<%= Constants.GET_TEMP %>" /></portlet:resourceURL>';

				if (!imageWrapperNode.loadingmask) {
					imageWrapperNode.plug(
						A.LoadingMask,
						{
							zIndex: 20
						}
					);
				}

				var maskHeight = Math.max(imageNode.height(), 50);
				var maskWidth = Math.max(imageNode.width(), 175);

				imageWrapperNode.setStyle('width', maskWidth);
				imageWrapperNode.setStyle('height', maskHeight);

				imageWrapperNode.loadingmask.show();

				A.io.request(
					uploadURL,
					{
						method: 'post',
						form: {
							id: '<portlet:namespace />fm',
							upload: true
						},
						on: {
							start: function() {
								submitButton.attr('disabled', true);
								submitButton.ancestor('.aui-button').addClass('aui-button-disabled');
							},
							complete: function(event, id, obj) {
								imageNode.set('src', previewURL);
							}
						}
					}
				);
			}

			function submitHandler(event) {
				if (imageCropper != null) {
					cropRegionNode.set('value', A.JSON.stringify(imageCropper.getCropRegion()));
				}
			}
		</aui:script>
	</c:otherwise>
</c:choose>