;(function() {
	var ALLOY = YUI().use(
		'liferay-dependency',
		'liferay-events',
		'liferay-portlet',
		'liferay-util'
	);

	if (ALLOY.html5shiv) {
		ALLOY.html5shiv();
	}

	window.AUI = function() {
		return ALLOY;
	};

	ALLOY.mix(AUI, YUI);
})();