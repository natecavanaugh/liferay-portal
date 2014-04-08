AUI.add(
	'liferay-app-view-move-touch',
	function(A) {
		var Util = Liferay.Util;

		A.mix(
			Liferay.AppViewMove.prototype,
			{
				initializer: function(config) {
					var instance = this;

					instance._portletContainer = instance.byId(instance.get('portletContainerId'));

					instance._entriesContainer = instance.byId('entriesContainer');

					instance._eventEditEntry = instance.ns('editEntry');

					var eventHandles = [
						Liferay.on(instance._eventEditEntry, instance._editEntry, instance)
					];

					if (!Util.isTablet()) {
						eventHandles.push(Liferay.after(instance.ns('dataRetrieveSuccess'), instance._initDropTargets, instance));

						if (themeDisplay.isSignedIn() && this.get('updateable')) {
							instance._initDragDrop();
						}
					}

					instance._eventHandles = eventHandles;
				}
			},
			true
		);
	},
	'',
	{
		requires: ['liferay-app-view-move']
	}
);