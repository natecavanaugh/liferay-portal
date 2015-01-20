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
String contents = (String)request.getAttribute("liferay-ui:input-editor:contents");
String cssClass = GetterUtil.getString((String)request.getAttribute("liferay-ui:input-editor:cssClass"));
String editorImpl = (String)request.getAttribute("liferay-ui:input-editor:editorImpl");
String initMethod = (String)request.getAttribute("liferay-ui:input-editor:initMethod");
String name = namespace + GetterUtil.getString((String)request.getAttribute("liferay-ui:input-editor:name"));

String onChangeMethod = (String)request.getAttribute("liferay-ui:input-editor:onChangeMethod");

if (Validator.isNotNull(onChangeMethod)) {
	onChangeMethod = namespace + onChangeMethod;
}

String onInitMethod = (String)request.getAttribute("liferay-ui:input-editor:onInitMethod");

if (Validator.isNotNull(onInitMethod)) {
	onInitMethod = namespace + onInitMethod;
}

boolean resizable = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-editor:resizable"));
boolean showSource = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-editor:showSource"));
boolean skipEditorLoading = GetterUtil.getBoolean((String)request.getAttribute("liferay-ui:input-editor:skipEditorLoading"));
String toolbarSet = (String)request.getAttribute("liferay-ui:input-editor:toolbarSet");
%>

<c:if test="<%= !skipEditorLoading %>">
	<liferay-util:html-top outputKey="js_editor_tinymce">

		<%
		long javaScriptLastModified = ServletContextUtil.getLastModified(application, "/html/js/", true);
		%>

		<script src="<%= HtmlUtil.escape(PortalUtil.getStaticResourceURL(request, themeDisplay.getCDNHost() + themeDisplay.getPathJavaScript() + "/editor/tiny_mce/tinymce.min.js", javaScriptLastModified)) %>" type="text/javascript"></script>

		<script type="text/javascript">
			Liferay.namespace('EDITORS')['<%= editorImpl %>'] = true;
		</script>
	</liferay-util:html-top>
</c:if>

<div class="<%= cssClass %>">
	<textarea id="<%= name %>" name="<%= name %>" style="height: 100%; visibility: hidden; width: 100%;"><%= (contents != null) ? contents : StringPool.BLANK %></textarea>
</div>

