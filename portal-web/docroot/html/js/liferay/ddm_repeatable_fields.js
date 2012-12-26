AUI.add(
	'liferay-ddm-repeatable-fields',
	function(A) {
		var Lang = A.Lang;

		var SELECTOR_REPEAT_BUTTONS = '.lfr-ddm-repeatable-add-button, .lfr-ddm-repeatable-delete-button';

		var TPL_ADD_REPEATABLE = '<a class="lfr-ddm-repeatable-add-button" href="javascript:;"></a>';

		var TPL_DELETE_REPEATABLE = '<a class="lfr-ddm-repeatable-delete-button" href="javascript:;"></a>';

		var RepeatableFields = A.Component.create(
			{
				ATTRS: {
					classNameId: {
					},

					classPK: {
					},

					container: {
						setter: A.one,
						validator: A.Lang.isNode,
						value: null
					},

					namespace: {
					},

					fieldsTreeInput: {
						setter: A.one,
						validator: A.Lang.isNode,
						value: null
					}
				},

				EXTENDS: A.Base,

				NAME: 'liferay-ddm-repeatable-fields',

				prototype: {
					initializer: function() {
						var instance = this;

						instance.fieldsTree = [];

						instance.bindUI();
						instance.syncUI();
					},

					bindUI: function() {
						var instance = this;

						var container = instance.get('container');

						container.delegate('click', instance._onClickRepeatableButton, SELECTOR_REPEAT_BUTTONS, instance);

						var hoverHandler = instance._onHoverRepeatableButton;

						container.delegate('hover', hoverHandler, hoverHandler, SELECTOR_REPEAT_BUTTONS, instance);
					},

					syncUI: function() {
						var instance = this;

						instance.fieldsTree = [];

						instance.getFieldsList().each(
							function(item, index, collection) {
								instance.renderRepeatableToolbar(item);

								instance.fieldsTree = instance.fieldsTree.concat(
									instance.createFieldTree(item)
								);
							}
						);

						instance.get('fieldsTreeInput').val(
							instance.fieldsTree.join()
						);
					},

					createFieldTree: function(fieldNode) {
						var instance = this;

						var fieldName = fieldNode.getData('fieldName');
						var randomNamespace = fieldNode.getData('randomNamespace');

						var tree = [fieldName + randomNamespace];

						instance.getFieldsList(null, fieldNode).each(
							function(item, index, collection) {
								tree = tree.concat(
									instance.createFieldTree(item)
								);
							}
						);

						return tree;
					},

					getField: function(fieldName, callback) {
						var instance = this;

						A.io.request(
							themeDisplay.getPathMain() + '/dynamic_data_mapping/render_structure_field',
							{
								data: {
									classNameId: instance.get('classNameId'),
									classPK: instance.get('classPK'),
									fieldName: fieldName,
									namespace: instance.get('namespace'),
									p_p_isolated: true,
									readOnly: instance.get('readOnly')
								},
								on: {
									success: function(event, id, xhr) {
										if (callback) {
											callback.call(instance, xhr.responseText);
										}
									}
								}
							}
						);
					},

					getFieldsList: function(fieldName, parentNode) {
						var instance = this;

						var container = parentNode || instance.get('container');

						var query = ['>'];

						if (container.test('.aui-field-wrapper')) {
							query.push(' .aui-field-wrapper-content >');
						}

						query.push(' .aui-field-wrapper');

						if (fieldName) {
							query.push('[data-fieldName="' + fieldName + '"]');
						}

						return container.all(query.join(''));
					},

					getParentFieldNode: function(fieldNode) {
						var instance = this;

						var container = instance.get('container');

						return fieldNode.ancestor('.aui-field-wrapper') || container;
					},

					insertField: function(fieldNode) {
						var instance = this;

						var fieldName = fieldNode.getData('fieldName');

						instance.getField(
							fieldName,
							function(newFieldHTML) {
								fieldNode.insert(newFieldHTML, 'after');

								instance.syncUI();
							}
						);
					},

					renderRepeatableToolbar: function(fieldNode) {
						var instance = this;

						if (fieldNode.getData('repeatable') === 'true') {
							if (!fieldNode.getData('rendered-toolbar')) {
								var fieldName = fieldNode.getData('fieldName');

								var parentFieldNode = instance.getParentFieldNode(fieldNode);

								var fieldsList = instance.getFieldsList(fieldName, parentFieldNode);

								var html = TPL_ADD_REPEATABLE;

								if (fieldsList.indexOf(fieldNode) > 0) {
									html += TPL_DELETE_REPEATABLE;
								}

								fieldNode.append(html);

								fieldNode.plug(A.Plugin.ParseContent);

								fieldNode.setData('rendered-toolbar', true);
							}

							instance.getFieldsList(null, fieldNode).each(
								function(item, index, collection) {
									instance.renderRepeatableToolbar(item);
								}
							);
						}
					},

					_onClickRepeatableButton: function(event) {
						var instance = this;

						var currentTarget = event.currentTarget;

						var fieldNode = currentTarget.ancestor('.aui-field-wrapper');

						if (currentTarget.hasClass('lfr-ddm-repeatable-add-button')) {
							instance.insertField(fieldNode);
						}
						else if (currentTarget.hasClass('lfr-ddm-repeatable-delete-button')) {
							fieldNode.remove();
						}
					},

					_onHoverRepeatableButton: function(event) {
						var instance = this;

						var fieldNode = event.currentTarget.ancestor('.aui-field-wrapper');

						fieldNode.toggleClass('lfr-ddm-repeatable-active', (event.phase === 'over'));
					}

				}
			}
		);

		Liferay.namespace('DDM').RepeatableFields = RepeatableFields;
	},
	'',
	{
		requires: ['aui-base', 'aui-io-request', 'aui-parse-content']
	}
);