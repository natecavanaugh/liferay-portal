<#include "../init.ftl">

<#assign editorName = propsUtil.get("editor.wysiwyg.portal-impl.portlet.ddm.text_html.ftl")>

<#assign inputEditorName = "${namespacedFieldName}Editor">

<#assign fieldValue = paramUtil.getString(request, "${inputEditorName}", fieldValue)>

<div class="lfr-ddm-field-group">
	<@aui["field-wrapper"] cssClass="field-wrapper-html" data=data helpMessage=escape(fieldStructure.tip) label=escape(label) name=inputEditorName required=required>
		<#assign skipEditorLoading = paramUtil.getBoolean(request, "p_p_isolated")>

		<@liferay_ui["input-editor"]
			contentsLanguageId="${requestedLocale}"
			editorName="${editorName}"
			initMethod=""
			name="${namespacedFieldName}Editor"
			onChangeMethod="${namespacedFieldName}OnChangeEditor"
			skipEditorLoading=skipEditorLoading
		/>

		<@aui.input name=namespacedFieldName type="hidden" value=fieldValue>
			<#if required>
				<@aui.validator name="required" />
			</#if>
		</@>
	</@>

	${fieldStructure.children}
</div>