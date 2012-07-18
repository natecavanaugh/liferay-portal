AUI.add(
	'liferay-localizer',
	function(A) {
		/**
		 * Localizer - Provides a UI to select from a list of localizations
		 * and stores the values of the chosen localizations in hidden inputs,
		 * ready for form submission.
		 *
		 * @module liferay-localizer
		 */

		var L = A.Lang,
			MODULE = 'localizer',

			isArray = L.isArray,
			isString = L.isString,

			CONTENT_BOX = 'contentBox',
			IMAGES_PATH = 'flagImagesPath',
			INPUT_CLASS = 'aui-field-input'
			INPUT_LOCALE_ID = 'name',
			LOCALES = 'locales',
			NAMESPACE = 'namespace',

			STR_BLANK = '',
			STR_DASH = '-',
			STR_DOT = '.',
			STR_HASH = '#',

			LOCALIZER_CLASS = 'lfr-localizer',
			LOCALIZER_NS = LOCALIZER_CLASS + STR_DASH,

			COUNT_CLASS = LOCALIZER_NS + 'locale-count',
			COUNT_OFFSEST = 1,

			FLAG_CLASS = LOCALIZER_NS + 'locale-flag',
			FLAG_IMAGE_EXT = 'png',

			LOCALE_ITEM_CLASS = LOCALIZER_NS + 'locale-item',
			LOCALE_ITEM_CLASS_HASVALUE = LOCALE_ITEM_CLASS + STR_DASH + 'hasvalue',
			LOCALE_ITEM_CLASS_ISSELECTED = LOCALE_ITEM_CLASS + STR_DASH + 'isselected',
			LOCALE_ITEM_FLAG_CLASS = LOCALE_ITEM_CLASS + STR_DASH + 'flag',
			LOCALE_ITEM_LOCALE_ID = 'data-langId',
			LOCALE_ITEM_PREFIX = 'localizerLocaleItem';

			LOCALE_SELECTOR_CLASS = LOCALIZER_NS + 'selector',

			DEBOUNCE_SHOW = 0,
			DEBOUNCE_HIDE = 500,

			TPL_LOCALE_SELECTOR = new A.Template(
				'<div class="', LOCALE_SELECTOR_CLASS, '">',
					'<ul>',
						'<tpl for="locales">',
							'<li class="', LOCALE_ITEM_CLASS, '" id="', LOCALE_ITEM_PREFIX, '{[parent.namespace]}{id}" title="{name}" ', LOCALE_ITEM_LOCALE_ID, '="{[parent.namespace]}{id}">',
								'<a class="', LOCALE_ITEM_FLAG_CLASS, '" href="javascript:;"><img src="{[parent.flagImagesPath]}{id}.', FLAG_IMAGE_EXT, '" /></a>',
								'<a href="javascript:;">{name}</a>',
							'</li>',
						'</tpl>',
					'</ul>',
				'</div>'
			);

		var Localizer = A.Component.create(
			{
				ATTRS: {
					flagImagesPath: {
						value: null,
						validator: isString
					},

					locales: {
						value: [],
						validator: isArray
					},

					namespace: {
						value: null,
						validator: isString
					}
				},

				NAME: MODULE,

				prototype: {
					_localeSelectorTemplate: null,
					_mapPrevLocales: null,
					_mapSelectedLocales: null,
					_overlay: null,
					_selectorNode: null,

					initializer: function() {
						var instance = this;

						instance._localeSelectorTemplate = TPL_LOCALE_SELECTOR;
						instance._mapPrevLocales = {};
						instance._mapSelectedLocales = {};
					},

					renderUI: function() {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var localizer = contentBox.one(STR_DOT + LOCALIZER_CLASS);

						instance._selectorNode = instance._localeSelectorTemplate.render(
							{
								flagImagesPath: instance.get(IMAGES_PATH),
								locales: instance.get(LOCALES),
								namespace: instance.get(NAMESPACE)
							}
						);

						instance._overlay = new A.Overlay(
							{
								align: {
									node: localizer,
									points: ['tr', 'br']
								},
								bodyContent: instance._selectorNode,
								trigger: localizer,
								visible: false,
								zIndex: 10000
							}
						).render();
					},

					bindUI: function() {
						var instance = this;

						instance._bindOverlayDisplay();

						instance._selectorNode.delegate('click', instance._bindSelectorNodeClick, 'li', instance);
					},

					syncUI: function() {
						var instance = this;

						instance._primeMapSelectedLocales();

						instance._primeUILocaleItems();
					},

					_bindOverlayDisplay: function() {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var localizer = contentBox.one(STR_DOT + LOCALIZER_CLASS);

						var hide = A.debounce(
							function(event) {
								show.cancel();
								instance._overlay.hide();
							},
							DEBOUNCE_HIDE
						);

						var show = A.debounce(
							function(event) {
								hide.cancel();
								instance._overlay.show();
							},
							DEBOUNCE_SHOW
						);

						var hoverConfig = {
							mouseenter: show,
							mouseleave: hide
						};

						localizer.on(hoverConfig);

						instance._overlay.on(hoverConfig);
					},

					_bindSelectorNodeClick: function(event) {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var textarea = contentBox.one(STR_DOT + INPUT_CLASS);

						var currentTarget = event.currentTarget;

						var currentLocaleId = currentTarget.attr(LOCALE_ITEM_LOCALE_ID);
						var currentLocaleName = currentTarget.attr('title');

						var prevVal = textarea.val();

						var prevLocaleId = textarea.attr(INPUT_LOCALE_ID);

						instance._updateUILocaleItemsHasValue(prevLocaleId, !!prevVal);

						if (!prevVal) {
							instance._localeRemove(prevLocaleId);
						}
						else {
							instance._localeAdd(prevLocaleId, prevVal);
						}

						instance._updateUILocaleItemsSelected(currentTarget);

						instance._updateUIFlag(currentLocaleId, currentLocaleName);

						instance._updateUITextarea(currentLocaleId);

						instance._updateUICount();

						textarea.focus();
					},

					_buildFlagPath: function(imagePath, localeId) {
						var regex = new RegExp('\/[A-Za-z_\]+.' + FLAG_IMAGE_EXT + '$');

						return imagePath.replace(regex, '/' + localeId + '.' + FLAG_IMAGE_EXT);
					},

					_localeAdd: function(localeId, localeVal) {
						var instance = this;

						var hiddenInput = instance._mapSelectedLocales[localeId];

						if (!hiddenInput) {
							hiddenInput = instance._mapPrevLocales[localeId] || A.Node.create('<input id="' + localeId + '" name="' + localeId + '" type="hidden" />');

							var contentBox = instance.get(CONTENT_BOX);

							var textarea = contentBox.one(STR_DOT + INPUT_CLASS);

							textarea.placeAfter(hiddenInput);

							instance._mapSelectedLocales[localeId] = hiddenInput;
						}

						hiddenInput.val(localeVal);
					},

					_localeRemove: function(localeId) {
						var instance = this;

						var prevSelectedLocaleRef = instance._mapSelectedLocales[localeId];

						if (prevSelectedLocaleRef) {
							prevSelectedLocaleRef.remove();

							delete instance._mapSelectedLocales[localeId];

							instance._mapPrevLocales[localeId] = prevSelectedLocaleRef;
						}
					},

					_primeMapSelectedLocales: function() {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						contentBox.all('input[type=hidden]').each(
							function(item, index, collection) {
								var localeId = item.attr('name');

								instance._mapSelectedLocales[localeId] = item;
							}
						);
					},

					_primeUILocaleItems: function() {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var textarea = contentBox.one(STR_DOT + INPUT_CLASS);

						var currentlocaleId = textarea.attr(INPUT_LOCALE_ID);
						var currentlocaleValue = textarea.val();

						var currentlocaleItemNode = A.one(STR_HASH + LOCALE_ITEM_PREFIX + currentlocaleId);

						instance._updateUILocaleItemsSelected(currentlocaleItemNode);

						instance._updateUILocaleItemsHasValue(currentlocaleId, !!currentlocaleValue);

						for (localeId in instance._mapSelectedLocales) {
							instance._updateUILocaleItemsHasValue(localeId, true);
						}
					},

					_updateUICount: function() {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var count = contentBox.one(STR_DOT + COUNT_CLASS);

						var selectedLocalesSize = A.Object.size(instance._mapSelectedLocales);

						var selectedLocalesMax = Math.max(selectedLocalesSize + COUNT_OFFSEST, 1);

						count.text(selectedLocalesMax);
					},

					_updateUIFlag: function(localeId, localeName) {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var namespace = instance.get(NAMESPACE);

						var selectedLocaleImage = contentBox.one(STR_DOT + FLAG_CLASS);

						var selectedLocaleImageSrc = selectedLocaleImage.attr('src');

						localeId = localeId.replace(namespace, STR_BLANK)

						selectedLocaleImageSrc = instance._buildFlagPath(selectedLocaleImageSrc, localeId);

						selectedLocaleImage.attr('src', selectedLocaleImageSrc);
						selectedLocaleImage.attr('title', localeName);
					},

					_updateUILocaleItemsHasValue: function(localeId, isToggleForced) {
						var localeItemNode = A.one(STR_HASH + LOCALE_ITEM_PREFIX + localeId);

						if (localeItemNode) {
							localeItemNode.toggleClass(LOCALE_ITEM_CLASS_HASVALUE, isToggleForced);
						}
					},

					_updateUILocaleItemsSelected: function(localeItemNode) {
						if (localeItemNode) {
							localeItemNode.radioClass(LOCALE_ITEM_CLASS_ISSELECTED);
						}
					},

					_updateUITextarea: function(localeId) {
						var instance = this;

						var contentBox = instance.get(CONTENT_BOX);

						var textarea = contentBox.one(STR_DOT + INPUT_CLASS);

						var hiddenInput = A.one(STR_HASH + localeId);

						var value = hiddenInput ? hiddenInput.val() : STR_BLANK;

						instance._localeRemove(localeId);

						textarea.val(value);

						textarea.attr(INPUT_LOCALE_ID, localeId);
					}
				}
			}
		);

		Liferay.Localizer = Localizer;
	},
	'',
	{
		requires: ['aui-base', 'aui-overlay-context', 'aui-template']
	}
);