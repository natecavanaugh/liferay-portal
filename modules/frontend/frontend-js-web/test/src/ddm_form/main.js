'use strict';

var chai = chai || {};

describe(
	'DDM Form Test Suite',
	function() {
		var assert = chai.assert;

		this.timeout(5000);

		before(
			function(done) {
				var instance = this;

				AUI().use(
					'aui-io-request',
					'liferay-ddm-form',
					function(A) {
						var getTestData = function(name) {
							var definition;

							var html;

							A.io.request(
								'/base/test/src/ddm_form/assets/' + name + '_definition.json',
								{
									dataType: 'json',
									on: {
										success: function() {
											definition = this.get('responseData');
										}
									},
									sync: true
								}
							);

							A.io.request(
								'/base/test/src/ddm_form/assets/' + name + '_definition.html',
								{
									on: {
										success: function() {
											html = this.get('responseData');
										}
									},
									sync: true
								}
							);

							return { definition: definition, html: html };
						};

						assert.ok(Liferay.DDM.Form);

						var data = getTestData('simple');

						document.body.innerHTML = data.html;

						instance.ddmForm = new Liferay.DDM.Form(
							{
								container: '#ddmContainer',
								ddmFormValuesInput: '#_167_ddmFormValues',
								definition: data.definition,
								doAsGroupId: 20180,
								fieldsNamespace: '',
								mode: 'null',
								p_l_id: 20173,
								portletNamespace: '_167_',
								repeatable: true
							}
						);

						assert.ok(instance.ddmForm);

						done();
					}
				);
			}
		);

		it(
			'should serialize a simple DDM Form with one unlocalizable text field',
			function(done) {
				var instance = this;

				var ddmForm = instance.ddmForm;

				var textField = ddmForm.get('fields')[0];

				assert.ok(textField);

				var textFieldInputNode = textField.getInputNode();

				assert.ok(textFieldInputNode);

				var value = 'simple text';

				textFieldInputNode.attr('value', value);

				var json = ddmForm.toJSON();

				assert.ok(json);

				assert.strictEqual(value, json.fieldValues[0].value);

				done();
			}
		);
	}
);