'use strict';

class Favicon {
	constructor(framesPaths) {
		this.framesPaths = framesPaths;
		this.head = document.getElementsByTagName('head')[0];
		this.imagesPath = themeDisplay.getPathThemeImages();
	}

	changeFavicon(iconURL) {
		if (iconURL && iconURL !== '') {
			var newLink = document.createElement("link");

			newLink.type = "image/x-icon";
			newLink.rel = "icon";
			newLink.href = iconURL;

			this.removeExistingFavicons();

			this.head.appendChild(newLink);
		}
	}

	getLink() {
		var link = this.head.getElementsByTagName('link');

		for (var l = link.length, i = (l - 1); i >= 0; i--) {
			if ((/(^|\s)icon(\s|$)/i).test(link[i].getAttribute('rel'))) {
				return link[i];
			}
		}

		return false;
	}

	preloadImages() {
		for (var i = 1; i < this.framesPaths.length; i++) {
			(new Image()).src = this.imagesPath + this.framesPaths[i];
		}
	}

	removeExistingFavicons() {
		var links = this.head.getElementsByTagName("link");

		for (var i = 0; i < links.length; i++) {
		    if (/\bicon\b/i.test(links[i].getAttribute("rel"))) {
		        this.head.removeChild(links[i]);
		    }
		}
	}

	startAnimation() {
		var instance = this;
		var framesPaths = instance.framesPaths;

		if (framesPaths != null && framesPaths.length) {
			instance.existingLink = instance.getLink();
			instance.iconIndex = 0;

			if (instance.interval != null) {
				clearInterval(instance.interval);
			}

			instance.interval = setInterval(function() {
				instance.changeFavicon(instance.imagesPath + framesPaths[instance.iconIndex]);

				instance.iconIndex++;

				if (instance.iconIndex == framesPaths.length) {
					instance.iconIndex = 0;
				}
			}, 75);
		}
	}

	stopAnimation() {
		clearInterval(this.interval);
		this.interval = null;

		this.removeExistingFavicons();

		if (this.existingLink) {
			this.head.appendChild(this.existingLink);
		}
	}
}

export default Favicon;