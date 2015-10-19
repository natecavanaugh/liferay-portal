AUI.add(
	'liferay-ddm-form-field-radio',
	function(A) {
		var Lang = A.Lang;

		var RadioField = A.Component.create(
			{
				ATTRS: {
					options: {
						validator: Array.isArray,
						value: []
					},

					type: {
						value: 'radio'
					}
				},

				EXTENDS: Liferay.DDM.Renderer.Field,

				NAME: 'liferay-ddm-form-field-radio',

				prototype: {
					getOptions: function() {
						var instance = this;

						var value = instance.get('value');

						if (instance.get('localizable')) {
							value = value[instance.get('locale')];
						}

						var predefinedValue = instance.get('predefinedValue');

						if (!value && predefinedValue) {
							if (Lang.isObject(predefinedValue)) {
								value = predefinedValue[instance.get('locale')];
							}
							else {
								value = predefinedValue;
							}
						}

						if (!Lang.isArray(value)) {
							try {
								value = JSON.parse(value);
							}
							catch (e) {
								value = [value];
							}
						}

						value = value[0];

						return A.map(
							instance.get('options'),
							function(item) {
								return {
									label: item.label[instance.get('locale')],
									status: value === item.value ? 'checked' : '',
									value: item.value
								};
							}
						);
					},

					getTemplateContext: function() {
						var instance = this;

						return A.merge(
							RadioField.superclass.getTemplateContext.apply(instance, arguments),
							{
								options: instance.getOptions()
							}
						);
					},

					setValue: function(value) {
						var instance = this;

						var container = instance.get('container');

						var radiosNodeList = container.all(instance.getInputSelector());

						radiosNodeList.attr('checked', false);

						var radiosToCheck = radiosNodeList.filter(
							function(node) {
								return node.val() === value;
							}
						);

						radiosToCheck.attr('checked', true);
					},

					_renderErrorMessage: function() {
						var instance = this;

						var container = instance.get('container');

						RadioField.superclass._renderErrorMessage.apply(instance, arguments);

						container.all('.validation-message').appendTo(container);
					}
				}
			}
		);

		Liferay.namespace('DDM.Field').Radio = RadioField;
	},
	'',
	{
		requires: ['liferay-ddm-form-renderer-field']
	}
);