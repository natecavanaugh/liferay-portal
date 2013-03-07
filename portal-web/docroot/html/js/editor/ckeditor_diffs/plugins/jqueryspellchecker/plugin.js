﻿/*
 * jQuery Spellchecker - CKeditor Plugin - v0.2.4
 * https://github.com/badsyntax/jquery-spellchecker
 * Copyright (c) 2012 Richard Willis; Licensed MIT
 */
//CUSTOM START
var baseJscPluginPath = themeDisplay.getPathJavaScript() + '/editor/ckeditor/plugins/jqueryspellchecker';
//1. Tried loading these only if the plugin loads but ran into scoping issue on the SpellChecker object even though the files were loaded correctly
//CKEDITOR.scriptLoader.load(CKEDITOR.getUrl(baseJscPluginPath + "/js/jquery-1.8.2.min.js"), function (success) {
//	CKEDITOR.scriptLoader.load(CKEDITOR.getUrl(baseJscPluginPath + "/js/jquery.spellchecker.js"));
//});

//2. The stock jqueryspellchecker attaches its stylesheet to the document itself but that didn't seem to work in our initial testing.
//CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(baseJscPluginPath + "/css/jquery.spellchecker.css"));
CKEDITOR.config.contentsCss = [CKEDITOR.config.contentsCss,baseJscPluginPath + '/css/jquery.spellchecker.css']
//CUSTOM END

CKEDITOR.plugins.add('jqueryspellchecker', {

  config: {
    lang: 'en',
    parser: 'html',
    webservice: {
      path: '/webservices/php/SpellChecker.php',
	  //CUSTOM START - "test" will use static json data with a small set of words
      //driver: 'pspell'
//      driver: 'test'
      driver: 'liferay'
      //CUSTOM END
    },
    suggestBox: {
      position: 'below',
      appendTo: 'body'
    }
  },

  init: function( editor ) {

    var t = this;
    var pluginName = 'jqueryspellchecker';

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
//CUSTOM START
      icon: baseJscPluginPath + '/assets/spellchecker.png',
//CUSTOM END
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
    this.editorWindow = this.editor.document.getWindow().$;

    this.createSpellchecker();
    this.spellchecker.check();
    
    $(this.editorWindow)
    .on('scroll.spellchecker', $.proxy(function scroll(){
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
    $(this.editorWindow).off('.spellchecker');
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
      return $('<div />').append(t.editor.getData()).text();
    };

	//CUSTOM START - needed to get past scope problem when loaded w/i Liferay
    //t.spellchecker = new SpellChecker(t.editor.document.$.body, this.config);
    t.spellchecker = new window.$.SpellChecker(t.editor.document.$.body, this.config);
    //CUSTOM END

    t.spellchecker.on('check.success', function() {
      alert('There are no incorrectly spelled words.');
      t.destroy();
    });
    t.spellchecker.on('replace.word', function() {
      if (t.spellchecker.parser.incorrectWords.length === 0) {
        t.destroy();
      }
    });
  },

  positionSuggestBox: function() {

    var t = this;

    return function() {

      var ed = t.editor;
      var word = (this.wordElement.data('firstElement') || this.wordElement)[0];

      var p1 = $(ed.container.$).find('iframe').offset();
      var p2 = $(ed.container.$).offset();
      var p3 = $(word).offset();

      var left = p3.left + p2.left;
      var top = p3.top + p2.top + (p1.top - p2.top) + word.offsetHeight;

      top -= $(t.editorWindow).scrollTop();

      this.container.css({ 
        top: top, 
        left: left  
      });
    };
  }
});
