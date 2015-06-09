window.Liferay = {
	AUI: {
		getAvailableLangPath: sinon.stub().returns('mock_available_languages.js'),
		getCombine: sinon.stub().returns(false),
		getComboPath: sinon.stub().returns('/'),
		getEditorsPath: sinon.stub().returns('../../../frontend-editors-web/src/META-INF/resources/html'),
		getFilter: sinon.stub().returns('raw'),
		getJavaScriptRootPath: sinon.stub().returns('/base'),
		getStaticResourceURLParams: sinon.stub().returns('')
	},

	Browser: {
		isIe: sinon.stub().returns(false)
	},

	ThemeDisplay: {
		getBCP47LanguageId: sinon.stub().returns('en_US'),
		getLanguageId: sinon.stub().returns('en_US'),
		getPathContext: sinon.stub().returns('/base'),
		getPathThemeImages: sinon.stub().returns('../../themes/classic/images'),
		isAddSessionIdToURL: sinon.stub().returns(false)
	}
};

window.themeDisplay = Liferay.ThemeDisplay;