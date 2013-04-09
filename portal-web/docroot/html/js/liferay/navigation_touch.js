AUI.add(
	'liferay-navigation-touch',
	function(A) {
		var NavigationProto = Liferay.Navigation.prototype;

		NavigationProto.TPL_DELETE_BUTTON = NavigationProto.TPL_DELETE_BUTTON.replace('aui-hide', '');
	},
	'',
	{
		requires: ['event-touch', 'liferay-navigation']
	}
);