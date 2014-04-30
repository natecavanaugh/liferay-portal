/**
 * Creates a Liferay Icon module and adds it to AUI
 *
 * @module liferay-icon
 */

AUI.add(
	'liferay-icon',
	function(A) {
		var Icon = {
			register: function(config) {
				var instance = this;

				var icon = A.one('#' + config.id);

				var forcePost = config.forcePost;
				var src = config.src;
				var srcHover = config.srcHover;
				var useDialog = config.useDialog;

				if (icon) {
					if (srcHover) {
						instance._onMouseOver = A.rbind('_onMouseHover', instance, srcHover);
						instance._onMouseOut = A.rbind('_onMouseHover', instance, src);

						icon.hover(instance._onMouseOver, instance._onMouseOut);
					}

					if (useDialog) {
						icon.on('click', instance._useDialog, instance);
					}
					else if (forcePost) {
						icon.on('click', instance._forcePost, instance);
					}
				}
			},
			
			/**
         		 * Triggers when the mouse clicks the icon and forces
			 * it to post if the icon is a link. 
         		 *
         		 * @method _forcePost
         		 * @param event
         		 * @protected
         		 */
			_forcePost: function(event) {
				var instance = this;

				Liferay.Util.forcePost(event.currentTarget);

				event.preventDefault();
			},
                         
                       /**
         		 * Triggers when the mouse hovers over top of the icon.
         		 *
         		 * @method _onMouseHover
         		 * @param event
			 * @param src
         		 * @protected
         		 */
			_onMouseHover: function(event, src) {
				var instance = this;

				var img = event.currentTarget.one('img');

				if (img) {
					img.attr('src', src);
				}
			},
			
			/**
         		 * Triggers when the mouse hovers over top of the icon.
         		 *
         		 * @method _useDialog
         		 * @param event
         		 * @protected
         		 */
			_useDialog: function(event) {
				Liferay.Util.openInDialog(event, event.currentTarget);
			}
		};

		Liferay.Icon = Icon;
	},
	'',
	{
		requires: ['aui-base', 'liferay-util-window']
	}
);