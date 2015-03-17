'use strict';

// Karma configuration

var basePath = process.cwd() + '/../../';

var jsPath = 'docroot/html/js/';

var defaultConfig = {
	// enable / disable watching file and executing tests whenever any file changes
	autoWatch: false,

	// base path that will be used to resolve all patterns (eg. files, exclude)
	basePath: basePath,

	browsers: ['Chrome', 'Firefox', 'Safari'],

	// enable / disable colors in the output (reporters and logs)
	colors: true,

	coverageReporter: {
		dir: 'test/js/coverage'
	},

	// list of files to exclude
	exclude: [],

	// frameworks to use
	// available frameworks: https://npmjs.org/browse/keyword/karma-adapter
	frameworks: ['chai', 'mocha', 'sinon'],

	// level of logging
	// possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
	logLevel: 'info',

	// web server port
	port: 9876,

	// preprocess matching files before serving them to the browser
	// available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
	preprocessors: {
		'docroot/html/js/liferay/*.js': ['coverage']
	},

	// test results reporter to use
	// possible values: 'dots', 'progress'
	// available reporters: https://npmjs.org/browse/keyword/karma-reporter
	reporters: ['coverage', 'progress'],

	// Continuous Integration mode
	// if true, Karma captures browsers, runs the tests and exits
	singleRun: true
};

// list of files / patterns to load in the browser
defaultConfig.files = [
	'test/js/mock_base.js'
];

var portalProperties = basePath + '../portal-impl/src/portal.properties';

var properties = require('./properties.js');

properties.read(
	portalProperties,
	function(data) {
		var props = data[0];

		var bareboneFiles = props['javascript.barebone.files'].split(',');

		bareboneFiles.forEach(
			function(file) {
				defaultConfig.files.push(
					{
						included: true,
						pattern: jsPath + file,
						served: true
					}
				);

				if (file === 'liferay/modules.js') {
					defaultConfig.files.push('test/js/mock_modules.js');
				}
			}
		);

		defaultConfig.files = defaultConfig.files.concat(
			[
				'test/js/mock_available_languages.js',
				'test/js/mock_language.js',

				{
					included: false,
					pattern: jsPath + 'aui/**/*.css',
					served: true
				},

				{
					included: false,
					pattern: jsPath + 'aui/**/*.js',
					served: true
				},

				{
					included: false,
					pattern: jsPath + 'liferay/*.js',
					served: true
				},

				{
					included: false,
					pattern: 'test/js/*/assets/*',
					served: true
				},

				{
					included: true,
					pattern: 'test/js/*/*-test.js',
					served: true
				}
			]
		);

		module.exports = function(config) {
			config.set(defaultConfig);
		};
	}
);