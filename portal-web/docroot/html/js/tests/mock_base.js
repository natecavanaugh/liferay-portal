Liferay = {
	AUI: jasmine.createSpyObj(
		'Liferay.AUI',
		[
			'getAvailableLangPath',
			'getCombine',
			'getComboPath',
			'getFilter',
			'getJavaScriptRootPath'
		]
	),

	Browser: jasmine.createSpyObj(
		'Liferay.Browser',
		[
			'isIe'
		]
	),

	ThemeDisplay: jasmine.createSpyObj(
		'themeDisplay',
		[
			'getBCP47LanguageId',
			'getLanguageId',
			'getPathContext',
			'getPathThemeImages'
		]
	)
};

Liferay.AUI.getFilter.and.returnValue('raw');

themeDisplay = Liferay.ThemeDisplay;