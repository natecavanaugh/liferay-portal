;(function() {
	var ENTITIES_MAP = {
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		'"': '&#034;',
		'\'': '&#039;',
		'/': '&#047;',
		'`': '&#096;',
		'[' : '&#91;',
		']' : '&#93;',
		'(' : '&#40;',
		')' : '&#41;'
	};

	var reversedEntitiesMap = {};

	var hasOwnProperty = Object.prototype.hasOwnProperty;

	for (var key in ENTITIES_MAP) {
		if (hasOwnProperty.call(ENTITIES_MAP, key)) {
			reversedEntitiesMap[ENTITIES_MAP[key]] = key;
		}
	}

	var REGEX_BBCODE_ESCAPE = /[\]&<>"'\/`\[()]/g;

	var BBCodeUtil = {
		escape: function(data) {
			var searchResult;

			var pointer = 0;

			var result = '';

			while ((searchResult = REGEX_BBCODE_ESCAPE.exec(data)) !== null) {
				var match = searchResult[0];
				var offset = searchResult.index;

				result += data.substring(pointer, offset);

				var entityFound = false;

				var nextSemicolonIndex = data.indexOf(';', offset);

				if (nextSemicolonIndex >= 0) {
					var entity = data.substring(offset, nextSemicolonIndex + 1);

					if (hasOwnProperty.call(reversedEntitiesMap, entity)) {
						result += entity;

						pointer = offset + entity.length;

						entityFound = true;
					}
				}

				if (!entityFound) {
					var escapedValue = ENTITIES_MAP[match];

					result += escapedValue;

					pointer = offset + match.length;
				}
			}

			if (pointer < data.length) {
				result += data.substring(pointer, data.length);
			}

			return result;
		}
	};

	Liferay.BBCodeUtil = BBCodeUtil;
}());