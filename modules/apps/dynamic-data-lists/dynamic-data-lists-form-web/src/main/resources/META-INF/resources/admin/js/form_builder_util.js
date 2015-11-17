AUI.add(
	'liferay-ddl-form-builder-util',
	function(A) {
		var RendererUtil = Liferay.DDM.Renderer.Util;

		var FormBuilderUtil = {
			getFieldClass: function(type) {
				var fieldClass = RendererUtil.getFieldClass(type);

				return A.Component.create(
					{
						ATTRS: {
							enableEvaluations: {
								value: false
							}
						},

						AUGMENTS: [Liferay.DDL.FormBuilderSettingsSupport],

						EXTENDS: fieldClass,

						NAME: fieldClass.NAME
					}
				);
			},
			_onModalXYChange: function(event) {
				var xy = event.newVal;

				if (!A.UA.mobile) {
					xy[1] = Math.min(90, xy[1]);
				}
			}
		};

		Liferay.namespace('DDL').FormBuilderUtil = FormBuilderUtil;
	},
	'',
	{
		requires: ['liferay-ddl-form-builder-settings-support', 'liferay-ddm-form-renderer-util']
	}
);