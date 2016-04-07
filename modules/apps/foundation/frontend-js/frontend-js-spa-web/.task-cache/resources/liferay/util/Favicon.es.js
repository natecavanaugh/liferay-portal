define("frontend-js-spa-web@1.0.6/liferay/util/Favicon.es", ['exports'], function (exports) {
	'use strict';

	Object.defineProperty(exports, "__esModule", {
		value: true
	});

	function _classCallCheck(instance, Constructor) {
		if (!(instance instanceof Constructor)) {
			throw new TypeError("Cannot call a class as a function");
		}
	}

	var Favicon = function () {
		function Favicon(framesPaths) {
			_classCallCheck(this, Favicon);

			this.framesPaths = framesPaths;
			this.head = document.getElementsByTagName('head')[0];
			this.imagesPath = themeDisplay.getPathThemeImages();
		}

		Favicon.prototype.changeFavicon = function changeFavicon(iconURL) {
			if (iconURL && iconURL !== '') {
				var newLink = document.createElement("link");

				newLink.type = "image/x-icon";
				newLink.rel = "icon";
				newLink.href = iconURL;

				this.removeExistingFavicons();

				this.head.appendChild(newLink);
			}
		};

		Favicon.prototype.getLink = function getLink() {
			var link = this.head.getElementsByTagName('link');

			for (var l = link.length, i = l - 1; i >= 0; i--) {
				if (/(^|\s)icon(\s|$)/i.test(link[i].getAttribute('rel'))) {
					return link[i];
				}
			}

			return false;
		};

		Favicon.prototype.preloadImages = function preloadImages() {
			for (var i = 1; i < this.framesPaths.length; i++) {
				new Image().src = this.imagesPath + this.framesPaths[i];
			}
		};

		Favicon.prototype.removeExistingFavicons = function removeExistingFavicons() {
			var links = this.head.getElementsByTagName("link");

			for (var i = 0; i < links.length; i++) {
				if (/\bicon\b/i.test(links[i].getAttribute("rel"))) {
					this.head.removeChild(links[i]);
				}
			}
		};

		Favicon.prototype.startAnimation = function startAnimation() {
			var instance = this;
			var framesPaths = instance.framesPaths;

			if (framesPaths != null && framesPaths.length) {
				instance.existingLink = instance.getLink();
				instance.iconIndex = 0;

				if (instance.interval != null) {
					clearInterval(instance.interval);
				}

				instance.interval = setInterval(function () {
					instance.changeFavicon(instance.imagesPath + framesPaths[instance.iconIndex]);

					instance.iconIndex++;

					if (instance.iconIndex == framesPaths.length) {
						instance.iconIndex = 0;
					}
				}, 75);
			}
		};

		Favicon.prototype.stopAnimation = function stopAnimation() {
			clearInterval(this.interval);
			this.interval = null;

			this.removeExistingFavicons();

			if (this.existingLink) {
				this.head.appendChild(this.existingLink);
			}
		};

		return Favicon;
	}();

	exports.default = Favicon;
});
//# sourceMappingURL=Favicon.es.js.map