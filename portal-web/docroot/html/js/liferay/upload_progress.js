function UploadProgress(config) {
	var instance = this;

	instance._config = config;

	instance.animateBar = UploadProgress_animateBar;
	instance.hideProgress = UploadProgress_hideProgress;
	instance.sendRedirect = UploadProgress_sendRedirect;
	instance.startProgress = UploadProgress_startProgress;
	instance.updateBar = UploadProgress_updateBar;
	instance.updateProgress = UploadProgress_updateProgress;
}

function UploadProgress_animateBar(percent) {
	var instance = this;

	instance._progressBar.set('value', percent);
}

function UploadProgress_hideProgress() {
	var instance = this;

	if (instance._progressBar) {
		instance._progressBar.destroy();

		instance._progressBar = null;
	}
}

function UploadProgress_sendRedirect() {
	var instance = this;

	window.location = decodeURIComponent(instance._config.redirect);
}

function UploadProgress_startProgress() {
	var instance = this;

	var A = AUI();

	var config = instance._config;

	var uploadProgressId = config.id;

	if (!instance._progressBar) {
		instance._progressBar = new A.ProgressBar(
			{
				boundingBox: '#' + uploadProgressId + '-bar-div',
				height: config.height,
				label: config.message,
				on: {
					complete: function(event) {
						var instance = this;

						instance.set('label', Liferay.Language.get('done'));
					},
					valueChange: function(event) {
						var instance = this;

						instance.set('label', event.newVal + '%');
					}
				}
			}
		).render();

		instance.animateBar(0);

		setTimeout(
			function() {
				instance.updateProgress();
			},
			1000
		);
	}
}

function UploadProgress_updateBar(percent, filename) {
	var instance = this;

	instance.currentPercent = percent;

	instance._progressBar.set('value', percent);
}

function UploadProgress_updateProgress() {
	var instance = this;

	var uploadProgressId = instance._config.id;

	var uploadProgressPoller = document.getElementById(uploadProgressId + '-poller');

	uploadProgressPoller.src = themeDisplay.getPathMain() + '/portal/upload_progress_poller?uploadProgressId=' + uploadProgressId;
}