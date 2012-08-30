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
			COUNT_OFFSEST = 1,
			DEFAULT_LOCALE = 'defaultLocale',
			DISABLED = 'disabled',
			FLAG_IMAGE_EXT = 'png',
			FLAG_IMAGES_PATH = 'flagImagesPath',
			INPUT_CLASS = 'aui-field-input'
			INPUT_LOCALE_ID = 'name',
			LOCALES = 'locales',
			LOCALIZER_CLASS = 'lfr-localizer',
			NAMESPACE = 'namespace',
			STR_BLANK = '',
			STR_DASH = '-',
			STR_DOT = '.',
			STR_HASH = '#',
			TRIGGER = 'trigger',

			LOCALIZER_NS = LOCALIZER_CLASS + STR_DASH,

			LOCALE_COUNT_CLASS = LOCALIZER_NS + 'locale-count',
			LOCALE_FLAG_CLASS = LOCALIZER_NS + 'locale-flag',
			LOCALE_ITEM_CLASS = LOCALIZER_NS + 'locale-item',
			LOCALE_ITEM_CLASS_FLAG = LOCALE_ITEM_CLASS + STR_DASH + 'flag',
			LOCALE_ITEM_CLASS_SELECTED = LOCALE_ITEM_CLASS + STR_DASH + 'selected',
			LOCALE_ITEM_CLASS_VALUE = LOCALE_ITEM_CLASS + STR_DASH + 'value',
			LOCALE_ITEM_LOCALE_ID = 'data-langId',
			LOCALE_ITEM_PREFIX = 'localizerLocaleItem';
			LOCALE_SELECTOR_CLASS = LOCALIZER_NS + 'selector',

			TPL_LOCALE_SELECTOR = new A.Template(
				'<div class="', LOCALE_SELECTOR_CLASS, '">',
					'<ul>',
						'<tpl for="locales">',
							'<li class="', LOCALE_ITEM_CLASS, '" id="', LOCALE_ITEM_PREFIX, '{[parent.namespace]}{id}" title="{name}" ', LOCALE_ITEM_LOCALE_ID, '="{[parent.namespace]}{id}">',
								'<a class="', LOCALE_ITEM_CLASS_FLAG, '" href="javascript:;"><img src="{[parent.flagImagesPath]}{id}.', FLAG_IMAGE_EXT, '" /></a>',
								'<a href="javascript:;">{name}</a>',
							'</li>',
						'</tpl>',
					'</ul>',
				'</div>'
			);

		var Localizer = A.Component.create(
			{
				NAME: MODULE,

				ATTRS: {
					defaultLocale: {
						setter: function(value) {
							var instance = this;

							var namespace = instance.get(NAMESPACE);

							return A.Lang.String.prefix(namespace, value);
						},
						value: themeDisplay.getDefaultLanguageId()
					},

					flagImagesPath: {
						validator: isString,
						value: null
					},

					locales: {
						validator: isArray,
						value: []
					},

					namespace: {
						validator: isString,
						value: null
					}
				},

				prototype: {
					initializer: function() {
						var instance = this;

						instance._contentBox = instance.get(CONTENT_BOX);
						instance._defaultLocale = instance.get(DEFAULT_LOCALE);
						instance._namespace = instance.get(NAMESPACE);

						instance._localizer = instance._contentBox.one(STR_DOT + LOCALIZER_CLASS);
						instance._textarea = instance._contentBox.one(STR_DOT + INPUT_CLASS);

						instance._mapPrevLocales = {};
						instance._mapSelectedLocales = {};
					},

					renderUI: function() {
						var instance = this;

						var localizer = instance._localizer;

						instance._selectorNode = TPL_LOCALE_SELECTOR.render(
							{
								flagImagesPath: instance.get(FLAG_IMAGES_PATH),
								locales: instance.get(LOCALES),
								namespace: instance._namespace
							}
						);

						instance._overlay = new A.OverlayContext(
							{
								align: {
									node: localizer,
									points: ['tr', 'br']
								},
								boundingBox: instance._selectorNode,
								hideDelay: 10,
								trigger: localizer,
								width: 225,
								zIndex: 10000
							}
						).render();

						instance._bindSelectorNodeDisplay();
					},

					bindUI: function() {
						var instance = this;

						var overlay = instance._overlay;

						instance._textarea.on(
							'keydown',
							A.debounce(
								function(event) {
									instance._bindSelectorNodeDisplay(event);
								},
								150
							)
						);

						overlay.on(
							'mouseleave',
							function(event) {
								overlay.hide()
							}
						);

						instance._selectorNode.delegate('click', instance._bindSelectorNodeClick, 'li', instance);
					},

					syncUI: function() {
						var instance = this;

						instance._primeMapSelectedLocales();

						instance._primeUILocaleItems();
					},

					_bindSelectorNodeClick: function(event) {
						var instance = this;

						var textarea = instance._textarea;

						var currentTarget = event.currentTarget;

						var currentLocaleId = currentTarget.attr(LOCALE_ITEM_LOCALE_ID);
						var currentLocaleName = currentTarget.attr('title');

						var prevVal = textarea.val();

						var prevLocaleId = textarea.attr(INPUT_LOCALE_ID);

						instance._updateUILocaleItemsValue(prevLocaleId, !!prevVal);

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

					_bindSelectorNodeDisplay: function(event) {
						var instance = this;

						var overlay = instance._overlay;
						var textarea = instance._textarea;

						var localId = textarea.attr(INPUT_LOCALE_ID);

						var textareaValue = textarea.val();

						overlay.hide();

						if (localId == instance._defaultLocale) {
							if (textareaValue != STR_BLANK) {
								instance.set(DISABLED, false);

								overlay.set(TRIGGER, instance._localizer);
							}
							else {
								instance.set(DISABLED, true);

								overlay.set(TRIGGER, '');
							}
						}
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

							instance._textarea.placeAfter(hiddenInput);

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

						instance._contentBox.all('input[type=hidden]').each(
							function(item, index, collection) {
								var localeId = item.attr('name');

								instance._mapSelectedLocales[localeId] = item;
							}
						);
					},

					_primeUILocaleItems: function() {
						var instance = this;

						var textarea = instance._textarea;

						var currentlocaleId = textarea.attr(INPUT_LOCALE_ID);

						var currentlocaleValue = textarea.val();

						var currentlocaleItemNode = A.one(STR_HASH + LOCALE_ITEM_PREFIX + currentlocaleId);

						instance._updateUILocaleItemsSelected(currentlocaleItemNode);

						instance._updateUILocaleItemsValue(currentlocaleId, !!currentlocaleValue);

						for (localeId in instance._mapSelectedLocales) {
							instance._updateUILocaleItemsValue(localeId, true);
						}
					},

					_updateUICount: function() {
						var instance = this;

						var count = instance._contentBox.one(STR_DOT + LOCALE_COUNT_CLASS);

						var selectedLocalesSize = A.Object.size(instance._mapSelectedLocales);

						var selectedLocalesMax = Math.max(selectedLocalesSize + COUNT_OFFSEST, 1);

						count.text(selectedLocalesMax);
					},

					_updateUIFlag: function(localeId, localeName) {
						var instance = this;

						var selectedLocaleImage = instance._contentBox.one(STR_DOT + LOCALE_FLAG_CLASS);

						var selectedLocaleImageSrc = selectedLocaleImage.attr('src');

						localeId = localeId.replace(instance._namespace, STR_BLANK)

						selectedLocaleImageSrc = instance._buildFlagPath(selectedLocaleImageSrc, localeId);

						selectedLocaleImage.attr('src', selectedLocaleImageSrc);
						selectedLocaleImage.attr('title', localeName);
					},

					_updateUILocaleItemsValue: function(localeId, isToggleForced) {
						var localeItemNode = A.one(STR_HASH + LOCALE_ITEM_PREFIX + localeId);

						if (localeItemNode) {
							localeItemNode.toggleClass(LOCALE_ITEM_CLASS_VALUE, isToggleForced);
						}
					},

					_updateUILocaleItemsSelected: function(localeItemNode) {
						if (localeItemNode) {
							localeItemNode.radioClass(LOCALE_ITEM_CLASS_SELECTED);
						}
					},

					_updateUITextarea: function(localeId) {
						var instance = this;

						var textarea = instance._textarea;

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