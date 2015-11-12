AUI.add(
	'liferay-admin',
	function(A) {
		var Lang = A.Lang;

		var STR_CLICK = 'click';

		var Admin = A.Component.create(
			{
				ATTRS: {
					form: {
						setter: A.one,
						value: null
					},

					redirectUrl: {
						validator: Lang.isString,
						value: null
					},

					submitButton: {
						validator: Lang.isString,
						value: null
					},

					submitUrl: {
						validator: Lang.isString,
						value: null
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'admin',

				prototype: {
					initializer: function() {
						var instance = this;

						var eventHandles = [];

						eventHandles.push(
							instance.get('form').delegate(
								STR_CLICK,
								A.bind('_onSubmit', instance),
								instance.get('submitButton')
							)
						);

						instance._eventHandles = eventHandles;
					},

					destructor: function() {
						var instance = this;

						A.Array.invoke(instance._eventHandles, 'detach');
					},

					_addInputsFromData: function(data) {
						var instance = this;

						var form = instance.get('form');

						A.each(
							data,
							function(value, key) {
								form.append('<input id="' + instance.ns(key) + '" name="' + instance.ns(key) + '" type="hidden" value="' + value + '" />');
							}
						);
					},

					_installXuggler: function() {
						var instance = this;

						var form = instance.get('form');

						var loadingMask = new A.LoadingMask(
							{
								'strings.loading': Liferay.Language.get('xuggler-library-is-installing'),
								target: A.one('#adminXugglerPanel')
							}
						);

						loadingMask.show();

						A.io.request(
							instance.get('submitUrl'),
							{
								dataType: 'HTML',
								form: form.getDOM(),
								on: {
									complete: function() {
										var cmdNode = instance.one('#cmd');

										if (cmdNode) {
											cmdNode.remove();

											loadingMask.hide();
										}
									},
									success: function(event, id, obj) {
										var responseData = this.get('responseData');

										var newAdminXugglerPanel = A.Node.create(responseData).one('#adminXugglerPanel');

										var adminXugglerPanel = A.one('#adminXugglerPanel');

										if (adminXugglerPanel && newAdminXugglerPanel) {
											var newAdminXugglerPanelHTML = newAdminXugglerPanel.html();

											adminXugglerPanel.html(newAdminXugglerPanelHTML);
										}
									}
								}
							}
						);
					},

					_onSubmit: function(event) {
						var instance = this;

						var data = event.currentTarget.getData();

						var cmd = data.cmd;

						instance._addInputsFromData(data);

						if (!!cmd && cmd === 'installXuggler') {
							instance._installXuggler();
						}
						else {
							submitForm(
								instance.get('form'),
								instance.get('submitUrl')
							);
						}

					}
				}
			}
		);

		Liferay.Portlet.Admin = Admin;
	},
	'',
	{
		requires: ['aui-loading-mask-deprecated', 'liferay-portlet-base']
	}
);