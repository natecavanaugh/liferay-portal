AUI.add(
	'portal-available-languages',
	function(A) {
		Liferay.Language.available = {};
		Liferay.Language.direction = {};
	},
	'',
	{
		requires: ['liferay-language']
	}
);