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

					url: {
						value: null
					}
				},

				AUGMENTS: [Liferay.PortletBase],

				EXTENDS: A.Base,

				NAME: 'admin',

				prototype: {
					initializer: function(config) {
						var instance = this;

						instance.delegate(STR_CLICK, A.bind(instance._onSubmit, instance), instance.get('submitButtonSelector'));
					},

					_addInputsFromData: function(data) {
						var instance = this;

						var form = instance.get('form');

						for (var key in data) {
							if (data.hasOwnProperty(key)) {
								form.append('<input id="' + instance.ns(key) + '" name="' + instance.ns(key) + '" type="hidden" value="' + data[key] + '" />');
							}
						}
					},

					_installXuggler: function(event) {
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
							instance.get('url'),
							{
								after: {
									success: function(event, id, obj) {
										var responseData = this.get('responseData');

										var adminXugglerPanel = AUI.$(responseData).find('#adminXugglerPanel');

										var adminXugglerPanelHTML = adminXugglerPanel.html();

										AUI.$('#adminXugglerPanel').html(adminXugglerPanelHTML);
									},
									complete: function() {
										loadingMask.hide();
									}
								},

								dataType: 'HTML',

								form: form.getDOM()
							}
						);
					},

					_onSubmit: function(event) {
						var instance = this;

						var currentTarget = event.currentTarget;

						var form = instance.get('form');

						form.one('#' + instance.ns('redirect')).val(instance.get('redirectURL'));

						var data = currentTarget.getData();

						instance._addInputsFromData(data);

						var cmd = data['cmd'];

						if (!!cmd && cmd === 'installXuggler') {
							instance._installXuggler(event);
						}
						else {
							submitForm(form, instance.get('url'));
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