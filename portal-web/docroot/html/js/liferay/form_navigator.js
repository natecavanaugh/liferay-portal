AUI.add(
	'liferay-form-navigator',
	function(A) {
		var CSS_HIDDEN = 'aui-helper-hidden-accessible';

		var CSS_SECTION_ERROR = 'section-error';

		var CSS_SELECTED = 'selected';

		var SELECTOR_FORM_SECTION = '.form-section';

		var SELECTOR_LIST_ITEM_SELECTED = 'li.selected';

		var SELECTOR_SECTION_ERROR = '.' + CSS_SECTION_ERROR;

		var STR_HREF = 'href';

		var History = Liferay.HistoryManager;

		var FormNavigator = function(options) {
			var instance = this;

			instance._namespace = options.namespace || '';

			instance._container = A.one(options.container);

			instance._formName = options.formName;

			Liferay.after('form:registered', instance._afterFormRegistered, instance);

			Liferay.publish(
				'formNavigator:reveal',
				{
					defaultFn: A.bind('_defRevealFn', instance)
				}
			);

			instance._navigation = instance._container.one('.form-navigator');
			instance._formNavigatorSections = instance._container.oneNS(instance._namespace, '#formNavigatorSections');
			instance._sections = instance._container.all(SELECTOR_FORM_SECTION);

			instance._formNavigatorSections.plug(
				[
					{
						fn: A.Plugin.ParseContent
					},
					{
						fn: A.LoadingMask
					}
				]
			);

			instance._errorMsg = A.Node.create('<div class="portlet-msg-error aui-helper-hidden">' + Liferay.Language.get('failed-to-load-content') + '</div>');

			instance._formNavigatorSections.append(instance._errorMsg);

			if (instance._navigation) {
				instance._navigation.delegate('click', instance._onClick, 'li a', instance);
			}

			if (options.modifiedSections) {
				instance._modifiedSections = A.all('[name=' + options.modifiedSections + ']');

				if (!instance._modifiedSections) {
					instance._modifiedSections = A.Node.create('<input name="' + options.modifiedSections + '" type="hidden" />');

					instance._container.append(instance._modifiedSections);
				}
			}
			else {
				instance._modifiedSections = null;
			}

			if (options.defaultModifiedSections) {
				instance._modifiedSectionsArray = options.defaultModifiedSections;
			}
			else {
				instance._modifiedSectionsArray = [];
			}

			//instance._revealSection(location.href);
			instance._restoreState();

			A.on('formNavigator:trackChanges', instance._trackChanges, instance);

			var inputs = instance._container.all('input, select, textarea');

			History.after('stateChange',function(event) {
				console.log('stateChange');
			});

			if (inputs) {
				inputs.on(
					'change',
					function(event) {
						A.fire('formNavigator:trackChanges', event.target);
					}
				);
			}

			Liferay.on(
				'submitForm',
				function(event, data) {
					if (instance._modifiedSections) {
						instance._modifiedSections.val(instance._modifiedSectionsArray.join(','));
					}
				}
			);
		};

		FormNavigator.prototype = {
			_restoreState: function() {
				var instance = this;

				if (!History.HTML5) {
					instance._revealSection('#' + Liferay.HistoryManager.get(instance._namespace + 'historyKey'));
				}
			},

			_addModifiedSection: function (section) {
				var instance = this;

				if (A.Array.indexOf(section, instance._modifiedSectionsArray) == -1) {
					instance._modifiedSectionsArray.push(section);
				}
			},

			_afterFormRegistered: function(event) {
				var instance = this;

				if (event.formName === instance._formName) {
					var formValidator = event.form.formValidator;

					instance._formValidator = formValidator;

					formValidator.on(['errorField', 'validField'], instance._updateSectionStatus, instance);

					formValidator.on('submitError', instance._revealSectionError, instance);
				}
			},

			_getId: function(id) {
				var instance = this;

				var namespace = instance._namespace;

				id = id || '';

				if (id.indexOf('#') > -1) {
					id = id.split('#')[1] || '';

					id = id.replace(instance._hashKey, '');
				}
				else if (id.indexOf('historyKey=') > -1) {
					id = id.match(/historyKey=([^&#]+)/);
					id = id && id[1];
				}
				else {
					id = '';
				}

				if (id && namespace && (id.indexOf(namespace) == -1)) {
					id = namespace + id;
				}

				return id;
			},

			_onClick: function(event) {
				var instance = this;

				event.preventDefault();

				var target = event.currentTarget;

				var li = target.get('parentNode');

				if (li && !li.test('.selected')) {
					var href = target.attr(STR_HREF);

					instance._revealSection(href, li);

					var hash = href.split('#');

					var hashValue = hash[1];

					if (hashValue) {
						A.later(0, instance, instance._updateHash, [hashValue]);
					}
				}
			},

			_defRevealFn: function(event) {
				var instance = this;

				var prevSection = A.one('#' + instance._currentSection);

				if (prevSection) {
					prevSection._hideClass = CSS_HIDDEN;
					prevSection.hide();
				}

				var namespacedId = event.id;
				var section = event.section;

				instance._currentSection = namespacedId;
				console.log(instance._currentSection);

				instance._sections.removeClass(CSS_SELECTED).addClass(CSS_HIDDEN);

				if (section) {
					section.addClass(CSS_SELECTED).removeClass(CSS_HIDDEN);
					console.log(section, section.test(':hidden'), CSS_HIDDEN);
				}
			},

			_revealSection: function(id, currentNavItem) {
				var instance = this;

				instance._errorMsg.hide();

				id = instance._getId(id);

				if (id) {
					id = id.charAt(0) != '#' ? '#' + id : id;

					if (!currentNavItem) {
						var link = instance._navigation.one('[href$=' + id + ']');

						if (link) {
							currentNavItem = link.get('parentNode');
						}
					}

					id = id.split('#');

					var namespacedId = id[1];

					if (currentNavItem && namespacedId) {
						var uri = location.href.split('#')[0];

						var baseId = namespacedId;

						if (uri.indexOf('historyKey') == -1) {
							var params = {};

							params[instance._namespace + 'historyKey'] = namespacedId;

							uri = Liferay.Util.addParams(params, uri);
						}
						else {
							var re = new RegExp('(?:' + instance._namespace + ')?historyKey=(?:[^&]*)?', 'g');

							uri = uri.replace(re, instance._namespace + 'historyKey=' + namespacedId);
						}

						uri = uri.replace(/p_p_state=(?:[^&]*)?/g, 'p_p_state=exclusive_stateful');

						var section = A.one('#' + namespacedId);

						if (li) {
							var selected = instance._navigation.one(SELECTOR_LIST_ITEM_SELECTED);

							if (selected) {
								selected.removeClass(CSS_SELECTED);
							}

							li.addClass(CSS_SELECTED);
						}

						var eventData = {
							id: namespacedId,
							section: section
						};

						if (!section) {
							instance._formNavigatorSections.loadingmask.show();

							A.io.request(
								uri,
								{
									selector: '#' + namespacedId,
									after: {
										success: function(event) {
											var section = this.get('responseData').item(0);

											if (section) {
												instance._formNavigatorSections.prepend(section);

												instance._sections.push(section);

												eventData.section = section;

												Liferay.fire('formNavigator:reveal', eventData);
											}
											else {
												if (instance._currentSection) {
													A.one('#' + instance._currentSection).addClass(CSS_HIDDEN);
												}

												instance._errorMsg.show();
											}

											instance._formNavigatorSections.loadingmask.hide();
										}
									}
								}
							);
						}
						else {
							Liferay.fire('formNavigator:reveal', eventData);
						}
					}
				}
			},

			_revealSectionError: function() {
				var instance = this;

				var sectionError = instance._navigation.one(SELECTOR_SECTION_ERROR);

				var sectionErrorLink = sectionError.one('a').attr(STR_HREF);

				instance._revealSection(sectionErrorLink, sectionError);
			},

			_trackChanges: function(el) {
				var instance = this;

				var currentSection = A.one(el).ancestor(SELECTOR_FORM_SECTION).attr('id');

				var currentSectionLink = A.one('#' + currentSection + 'Link');

				if (currentSectionLink) {
					currentSectionLink.get('parentNode').addClass('section-modified');
				}

				instance._addModifiedSection(currentSection);
			},

			_updateHash: function(section) {
				var instance = this;

				var params = {};

				params[instance._namespace + 'historyKey'] = section;

				History.add(params);
			},

			_updateSectionStatus: function() {
				var instance = this;

				var navigation = instance._navigation;

				var lis = navigation.all('li');

				lis.removeClass(CSS_SECTION_ERROR);

				var formValidator = instance._formValidator;

				if (formValidator.hasErrors()) {
					var selectors = A.Object.keys(formValidator.errors);

					A.all('#' + selectors.join(', #')).each(
						function(item, index, collection) {
							var section = item.ancestor(SELECTOR_FORM_SECTION);

							if (section) {
								var navItem = navigation.one('a[href="#' + section.attr('id') + '"]');

								if (navItem) {
									navItem.ancestor().addClass(CSS_SECTION_ERROR);
								}
							}
						}
					);
				}
			},

			_hashKey: '_LFR_FN_'
		};

		Liferay.FormNavigator = FormNavigator;
	},
	'',
	{
		requires: ['aui-base', 'aui-io-plugin', 'liferay-history-manager']
	}
);