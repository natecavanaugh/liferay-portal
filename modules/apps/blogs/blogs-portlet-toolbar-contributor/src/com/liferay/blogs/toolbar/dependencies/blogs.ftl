<#assign aui = taglibLiferayHash["/WEB-INF/tld/aui.tld"] />
<#assign liferay_portlet = taglibLiferayHash["/WEB-INF/tld/liferay-portlet.tld"] />
<#assign liferay_ui = taglibLiferayHash["/WEB-INF/tld/liferay-ui.tld"] />
<#assign portlet = taglibLiferayHash["/WEB-INF/tld/liferay-portlet_2_0.tld"] />

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

<#if showSubscribeButton>
	<#if isSubscribed>
		<@liferay_portlet["actionURL"] varImpl="unsubscribeURL">
			<@portlet["param"] name="struts_action" value="/blogs/edit_entry" />
			<@portlet["param"] name="cmd" value="unsubscribe" />
			<@portlet["param"] name="redirect" value=currentURL />
		</@>

		<@liferay_ui["icon"]
			iconCssClass="icon-remove-sign"
			label=true
			message="unsubscribe"
			url=unsubscribeURL.toString()
		/>
	<#else>
		<@liferay_portlet["actionURL"] varImpl="subscribeURL">
			<@portlet["param"] name="struts_action" value="/blogs/edit_entry" />
			<@portlet["param"] name="cmd" value="subscribe" />
			<@portlet["param"] name="redirect" value=currentURL />
		</@>

		<@liferay_ui["icon"]
			iconCssClass="icon-ok-sign"
			label=true
			message="subscribe"
			url=subscribeURL.toString()
		/>
	</#if>
</#if>