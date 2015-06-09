'use strict';

var properties = require('./test/src/properties.js');

// Karma configuration

var defaultConfig = {
	// enable / disable watching file and executing tests whenever any file changes
	autoWatch: false,

	// base path that will be used to resolve all patterns (eg. files, exclude)
	basePath: './',

	browsers: ['PhantomJS'],

	// enable / disable colors in the output (reporters and logs)
	colors: true,

	coverageReporter: {
		dir: 'test/coverage'
	},

	// list of files to exclude
	exclude: [],

	// frameworks to use
	// available frameworks: https://npmjs.org/browse/keyword/karma-adapter
	frameworks: ['chai', 'mocha', 'sinon'],

    junitReporter: {
      outputFile: 'test/junit/test-results.xml',
      suite: ''
    },

	// level of logging
	// possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
	logLevel: 'info',

	// web server port
	port: 9876,

	// preprocess matching files before serving them to the browser
	// available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
	preprocessors: {
		'src/META-INF/resources/html/js/liferay/*.js': ['coverage']
	},

	// test results reporter to use
	// possible values: 'dots', 'progress'
	// available reporters: https://npmjs.org/browse/keyword/karma-reporter
	reporters: ['coverage', 'progress', 'junit'],

	// Continuous Integration mode
	// if true, Karma captures browsers, runs the tests and exits
	singleRun: true
};

// list of files / patterns to load in the browser
defaultConfig.files = [
	'test/src/mock_base.js',
	{
		included: false,
		pattern: 'test/src/mock_available_languages.js',
		served: true
	},
	{
		included: false,
		pattern: 'test/src/mock_language.js',
		served: true
	}
];

properties.read(
	'../../../portal-impl/src/portal.properties',
	function(data) {
		var props = data[0];

		var bareboneFiles = props['javascript.barebone.files'].split(',');

		bareboneFiles.forEach(
			function(file) {
				var filePath = [file];

				if ((file.indexOf('aui') === 0) || (file.indexOf('bootstrap') === 0)) {
					filePath.unshift('tmp/META-INF/resources/html/js');
				}
				else {
					filePath.unshift('src/META-INF/resources/html/js');
				}

				defaultConfig.files.push(
					{
						included: true,
						pattern: filePath.join('/'),
						served: true
					}
				);

				if (file === 'liferay/modules.js') {
					defaultConfig.files.push('test/src/mock_modules.js');
				}
			}
		);

		defaultConfig.files = defaultConfig.files.concat(
			[
				{
					included: false,
					pattern: 'tmp/META-INF/resources/html/js/aui/**/*.css',
					served: true
				},

				{
					included: false,
					pattern: 'tmp/META-INF/resources/html/js/aui/**/*.js',
					served: true
				},

				{
					included: false,
					pattern: 'src/META-INF/resources/html/js/liferay/*.js',
					served: true
				},

				{
					included: false,
					pattern: 'test/src/*/assets/*',
					served: true
				},

				{
					included: true,
					pattern: 'test/src/*/main.js',
					served: true
				}
			]
		);

		module.exports = function(config) {
			config.set(defaultConfig);
		};
	}
);