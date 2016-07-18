AUI.add(
	'liferay-portlet-topper',
	function(A) {
		var body = A.getBody();

		var NAME = 'liferayportlettopper';

		var PluginBase = A.Plugin.Base;

		var PortletTopper = A.Component.create(
			{
				EXTENDS: PluginBase,

				NAME: NAME,

				NS: NAME,

				prototype: {
					initializer: function(config) {
						var instance = this;

						PluginBase.prototype._handles = [
							A.on('hidePortletTopper', A.bind('_hidePortletTopper', instance)),
							A.on('showPortletTopper', A.bind('_showPortletTopper', instance)),
							body.delegate('mouseover', A.bind('_onPortletMouseover', instance), '.portlet'),
							body.delegate('mouseleave', A.bind('_onPortletMouseleave', instance), '.portlet'),
							body.delegate('mouseenter', A.bind('_cancelPortletTopperTimer', instance), '.lfr-icon-menu-open'),
							body.delegate('mouseenter', A.bind('_cancelPortletTopperTimer', instance), '.tooltip')
						];
					},

					_cancelPortletTopperTimer: function(event) {
						var instance = this;

						instance._hidePortletTopperTimer.cancel();
					},

					_getIconMenuState: function(event) {
						var state;

						var iconMenus = event.currentTarget.all('.portlet-topper .lfr-icon-menu');

						iconMenus.each(function(node) {
							if (node.hasClass('open')) {
								state = 'open';
							}
						});

						return state || 'closed';
					},

					_hidePortletTopper: function(event) {
						event.currentTarget.removeClass('show-portlet-topper');
					},

					_onPortletClickOutside: function(node) {
						node.on('clickoutside', function(event) {
							A.fire('hidePortletTopper', {
								currentTarget: event.currentTarget,
								target: event.target
							});
						});
					},

					_onPortletMouseleave: function(event) {
						var instance = this;

						var iconMenuState = instance._getIconMenuState(event);

						instance._hidePortletTopperTimer = A.later(0, instance, function() {
							if (iconMenuState === 'closed') {
								A.fire('hidePortletTopper', {
									currentTarget: event.currentTarget,
									target: event.target
								});
							}
						});
					},

					_onPortletMouseover: function(event) {
						var instance = this;

						instance._onPortletClickOutside(event.currentTarget);

						A.fire('showPortletTopper', {
							currentTarget: event.currentTarget,
							target: event.target
						});
					},

					_showPortletTopper: function(event) {
						event.currentTarget.addClass('show-portlet-topper');
					}
				}
			}
		);

		Liferay.PortletTopper = PortletTopper;
	},
	'',
	{
		requires: ['aui-component', 'plugin']
	}
);