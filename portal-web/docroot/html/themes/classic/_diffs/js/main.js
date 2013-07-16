AUI().ready(
	'aui-io-request', 'aui-modal', 'liferay-hudcrumbs', 'liferay-navigation-interaction',
	function(A) {
		var navigation = A.one('#navigation');

		if (navigation) {
			navigation.plug(Liferay.NavigationInteraction);

			A.getDoc().delegate('click', function(event) {
				var target = event.currentTarget;
				var navMenu = target.ancestor().one('.nav-collapse');

				var closeMenu = function(navMenu) {
					var height = navMenu.get('offsetHeight');

					navMenu.setStyle('height', height + 'px');

					navMenu.transition({
						duration: 0.4,
						easing: 'ease',
						height: 0
					}, function() {
						navMenu.setStyle('height', '');
					});

					navMenu.setStyle('height', '0').removeClass('in');
				};

				var openMenu = function(navMenu) {
					navMenu.setStyles({
						height: 'auto',
						position: 'absolute',
						opacity: 0
					}).addClass('in');

					var height = navMenu.get('offsetHeight');

					navMenu.setStyles({
						height: '',
						position: '',
						opacity: ''
					});

					navMenu.transition({
						duration: 0.4,
						easing: 'ease',
						height: height + 'px'
					}, function() {
						this.setStyle('height', 'auto');
					});
				};

				if (navMenu) {
					if (navMenu.hasClass('in')) {
						closeMenu(navMenu);
						target.ancestor('.navbar').detach('clickoutside');
					}
					else {
						openMenu(navMenu);

						target.ancestor('.navbar').once('clickoutside', function(event) {
							closeMenu(navMenu);
						});

					}
				}
			}, 'nav.navbar .btn-navbar');
		}

		var siteBreadcrumbs = A.one('#breadcrumbs');

		if (siteBreadcrumbs) {
			siteBreadcrumbs.plug(A.Hudcrumbs);
		}

		var signIn = A.one('li.sign-in a');

		if (signIn) {
			signIn.on(
				'click',
				function(event) {
					event.preventDefault();

					var signInURL = event.currentTarget.attr('href');

					var redirectPage = function() {
						A.config.win.location.href = signInURL;
					};

					A.io.request(
						signInURL,
						{
							on: {
								failure: redirectPage,
								success: function(event, id, obj) {
									var responseData = this.get('responseData');

									var modal;

									if (responseData) {
										var renderData = A.Node.create(responseData).one('#portlet_58 .portlet-body');

										if (renderData) {
											modal = new A.Modal(
												{
													bodyContent: renderData,
													centered: true,
													constrain: true,
													headerContent: '<h3>' + Liferay.Language.get('sign-in') + '</h3>',
													modal: true,
													zIndex: 400
												}
											).render();
										}
									}

									if (!modal) {
										redirectPage();
									}
								}
							}
						}
					);
				}
			);
		}
	}
);