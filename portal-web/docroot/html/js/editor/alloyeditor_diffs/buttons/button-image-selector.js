AUI.add(
	'button-imageselector',
	function(A) {
		var Lang = A.Lang;

		var BtnImageselector = A.Base.create
		(
			'imageselector',
			A.Plugin.Base,
			[A.ButtonBase],
			{
				initializer: function() {
					var instance = this;

					instance._imageTPL = new CKEDITOR.template(instance.TPL_IMAGE);
					instance._onDocumentSelectedFn = A.bind('_onDocumentSelected', instance);
				},

				_onClick: function(event) {
					var instance = this;

					var editor = instance.get('host').get('editor');

					Liferay.Util.selectEntity(
						{
							dialog: {
								constrain: true,
								destroyOnHide: true,
								modal: true
							},
							eventName: editor.name + 'selectDocument',
							id: editor.name + 'selectDocument',
							title: Liferay.Language.get('select-image'),
							uri: editor.config.filebrowserImageBrowseUrl
						},
						instance._onDocumentSelectedFn
					);
				},

				_onDocumentSelected: function(event) {
					var instance = this;

					var editor = instance.get('host').get('editor');

					var image = CKEDITOR.dom.element.createFromHtml(
						instance._imageTPL.output(
							{
								src: event.url
							}
						)
					);

					A.soon(A.bind('insertElement', editor, image));
				},

				TPL_CONTENT: '<i class="alloy-editor-icon-image-sign"></i>',

				TPL_IMAGE: '<img src="{src}" />'
			},
			{
				NAME: 'imageselector',

				NS: 'imageselector'
			}
		);

		A.ButtonImageselector = BtnImageselector;
	},
	'',
	{
		requires: ['button-base', 'timers']
	}
);