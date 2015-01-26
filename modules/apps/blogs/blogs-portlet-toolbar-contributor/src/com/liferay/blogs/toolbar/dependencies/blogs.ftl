<#assign aui = taglibLiferayHash["/WEB-INF/tld/aui.tld"] />
<#assign liferay_portlet = taglibLiferayHash["/WEB-INF/tld/liferay-portlet.tld"] />
<#assign liferay_ui = taglibLiferayHash["/WEB-INF/tld/liferay-ui.tld"] />
<#assign portlet = taglibLiferayHash["/WEB-INF/tld/liferay-portlet_2_0.tld"] />

<@aui.input name="switch_test_0" label="" type="switch" />

<#if blogsPortletInstanceSettings.isEnableRSS()>
	<@liferay_portlet["resourceURL"] varImpl="rssURL">
		<@portlet["param"] name="struts_action" value="/blogs/rss" />
	</@>

	<@liferay_ui["rss"]
		delta=blogsPortletInstanceSettings.getRssDelta()
		displayStyle=blogsPortletInstanceSettings.getRssDisplayStyle()
		feedType=blogsPortletInstanceSettings.getRssFeedType()
		resourceURL=rssURL
	/>
</#if>