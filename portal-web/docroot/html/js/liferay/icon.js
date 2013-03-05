AUI.add(
	'liferay-icon',
	function(A) {
		var Icon = {
			register: function(config) {
				var instance = this;

				var icon = document.getElementById(config.id);

				if (icon) {
					var forcePost = config.forcePost;
					var srcHover = config.srcHover;

					if (forcePost || srcHover) {
						icon = A.one(icon);

						if (forcePost) {
							icon.on('click', instance._onClick, instance);
						}

						if (srcHover) {
							var src = config.src;

							instance._onMouseOver = A.rbind(instance._onMouseHover, instance, srcHover);
							instance._onMouseOut = A.rbind(instance._onMouseHover, instance, src);

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