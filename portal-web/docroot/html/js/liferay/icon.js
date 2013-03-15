AUI.add(
	'liferay-icon',
	function(A) {
		var Icon = {
			register: function(config) {
				var instance = this;

				var forcePost = config.forcePost;
				var srcHover = config.srcHover;

				if (forcePost || srcHover) {
					var icon = document.getElementById(config.id);

					if (icon) {
						icon = A.one(icon);

						if (forcePost) {
							icon.on('click', instance._onClick, instance);
						}

						if (srcHover) {
							var onMouseHover = instance._onMouseHover;

							instance._onMouseOver = A.rbind(onMouseHover, instance, srcHover);
							instance._onMouseOut = A.rbind(onMouseHover, instance, config.src);

							icon.hover(instance._onMouseOver, instance._onMouseOut);
						}
					}
				}
			},

			_onClick: function(event) {
				var instance = this;

				Liferay.Util.forcePost(event.currentTarget);

				event.preventDefault();
			},

			_onMouseHover: function(event, src) {
				var instance = this;

				var img = event.currentTarget.one('img');

				if (img) {
					img.attr('src', src);
				}
			}
		};

		Liferay.Icon = Icon;
	},
	'',
	{
		requires: ['aui-base']
	}
);