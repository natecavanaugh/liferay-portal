Liferay.Util.portletTitleEdit = function() {
};

if (!themeDisplay.isStatePopUp()) {
	AUI().ready('aui-live-search', 'aui-io-request', 'aui-overlay-context-panel', 'aui-viewport', 'event-mouseenter', 'liferay-message', 'liferay-panel', 'liferay-store', 'node-focusmanager', 'transition',
		function(A) {
			var body = A.getBody();
			var win = A.getWin();

			var portletInformationEl = A.one('#cpContextPanelTemplate');
			var portletInformationIcon = A.one('#cpPortletTitleHelpIcon');

			var portletId = '';
			var visible = false;

			if (portletInformationEl) {
				portletId = portletInformationEl.attr('data-portlet-id');
				visible = (portletInformationEl.attr('data-visible-panel') == 'true');
			}

			var sessionKey = 'show-portlet-description-' + portletId;

			var trim = A.Lang.trim;

			var CSS_PANELS_MINIMIZED = 'panels-minimized';

			var CSS_SEARCH_PANEL_ACTIVE = 'search-panel-active';

			var EVENT_DATA_SIDEBAR = {
				persist: true
			};

			var SELECTOR_SEARCH_NODES = '#controlPanelMenuAddContentPanelContainer ul li a';

			var TPL_CANCEL_SEARCH_BUTTON = '<a class="cancel-search" href="javascript:;"></a>';

			var TPL_NAVIGATION_LIST_ITEM = '<li class="toggle-navigation"><a href="javascript:;"><span>' + Liferay.Language.get('minimize-panels') + '</span></a></li>';

			var ControlPanel = {
				init: function() {
					var instance = this;

					instance._renderUI();

					instance._bindUI();

					instance._createCancelButton();

					instance._createFocusManager();

					instance._createLiveSearch();
				},

				_afterHiddenChange: function(event) {
					var instance = this;

					instance._uiSetHidden(event.newVal, event.persist);
				},

				_bindUI: function() {
					var instance = this;

					Liferay.set('controlPanelSidebarHidden', body.hasClass(CSS_PANELS_MINIMIZED));

					Liferay.after('controlPanelSidebarHiddenChange', instance._afterHiddenChange, instance);

					instance._toggleNavigationLink.on('click', instance._toggleHidden, instance);

					instance._searchPanelHolder.on('click', instance._setVisible, instance);

					var sidebarPanel = Liferay.Panel.get('addContentPanelContainer');

					if (sidebarPanel) {
						sidebarPanel.on(
							'collapse',
							function(event) {
								if (Liferay.get('controlPanelSidebarHidden')) {
									event.preventDefault();
								}
							}
						);
					}

					Liferay.publish(
						'focusSearchBar',
						{
							defaultFn: A.bind(instance._focusSearchBar, instance)
						}
					);

					A.getDoc().on(
						'keyup',
						function(event) {
							if (event.isKey('ESC')) {
								Liferay.fire('focusSearchBar');
							}
						}
					);
				},

				_createCancelButton: function() {
					var instance = this;

					var cancelSearch = A.Node.create(TPL_CANCEL_SEARCH_BUTTON);
					var toggleNavigation = A.Node.create(TPL_NAVIGATION_LIST_ITEM);

					var toggleNavigationLink = toggleNavigation.one('a');
					instance._searchPanelHolder.append(cancelSearch);

					var searchNodes = A.all(SELECTOR_SEARCH_NODES);

					cancelSearch.on(
						'click',
						function(event) {
							body.removeClass(CSS_SEARCH_PANEL_ACTIVE);

							instance._searchPanelInput.val('').focus();

							searchNodes.show();

							instance._searchActive = false;
						}
					);

					toggleNavigationLink.on(
						'click',
						function(event){
							body.removeClass(CSS_SEARCH_PANEL_ACTIVE);

							instance._searchPanelInput.val('').focus();

							searchNodes.show();

							instance._searchActive = false;
						}
					);
				},

				_createDataConnection: function() {
					var instance = this;

					instance._saveData = A.io.request(
						themeDisplay.getPathMain() + '/portal/session_click',
						{
							autoLoad: false
						}
					);
				},

				_createFocusManager: function() {
					var instance = this;

					var addContentPanelContainer = A.one('#controlPanelMenuAddContentPanelContainer');

					addContentPanelContainer.plug(
						A.Plugin.NodeFocusManager,
						{
							descendants: '.lfr-panel-content ul li a:visible',
							keys: {
								next: 'down:40',
								previous: 'down:38'
							}
						}
					);

					var focusManager = addContentPanelContainer.focusManager;

					instance._refreshFocusManagerTask = A.debounce(focusManager.refresh, 50, focusManager);

					instance._searchPanelInput.on(
						'key',
						function(event) {
							focusManager.focus(0);
						},
						'down:40'
					);
				},

				_createLiveSearch: function() {
					var instance = this;

					var searchPanelInput = instance._searchPanelInput;

					var liveSearch = new A.LiveSearch(
						{
							input: searchPanelInput,
							nodes: SELECTOR_SEARCH_NODES,

							data: function(node) {
								return node.text();
							},

							on: {
								search: function(event) {
									if (trim(liveSearch.get('searchValue'))) {
										body.addClass(CSS_SEARCH_PANEL_ACTIVE);

										instance._searchActive = true;
									}
								}
							},

							after: {
								search: function(event) {
									instance._searchActive = true;

									instance._refreshFocusManagerTask();

									if (!trim(liveSearch.get('searchValue'))) {
										body.removeClass(CSS_SEARCH_PANEL_ACTIVE);

										instance._searchActive = false;
									}
								}
							}
						}
					);

					instance._liveSearch = liveSearch;
				},

				_focusSearchBar: function(event) {
					var instance = this;

					instance._searchPanelInput.selectText();

					Liferay.set('controlPanelSidebarHidden', false, EVENT_DATA_SIDEBAR);
				},

				_renderUI: function() {
					var instance = this;

					var controlPanelTools = A.one('.control-panel-tools');
					var panelHolder = A.one('.panel-page-menu');
					var pinDockbar = A.one('.pin-dockbar');
					var searchPanelHolder = A.one('.search-panels');

					var toggleNavigation = A.Node.create(TPL_NAVIGATION_LIST_ITEM);

					var searchPanelInput = searchPanelHolder.one('input');

					instance._toggleNavigationLink = toggleNavigation.one('a');

					pinDockbar.placeAfter(toggleNavigation);

					searchPanelHolder.on(
						'click',
						function(event){
							body.removeClass('panels-minimized');
						}
					);

					win.on(
						'orientationchange',
						function(event) {
							var orientation = win.orientation;

							if (orientation == 0 || orientation == 180) {
								body.addClass('panels-minimized');
							}
							else {
								var selectNavigation = A.one('.select-navigation-wrapper');
								var selectSite = A.one('.select-site-wrapper');

								selectNavigation.blur();
								selectSite.blur();

								body.removeClass('panels-minimized');
							}
						}
					);

					searchPanelInput.attr('autocomplete', 'off');

					if (portletInformationEl) {
						instance._helpBox = new Liferay.Message(
							{
								contentBox: portletInformationEl,
								id: sessionKey,
								persistenceCategory: 'enable-portlet-descriptions',
								strings: {
									dismissAll: Liferay.Language.get('or-disable-for-all-portlets')
								},
								trigger: portletInformationIcon,
								type: 'help',
								visible: portletInformationEl.test(':visible')
							}
						).render();
					}

					instance._controlPanelTools = controlPanelTools;
					instance._panelHolder = panelHolder;
					instance._searchPanelHolder = searchPanelHolder;
					instance._searchPanelInput = searchPanelInput;
				},

				_setVisible: function(event) {
					var instance = this;

					Liferay.set('controlPanelSidebarHidden', false, EVENT_DATA_SIDEBAR);
				},

				_toggleHidden: function(event) {
					var instance = this;

					Liferay.set('controlPanelSidebarHidden', !Liferay.get('controlPanelSidebarHidden'), EVENT_DATA_SIDEBAR);
				},

				_uiSetHidden: function(newVal, persist) {
					var instance = this;

					var liveSearch = instance._liveSearch;

					var panelCfg = instance._panelCfg;

					var toggleValue = panelCfg.closeValue;

					var searchValue = instance._searchValue;

					if (!newVal) {
						toggleValue = panelCfg.openValue;
					}
					else {
						searchValue = '';

						instance._searchValue = liveSearch.get('searchValue');
					}

					liveSearch.search(searchValue);

					if (!A.UA.touch) {
						instance._panelHolder.transition(
							{
								left: toggleValue,
								easing: 'ease-out',
								duration: 0.2
							},
							function() {
								body.toggleClass(CSS_PANELS_MINIMIZED, newVal);

								if (!newVal) {
									instance._searchPanelInput.selectText();
								}
							}
						);
					}
					else {
						body.toggleClass(CSS_PANELS_MINIMIZED, newVal);
					}

					if (persist) {
						Liferay.Store('control-panel-sidebar-minimized', newVal);
					}

					body.toggleClass(CSS_SEARCH_PANEL_ACTIVE, (instance._searchActive && !newVal));
				},

				_eventOpacities: {
					mouseenter: 1,
					mouseleave: 0
				},

				_panelCfg: {
					closeValue: '-238px',
					openValue: '0'
				},

				_searchActive: false,
				_searchValue: ''
			};

			ControlPanel.init();
		}
	);
}