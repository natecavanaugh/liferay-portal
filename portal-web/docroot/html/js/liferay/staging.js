AUI.add(
	'liferay-staging',
	function(A) {
		var Lang = A.Lang;

		var StagingBar = {
			init: function(config) {
				var instance = this;

				instance._namespace = config.namespace;

				instance._bindUI();

				Liferay.publish(
					{
						fireOnce: true
					}
				);

				Liferay.after(
					'initStagingBar',
					function(event) {
						A.getBody().addClass('staging-ready');
					}
				);

				Liferay.fire('initStagingBar', config);
			},

			_bindUI: function() {
				var instance = this;

				instance._selectBoxNavInit();

				instance._stagingLinkInit();
			},

			_selectBoxNavInit: function() {
				var selectBoxNav = 'select.select-box-nav';
				var stagingBarContainer = A.one('.staging-bar');

				if (stagingBarContainer) {
					stagingBarContainer.delegate(
						'change', 
						function(event) {
							var currentTarget = event.currentTarget;

							window.location.href = currentTarget.get('value');
						},
						selectBoxNav
					);
				}
			},

			_stagingLinkInit: function() {
				var instance = this;

				var initialized = false;
				var stagingLink = A.one('.staging-bar .staging-link');

				if (stagingLink) {
					stagingLink.on(
						'click', 
						function(event) {
							var dropdown = event.currentTarget;
							var dropdownMenu = dropdown.one('.dropdown-menu');
							var clickedElement = event.target;
							var menuOpen = dropdown.hasClass('open');

							if (clickedElement.ancestor('.dropdown-toggle') || clickedElement.hasClass('dropdown-toggle')) {
								if (!menuOpen) {
									var W = dropdown.ancestor('.nav-account-controls').get('offsetWidth') - 5;

									dropdownMenu.setStyle('width', W + 'px');
								}
									dropdown.toggleClass('open');
							}

							menuOpen = dropdown.hasClass('open');

							if (menuOpen && !initialized) {
								dropdown.once('clickoutside', function(event) {
									this.removeClass('open');

									initialized = false;
								});

								initialized = true;
							}
							else if (!menuOpen) {
								dropdown.detach('clickoutside');

								initialized = false;
							}
						}
					);
				}
			}
		};

		Liferay.StagingBar = StagingBar;
	},
	'',
	{
		requires: ['aui-io-plugin-deprecated', 'liferay-util-window']
	}
);