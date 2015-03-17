YUI_config.groups.mock = {
	base: Liferay.AUI.getJavaScriptRootPath() + '/liferay/',
	modules: {
		'liferay-language-mock': {
			condition: {
				name: 'liferay-language-mock',
				trigger: 'liferay-language',
				when: 'instead'
			},
			path: '../../../../test/js/mock_language.js'
		},
		'portal-available-languages-mock': {
			condition: {
				name: 'portal-available-languages-mock',
				trigger: 'portal-available-languages',
				when: 'instead'
			},
			path: '../../../../test/js/mock_available_languages.js',
			requires: [
				'liferay-language'
			]
		}
	},
	root: Liferay.AUI.getJavaScriptRootPath() + '/liferay/'
};