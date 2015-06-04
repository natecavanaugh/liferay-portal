'use strict';

(function() {
	var getTestData = function(callback) {
		$.when(
			$.get('/base/test/src/ddm_form/assets/simple_definition.json'),
			$.get('/base/test/src/ddm_form/assets/simple_definition.html')
		).done(callback);
	};

	describe(
		'DDM Form Test Suite',
		function() {
			var assert = chai.assert;

			before(
				function(done) {
					var instance = this;

					AUI().use(
						'liferay-ddm-form',
						function(A) {
							assert.ok(Liferay.DDM.Form);

							getTestData(
								function(jsonResponse, htmlResponse) {
									document.body.innerHTML = htmlResponse[0];

									instance.ddmForm = new Liferay.DDM.Form(
										{
											container: '#ddmContainer',
											ddmFormValuesInput: '#_167_ddmFormValues',
											definition: jsonResponse[0],
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
})();