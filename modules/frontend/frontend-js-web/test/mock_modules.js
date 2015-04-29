var jsRootPath = Liferay.AUI.getJavaScriptRootPath();

YUI_config.base = jsRootPath + '/tmp/META-INF/resources/html/js/aui/';
YUI_config.root = jsRootPath + '/tmp/META-INF/resources/html/js/aui/';

var groupsPath = jsRootPath + '/src/META-INF/resources/html/js';

YUI_config.groups.editor.base = groupsPath + '/editor/';
YUI_config.groups.editor.root = groupsPath + '/editor/';

YUI_config.groups.liferay.base = groupsPath + '/liferay/';
YUI_config.groups.liferay.root = groupsPath + '/liferay/';

YUI_config.groups.misc.base = groupsPath + '/misc/';
YUI_config.groups.misc.root = groupsPath + '/misc/';

YUI_config.groups.portal.base = jsRootPath + '/test/';
YUI_config.groups.portal.root = jsRootPath + '/test/';

YUI_config.groups.mock = {
	base: Liferay.AUI.getJavaScriptRootPath() + '/test/',
	modules: {
		'liferay-language-mock': {
			condition: {
				name: 'liferay-language-mock',
				trigger: 'liferay-language',
				when: 'instead'
			},
			path: 'mock_language.js'
		}
	}
};