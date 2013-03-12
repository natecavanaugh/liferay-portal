/*
 * jQuery Spellchecker - CKeditor Plugin - v0.2.4
 * https://github.com/badsyntax/jquery-spellchecker
 * Copyright (c) 2012 Richard Willis; Licensed MIT
 */
//CUSTOM START
var baseJscPluginPath = themeDisplay.getPathJavaScript() + '/editor/ckeditor/plugins/jqueryspellchecker';
var A = AUI();
//1. Tried loading these only if the plugin loads but ran into scoping issue on the SpellChecker object even though the files were loaded correctly
//CKEDITOR.scriptLoader.load(CKEDITOR.getUrl(baseJscPluginPath + "/js/jquery-1.8.2.min.js"), function (success) {
//  CKEDITOR.scriptLoader.load(CKEDITOR.getUrl(baseJscPluginPath + "/js/jquery.spellchecker.js"));
//});

//2. The stock jqueryspellchecker attaches its stylesheet to the document itself but that didn't seem to work in our initial testing.
CKEDITOR.document.appendStyleSheet(CKEDITOR.getUrl(baseJscPluginPath + "/css/jquery.spellchecker.css"));
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
      driver: 'test'
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
      // CUSTOM START -- $ is not for jQuery, it is for the element in the window object
      this.editorWindow = this.editor.document.getWindow().$;
      // CUSTOM END

    this.createSpellchecker();
    this.spellchecker.check();

      // CUSTOM START
    AUI().one(this.editorWindow)
    .on('scroll.spellchecker', A.bind(function scroll(){
      if (this.spellchecker.suggestBox) {
        this.spellchecker.suggestBox.close();
      }
    }, this));
      // CUSTOM END
  },

  destroy: function() {
    if (!this.spellchecker) 
      return;
    this.spellchecker.destroy();
    this.spellchecker = null;
    this.editor.setReadOnly(false);
    this.editor.commands.jqueryspellchecker.toggleState();
      // CUSTOM START - workaround since AUI doesn't have an off function
      // AUI().one(this.editorWindow).off('.spellchecker');
      // AUI().one(this.editorWindow).on('scroll.spellchecker', null);
      // CUSTOM END
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
        // CUSTOM START -- removing .append
        //return A.Node.create('<div />').append(text.text());
        return A.Node.create('<div />').append(text).attr('textContent');
        // CUSTOM END
    };

  //CUSTOM START - needed to get past scope problem when loaded w/i Liferay
    //t.spellchecker = new SpellChecker(t.editor.document.$.body, this.config);
    t.spellchecker = new window.AUI.SpellChecker(t.editor.document.$.body, this.config);
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