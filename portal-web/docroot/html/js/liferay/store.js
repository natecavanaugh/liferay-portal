AUI.add(
	'liferay-store',
	function(A) {
		var Lang = A.Lang;

		var Store = function(key, value) {
			var config = {
				after: {},
				data: {}
			};

			if (Lang.isFunction(value)) {
				config.data.cmd = 'get';
				config.data.key = key;

				var callback = value;

				config.after.success = function(event) {
					var responseData = this.get('responseData');

					
					if (responseData.substring(0, 12) == "serialize://"){

						responseData = A.JSON.parse(responseData.substring(12, responseData.length -1));
					}

					callback(responseData);
				};
			}
			else {
				if (Lang.isObject(key)) {
					config.data = key;
				}
				else {
					config.data[key] = value;
				}

				if (Lang.isObject(value)){
					config.data[key] = 'serialize://' + A.JSON.stringify(value);
				}
			}

			A.io.request(
				themeDisplay.getPathMain() + '/portal/session_click',
				config
			);
		};

		Liferay.Store = Store;
	},
	'',
	{
		requires: ['aui-io-request']
	}
);