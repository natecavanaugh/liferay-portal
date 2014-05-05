AUI.add(
	'liferay-ajax-session',
	function(A) {
		var Lang = A.Lang;

		A.on(
			'io:complete',
			function(transactionId, response, args) {
				var session = Liferay.Session;

				if (session && (!args || (args && (args.sessionExtend || !Lang.isBoolean(args.sessionExtend))))) {
					session.resetInterval();
				}
			}
		);
	},
	'',
	{
		requires: ['io-base', 'liferay-session']
	}
);