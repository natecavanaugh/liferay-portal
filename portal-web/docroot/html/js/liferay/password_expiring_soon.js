AUI.add(
	'liferay-password-expiring-soon',
	function(A) {
		var PasswordExpiringSoon = A.Component.create(
			{
				EXTENDS: A.Base,

				NAME: 'passwordexpiringsoon',

				prototype: {
					initializer: function(config) {
						var instance = this;

						new Liferay.Notice(
							{
								closeText: Liferay.Language.get('close'),
								content: Liferay.Language.get('warning-your-password-will-expire-soon'),
								noticeClass: 'aui-helper-hidden',
								toggleText: false
							}
						).show();
					}
				}
			}
		);

		Liferay.PasswordExpiringSoon = PasswordExpiringSoon;
	},
	'',
	{
		requires: ['liferay-notice']
	}
);