AUI.add(
	'liferay-navigation-interaction-touch',
	function(A) {
		var NavigationInteractionProto = Liferay.NavigationInteraction.prototype;

		NavigationInteractionProto._initChildMenuHandlers = function(navigation) {
			var instance = this;

			if (navigation) {
				navigation.delegate('click', instance._onTouchClick, '> li > a', instance);
			}
		};

		NavigationInteractionProto._initNodeFocusManager = A.Lang.emptyFn;

		NavigationInteractionProto._onTouchClick = function(event) {
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