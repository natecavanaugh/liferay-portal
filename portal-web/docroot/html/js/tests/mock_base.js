Liferay = {
	AUI: {
		getAvailableLangPath: function() {},
		getCombine: function() {},
		getComboPath: function() {},
		getFilter: function() {
			return 'raw';
		},
		getJavaScriptRootPath: function() {}
	},

	Browser: {
		isIe: function() {}
	},

	ThemeDisplay: {
		getBCP47LanguageId: function() {},
		getLanguageId: function() {},
		getPathContext: function() {},
		getPathThemeImages: function() {}
	}
};

themeDisplay = Liferay.ThemeDisplay;