(function() {
	var A = AUI();

	var baseJscPluginPath = themeDisplay.getPathJavaScript() +
		'/editor/ckeditor/plugins/jqueryspellchecker';

	var jscCssPath = baseJscPluginPath + '/css/jquery.spellchecker.css';

	CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(jscCssPath));

	CKEDITOR.config.contentsCss = [CKEDITOR.config.contentsCss, jscCssPath];

	CKEDITOR.plugins.add(
		'jqueryspellchecker',
		{
			config: {
				lang: 'en',
				parser: 'html',
				suggestBox: {
					position: 'below',
					appendTo: 'body'
				},
				webservice: {
					driver: 'liferay',
					path: '/webservices/php/SpellChecker.php'
				}
			},

			create: function() {
				this.editor.setReadOnly(true);
				this.editor.commands.jqueryspellchecker.toggleState();
				this.editorWindow = this.editor.document.getWindow().$;

				this.createSpellchecker();
				this.spellchecker.check();

				A.one(this.editorWindow).on(
					'scroll.spellchecker',
					A.bind(
						function scroll() {
							if (this.spellchecker.suggestBox) {
								this.spellchecker.suggestBox.close();
							}
						},
						this
					)
				);
			},

			createSpellchecker: function() {
				var instance = this;

				instance.config.getText = function() {
					var node = A.Node.create('<div />');
					var text = instance.editor.getData();

					return node.append(text).attr('textContent');
				};

				instance.spellchecker = new A.SpellChecker(
					instance.editor.document.$.body,
					this.config
				);

				instance.spellchecker.on(
					'check.success',
					function() {
						alert('There are no incorrectly spelled words.');
						instance.destroy();
					}
				);
			},

			destroy: function() {
				if (!this.spellchecker) {
					return;
				}

				this.spellchecker.destroy();
				this.spellchecker = null;
				this.editor.setReadOnly(false);
				this.editor.commands.jqueryspellchecker.toggleState();
			},

			init: function( editor ) {
				var instance = this;
				var pluginName = 'jqueryspellchecker';

				var path = instance.path;
				var dependency = [CKEDITOR.getUrl(path + 'js/jquery.spellchecker.js')];

				CKEDITOR.scriptLoader.load(dependency);

				this.config.suggestBox.position = this.positionSuggestBox();

				editor.addCommand(
					pluginName,
					{
						canUndo: false,
						readOnly: 1,
						exec: function() {
							instance.toggle(editor);
						}
					}
				);

				editor.ui.addButton(
					'jQuerySpellChecker',
					{
						label: 'SpellCheck',
						icon: baseJscPluginPath + '/assets/spellchecker.png',
						command: pluginName,
						toolbar: 'spellchecker,10'
					}
				);

				editor.on('saveSnapshot', function() {
					instance.destroy();
				});
			},

			positionSuggestBox: function() {
				var instance = this;

				return function() {

					var ed = instance.editor;
					var word = this.wordElement;

					var container = AUI().one(ed.container.$);

					var p1 = container.one('iframe').getXY();
					var p2 = container.getXY();
					var p3 = AUI().one(word).getXY();

					var left = p3[0] + p2[0];
					var top = p3[1] + p2[1] + (p1[1] - p2[1]) + word.innerHeight();

					this.container.setStyle('top', top);
					this.container.setStyle('left', left);
				};
			},

			toggle: function(editor) {
				this.editor = editor;

				if (!this.spellchecker) {
					this.create();
				}
				else {
					this.destroy();
				}
			}
		}
	);
})();