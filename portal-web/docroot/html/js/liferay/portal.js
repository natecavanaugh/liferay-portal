;(function(A, Liferay) {
	var Tabs = Liferay.namespace('Portal.Tabs');
	var ToolTip = Liferay.namespace('Portal.ToolTip');

	var arrayIndexOf = A.Array.indexOf;

	var toCharCode = Liferay.Util.toCharCode;

	Liferay.Portal.Tabs._show = function(event) {
		var id = event.id;
		var names = event.names;
		var namespace = event.namespace;

		var selectedIndex = event.selectedIndex;

		var tabItem = event.tabItem;
		var tabSection = event.tabSection;

		if (tabItem) {
			tabItem.radioClass('aui-selected');
			tabItem.radioClass('aui-state-active');
			tabItem.radioClass('aui-tab-active');
			tabItem.radioClass('current');
		}

		if (tabSection) {
			tabSection.show();
		}

		names.splice(selectedIndex, 1);

		var el;

		for (var i = 0; i < names.length; i++) {
			el = A.one('#' + namespace + toCharCode(names[i]) + 'TabsSection');

			if (el) {
				el.hide();
			}
		}
	};

	Liferay.provide(
		Tabs,
		'show',
		function(namespace, names, id, callback) {
			var namespacedId = namespace + toCharCode(id);

			var tab = A.one('#' + namespacedId + 'TabsId');
			var tabSection = A.one('#' + namespacedId + 'TabsSection');

			var details = {
				id: id,
				names: names,
				namespace: namespace,
				selectedIndex: arrayIndexOf(names, id),
				tabItem: tab,
				tabSection: tabSection
			};

			if (callback && A.Lang.isFunction(callback)) {
				callback.call(this, namespace, names, id, details);
			}

			Liferay.fire('showTab', details);
		},
		['aui-base']
	);

	Liferay.publish(
		'showTab',
		{
			defaultFn: Liferay.Portal.Tabs._show
		}
	);

	Liferay.provide(
		ToolTip,
		'hide',
		function() {
			var instance = this;

			if (instance._cached) {
				instance._cached.hide();
			}
		},
		['aui-tooltip']
	);

	Liferay.provide(
		ToolTip,
		'addShowListeners',
		function() {
			var DOC = A.config.doc;

			var TOOLTIP_TARGET = '.tooltip-target';

			var showTooltip = function(event) {
				var node = event.currentTarget;

				var tooltipContainerNode = node.next('.tooltip-container');

				if (tooltipContainerNode) {
					var content = tooltipContainerNode.get('innerHTML');

					Liferay.Portal.ToolTip.show(node, content);
				}
			};

			A.delegate('mouseover', showTooltip, DOC, TOOLTIP_TARGET );

			A.delegate('focus', showTooltip, DOC, TOOLTIP_TARGET );

			A.delegate('blur', Liferay.Portal.ToolTip.hide, DOC, TOOLTIP_TARGET);
		},
		['aui-tooltip']
	);

	Liferay.provide(
		ToolTip,
		'show',
		function(obj, text) {
			var instance = this;

			if (!instance._cached) {
				instance._cached = new A.Tooltip(
					{
						trigger: '.liferay-tooltip',
						zIndex: 10000
					}
				).render();
			}

			var cached = instance._cached;

			var trigger = cached.get('trigger');
			var newElement = (trigger.indexOf(obj) == -1);
			var bodyContent = cached.get('bodyContent');

			if (newElement || (bodyContent != text)) {
				cached.set('trigger', obj);
				cached.set('bodyContent', text);
				cached.show();
			}

			cached.refreshAlign();
		},
		['aui-tooltip']
	);
})(AUI(), Liferay);