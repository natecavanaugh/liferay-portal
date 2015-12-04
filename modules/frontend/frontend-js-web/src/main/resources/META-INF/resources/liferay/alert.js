AUI.add(
	'liferay-alert',
	function(A) {
		var getClassName = A.getClassName;
		var Lang = A.Lang;

		var STR_ALERT_NODE = '<div class="portlet-alert"><div class="container-fluid-1280 portlet-alert-container"></div></div>';

		var TPL_CONTENT = '<strong class="lead">{title}</strong>{message}';

		var Alert = A.Component.create(
			{
				ATTRS: {
					animated: {
						validator: Lang.isBoolean,
						value: true
					},

					message: {
						validator: Lang.isString,
						value: ''
					},

					title: {
						validator: Lang.isString
					},

					type: {
						validator: Lang.isString,
						value: 'info'
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Alert,

				NAME: 'liferayalert',

				prototype: {
					bindUI: function() {
						var instance = this;

						var boundingBox = instance.get('boundingBox');

						instance._eventHandles = [
							instance.after('messageChange', instance._setBodyContent, instance),
							instance.after('titleChange', instance._setBodyContent, instance),
							instance.after('typeChange', instance._afterTypeChange, instance),
							boundingBox.on('mouseenter', instance._cancelHide, instance),
							boundingBox.on('mouseleave', instance.hide, instance)
						];

						Alert.superclass.bindUI.call(this);
					},

					render: function(parentNode) {
						var instance = this;

						instance._setBodyContent();
						instance._setCssClass();

						return Alert.superclass.render.call(this, parentNode || this._getParentNode());
					},

					_afterTypeChange: function(event) {
						var instance = this;

						instance._setCssClass();
					},

					_cancelHide: function() {
						var instance = this;

						instance._clearHideTimer();
						instance._set('visible', true);
					},

					_getParentNode: function() {
						var instance = this;

						var parentNode = instance._parentNode || instance.one('.portlet-alert-container');

						if (!parentNode) {
							var rootNode = instance.get('rootNode');

							var alertNode = A.Node.create(STR_ALERT_NODE);

							var navbar = rootNode.one('.navbar');

							if (navbar) {
								navbar.insert(alertNode, 'after');
							}
							else {
								rootNode.one('.portlet-body').prepend(alertNode);
							}

							parentNode = alertNode.one('div');

							instance._parentNode = parentNode;
						}

						return parentNode;
					},

					_maybeHide: function() {
						var instance = this;

						if (instance._ignoreHideDelay) {
							instance._prepareTransition(false);
							instance._transition(false);
						}
						else {
							Alert.superclass._maybeHide.call(this);
						}
					},

					_onClickBoundingBox: function(event) {
						if (event.target.test('.' + getClassName('close'))) {
							this._ignoreHideDelay = true;
							this.hide();
						}
					},

					_prepareTransition: function(visible) {
						var instance = this;

						var parentNode = instance._getParentNode();

						instance._clearHideTimer();

						if (visible && !parentNode.test('.in')) {
							instance._uiSetVisibleHost(true);
							parentNode.setStyle('height', 0);
						}
					},

					_setBodyContent: function() {
						var instance = this;

						var bodyContent = Lang.sub(
							TPL_CONTENT,
							{
								message: instance.get('message'),
								title: instance.get('title') || ''
							}
						);

						instance.set('bodyContent', bodyContent);
					},

					_setCssClass: function() {
						var instance = this;

						instance.set('cssClass', getClassName('alert', instance.get('type')));
					},

					_transition: function(visible) {
						var instance = this;

						var parentNode = instance._getParentNode();

						if (!visible || !parentNode.test('.in')) {
							parentNode.transition(
								{
									duration: instance.get('duration') / 1000,
									easing: 'ease-out',
									height: visible ? instance.get('boundingBox').getComputedStyle('height') : 0
								},
								function() {
									parentNode.toggleClass('in', visible);

									instance._uiSetVisibleHost(visible);

									var delay = instance.get('delay');

									if (visible && delay.hide) {
										instance.hide();
									}
									else if (instance.get('destroyOnHide')) {
										A.soon(A.bind('destroy', instance));
									}
								}
							);
						}
					}
				}
			}
		);

		Liferay.Alert = Alert;
	},
	'',
	{
		requires: ['aui-alert', 'event-mouseenter', 'liferay-portlet-base', 'timers']
	}
);