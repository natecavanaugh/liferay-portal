YUI_config.groups.mock = {
	base: Liferay.AUI.getJavaScriptRootPath() + '/liferay/',
	modules: {
		'liferay-language-mock': {
			condition: {
				name: 'liferay-language-mock',
				trigger: 'liferay-language',
				when: 'instead'
			},
			path: '../tests/mock_language.js'
		},
		'portal-available-languages-mock': {
			condition: {
				name: 'portal-available-languages-mock',
				trigger: 'portal-available-languages',
				when: 'instead'
			},
			path: '../tests/mock_available_languages.js',
			requires: [
				'liferay-language'
			]
		}
	},
	root: Liferay.AUI.getJavaScriptRootPath() + '/liferay/'
};

YUI_config.groups.utility = {
	base: Liferay.AUI.getJavaScriptRootPath() + '/liferay/',
	modules: {
		'liferay-utility-dependency': {
			condition: {
				name: 'liferay-utility-dependency',
				trigger: 'test'
			},
			path: 'dependency.js',
			requires: ['oop']
		},
		'liferay-utility-events': {
			condition: {
				name: 'liferay-utility-events',
				trigger: 'test'
			},
			path: 'events.js',
			requires: ['liferay-utility-dependency']
		},
		'liferay-utility-language': {
			condition: {
				name: 'liferay-utility-language',
				trigger: 'test'
			},
			path: 'language.js'
		},
		'liferay-utility-liferay': {
			condition: {
				name: 'liferay-utility-liferay',
				trigger: 'test'
			},
			path: 'liferay.js',
			requires: ['oop']
		},
		'liferay-utility-util': {
			condition: {
				name: 'liferay-utility-util',
				trigger: 'test'
			},
			path: 'util.js',
			requires: [
				'liferay-utility-events',
				'liferay-utility-liferay'
			]
		}
	},
	root: Liferay.AUI.getJavaScriptRootPath() + '/liferay/'
};