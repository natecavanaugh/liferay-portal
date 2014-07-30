AUI.add('liferay-portlet-dynamic-data-mapping-tests', function(A) {
	var suite = new A.Test.Suite('liferay-portlet-dynamic-data-mapping');

	var liferayFormBuilder;

	suite.add(new A.Test.Case({
		name: 'Dynamic Data Mapping tests',

		setUp: function () {
			liferayFormBuilder = new Liferay.FormBuilder();
		},

		tearDown: function () {
			liferayFormBuilder.destroy();
		},

		'Test DDM Definition Field Names': function() {
			var validateStructureFieldName = A.config.FormValidator.RULES.structureFieldName;

			A.Assert.isFalse(
				validateStructureFieldName('inva.lid,name'),
				'Do not allow punctuation.'
			);

			A.Assert.isFalse(
				validateStructureFieldName('-invalidname'),
				'Cannot start with hifen.'
			);

			A.Assert.isTrue(
				validateStructureFieldName('validname'),
				'Allow alphanumeric characters.'
			);
		}
	}));

	A.Test.Runner.add(suite);
}, '', {
	requires: ['test', 'liferay-portlet-dynamic-data-mapping-custom-fields']
});