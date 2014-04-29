YUI.add(
	'lang/calendar-base-lang',
	function(Y) {
		var availableLanguages = Y.Intl.getAvailableLangs('calendar-base');

		if (Y.Array.lastIndexOf(availableLanguages, themeDisplay.getBCP47LanguageId()) === -1) {
			Y.Intl.add(
				'calendar-base', themeDisplay.getBCP47LanguageId(),
				{
					very_short_weekdays: Y.Intl.get('datatype-date-format').a
				}
			);
		}
	},
	'',
	{
		requires: ['datatype-date-format']
	}
);