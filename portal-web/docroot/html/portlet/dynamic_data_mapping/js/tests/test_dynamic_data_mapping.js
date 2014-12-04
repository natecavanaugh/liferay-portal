AUI.add(
	'liferay-portlet-dynamic-data-mapping-tests',
	function(A) {
		var AArray = A.Array;

		var suite = new A.Test.Suite('liferay-portlet-dynamic-data-mapping');

		var getTestDefinition = function(name, callback) {
			A.io(
				name,
				{
					on: {
						complete: callback
					},
					type: 'GET'
				}
			);
		};

		var liferayFormBuilder;

		var assertLocalizationMapIsCorrect = function(originalField, deserializedField, localizableAttributes) {
			var localizationMap = deserializedField.localizationMap;

			A.Assert.isNotUndefined(
				localizationMap,
				'Every field should have a localizationMap.'
			);

			AArray.each(
				localizableAttributes,
				function(attribute) {
					if (attribute !== 'options') {
						A.each(
							originalField[attribute],
							function(originalValue, originalLanguageId) {
								A.Assert.isNotUndefined(
									localizationMap[originalLanguageId],
									'Translation to [' + originalLanguageId + '] should be present in localizationMap.'
								);

								A.Assert.areSame(
									localizationMap[originalLanguageId][attribute],
									originalValue,
									'Value in [' + originalLanguageId + '] for attribute [' + attribute + '] should be the same as it was before deserialization.'
								);
							}
						);
					}
					else {
						AArray.each(
							originalField[attribute],
							function(originalFieldOptionsItem, index) {
								var deserializedFieldOptions = deserializedField[attribute];

								assertLocalizationMapIsCorrect(
									originalFieldOptionsItem,
									deserializedFieldOptions[index],
									['label']
								);
							}
						)
					}
				}
			);

			var originalFieldNestedFields = originalField.nestedFields;

			var deserializedFieldNestedFields = deserializedField.fields;

			if (A.Lang.isArray(originalFieldNestedFields)) {
				AArray.each(
					originalFieldNestedFields,
					function(originalChildField, index) {
						assertLocalizationMapIsCorrect(
							originalChildField,
							deserializedFieldNestedFields[index],
							localizableAttributes
						);
					}
				);
			}
		};

		var assertRenamedNestedFields = function(originalField, deserializedField) {
			var originalFieldNestedFields = originalField.nestedFields;

			var deserializedFieldNestedFields = deserializedField.fields;

			if (originalField.nestedFields) {
				A.Assert.isUndefined(deserializedField.nestedFields, 'Deserialization should remove attribute [nestedFields] from fields.');

				A.Assert.isNotUndefined(deserializedField.fields, 'Deserialization should rename attribute [nestedFields] to [fields].');
			}
		};

		var assertUnlocalizableAttributesValuesAreSame = function(originalField, deserializedField) {
			AArray.each(
				Liferay.FormBuilder.UNLOCALIZABLE_FIELD_ATTRS,
				function(attribute) {
					A.Assert.areSame(
						originalField[attribute],
						deserializedField[attribute],
						'Unlocalizable attribute [' + attribute + '] should be the same before and after deserialization.'
					);
				}
			);
		};

		suite.add(
			new A.Test.Case(
				{
					name: 'Dynamic Data Mapping tests',

					setUp: function() {
						liferayFormBuilder = new Liferay.FormBuilder();
					},

					tearDown: function() {
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
					},

					'Test DDM Fields Deserialization': function() {
						var test = this;

						getTestDefinition(
							'ddm-definition.json',
							function(i, o) {
								var original = JSON.parse(o.responseText);
								var deserializable = JSON.parse(o.responseText);

								test.resume(
									function() {
										var deserializedFields = liferayFormBuilder.deserializeDefinitionFields(deserializable);

										AArray.each(
											deserializedFields,
											function(deserializedField, index) {
												var originalField = original.fields[index];

												assertRenamedNestedFields(originalField, deserializedField);

												assertUnlocalizableAttributesValuesAreSame(originalField, deserializedField);

												assertLocalizationMapIsCorrect(
													originalField,
													deserializedField,
													Liferay.FormBuilder.LOCALIZABLE_FIELD_ATTRS
												);
											}
										);
									}
								);
							}
						);

						test.wait();
					}
				}
			)
		);

		A.Test.Runner.add(suite);
	},
	'',
	{
		requires: ['io-base', 'test', 'liferay-portlet-dynamic-data-mapping-custom-fields']
	}
);