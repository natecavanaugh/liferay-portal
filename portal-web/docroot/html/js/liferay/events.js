YUI.add(
	'liferay-events',
	function(A) {
		var CLICK_EVENTS = {};
		var DOC = A.config.doc;

		Liferay.provide(
			Liferay,
			'delegateClick',
			function(id, fn) {
				var el = DOC.getElementById(id);

				if (!el || el.id != id) {
					return;
				}

				var guid = A.one(el).addClass('lfr-delegate-click').guid();

				CLICK_EVENTS[guid] = fn;

				if (!Liferay._baseDelegateHandle) {
					Liferay._baseDelegateHandle = A.getBody().delegate('click', Liferay._baseDelegate, '.lfr-delegate-click');
				}
			},
			['aui-base']
		);

		Liferay._baseDelegate = function(event) {
			var id = event.currentTarget.attr('id');

			var fn = CLICK_EVENTS[id];

			if (fn) {
				fn.apply(this, arguments);
			}
		};

		Liferay._CLICK_EVENTS = CLICK_EVENTS;

		A.augment(Liferay, A.Attribute, true);
	},
	'',
	{
		requires: ['attribute', 'oop']
	}
);