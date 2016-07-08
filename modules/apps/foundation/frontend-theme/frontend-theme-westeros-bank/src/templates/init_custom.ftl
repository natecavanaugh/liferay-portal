<#assign showGlobaMenuOn = themeDisplay.getThemeSetting("show-global-menu-on")>
<#assign showRecursiveMenuOn = themeDisplay.getThemeSetting("show-recursive-menu-on")>
<#assign showMainSearch = themeDisplay.getThemeSetting("show-main-search")>

<#if showGlobaMenuOn = "all-screens">
	<#assign global_menu_class = "all-screens">
<#elseif showGlobaMenuOn = "big-screens">
	<#assign global_menu_class = "hidden-xs">
<#elseif showGlobaMenuOn = "small-screens">
	<#assign global_menu_class = "hidden-md">
<#else>
	<#assign global_menu_class = "no-screen">
</#if>

<#if showRecursiveMenuOn = "all-screens">
	<#assign main_recursive_menu_class = "all-screens">
<#elseif showRecursiveMenuOn = "big-screens">
	<#assign main_recursive_menu_class = "hidden-xs">
<#elseif showRecursiveMenuOn = "small-screens">
	<#assign main_recursive_menu_class = "hidden-md">
<#else>
	<#assign main_recursive_menu_class = "no-screen">
</#if>

<#if showMainSearch = "all-screens">
	<#assign main_search_class = "all-screens">
<#elseif showMainSearch = "big-screens">
	<#assign main_search_class = "hidden-xs">
<#elseif showMainSearch = "small-screens">
	<#assign main_search_class = "hidden-md">
<#else>
	<#assign main_search_class = "no-screen">
</#if>