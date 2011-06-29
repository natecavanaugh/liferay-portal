AUI().add(
	'liferay-history-html5',
	function(A) {
		var AObject = A.Object;

		var DOC = A.config.doc;

		var NAME = 'liferayhistoryhtml5';

		var URI = new Liferay.URI(
			{
				strictMode: true
			}
		);

		var WIN = A.config.win;

		var History = A.Component.create(
			{
				EXTENDS: A.HistoryHTML5,

				NAME: NAME,

				prototype: {
					add: function(state, options) {
						var instance = this;

						options = options || {};

						if (!options.url) {
							var currentURI = URI.parse(DOC.location.href);

							var queryMap = currentURI.queryMap;

							A.mix(queryMap, state, true);

							options.url = URI.toString(currentURI);
						}

						var value = History.superclass.add.call(instance, state, options);

						return value;
					},

					_init: function (config) {
						var instance = this;

						var currentURI = URI.parse(DOC.location.href);

						if (!AObject.isEmpty(currentURI.hashMap)) {
							A.mix(currentURI.queryMap, currentURI.hashMap, true);

							var hashState = currentURI.hashMap;

							currentURI.hashMap = {};

							currentURI = URI.toString(currentURI);

							WIN.history.replaceState(null, null, currentURI);
						}

						config = config || {};

						if (!AObject.owns(config, 'initialState')) {
							if (WIN.history) {
								var bookmarkedState = WIN.history.state;
							}

							var initialState = A.merge(hashState, bookmarkedState);

							if (!AObject.isEmpty(initialState)) {
								config.initialState = initialState;
							}

							History.superclass._init.apply(instance, arguments);
						}
					}
				}
			}
		);

		Liferay.HistoryHTML5 = History;
	},
	'',
	{
		requires: ['history', 'liferay-uri']
	}
);