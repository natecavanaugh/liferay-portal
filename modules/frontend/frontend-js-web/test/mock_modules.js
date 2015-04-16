YUI_config.base = Liferay.AUI.getJavaScriptRootPath() + '/tmp/aui/';
YUI_config.root = Liferay.AUI.getJavaScriptRootPath() + '/tmp/aui/';

YUI_config.groups.editor.base = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/editor/';
YUI_config.groups.editor.root = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/editor/';

YUI_config.groups.liferay.base = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/liferay/';
YUI_config.groups.liferay.root = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/liferay/';

YUI_config.groups.misc.base = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/misc/';
YUI_config.groups.misc.root = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/misc/';

YUI_config.groups.portal.base = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/liferay/';
YUI_config.groups.portal.root = Liferay.AUI.getJavaScriptRootPath() + '/src/META-INF/resources/html/js/liferay/';

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
		},
		'portal-available-languages-mock': {
			condition: {
				name: 'portal-available-languages-mock',
				trigger: 'portal-available-languages',
				when: 'instead'
			},
			path: 'mock_available_languages.js',
			requires: [
				'liferay-language'
			]
		}
	},
	root: Liferay.AUI.getJavaScriptRootPath() + '/test/'
};