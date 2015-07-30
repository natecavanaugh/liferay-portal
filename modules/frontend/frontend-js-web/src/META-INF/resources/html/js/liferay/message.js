AUI.add(
	'liferay-message',
	function(A) {

		/**
 		 * The Message Component.
 		 *
		 * @module liferay-message
		 */
		var Lang = A.Lang;

		var EVENT_DATA_DISMISS_ALL = {
			categoryVisible: false
		};

		var EVENT_HOVER = ['mouseenter', 'mouseleave'];

		var NAME = 'liferaymessage';

		var REGEX_CSS_TYPE = A.DOM._getRegExp('\\blfr-message-(alert|error|help|info|success)\\b', 'g');

		var TPL_HIDE_NOTICES = '<button type="button" class="close">&#x00D7;</button>';

		/**
		 * A base class for `A.Message`.
	   	 *
		 * @class A.Message
		 * @extends Base
		 * @param {Object} config Object literal specifying
		 * widget configuration properties.
		 * @constructor
		 */
		var Message = A.Component.create(
			{
				/**
				 * static property used to define the default attribute
				 * configuration for the `A.Message`.
				 *
				 * @property ATTRS
				 * @type Object 
				 * @static
				 */
				ATTRS: {
    
					/**
					 * Creates a close button to hide all notices. 
					 *
					 * @attribute closeButton
					 * @type Object
					 * @return A.Node.create(TPL_HIDE_NOTICES)
					 */
					closeButton: {
						valueFn: function() {
							return A.Node.create(TPL_HIDE_NOTICES);
						}
					},

					/**
					 * If true, the UI is rendered for the message.
					 *
					 * @attribute dismissble
					 * @type Boolean
					 * @default true
					 */
					dismissible: {
						value: true
					},

					/**
					 * Creates two links to hide all the notices for the message. 
					 *
					 * @attribute hideAllNotices
					 * @type Object
					 * @return A.Node.create('<a href="javascript:;"><small> …
					 */
					hideAllNotices: {
						valueFn: function() {
							var instance = this;

							return A.Node.create('<a href="javascript:;"><small>' + instance.get('strings.dismissAll') || Liferay.Language.get('disable-this-note-for-all-portlets') + '</small></a>');
						}
					},

					/** If true, the UI is rendered for the message.
					 * 
					 *
					 * @attribute persistenceCategory
					 * @type String
					 * @default ''
					 */
					persistenceCategory: {
						value: ''
					},

					/**
					 * if true, a sessionData Object is created for the message.
					 *
					 * @attribute persistent
					 * @type Boolean
					 * @default true
					 */
					persistent: {
						value: true
					},

					/**
					 * Trigger.
					 *
					 * @attribute trigger 
					 */
					trigger: {
						setter: A.one
					},

					/**
					 * Defines the type of message.
					 *
					 * @attribute type
					 * @type String
					 * @default 'info'
					 */
					type: {
						value: 'info'
					}
				},

				/**
				 * The prefix for all CSS classes.
				 *
				 * @property CSS_PREFIX
				 * @type String 
				 * @static
				 */
				CSS_PREFIX: 'lfr-message',

				/**
				 * Object hash, defining how attribute values have to be parsed from markup.
				 *
				 * @property HTML_PARSER
				 * @type Object 
				 * @static
				 */
				HTML_PARSER: {
					closeButton: '.close',
					hideAllNotices: '.btn-link'
				},

				NAME: NAME,

				/**
				 * Static property used to define the UI attributes.
				 *
				 * @property UI_ATTRS
				 * @type ARRAY 
				 * @static
				 */
				UI_ATTRS: ['dismissible', 'persistent', 'type'],

				prototype: {

					/**
					 * Construction logic executed during `A.Message` instantiation.
					 * Lifecycle.
					 *
					 * @method initializer
					 * @protected
					 */
					initializer: function() {
						var instance = this;

						instance._boundingBox = instance.get('boundingBox');
						instance._contentBox = instance.get('contentBox');

						instance._cssDismissible = instance.getClassName('dismissible');
						instance._cssPersistent = instance.getClassName('persistent');
					},

					/**
					 * Render the `A.Message` component instance. Lifecycle.
					 *
					 * @method renderUI
					 * @protected
					 */
					renderUI: function() {
						var instance = this;

						var dismissible = instance.get('dismissible');

						if (dismissible) {
							var trigger = instance.get('trigger');

							instance._trigger = trigger;

							var closeButton = instance.get('closeButton');

							if (instance.get('persistenceCategory')) {
								var hideAllNotices = instance.get('hideAllNotices');

								instance._contentBox.append(hideAllNotices);

								instance._contentBox.addClass('dismiss-all-notes');

								instance._hideAllNotices = hideAllNotices;
							}

							instance._closeButton = closeButton;

							instance._contentBox.prepend(closeButton);
						}

						instance._dismissible = dismissible;
					},

					/**
					 * Bind events on the UI. Lifecycle.
					 *
					 * @method bindUI
					 * @protected
					 */
					bindUI: function() {
						var instance = this;

						if (instance._dismissible) {
							instance.after('visibleChange', instance._afterVisibleChange);

							var closeButton = instance._closeButton;

							if (closeButton) {
								closeButton.on('click', instance._onCloseButtonClick, instance);
							}

							var trigger = instance._trigger;

							if (trigger) {
								trigger.on('click', instance._onTriggerClick, instance);
							}

							var hideAllNotices = instance._hideAllNotices;

							if (hideAllNotices) {
								hideAllNotices.on('click', instance._onHideAllClick, instance);
							}
						}
					},

				/**
	         		 * Fires after 'visibleChange' attribute value change.
	         		 * 
	         		 * @method _afterVisibleChange
	         		 * @param event
	         		 * @protected
	         		 */
					_afterVisibleChange: function(event) {
						var instance = this;

						var messageVisible = event.newVal;

						instance._contentBox.toggle(messageVisible);

						instance.get('trigger').toggle(!messageVisible);

						if (instance.get('persistent')) {
							var sessionData = {};

							if (themeDisplay.isImpersonated()) {
								sessionData.doAsUserId = themeDisplay.getDoAsUserIdEncoded();
							}

							if (event.categoryVisible === false) {
								sessionData[instance.get('persistenceCategory')] = true;
							}

							sessionData[instance.get('id')] = messageVisible;

							Liferay.Store(sessionData);
						}
					},

				/**
	         		 * Triggers when the close button is clicked.
	         		 * Hides the message.
	         		 *
	         		 * @method _onCloseButtonClick
	         		 * @param event
	         		 * @protected
	         		 */
					_onCloseButtonClick: function(event) {
						var instance = this;

						instance.hide();
					},

				/**
	         		 * Triggers when hide all is clicked. 
	         		 * Sets the visibility to false.
	         		 *
	         		 * @method _onHideAllClick
	         		 * @param event
	         		 * @protected
	         		 */
					_onHideAllClick: function(event) {
						var instance = this;

						instance.set('visible', false, EVENT_DATA_DISMISS_ALL);
					},

					
				/**
	         		 * Shows the message if CSS class  
				 * 'dismissible' exists. 
	         		 *
	         		 * @method _onTriggerClick
	         		 * @param event
	         		 * @protected
	         		 */
					_onTriggerClick: function(event) {
						var instance = this;

						instance.show();
					},

				/**
	         		 * Sets the UI boundingBox CSS class name to  
				 * 'dismissible.' 
	         		 *
	         		 * @method _uiSetDismissible
	         		 * @param value
	         		 * @protected
	         		 */
					_uiSetDismissible: function(value) {
						var instance = this;

						instance._boundingBox.toggleClass(instance._cssDismissible, value);
					},

				/**
	         		 * sets the UI boundingBox CSS class name to
				 * 'persistent.' 
	         		 *
	         		 * @method _uiSetPersistent
	         		 * @param value
	         		 * @protected
	         		 */
					_uiSetPersistent: function(value) {
						var instance = this;

						instance._boundingBox.toggleClass(instance._cssPersistent, value);
					},

			        /**
	         		 * Sets the CSS class for the content box
				 * based on the type of message. 
	         		 *
	         		 * @method _uiSetType
	         		 * @param value
	         		 * @protected
	         		 */
					_uiSetType: function(value) {
						var instance = this;

						var contentBox = instance._contentBox;

						var cssClass = contentBox.attr('class').replace(REGEX_CSS_TYPE, '');

						cssClass += ' ' + instance.getClassName(value);

						contentBox.attr('class', cssClass);
					}
				}
			}
		);

		Liferay.Message = Message;
	},
	'',
	{
		requires: ['aui-base', 'liferay-store']
	}
);