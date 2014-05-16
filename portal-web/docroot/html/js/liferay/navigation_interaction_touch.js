AUI.add(
	'liferay-navigation-interaction-touch',
	function(A) {
		var STR_OPEN = 'open';

		var android = A.UA.android;

		var androidLegacy = (android && android < 4.4);

		A.mix(
			Liferay.NavigationInteraction.prototype,
			{
				_handleShowNavigationMenu: function(menuNew, menuOld, event) {
					var instance = this;

					var mapHover = instance.MAP_HOVER;

					mapHover.menu = menuNew;

					var menuOpen = menuNew.hasClass(STR_OPEN);

					var handleId = menuNew.attr('id') + 'Handle';

					var handle = Liferay.Data[handleId];

					if (!menuOpen) {
						Liferay.fire('showNavigationMenu', mapHover);

						var outsideEvents = ['clickoutside', 'touchendoutside'];

						if (androidLegacy) {
							outsideEvents = outsideEvents[0];
						}

						handle = menuNew.on(
							outsideEvents,
							function() {
								Liferay.fire(
									'hideNavigationMenu',
									{
										menu: menuNew
									}
								);

								Liferay.Data[handleId] = null;

								handle.detach();
							}
						);
					}
					else {
						Liferay.fire('hideNavigationMenu', mapHover);

						if (handle) {
							handle.detach();

							handle = null;
						}
					}

					Liferay.Data[handleId] = handle;
				},

				_initChildMenuHandlers: function(navigation) {
					var instance = this;

					if (navigation) {
						A.Event.defineOutside('touchend');

						navigation.delegate('tap', instance._onTouchClick, '.lfr-nav-child-toggle', instance);

						if (androidLegacy) {
							navigation.delegate(
								'click',
								function(event) {
									event.preventDefault();
								},
								'.lfr-nav-child-toggle'
							);
						}
					}
				},

				_initNodeFocusManager: A.Lang.emptyFn,

				_onTouchClick: function(event) {
					var instance = this;

					var menuNew = event.currentTarget.ancestor(instance._directChildLi);

					if (menuNew.one('.child-menu')) {
						event.preventDefault();

						instance._handleShowNavigationMenu(menuNew, instance.MAP_HOVER.menu, event);
					}
				}
			},
			true
		);
	},
	'',
	{
		requires: ['event-tap', 'event-touch', 'liferay-navigation-interaction']
	}
);