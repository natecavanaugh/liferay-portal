'use strict';

(function() {
	describe(
		'Storage Formatter Test Suite',
		function() {
			var assert = chai.assert;

			before(
				function(done) {
					var instance = this;

					AUI().use(
						'liferay-storage-formatter',
						function(A) {
							assert.ok(Liferay.StorageFormatter);

							var StorageFormatterComponent = A.Component.create(
								{
									AUGMENTS: [Liferay.StorageFormatter],
									EXTENDS: A.Base
								}
							);

							instance.getStorageFormatter = function(config) {
								return new StorageFormatterComponent(config);
							};

							done();
						}
					);
				}
			);

			it(
				'should add a space between number and suffix',
				function(done) {
					var instance = this;

					instance.getStorageFormatter().set('addSpaceBeforeSuffix');

					assert.equal(
						instance.getStorageFormatter(
							{
								addSpaceBeforeSuffix: true
							}
						).formatStorage(1345678896),
						'1.3 GB'
					);

					done();
				}
			);

			it(
				'should format with configurable decimal separator',
				function(done) {
					var instance = this;

					instance.getStorageFormatter().set('addSpaceBeforeSuffix');

					assert.equal(
						instance.getStorageFormatter(
							{
								decimalSeparator: ','
							}
						).formatStorage(1345678896),
						'1,3GB'
					);

					done();
				}
			);

			it(
				'should format zero',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(0),
						'0KB'
					);

					done();
				}
			);

			it(
				'should format bytes',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(3),
						'0KB'
					);

					done();
				}
			);

			it(
				'should format kilobytes',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(3562),
						'3KB'
					);

					done();
				}
			);

			it(
				'should format megabytes',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(234123456),
						'223.3MB'
					);

					done();
				}
			);

			it(
				'should format gigabytes',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(1345678896),
						'1.3GB'
					);

					done();
				}
			);

			it(
				'should format terabytes and higher as multiples of gigabytes',
				function(done) {
					var instance = this;

					assert.equal(
						instance.getStorageFormatter().formatStorage(44122342354565),
						'41092.1GB'
					);

					done();
				}
			);
		}
	);
})();