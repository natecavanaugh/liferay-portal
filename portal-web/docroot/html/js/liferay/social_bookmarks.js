AUI.add(
	'liferay-social-bookmarks',
	function(A) {
		var NAME = 'social-bookmarks';

		var SHARE_WINDOW_HEIGHT = 436;

		var SHARE_WINDOW_WIDTH = 626;

		var WIN = A.getWin();

		var SocialBookmarks = A.Component.create(
			{
				ATTRS: {
					contentBox: {
						setter: A.one
					}
				},

				EXTENDS: A.Base,

				NAME: NAME,

				prototype: {
					initializer: function() {
						var instance = this;

						var portletBody = instance.get('contentBox').ancestor('.portlet-body'),
							id = portletBody._yuid;

						if (!Liferay.SocialBookmarks.linkHandle[id]) {
							portletBody.delegate(
								'click',
								function(event) {
									event.preventDefault();

									var shareWindowFeatures = [
										'left=' + ((WIN.get('innerWidth') / 2) - (SHARE_WINDOW_WIDTH / 2)),
										'height=' + SHARE_WINDOW_HEIGHT,
										'toolbar=0',
										'top=' + ((WIN.get('innerHeight') / 2) - (SHARE_WINDOW_HEIGHT / 2)),
										'status=0',
										'width=' + SHARE_WINDOW_WIDTH
									];

									var url = event.currentTarget.attr('href');

									WIN.getDOM().open(url, null, shareWindowFeatures.join()).focus();
								},
								'.social-bookmark a'
							);

							Liferay.SocialBookmarks.linkHandle[id] = true;
						}
					}
				}
			}
		);

		Liferay.SocialBookmarks = SocialBookmarks;
		Liferay.SocialBookmarks.linkHandle = [];
	},
	'',
	{
		requires: ['aui-component', 'aui-node']
	}
);