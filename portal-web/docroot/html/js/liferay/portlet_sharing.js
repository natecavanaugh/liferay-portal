Liferay.namespace('PortletSharing');

Liferay.provide(
	Liferay.PortletSharing,
	'showNetvibesInfo',
	function(netvibesURL) {
		var A = AUI();

		var portletURL = Liferay.PortletURL.createResourceURL();

		portletURL.setPortletId(133);

		portletURL.setParameter('netvibesURL', netvibesURL);

		var dialog = new A.Modal(
			{
				centered: true,
				destroyOnClose: true,
				headerContent: Liferay.Language.get('add-to-netvibes'),
				modal: true,
				width: 550
			}
		).render();

		dialog.plug(
			A.Plugin.IO,
			{
				uri: portletURL.toString()
			}
		);
	},
	['aui-io-plugin-deprecated', 'aui-modal', 'liferay-portlet-url']
);

Liferay.provide(
	Liferay.PortletSharing,
	'showWidgetInfo',
	function(widgetURL) {
		var A = AUI();

		var portletURL = Liferay.PortletURL.createResourceURL();

		portletURL.setPortletId(133);

		portletURL.setParameter('widgetURL', widgetURL);

		var dialog = new A.Modal(
			{
				centered: true,
				destroyOnClose: true,
				headerContent: Liferay.Language.get('add-to-any-website'),
				modal: true,
				width: 550
			}
		).render();

		dialog.plug(
			A.Plugin.IO,
			{
				uri: portletURL.toString()
			}
		);
	},
	['aui-io-plugin-deprecated', 'aui-modal', 'liferay-portlet-url']
);