(function() {
	var pluginName = 'accessibilityhelp';

	CKEDITOR.plugins.add(
		pluginName,
		{
			init: function(editor) {
				if (editor.ui.addButton) {
					editor.ui.addButton(
						'A11YBtn',
						{
							command: 'a11yHelp',
							icon: themeDisplay.getPathJavaScript() + '/editor/ckeditor/plugins/accessibilityhelp/assets/accessibilityhelp.png',
							label: Liferay.Language.get('action.HELP')
						}
					);
				}

				editor.on(
					'uiSpace',
					function(event) {
						var toolbarHTML = event.data.html;

						var a11ybtnIndex = toolbarHTML.indexOf('cke_button__a11ybtn');

						if (a11ybtnIndex !== -1) {
							var a11ToolbarIndex = toolbarHTML.lastIndexOf('class="cke_toolbar"', a11ybtnIndex);

							var toolbarText = toolbarHTML.substr(a11ToolbarIndex).replace('class="cke_toolbar"', 'class="cke_toolbar cke_toolbar__accessibilityhelp"');

							event.data.html = toolbarHTML.substr(0, a11ToolbarIndex) + toolbarText;
						}
					}
				);
			}
		}
	);
})();