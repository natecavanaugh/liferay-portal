AUI().add(
	'liferay-history',
	function(A) {
		var Do = A.Do;

		var HistoryBase = A.HistoryBase;

		var HistoryHash = A.HistoryHash;

		var Lang = A.Lang;

		var URI = new Liferay.URI(
			{
				strictMode: true
			}
		);

		if (HistoryBase.html5) {
			History = new Liferay.HistoryHTML5();
		}
		else {
			History = new HistoryHash();
		}

		Do.after(
			function(key){
				var currentRetVal = Do.currentRetVal;

				if (!Lang.isValue(currentRetVal)) {
					currentRetVal = Do.originalRetVal;
				}

				if (Lang.isValue(key) && !Lang.isValue(currentRetVal)) {
					var currentURI = URI.parse(A.config.doc.location.href);

					var queryMap = currentURI.queryMap;

					if (A.Object.owns(queryMap, key)) {
						currentRetVal = queryMap[key];
					}
				}

				return new Do.AlterReturn('', currentRetVal);
			},
			History,
			'get',
			History
		);

		History.SRC_ADD = HistoryBase.SRC_ADD,
		History.SRC_HASH = HistoryHash.SRC_HASH;
		History.SRC_POPSTATE = A.HistoryHTML5.SRC_POPSTATE;
		History.SRC_REPLACE = HistoryBase.SRC_REPLACE;

		History.html5 = HistoryBase.html5;

		Liferay.History = History;
	},
	'',
	{
		requires: ['liferay-history-html5', 'liferay-uri']
	}
);