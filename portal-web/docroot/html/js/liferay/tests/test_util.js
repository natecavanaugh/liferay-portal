AUI.add(
	'liferay-util-tests',
	function(A) {
		var suite = new A.Test.Suite('liferay-util');

		suite.add(
			new A.Test.Case(
				{
					name: 'Liferay.Util tests',

					setUp: function() {
						//
					},

					tearDown: function() {
						//
					},

					'Test Liferay.Util.camelize': function() {
						A.Assert.areSame(
							Liferay.Util.camelize('this-will-pass'),
							'thisWillPass'
						);

						A.Assert.areNotSame(
							Liferay.Util.camelize('this-will-fail'),
							'thisWillfail'
						);
					}
				}
			)
		);

		A.Test.Runner.add(suite);
	},
	'',
	{
		requires: ['test']
	}
);