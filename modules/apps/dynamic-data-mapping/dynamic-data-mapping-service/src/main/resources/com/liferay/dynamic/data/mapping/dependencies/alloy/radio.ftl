<#include "../init.ftl">

<#assign cssClass = "feopwfkqwepfkqewf">

<#if required>
	<#assign label = label + " (" + languageUtil.get(requestedLocale, "required") + ")">
</#if>

<div class="lfr-ddm-field-group">
	<@aui["field-wrapper"] data=data helpMessage=escape(fieldStructure.tip) label=escape(label)>
		${fieldStructure.children}
	</@>
</div>