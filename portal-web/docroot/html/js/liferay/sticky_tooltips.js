AUI.add(
	'liferay-sticky-tooltips',
	function(A) {
		var Lang = A.Lang;

		var BODY_CONTENT = 'bodyContent';

		var HOST = 'host';

		var MOUSEENTER = 'mouseenter';

		var MOUSELEAVE = 'mouseleave';

		var NAME = 'liferaystickytooltips';

		var STR_EMPTY = '';

		var TOOLTIPTEXT = 'toolTipText';

		var TRIGGER = 'trigger';

		var StickyToolTips = A.Component.create(
			{
				EXTENDS: A.Plugin.Base,

				NAME: NAME,

				NS: NAME,

				ATTRS: {
					icon: {
						value: null
					},

					toolTipText: {
						value: 'n/a'
					}
				},

				prototype: {
					initializer: function() {
						var instance = this;

						var cached = instance._cached;

						var icon = instance.get(HOST);

						var text = instance.get(TOOLTIPTEXT);

						if (!cached) {
							cached = new A.Tooltip(
								{
									cssClass: 'tooltip-help',
									opacity: 1,
									triggerHideEvent: STR_EMPTY,
									visible: false,
									zIndex: Liferay.zIndex.TOOLTIP
								}
							).render();

							instance._cached = cached;
						}

						cached.set(BODY_CONTENT, text);

						cached.set(TRIGGER, icon).show();

						icon.on(MOUSELEAVE, instance._handleIconMouseLeave, instance);

						cached.on(MOUSELEAVE, instance._handleToolTipMouseLeave, instance);
					},

					_handleIconMouseLeave: function(event) {
						var instance = this;

						var cached = instance._cached;

						var icon = instance.get(HOST);

						var img = event.currentTarget;

						var toolTipTimeout = setTimeout(function() {
							cached.set(TRIGGER, icon).hide();
							cached.detach(MOUSEENTER);
						}, 25);

						cached.on(MOUSEENTER, function(event) {
							clearTimeout(toolTipTimeout);
						});
					},

					_handleToolTipMouseLeave: function(event) {
						var instance = this;

						var cached = instance._cached;

						var icon = instance.get(HOST);

						cached.set(TRIGGER, icon).hide();
						cached.detach(MOUSEENTER);
					}
				}
			}
		);

		Liferay.StickyToolTips = StickyToolTips;
	},
	'',
	{
		requires: ['aui-tooltip-delegate', 'plugin']
	}
);