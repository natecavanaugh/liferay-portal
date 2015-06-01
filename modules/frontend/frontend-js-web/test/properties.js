'use strict';

var fs = require('fs');

var Properties = {
	preprocess: function(contents) {
		return contents.replace(/\s*#.*/g, '');
	},

	read: function(fileName, callback) {
		var instance = this;

		var contents = fs.readFileSync(
			fileName,
			{
				encoding: 'utf8'
			}
		);

		var properties = require('properties');

		properties.parse(
			instance.preprocess(contents),
			{
				include: true,
				path: false
			},
			function(error, obj) {
				callback.call(instance, [obj, error]);
			}
		);
	}
};

module.exports = Properties;