AUI.add(
	'liferay-portlet-alert',
	function(A) {
		var Lang = A.Lang;

		var STR_TARGET_CONTAINER = '<div class="portlet-alert"><div class="container-fluid-1280"></div></div>';

		var TPL_LEAD = '<strong class="lead">{type}</strong>';

		var PortletAlert = A.Component.create(
			{
				ATTRS: {
					animationTime: {
						validator: Lang.isNumber,
						value: 0.5
					},

					closeable: {
						validator: Lang.isBoolean,
						value: true
					},

					content: {
						validator: Lang.isString
					},

					cssClass: {
						validator: Lang.isString
					},

					destroyOnHide: {
						validator: Lang.isBoolean,
						value: false
					},

					targetContainer: {
						setter: A.one
					},

					timeout: {
						setter: function(value) {
							if (value) {
								return value * 1000;
							}

							return -1;
						}
					},

					type: {
						validator: Lang.isString,
						value: 'info'
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'portletalert',

				prototype: {
					initializer: function(config) {
						var instance = this;

						var content = instance._getContent();

						var cssClass = 'alert-' + instance.get('type') + ' ' + instance.get('cssClass');

						instance._setTargetContainer();

						instance._alert = new A.Alert(
							{
								animated: false,
								bodyContent: content,
								closeable: instance.get('closeable'),
								cssClass: cssClass,
								destroyOnHide: instance.get('destroyOnHide'),
								render: instance.get('targetContainer')
							}
						);

						instance._bindUI();

						instance._startClosingTimeout();
					},

					destructor: function() {
						var instance = this;

						instance._alert.destroy();

						(new A.EventHandle(instance._eventHandles)).detach();
					},

					close: function(event) {
						var instance = this;

						if (event) {
							event.preventDefault();
						}

						instance._stopClosingTimeout();

						instance._slideUp();
					},

					_bindUI: function() {
						var instance = this;

						instance._eventHandles = [
							instance._alert.on('render', instance._slideDown, instance),
							instance._alert.on('visibleChange', instance.close, instance),
							instance._alert.get('contentBox').on('mouseenter', instance._stopClosingTimeout, instance),
							instance._alert.get('contentBox').on('mouseleave', instance._startClosingTimeout, instance)
						];
					},

					_getContent: function() {
						var instance = this;

						var lead = Lang.sub(
								TPL_LEAD,
								{
									type: Liferay.Language.get(instance.get('type'))
								}
							);

						var content = lead + instance.get('content');

						return content;
					},

					_setTargetContainer: function() {
						var instance = this;

						var targetContainer = instance.get('targetContainer');

						if (!targetContainer) {
							var referenceNode;

							targetContainer = A.Node.create(STR_TARGET_CONTAINER);

							var navbar = instance.one('.navbar');

							if (navbar) {
								navbar.insert(targetContainer, 'after');
							}
							else {
								instance.one('.portlet-body').prepend(targetContainer);
							}

							targetContainer = targetContainer.one('.container-fluid-1280');

							instance.set('targetContainer', targetContainer);
						}
					},

					_slide: function(height) {
						var instance = this;

						instance.get('targetContainer').transition(
							{
								duration: instance.get('animationTime'),
								easing: 'ease-out',
								height: height
							}
						);
					},

					_slideDown: function() {
						var instance = this;

						instance._slide(instance._alert.get('contentBox').getComputedStyle('height'));
					},

					_slideUp: function() {
						var instance = this;

						instance._slide(0);
					},

					_startClosingTimeout: function() {
						var instance = this;

						var timeout = instance.get('timeout');

						if (timeout > -1) {
							instance._timer = A.later(
								timeout,
								instance,
								instance.close
							);
						}
					},

					_stopClosingTimeout: function() {
						var instance = this;

						if (instance._timer) {
							instance._timer.cancel();
						}
					}
				}
			}
		);

		Liferay.Portlet.Alert = PortletAlert;
	},
	'',
	{
		requires: ['aui-alert, liferay-portlet-base']
	}
);