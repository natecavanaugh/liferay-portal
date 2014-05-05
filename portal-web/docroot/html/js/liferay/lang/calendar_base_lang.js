YUI.add(
	'lang/calendar-base-lang',
	function(A) {
		var availableLanguages = A.Intl.getAvailableLangs('calendar-base');

		if (A.Array.lastIndexOf(availableLanguages, themeDisplay.getBCP47LanguageId()) === -1) {
			A.Intl.add(
				'calendar-base', themeDisplay.getBCP47LanguageId(),
				{
					very_short_weekdays: A.Intl.get('datatype-date-format').a
				}
			);
		}
	},
	'',
	{
		requires: ['datatype-date-format']
	}
);