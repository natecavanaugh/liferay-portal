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

						instance._errorCount = 0;

						instance._eventHandles = [];

						var submitButton = instance.one(instance.get('submitButtonSelector'));

						if (submitButton) {
							instance._eventHandles.push(
								submitButton.on(STR_CLICK, instance._submitForm, instance)
							);
						}

						instance._bindXuggler();
					},

					destructor: function() {
						var instance = this;

						A.Array.invoke(instance._eventHandles, 'detach');
					},

					_addInputsFromData: function(node) {
						var instance = this;

						var form = instance.get('form');

						var data = node.getData();

						for (var key in data) {
							if (data.hasOwnProperty(key)) {
								form.append('<input id="' + instance.ns(key) + '" name="' + instance.ns(key) + '" type="hidden" value="' + data[key] + '" />');
							}
						}
					},

					_bindXuggler: function() {
						var instance = this;

						var installXugglerButton = instance.one('#installXugglerButton');

						if (installXugglerButton) {
							instance._eventHandles.push(
								installXugglerButton.on(STR_CLICK, instance._installXuggler, instance)
							);

							instance._installXugglerButton = installXugglerButton;
						}
					},

					_installXuggler: function(event) {
						var instance = this;

						var form = instance.get('form');

						var currentTarget = event.currentTarget;

						form.one('#' + instance.ns('redirect')).val(instance.get('redirectURL'));

						instance._addInputsFromData(currentTarget);

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

										instance._bindXuggler();
									}
								},

								dataType: 'HTML',

								form: form.getDOM()
							}
						);
					},

					_submitForm: function(event) {
						var instance = this;

						var currentTarget = event.currentTarget;

						var form = instance.get('form');

						form.one('#' + instance.ns('redirect')).val(instance.get('redirectURL'));

						instance._addInputsFromData(currentTarget);

						submitForm(form, instance.get('url'));
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