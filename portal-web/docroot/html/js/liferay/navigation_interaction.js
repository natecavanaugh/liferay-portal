AUI.add(
	'liferay-navigation-interaction',
	function(A) {
		var ACTIVE_DESCENDANT = 'activeDescendant';

		var DIRECTION_LEFT = 0;

		var DIRECTION_RIGHT = 1;

		var HOVER = 'hover';

		var MAP_HOVER = {};

		var NAME = 'liferaynavigationinteraction';

		var TOUCH = A.UA.touch;

		var hideMenu = function() {
			if (MAP_HOVER.menu) {
				Liferay.fire('hideNavigationMenu', MAP_HOVER);

				MAP_HOVER = {};
			}
		};

		var NavigationInteraction = A.Component.create(
			{
				EXTENDS: A.Plugin.Base,

				NAME: NAME,

				NS: NAME,

				prototype: {
					initializer: function(config) {
						var instance = this;

						var host = instance.get('host');
						var navigation = host.one('> ul');

						var hostULId = '#' + navigation.guid();

						instance._directChildLi = hostULId + '> li';

						instance._hostULId = hostULId;

						Liferay.on(
							['hideNavigationMenu', 'showNavigationMenu'],
							function(event) {
								var showMenu = event.type == 'showNavigationMenu';

								instance._lastShownMenu = null;

								if (showMenu) {
									instance._lastShownMenu = event.menu;
								}

								event.menu.toggleClass(HOVER, showMenu);
							}
						);

						if (navigation) {
							if (TOUCH) {
								navigation.delegate('click', instance._onTouchClick, '> li > a', instance);
							}
							else {
								navigation.delegate(['mouseenter', 'mouseleave'], instance._onMouseToggle, '> li', instance);

								navigation.delegate('keydown', instance._handleKeyDown, 'a', instance);
							}
						}

						if (!TOUCH) {
							host.plug(
								A.Plugin.NodeFocusManager,
								{
									descendants: 'a',
									focusClass: 'active',
									keys: {
										next: 'down:40',
										previous: 'down:38'
									}
								}
							);

							var focusManager = host.focusManager;

							focusManager.after(['activeDescendantChange', 'focusedChange'], instance._showMenu, instance);

							instance._focusManager = focusManager;
						}
					},

					_handleExit: function(event) {
						var instance = this;

						var focusManager = instance._focusManager;

						if (focusManager.get(ACTIVE_DESCENDANT)) {
							focusManager.set(ACTIVE_DESCENDANT, 0);

							focusManager.blur();

							hideMenu();
						}
						else {
							setTimeout(hideMenu, 0);
						}
					},

					_handleKey: function(event, direction) {
						var instance = this;

						var target = event.target;

						var parent = target.ancestors(instance._directChildLi).item(0);

						var fallbackFirst = true;
						var item;

						if (direction === DIRECTION_LEFT) {
							item = parent.previous();

							fallbackFirst = false;
						}
						else {
							item = parent.next();
						}

						if (!item) {
							var siblings = parent.siblings();

							if (fallbackFirst) {
								item = siblings.first();
							}
							else {
								item = siblings.last();
							}
						}

						instance._focusManager.focus(item.one('a'));
					},

					_handleKeyDown: function(event) {
						var instance = this;

						var handler;

						if (event.isKey('LEFT')) {
							handler = '_handleLeft';
						}
						else if (event.isKey('RIGHT')) {
							handler = '_handleRight';
						}
						else if (event.isKey('TAB') || event.isKey('ESC')) {
							handler = '_handleExit';
						}

						if (handler) {
							instance[handler](event);
						}
					},

					_handleLeft: function(event) {
						var instance = this;

						instance._handleKey(event, DIRECTION_LEFT);
					},

					_handleRight: function(event) {
						var instance = this;

						instance._handleKey(event, DIRECTION_RIGHT);
					},

					_onMouseToggle: function(event) {
						var instance = this;

						var showMenu = event.type == 'mouseenter';
						var eventType = 'hideNavigationMenu';

						if (showMenu) {
							eventType = 'showNavigationMenu';
						}

						MAP_HOVER.menu = event.currentTarget;

						Liferay.fire(eventType, MAP_HOVER);
					},

					_onTouchClick: function(event) {
						var instance = this;

						var menuNew = event.currentTarget.ancestor(instance._directChildLi);

						var childMenu = menuNew.one('.child-menu');

						if (childMenu && !menuNew.hasClass(HOVER)) {
							event.preventDefault();
						}

						instance._showNavigationMenu(menuNew, MAP_HOVER.menu);
					},

					_showMenu: function(event) {
						var instance = this;

						event.halt();

						var menuOld = MAP_HOVER.menu;

						var newVal = event.newVal;

						var handleHover = (newVal || newVal === 0);

						if (handleHover) {
							var focusManager = instance._focusManager;

							var activeDescendant = focusManager.get(ACTIVE_DESCENDANT);
							var descendants = focusManager.get('descendants');

							var link = descendants.item(activeDescendant);

							var menuNew = link.ancestor(instance._directChildLi);

							instance._showNavigationMenu(menuNew, menuOld);
						}
						else if (menuOld) {
							Liferay.fire('hideNavigationMenu', MAP_HOVER);

							MAP_HOVER = {};
						}
					},

					_showNavigationMenu: function(menuNew, menuOld) {
						var instance = this;

						if (!(instance._lastShownMenu && (event.type.indexOf('focusedChange') !== -1))) {
							var updateMenu = (menuOld && menuOld != menuNew);

							if (updateMenu) {
								Liferay.fire('hideNavigationMenu', MAP_HOVER);
							}

							if (!menuOld || updateMenu) {
								MAP_HOVER.menu = menuNew;

								Liferay.fire('showNavigationMenu', MAP_HOVER);
							}
						}
					}
				}
			}
		);

		Liferay.NavigationInteraction = NavigationInteraction;
	},
	'',
	{
		requires: ['node-focusmanager', 'plugin']
	}
);