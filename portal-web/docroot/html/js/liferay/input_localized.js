AUI.add(
	'liferay-input-localized',
	function(A) {

		var defaultLanguageId = themeDisplay.getLanguageId();

		var availableLanguages = Liferay.Language.available;

		var availableLanguageIds = A.Array.dedupe(
			[ defaultLanguageId ].concat(A.Object.keys(availableLanguages))
		);

		var InputLocalized = A.Component.create(
			{
				NAME: 'input-localized',

				ATTRS: {
					animateClass: {
						value: 'highlight-animation'
					},

					inputNamespace: {},

					inputPlaceholder: {
						setter: A.one
					},

					selected: {
						valueFn: function() {
							return A.Array.indexOf(availableLanguageIds, defaultLanguageId);
						}
					},

					items: {
						value: availableLanguageIds
					}
				},

				EXTENDS: A.Palette,

				prototype: {
					BOUNDING_TEMPLATE: '<span />',

					INPUT_HIDDEN_TEMPLATE: '<input id="{inputNamespace}{value}" name="{inputNamespace}{value}" type="hidden" value="" />',

					ITEM_TEMPLATE:  '<td class="aui-palette-item {selectedClassName}" data-column={column} data-index={index} data-row={row} data-value="{value}">' +
										'<a href="" class="aui-palette-item-inner" onclick="return false;">' +
											'<img class="lfr-input-localized-flag" data-languageId="{value}" src="' + themeDisplay.getPathThemeImages() + '/language/{value}.png" />' +
										'</a>' +
									'</td>',

					TOOLTIP_TEMPLATE:   '<div class="aui-tooltip aui-top">' +
											'<div class="aui-tooltip-arrow"></div>' +
											'<div class="aui-tooltip-inner"></div>' +
										'</div>',

					_animating: null,
					_flags: null,
					_tooltip: null,

					initializer: function() {
						var instance = this;

						var inputPlaceholder = instance.get('inputPlaceholder');

						A.after(instance._afterRenderUI, instance, 'renderUI');

						instance.on(
							{
								enter: instance._onMouseEnterFlag,
								focusedChange: instance._onFocusedChange,
								leave: instance._onMouseLeaveFlag,
								select: instance._onSelectFlag
							}
						);

						inputPlaceholder.on('input', A.debounce(instance._onInputValueChange, 100), instance);
					},

					getSelectedLanguageId: function() {
						var instance = this;

						var items = instance.get('items');
						var selected = instance.get('selected');

						return items[selected];
					},

					hideFlags: function() {
						var instance = this;

						instance._flags.hide();
					},

					hideTooltip: function() {
						var instance = this;

						var tooltip = instance._fetchOrCreateTooltip();

						tooltip.setStyle('opacity', 0);
					},

					showFlags: function() {
						var instance = this;

						instance._flags.show();
					},

					showTooltip: function(alignNode, content) {
						var instance = this;

						var tooltip = instance._fetchOrCreateTooltip();

						tooltip.one('.aui-tooltip-inner').html(content);

						var nodeRegion = alignNode.get('region');
						var tooltipRegion = tooltip.get('region');

						tooltip.setStyle('opacity', 0.7);

						tooltip.setXY([
							nodeRegion.left + nodeRegion.width/2 - tooltipRegion.width/2,
							nodeRegion.top - tooltipRegion.height
						]);
					},

					_afterRenderUI: function() {
						var instance = this;

						instance._flags = instance.get('boundingBox').one('.aui-palette-container');

						instance.hideFlags();
					},

					_animate: function(input) {
						var instance = this;

						var animateClass = instance.get('animateClass');

						if (!animateClass) {
							return;
						}

						input.removeClass(animateClass);
						clearTimeout(instance._animating);

						setTimeout(
							function() {
								input.addClass(animateClass).focus();
							},
							0
						);

						instance._animating = setTimeout(
							function() {
								input.removeClass(animateClass);
								clearTimeout(instance._animating);
							},
							700
						);
					},

					_clearFormValidator: function(input) {
						var instance = this;

						var form = input.get('form');

						var liferayForm = Liferay.Form.get(form.attr('id'));

						if (liferayForm) {
							var validator = liferayForm.formValidator;

							if (A.instanceOf(validator, A.FormValidator)) {
								validator.resetAllFields();
							}
						}
					},

					_fetchOrCreateInputLanguage: function(languageId) {
						var instance = this;

						var boundingBox = instance.get('boundingBox');

						var inputNamespace = instance.get('inputNamespace');

						var inputLanguage = boundingBox.one('#' + inputNamespace + languageId);

						if (!inputLanguage) {
							inputLanguage = A.Node.create(
								A.Lang.sub(
									instance.INPUT_HIDDEN_TEMPLATE,
									{
										inputNamespace: inputNamespace,
										value: languageId
									}
								)
							);

							boundingBox.append(inputLanguage);
						}

						return inputLanguage;
					},

					_fetchOrCreateTooltip: function() {
						var instance = this;

						var tooltip = instance._tooltip;

						if (!tooltip) {
							tooltip = instance._tooltip = A.Node.create(instance.TOOLTIP_TEMPLATE).appendTo('body');
						}

						return tooltip;
					},

					_onFocusedChange: function(event) {
						var instance = this;

						if (event.newVal) {
							instance.showFlags();
						}
						else {
							instance.hideFlags();
						}
					},

					_onInputValueChange: function(event) {
						var instance = this;

						var selectedLanguageId = instance.getSelectedLanguageId();

						var inputLanguage = instance._fetchOrCreateInputLanguage(selectedLanguageId);

						instance.showFlags();

						inputLanguage.val(event.currentTarget.val());
					},

					_onMouseEnterFlag: function(event) {
						var instance = this;

						instance.showTooltip(event.item, availableLanguages[event.value]);
					},

					_onMouseLeaveFlag: function(event) {
						var instance = this;

						instance.hideTooltip();
					},

					_onSelectFlag: function(event) {
						var instance = this;

						if (event.domEvent) {
							return;
						}

						var inputPlaceholder = instance.get('inputPlaceholder');

						var inputLanguage = instance._fetchOrCreateInputLanguage(event.value);

						var defaultInputLanguage = instance._fetchOrCreateInputLanguage(defaultLanguageId);

						inputPlaceholder.val(inputLanguage.val());
						inputPlaceholder.attr('placeholder', defaultInputLanguage.val());

						instance._animate(inputPlaceholder);
						instance._clearFormValidator(inputPlaceholder);
					}
				},

				_handleDoc: null,
				_registered: {},

				register: function(id, config) {
					var instance = this;

					InputLocalized._registered[id] = config;

					if (!InputLocalized._handleDoc) {
						InputLocalized._handleDoc = A.getDoc().delegate(['focus', 'input'], InputLocalized._onInputUserInteraction, '.language-value');
					}
				},

				_onInputUserInteraction: function(event) {
					var instance = this;

					var input = event.currentTarget;
					var inputId = input.attr('id');
					var config = InputLocalized._registered[inputId];

					if (config) {
						var inputLocalized = new InputLocalized(config).render();

						inputLocalized._onDocFocus(event);

						delete InputLocalized._registered[inputId];
					}
				}
			}
		);

		Liferay.InputLocalized = InputLocalized;
	},
	'',
	{
		requires: ['aui-base', 'aui-component', 'aui-event-input', 'aui-palette', 'liferay-available-languages']
	}
);