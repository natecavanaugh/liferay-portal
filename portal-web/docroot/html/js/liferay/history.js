AUI().add(
	'liferay-history',
	function(A) {
		var History;

		var Do = A.Do;

		var HistoryBase = A.HistoryBase;

		var Lang = A.Lang;

		var QueryString = A.QueryString;

		var REGEX_ESCAPE_REGEX = /[-[\]{}()*+?.,\\^$|#\s]/g;

		var WIN = A.config.win;

		if (HistoryBase.html5) {
			var HistoryHTML5 = createHistoryHTML5Module();

			History = new HistoryHTML5();
		}
		else {
			History = new A.HistoryHash();
		}

		Do.after(
			function(key) {
				var currentRetVal = Do.currentRetVal;

				if (!Lang.isValue(currentRetVal)) {
					currentRetVal = Do.originalRetVal;
				}

				if (Lang.isValue(key) && !Lang.isValue(currentRetVal)) {
					var originalQuery = WIN.location.search;

					var queryMap = QueryString.parse(originalQuery.substr(1));

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

		function createHistoryHTML5Module() {
			var AObject = A.Object;

			var HistoryHTML5 = A.Component.create(
				{
					EXTENDS: A.HistoryHTML5,

					NAME: 'liferayhistoryhtml5',

					prototype: {
						add: function(state, options) {
							var instance = this;

							options = options || {};

							if (!options.url) {
								var currentURI = WIN.location.href;

								var originalQuery = WIN.location.search;

								var queryMap = QueryString.parse(originalQuery.substr(1));

								A.mix(queryMap, state, true);

								A.each(
									queryMap,
									function(value, key, collection) {
										if (!Lang.isValue(value)) {
											delete queryMap[key];
										}
									}
								);

								currentURI = updateURI(
									{
										match: originalQuery,
										newValue: '?' + QueryString.stringify(queryMap)
									},
									currentURI
								);

								options.url = currentURI;
							}

							var value = HistoryHTML5.superclass.add.call(instance, state, options);

							return value;
						},

						_init: function (config) {
							var instance = this;

							var originalHash = WIN.location.hash;

							if (originalHash) {
								var currentURI = WIN.location.href;

								var hashMap = QueryString.parse(originalHash.substr(1));

								if (!AObject.isEmpty(hashMap)) {
									var originalQuery = WIN.location.search;

									var queryMap = QueryString.parse(originalQuery.substr(1));

									A.mix(queryMap, hashMap, true);

									currentURI = updateURI(
										[
											{
												match: originalQuery,
												newValue: '?' + QueryString.stringify(queryMap)
											},
											{
												match: originalHash,
												newValue: ''
											}
										],
										currentURI
									);

									WIN.history.replaceState(null, null, currentURI);
								}
							}

							config = config || {};

							if (!AObject.owns(config, 'initialState')) {
								if (WIN.history) {
									var bookmarkedState = WIN.history.state;
								}

								var initialState = A.merge(hashMap, bookmarkedState);

								if (!AObject.isEmpty(initialState)) {
									config.initialState = initialState;
								}

								HistoryHTML5.superclass._init.apply(instance, arguments);
							}
						}
					}
				}
			);

			Liferay.HistoryHTML5 = HistoryHTML5;

			return HistoryHTML5;
		}

		function updateURI(maps, currentURI) {
			maps = A.Array(maps);

			A.each(
				maps,
				function(value, index, collection) {
					var escapedMatch = value.match.replace(REGEX_ESCAPE_REGEX, "\\$&");

					currentURI = currentURI.replace(new RegExp(escapedMatch, 'g'), value.newValue);
				}
			);

			return currentURI;
		}

		History.SRC_ADD = HistoryBase.SRC_ADD,
		History.SRC_HASH = A.HistoryHash.SRC_HASH;
		History.SRC_POPSTATE = A.HistoryHTML5.SRC_POPSTATE;
		History.SRC_REPLACE = HistoryBase.SRC_REPLACE;

		History.html5 = HistoryBase.html5;

		Liferay.History = History;
	},
	'',
	{
		requires: ['history', 'querystring-parse-simple', 'querystring-stringify-simple']
	}
);