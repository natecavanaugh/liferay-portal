/*
 * jQuery Spellchecker - CKeditor Plugin - v0.2.4
 * https://github.com/badsyntax/jquery-spellchecker
 * Copyright (c) 2012 Richard Willis; Licensed MIT
 *
 * Note: This plugin was forked from jQuery SpellChecker v0.2.4 and converted
 * to AlloyUI for use in Liferay. The name was left in place to make its
 * origins more clear.
 */
var baseJscPluginPath = themeDisplay.getPathJavaScript() +
    '/editor/ckeditor/plugins/jqueryspellchecker';
var A = AUI();
var jscCssPath = baseJscPluginPath + '/css/jquery.spellchecker.css';
CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(jscCssPath));
CKEDITOR.config.contentsCss = [CKEDITOR.config.contentsCss, jscCssPath];

CKEDITOR.plugins.add('jqueryspellchecker', {

  config: {
    lang: 'en',
    parser: 'html',
    webservice: {
      path: '/webservices/php/SpellChecker.php',
      // pspell is the stock php spellchecker
      // test is an inline json test driver for testing purposes.
      // liferay is the spell checker backed by Liferay's set of dictionaries
      // driver: 'pspell'
      // driver: 'test'
      driver: 'liferay'
    },
    suggestBox: {
      position: 'below',
      appendTo: 'body'
    }
  },

  init: function( editor ) {

    var t = this;
    var pluginName = 'jqueryspellchecker';

    var path = t.path;
    var dependency = [CKEDITOR.getUrl(path + 'js/jquery.spellchecker.js')];

    CKEDITOR.scriptLoader.load(dependency);

    this.config.suggestBox.position = this.positionSuggestBox();
    
    editor.addCommand(pluginName, {
      canUndo: false,
      readOnly: 1,
      exec: function() {
        t.toggle(editor);
      }
    });

    editor.ui.addButton('jQuerySpellChecker', {
      label: 'SpellCheck',
      icon: baseJscPluginPath + '/assets/spellchecker.png',
      command: pluginName,
      toolbar: 'spellchecker,10'
    });

    editor.on('saveSnapshot', function() {
      t.destroy();
    });
  },

  create: function() {

    this.editor.setReadOnly(true);
    this.editor.commands.jqueryspellchecker.toggleState();
      // Note: $ is not for jQuery, it is for the element in the window object
      this.editorWindow = this.editor.document.getWindow().$;

    this.createSpellchecker();
    this.spellchecker.check();

    AUI().one(this.editorWindow)
    .on('scroll.spellchecker', A.bind(function scroll(){
      if (this.spellchecker.suggestBox) {
        this.spellchecker.suggestBox.close();
      }
    }, this));
  },

  destroy: function() {
    if (!this.spellchecker) 
      return;
    this.spellchecker.destroy();
    this.spellchecker = null;
    this.editor.setReadOnly(false);
    this.editor.commands.jqueryspellchecker.toggleState();
  },

  toggle: function(editor) {
    this.editor = editor;
    if (!this.spellchecker) {
      this.create();
    } else {
      this.destroy();
    }
  },

  createSpellchecker: function() {
    var t = this;

    t.config.getText = function() {
        var text = t.editor.getData();
        return A.Node.create('<div />').append(text).attr('textContent');
    };

    t.spellchecker = new window.AUI.SpellChecker(t.editor.document.$.body,
        this.config);

    t.spellchecker.on('check.success', function() {
      alert('There are no incorrectly spelled words.');
      t.destroy();
    });
  },

  positionSuggestBox: function() {

    var t = this;

    return function() {

      var ed = t.editor;
      // var word = (this.wordElement.data('firstElement') || this.wordElement)[0];
      var word = this.wordElement;

      var container = AUI().one(ed.container.$);

      var p1 = container.one('iframe').getXY();
      var p2 = container.getXY();
      var p3 = AUI().one(word).getXY();

      // var left = p3.left + p2.left;
      var left = p3[0] + p2[0];
      // var top = p3.top + p2.top + (p1.top - p2.top) + word.offsetHeight;
      var top = p3[1] + p2[1] + (p1[1] - p2[1]) + word.innerHeight();

      // top -= AUI().one(t.editorWindow).scrollTop();

      /*this.container.css({
        top: top, 
        left: left  
      });*/
      this.container.setStyle('top', top);
      this.container.setStyle('left', left);
    };
  }
});