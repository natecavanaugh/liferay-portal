/*
 * Based on Steven Levithan's parseUri library
 *
 * parseUri 1.2.2
 * (c) Steven Levithan <stevenlevithan.com>
 * MIT License
 */

AUI().add(
	'liferay-uri',
	function(A) {
		var Lang = A.Lang;

		var REGEX_QUERY = /(?:^|&)([^&=]*)=?([^&]*)/g;

		var owns = A.Object.owns;

		var URI = A.Component.create(
			{
				ATTRS: {
					strictMode: {
						value: false,
						validator: Lang.isBoolean
					},

					key: {
						value: ['source','protocol','authority','userInfo','user','password','host','port','relative','path','directory','file','query','hash'],
						validator: Lang.isArray
					},
					queryMap: {
						value: {
							name:   'queryMap',
							parser: REGEX_QUERY
						},
						validator: function(value) {
							return Lang.isObject(value) &&
								owns.call(value, 'name') && Lang.isString(value.name) &&
								owns.call(value, 'parser') && value.parser instanceof RegExp;
						}
					},
					hashMap: {
						value: {
							name:   'hashMap',
							parser: REGEX_QUERY
						},
						validator: function(value) {
							return Lang.isObject(value) &&
								owns.call(value, 'name') && Lang.isString(value.name) &&
								owns.call(value, 'parser') && value.parser instanceof RegExp;
						}
					},
					parser: {
						value: {
							strict: /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/,
							loose:  /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/
						},
						validator: function(value) {
							return Lang.isObject(value) &&
								owns.call(value, 'strict') && value.strict instanceof RegExp &&
								owns.call(value, 'loose') && value.loose instanceof RegExp;
						}
					}
				},

				EXTENDS: A.Base,

				NAME: 'liferayuri',

				prototype: {
					parse: function(str) {
						var instance = this;

						if (!Lang.isString(str)) {
							return {};
						}

						var key = instance.get('key');
						var parser  = instance.get('parser');
						var strictMode = instance.get('strictMode');
						var matches = parser[strictMode ? 'strict' : 'loose'].exec(str);

						var uri = {};
						var index = 14;

						while (index--) {
							uri[key[index]] = matches[index] || '';
						}

						instance._createMap(uri, 'query');
						instance._createMap(uri, 'hash');

						return uri;
					},

					toString: function(uri) {
						var instance = this;

						var result = [];

						var protocol = uri.protocol;

						if (protocol) {
							result.push(protocol);

							if (protocol.indexOf(':') != protocol.length - 1) {
								result.push(':');
							}

							result.push('//');
						}
						else if (uri.source.indexOf('//') != -1 && uri.host) {
							result.push('//');
						}

						var userInfo = uri.userInfo;

						if (userInfo && uri.host) {
							result.push(userInfo);

							if (userInfo.indexOf('@') != userInfo().length - 1) {
								result.push('@');
							}
						}

						if (uri.host) {
							result.push(uri.host);

							if (uri.port) {
								result.push(':', uri.port);
							}
						}

						var hash = uri.hash = instance._stringifyQuery(uri.hashMap);
						var query = uri.query = instance._stringifyQuery(uri.queryMap);

						if (uri.path) {
							result.push(uri.path);
						}
						else if (uri.host && (query || hash)) {
							result.push('/');
						}

						if (query) {
							if (query.indexOf('?') != 0) {
								result.push('?');
							}

							result.push(query);
						}

						if (hash) {
							if (hash.indexOf('#') != 0) {
								result.push('#');
							}

							result.push(hash);
						}

						result = result.join('');

						return result;
					},

					_createMap: function(uri, mapName) {
						var instance = this;

						var map = instance.get(mapName + 'Map');

						var key = instance.get('key');

						var index = key.indexOf(mapName);

						uri[map.name] = {};

						uri[key[index]].replace(
							map.parser,
							function (match, key, value) {
								if (key) {
									uri[map.name][key] = value;
								}
							}
						);
					},

					_stringifyQuery: function(query) {
						var result = [];

						A.each(
							query,
							function(value, key, collection) {
								if (Lang.isValue(value)) {
									result.push('&', key, '=', value);
								}
							}
						);

						result.shift();

						return result.join('');
					}
				}
			}
		);

		Liferay.URI = URI;
	},
	'',
	{
		requires: ['aui-base']
	}
);