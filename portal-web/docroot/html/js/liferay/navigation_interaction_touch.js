AUI.add(
	'liferay-navigation-interaction-touch',
	function(A) {
		var NavigationInterProto = Liferay.NavigationInteraction.prototype;

		NavigationInterProto._initChildMenuHandlers = function(navigation) {
			var instance = this;

			if (navigation) {
				navigation.delegate('click', instance._onTouchClick, '> li > a', instance);
			}
		};

		NavigationInterProto._initNodeFocusManager = A.Lang.emptyFn;

		NavigationInterProto._onTouchClick = function(event) {
			var instance = this;

			var menuNew = event.currentTarget.ancestor(instance._directChildLi);

			if (menuNew.one('.child-menu') && !menuNew.hasClass('hover')) {
				event.preventDefault();
			}

			instance._handleShowNavigationMenu(menuNew, instance.MAP_HOVER.menu);
		};
	},
	'',
	{
		requires: ['event-touch', 'liferay-navigation-interaction']
	}
);