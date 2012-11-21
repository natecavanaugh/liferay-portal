<#--
This file allows you to override and define new FreeMarker variables.
-->

<#assign aui_guid = 0 />

<#macro aui_drop_down_ul nav_items orientation>
	<#assign aui_guid = aui_guid + 1 />

	<#assign menu_class="yui3-menu" />

	<#if validator.isNotNull(orientation)>
		<#assign menu_class = "${menu_class} yui3-menu-${orientation}" />
	<#else>
		<#assign menu_class = "${menu_class} yui3-menu-hidden" />
	</#if>

	<div class="${menu_class}" id="yui3-menu-${aui_guid}">
		<div class="yui3-menu-content">
			<ul>
				<#list nav_items as nav_item>
					<#assign has_child = "" />
					<#assign menu_item = "lfr-nav-item" />
					<#assign menu_item_label = "yui3-menuitem-content" />
					<#assign selected_anchor = "" />

					<#if nav_item.hasChildren()>
						<#assign has_child = "has-child" />
						<#assign menu_item_label = "${menu_item_label} yui3-menu-label" />
					<#else>
						<#assign menu_item = "${menu_item} yui3-menuitem" />
					</#if>

					<#if nav_item.isSelected() || nav_item.isChildSelected()>
						<#assign menu_item = "${menu_item} selected" />
						<#assign selected_anchor = "selected-anchor" />
					</#if>

					<li class="${menu_item}" data-layoutId="${nav_item.getLayout().getLayoutId()}">
						<a class="${menu_item_label} ${selected_anchor} ${has_child}" href="${nav_item.getURL()}" ${nav_item.getTarget()}>${nav_item.icon()} ${nav_item.getName()}</a>

						<#if nav_item.hasChildren()>
							<@aui_drop_down_ul nav_items=nav_item.getChildren() orientation="" />
						</#if>
					</li>
				</#list>
			</ul>
		</div>
	</div>
</#macro>

<#macro aui_drop_down nav_items orientation>
	<#if aui_guid == 0>
		<link href="${theme_display.getPathJavaScript()}/aui/node-menunav/assets/node-menunav-core.css" rel="stylesheet" type="text/css" />
	</#if>

	<#assign menu_id = aui_guid + 1 />

	<@aui_drop_down_ul nav_items=nav_items orientation=orientation />

	<script type="text/javascript">
		Liferay.Data.NAV_ITEM_SELECTOR = '> .yui3-menu > .yui3-menu-content > ul > li';

		AUI().use(
			'node-menunav',
			function(A) {
				A.one('\#yui3-menu-${menu_id}').plug(A.Plugin.NodeMenuNav);
			}
		);
	</script>
</#macro>