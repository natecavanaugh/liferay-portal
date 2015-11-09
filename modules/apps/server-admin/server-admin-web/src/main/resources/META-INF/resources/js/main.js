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

					submitButtonSelector: {
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

						A.one(instance.get('form')).delegate(STR_CLICK, A.bind(instance._onSubmit, instance), instance.get('submitButtonSelector'));
					},

					_addInputsFromData: function(data) {
						var instance = this;

						var form = instance.get('form');

						A.each(
							data,
							function(value, key, obj) {
								form.append('<input id="' + instance.ns(key) + '" name="' + instance.ns(key) + '" type="hidden" value="' + value + '" />');
							}
						);
					},

					_installXuggler: function() {
						var instance = this;

						var form = instance.get('form');

						var loadingMask = new A.LoadingMask(
							{
								'strings.loading': Liferay.Language.get('xuggler-library-is-installed'),
								target: A.one('#adminXugglerPanel')
							}
						);

						loadingMask.show();

						A.io.request(
							instance.get('submitUrl'),
							{
								on: {
									complete: function() {
										A.one('#' + instance.ns('cmd')).remove();

										loadingMask.hide();
									},
									success: function(event, id, obj) {
										var responseData = this.get('responseData');

										var newAdminXugglerPanelHTML = A.Node.create(responseData).one('#adminXugglerPanel').html();

										A.one('#adminXugglerPanel').html(newAdminXugglerPanelHTML);
									}
								},

								dataType: 'HTML',

								form: form.getDOM()
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
							submitForm(instance.get('form'), instance.get('submitUrl'));
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