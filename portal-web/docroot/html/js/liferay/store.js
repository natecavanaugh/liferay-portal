AUI.add(
	'liferay-store',
	function(A) {
		var Lang = A.Lang;

		var isObject = Lang.isObject;

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

					if (responseData.substring(0, 12) === "serialize://"){
						var responseDataLength = responseData.length - 1;

						responseDataSubString = responseData.substring(12, responseDataLength);

						responseData = A.JSON.parse(responseDataSubString);
					}

					callback(responseData);
				};
			}
			else {
				if (isObject(key)) {
					config.data = key;
				}
				else {
					config.data[key] = value;
				}

				if (isObject(value)) {
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