<aui:script use="aui-node-base">
	window['<%= name %>'] = {
		onChangeCallbackCounter: 0,

		init: function(value) {
			if (typeof value != 'string') {
				value = '';
			}

			window['<%= name %>'].setHTML(value);
		},

		destroy: function() {
			tinyMCE.editors['<%= name %>'].destroy();

			window['<%= name %>'] = null;
		},

		fileBrowserCallback: function(field_name, url, type) {
		},

		focus: function() {
			tinyMCE.editors['<%= name %>'].focus();
		},

		getHTML: function() {
			var data;

			if (!window['<%= name %>'].instanceReady) {
				if (window['<%= HtmlUtil.escape(namespace + initMethod) %>']) {
					data = <%= HtmlUtil.escape(namespace + initMethod) %>();
				}
				else {
					data = '<%= contents != null ? contents: StringPool.BLANK %>';
				}
			}
			else {
				data = tinyMCE.editors['<%= name %>'].getContent();
			}

			return data;
		},

		initInstanceCallback: function() {
			<c:if test="<%= (contents == null) && Validator.isNotNull(initMethod) %>">
				window['<%= name %>'].init(<%= HtmlUtil.escape(namespace + initMethod) %>());
			</c:if>

			var iframe = A.one('#<%= name %>_ifr');

			if (iframe) {
				var iframeWin = iframe.getDOM().contentWindow;

				if (iframeWin) {
					var iframeDoc = iframeWin.document.documentElement;

					A.one(iframeDoc).addClass('aui');
				}
			}

			<c:if test="<%= Validator.isNotNull(onInitMethod) %>">
				window['<%= HtmlUtil.escapeJS(onInitMethod) %>']();
			</c:if>

			window['<%= name %>'].instanceReady = true;
		},

		instanceReady: false,

		<%
		if (Validator.isNotNull(onChangeMethod)) {
		%>

			onChangeCallback: function(tinyMCE) {

				// This purposely ignores the first callback event because each call
				// to setContent triggers an undo level which fires the callback
				// when no changes have yet been made.

				// setContent is not really the correct way of initializing this
				// editor with content. The content should be placed statically
				// (from the editor's perspective) within the textarea. This is a
				// problem from the portal's perspective because it's passing the
				// content via a javascript method (initMethod).

				var onChangeCallbackCounter = window['<%= name %>'].onChangeCallbackCounter;

				if (onChangeCallbackCounter > 0) {

					<%= HtmlUtil.escapeJS(onChangeMethod) %>(window['<%= name %>'].getHTML());

				}

				window['<%= name %>'].onChangeCallbackCounter++;
			},

		<%
		}
		%>

		setHTML: function(value) {
			if (window['<%= name %>'].instanceReady) {
				tinyMCE.editors['<%= name %>'].setContent(value);
			}
			else {
				document.getElementById('<%= name %>').innerHTML = value;
			}
		}
	};

	var toolbars = {
		email: {
			toolbar1: 'fontselect fontsizeselect | forecolor backcolor | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify',
			toolbar2: 'cut copy paste bullist numlist | blockquote | undo redo | link unlink image <c:if test="<%= showSource %>">code</c:if> | hr removeformat  | preview print fullscreen',
			toolbar3: ''
		},
		liferay: {
			toolbar1: 'styleselect fontselect fontsizeselect | forecolor backcolor | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify',
			toolbar2: 'cut copy paste searchreplace bullist numlist | outdent indent blockquote | undo redo | link unlink anchor image media <c:if test="<%= showSource %>">code</c:if>',
			toolbar3: 'table | hr removeformat | subscript superscript | charmap emoticons | preview print fullscreen'
		},
		phone: {
			toolbar1: 'bold italic underline | bullist numlist',
			toolbar2: 'link unlink image',
			toolbar3: ''
		},
		simple: {
			toolbar1: 'bold italic underline strikethrough | bullist numlist | table | link unlink image <c:if test="<%= showSource %>">code</c:if>',
			toolbar2: '',
			toolbar3: ''
		},
		tablet: {
			toolbar1: 'styleselect fontselect fontsizeselect | bold italic underline strikethrough |  alignleft aligncenter alignright alignjustify',
			toolbar2: 'bullist numlist | link unlink image <c:if test="<%= showSource %>">code</c:if>',
			toolbar3: ''
		}
	};

	var currentToolbarSet = '<%= TextFormatter.format(HtmlUtil.escapeJS(toolbarSet), TextFormatter.M) %>';

	var Util = Liferay.Util;

	if (Util.isPhone()) {
		currentToolbarSet = 'phone';
	}
	else if (Util.isTablet()) {
		currentToolbarSet = 'tablet';
	}

	tinyMCE.init(
		{
			content_css: '<%= HtmlUtil.escapeJS(themeDisplay.getPathThemeCss()) %>/aui.css,<%= HtmlUtil.escapeJS(themeDisplay.getPathThemeCss()) %>/main.css',
			convert_urls: false,
			extended_valid_elements: 'a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name|usemap],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]',
			file_browser_callback: window['<%= name %>'].fileBrowserCallback,
			init_instance_callback: window['<%= name %>'].initInstanceCallback,
			invalid_elements: 'script',
			language: '<%= HtmlUtil.escape(locale.getLanguage()) %>',
			menubar: false,
			mode: 'exact',

			<%
			if (Validator.isNotNull(onChangeMethod)) {
			%>

				onchange_callback: window['<%= name %>'].onChangeCallback,

			<%
			}
			%>

			plugins: [
				'advlist autolink autosave link image lists charmap print preview hr anchor',
				'searchreplace wordcount fullscreen media <c:if test="<%= showSource %>">code</c:if>',
				'table contextmenu emoticons textcolor paste fullpage textcolor colorpicker textpattern'
			],
			relative_urls: false,
			remove_script_host: false,
			selector: '#<%= name %>',
			style_formats: [
				{title: 'Normal', inline: 'p'},
				{title: 'Heading 1', block: 'h1'},
				{title: 'Heading 2', block: 'h2'},
				{title: 'Heading 3', block: 'h3'},
				{title: 'Heading 4', block: 'h4'},
				{title: 'Preformatted Text', block: 'pre'},
				{title: 'Cited Work', inline: 'cite'},
				{title: 'Computer Code', inline: 'code'},
				{title: 'Info Message', block: 'div', classes: 'portlet-msg-info'},
				{title: 'Alert Message', block: 'div', classes: 'portlet-msg-alert'},
				{title: 'Error Message', block: 'div', classes: 'portlet-msg-error'}
			],
			toolbar1: toolbars[currentToolbarSet].toolbar1,
			toolbar2: toolbars[currentToolbarSet].toolbar2,
			toolbar3: toolbars[currentToolbarSet].toolbar3,
			toolbar_items_size: 'small'
		}
	);
</aui:script>