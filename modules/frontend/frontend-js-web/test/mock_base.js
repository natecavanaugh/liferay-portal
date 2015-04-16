window.Liferay = {
	AUI: {
		getAvailableLangPath: function() {
			return '';
		},
		getCombine: function() {
			return false;
		},
		getComboPath: function() {
			return '/';
		},
		getFilter: function() {
			return 'raw';
		},
		getJavaScriptRootPath: function() {
			return '/base';
		},
		getStaticResourceURLParams: function() {
			return '';
		}
	},

	Browser: {
		isIe: function() {
			return false;
		}
	},

	ThemeDisplay: {
		getBCP47LanguageId: function() {
			return 'en_US';
		},
		getLanguageId: function() {
			return 'en_US';
		},
		getPathContext: function() {
			return '/base';
		},
		getPathThemeImages: function() {
			return '../../themes/classic/images';
		},
		isAddSessionIdToURL: function() {
			return false;
		}
	}
};

window.themeDisplay = Liferay.ThemeDisplay;