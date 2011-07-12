AUI().add(
	'liferay-history-manager',
	function(A) {
		var HistoryBase = A.HistoryBase;

		var HistoryManager = new Liferay.History();

		HistoryManager.SRC_ADD = HistoryBase.SRC_ADD;
		HistoryManager.SRC_REPLACE = HistoryBase.SRC_REPLACE;

		HistoryManager.SRC_HASH = A.HistoryHash ? A.HistoryHash.SRC_HASH : 'hash';
		HistoryManager.SRC_POPSTATE = A.HistoryHTML5 ? A.HistoryHTML5.SRC_POPSTATE : 'popstate';

		HistoryManager.HTML5 = HistoryBase.html5;

		Liferay.HistoryManager = HistoryManager;
	},
	'',
	{
		requires: ['liferay-history']
	}
);