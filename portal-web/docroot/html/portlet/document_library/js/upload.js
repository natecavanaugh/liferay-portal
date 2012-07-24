AUI.add(
	'document-library-upload',
	function(A) {
		var Lang = A.Lang;
		var UA = A.UA;

		var BOUNDING_BOX = 'boundingBox';

		var CONTENT_BOX = 'contentBox';

		var CSS_ACTIVE_AREA = 'active-area';

		var CSS_DOCUMENT_DISPLAY_STYLE = '.document-display-style';

		var DATA_FOLDER_ID = 'data-folder-id';

		var UPLOAD_MASK = 'lfr-upload-mask';

		DocumentLibraryUpload = function() {};

		DocumentLibraryUpload.NAME = 'documentlibraryupload';

		DocumentLibraryUpload.prototype = {
			destructor: function() {
				var instance = this;

				A.Array.invoke(instance._subscriptions, 'detach');

				if (instance._dataset) {
					instance._dataset.destroy();
				}

				if (instance._overlayManager) {
					instance._overlayManager.destroy();
				}

				if (instance._uploader) {
					instance._uploader.destroy();
				}
			},

			_addEntry: function(file) {
				var instance = this;

				var displayStyle = instance._appViewSelect._getDisplayStyle();

				var entriesContainer = instance._entriesContainer;

				var emptyMessage = entriesContainer.one('.entries-empty');

				var entryNode;

				var title = file.name;

				if (displayStyle === 'icon') {
					entryNode = instance._invisibleIconEntry.clone();
				}
				else if (displayStyle === 'descriptive') {
					entryNode = instance._invisibleDescriptiveEntry.clone();
				}
				else {
					instance._createEntryListRow(file);

					entryNode = entriesContainer.one('.lfr-search-container');
				}

				entryNode.attr('data-title', title);

				var entryNodeId = A.guid();

				entryNode.set('id', entryNodeId);

				entryNode.show();

				if (emptyMessage) {
					emptyMessage.hide();
				}

				var errorMessage = file.errorMessage;

				if (displayStyle == 'list') {
					if (errorMessage) {
						var icon = entryNode.one('img');

						icon.set('src', '/html/themes/classic/images/common/close.png');
					}

					file.row = entryNode;
				}
				else {
					entriesContainer.append(entryNode);

					var entryDocumentLink = entryNode.one('.document-link');

					entryDocumentLink.set('title', title);

					if (errorMessage) {
						instance._displayEntryError(entryDocumentLink);
					}

					var entryTitle = entryDocumentLink.one('.entry-title');

					entryTitle.setContent(title);

					file.entryDocumentLink = entryDocumentLink;
				}

				if (errorMessage) {
					new A.Tooltip(
						{
							bodyContent: errorMessage,
							constrain: true,
							trigger: '#' + entryNodeId
						}
					).render();
				}
				else {
					var overlay = instance._createOverlay(entryNode);

					instance._overlayManager.register(overlay);

					overlay.show();

					var progressBar = instance._createProgressBar();

					file.overlay = overlay;

					file.progressBar = progressBar;
				}
			},

			_attachSubscriptions: function(event) {
				var instance = this;

				var folderEntry = event.folderEntry;

				var subscriptions = instance._subscriptions;

				var uploader = instance._uploader;

				if (folderEntry) {
					var showFolderUploadStarting = uploader.on('uploadstart', instance._showFolderUploadStarting, instance);
					var showFolderUploadProgress = uploader.on('totaluploadprogress', instance._showFolderUploadProgress, instance);

					subscriptions.push(showFolderUploadStarting, showFolderUploadProgress);
				}
				else {
					var showFileUploadStarting = uploader.after('fileuploadstart', instance._showFileUploadStarting, instance);
					var showFileUploadProgress = uploader.on('uploadprogress', instance._showFileUploadProgress, instance);
					var showFileUploadComplete = uploader.on('uploadcomplete', instance._showFileUploadComplete, instance);

					subscriptions.push(showFileUploadStarting, showFileUploadProgress, showFileUploadComplete);
				}
			},

			_combineFileLists: function(fileList, queuedFiles) {
				var combinedList = A.each(
					queuedFiles,
					function(item, index, collection) {
						fileList.push(item);
					}
				);

				return fileList;
			},

			_containsInvalidCharacters: function(fileName) {
				var instance = this;

				var invalidFileNameCharacters = [
					'[',
					']'
				];

				A.Array.map(
					invalidFileNameCharacters,
					function(item, index, collection) {
						if (Lang.isString(fileName)) {
							var invalidCharacterFound = fileName.indexOf(item);

							if (invalidCharacterFound) {
								return true;
							}
						}
					}
				);

				return false;
			},

			_createEntries: function(files) {
				var instance = this;

				var validFiles = files.validFiles;

				A.Array.map(
					validFiles,
					function(item, index, collection) {
						instance._addEntry(item);
					}
				);

				var invalidFiles = files.invalidFiles;

				A.Array.map(
					invalidFiles,
					function(item, index, collection) {
						instance._addEntry(item);
					}
				);
			},

			_createEntryListRow: function(file) {
				var instance = this;

				var searchContainer = A.one('.aui-searchcontainer');

				var templateRow = searchContainer.one('tr.lfr-template');

				var row = templateRow.clone();

				var tableBody = searchContainer.one('tbody');

				tableBody.append(row);

				row.removeClass('lfr-template');

				row.addClass('document-display-style');

				var entriesCount = instance._getEntriesCount();

				instance._entriesCount++;

				var alternate = entriesCount % 2;

				if (alternate) {
					row.addClass('alt portlet-section-alternate-hover');
				}
				else {
					row.addClass('portlet-section-hover');
				}

				row.attr('data-draggable', true);

				var columnNames = instance._config.columnNames;

				var td = row.one('td');

				A.each(
					columnNames,
					function(item, index, collection) {
						td = td.next();

						if (!td) {
							td = A.Node.create('<td></td>');

							row.append(td);
						}

						var columnName = item;

						var rowNumber = entriesCount + 1;

						instance.entriesCount = rowNumber;

						if (item === 'name') {
							columnName = 'title';

							var ns_struts_action = instance.ns('struts_action=%2Fdocument_library%2Fview_file_entry');

							var anchor = A.Node.create('<a class="taglib-icon" href="' + instance._config.viewFileEntryUrl + '" > ' + file.name + '</a>');

							var icon = A.Node.create('<img alt="" class="icon" id="' + A.guid() + '" src="/html/themes/classic/images/file_system/small/page.png" />')

							td.append(icon);

							td.append(anchor);
						}
						else if (item ==='size') {
							td.setContent('--');
						}
						else if (item === 'downloads') {
							td.setContent('--');
						}
						else if (item === 'action') {
							columnName = rowNumber;

							td.set('id', instance.ns('ocerSearchContainer_col-5_row-1'));

							td.addClass('last');
						}

						td.addClass('align-left col-' + columnName + ' valign-middle');

						td.attr('colspan', 1);
						td.attr('headers', instance.ns('ocerSearchContainer_col-' + columnName));

						td.set('id', instance.ns('ocerSearchContainer_col-' + columnName + '_row-1'));
					}
				);

				return row;
			},

			_createNavigationOverlays: function() {
				var instance = this;

				var container = instance._entriesContainer;

				var columnContent = container.ancestor('.aui-column-content');

				var headerRow = columnContent.one('.lfr-header-row');

				var headerRowOverlay = instance._createOverlay(headerRow, UPLOAD_MASK);

				var documentEntriesPaginator = A.one('.document-entries-paginator');

				var documentEntriesPaginatorOverlay = instance._createOverlay(documentEntriesPaginator, UPLOAD_MASK);

				var navigationPane = instance.byId('listViewContainer');

				var navigationPaneOverlay = instance._createOverlay(navigationPane, UPLOAD_MASK);

				var navigationOverlays = [];

				navigationOverlays.push(headerRowOverlay, documentEntriesPaginatorOverlay, navigationPaneOverlay);

				var uploader = instance._uploader;

				uploader.before(
					'uploadstart',
					function(event) {
						A.Array.invoke(navigationOverlays, 'show');
					}
				);

				uploader.after(
					'alluploadscomplete',
					function(event) {
						var dataset = instance._dataset;

						if (!dataset.length) {
							A.Array.invoke(navigationOverlays, 'hide');
						}
					}
				);
			},

			_createOverlay: function(target, cssClass) {
				var instance = this;

				var background = null;

				if (cssClass) {
					background = '#FFF';
				}

				var overlay = new A.OverlayMask(
					{
						background: background,
						cssClass: cssClass || null,
						target: target
					}
				).render();

				return overlay;
			},

			_createProgressBar: function(height) {
				return new A.ProgressBar(
					{
						height: height || 25,
						on: {
							complete: function(event) {
								this.set('label', 'complete!');
							},
							valueChange: function(event) {
								this.set('label', event.newVal + '%');
							}
						},
						width: 200
					}
				);
			},

			_detachSubscriptions: function() {
				var instance = this;

				var subscriptions = instance._subscriptions;

				A.Array.invoke(subscriptions, 'detach');
			},

			_displayEntryError: function(entryNode) {
				var instance = this;

				var entryThumbnailImage = entryNode.one('img');

				var entryErrorSrc = themeDisplay.getPathContext() + '/html/themes/classic/images/file_system/large/default_error.png';

				entryThumbnailImage.set('src', entryErrorSrc);
			},

			_getDragDropFiles: function(files) {
				var instance = this;

				var fileList = [];

				A.each(
					files,
					function (item, index, collection) {
						fileList.push(new A.FileHTML5(item));
					}
				);

				return fileList;
			},

			_getEntriesCount: function() {
				var instance = this;

				var entriesCount = instance._entriesCount;

				if (!entriesCount) {
					instance._entriesCount = instance._config.entriesTotal;
				}

				return entriesCount;
			},

			_getFolderEntry: function(target) {
				var instance = this;

				var folderEntry;

				var dataFolder = target.ancestor('[data-folder="true"]');

				var overlayBoundingBox = target.ancestor('.aui-overlaymask');

				if (overlayBoundingBox) {
					folderEntry = overlayBoundingBox.folderEntry || null;
				}
				else if (dataFolder) {
					folderEntry = dataFolder;

					folderEntry.id = dataFolder.attr(DATA_FOLDER_ID);

					var displayIcon = target.ancestor(CSS_DOCUMENT_DISPLAY_STYLE);

					displayIcon.removeClass(CSS_ACTIVE_AREA);

					var folderEntryOverlay = folderEntry.overlay;

					if (folderEntryOverlay) {
						folderEntryOverlay.show();
					}
					else {
						var overlay = instance._createOverlay(displayIcon);

						overlay.show();

						var overlayBoundingBox = overlay.get(BOUNDING_BOX);

						overlayBoundingBox.folderEntry = folderEntry;

						overlay.set(BOUNDING_BOX, overlayBoundingBox);

						var progressBar = instance._createProgressBar();

						folderEntry.overlay = overlay;
						folderEntry.progressBar = progressBar;
					}
				}

				return folderEntry;
			},

			_getFolderURL: function(folderId) {
				var instance = this;

				return themeDisplay.getPathContext() + '/web/guest/home?p_auth=u8fDMAbA&p_p_id=20&p_p_lifecycle=1&p_p_state=normal&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&doAsUserId=' + themeDisplay.getDoAsUserIdEncoded() + '&_20_struts_action=%2Fdocument_library%2Fedit_file_entry&_20_cmd=add_dynamic&_20_repositoryId=' + themeDisplay.getScopeGroupId() + '&_20_folderId=' + folderId + '&ticketKey=96d78064-88d5-4de2-9930-3d2ea54d7f54&_20_groupPermissions=ADD_DISCUSSION&_20_guestPermissions=ADD_DISCUSSION&_20_groupPermissions=VIEW&_20_guestPermissions=VIEW&_20_inputPermissionsViewRole=Guest';
			},

			_getMediaThumbnail: function(fileName) {
				var instance = this;

				var extension = fileName.substring(fileName.length, fileName.lastIndexOf('.'));

				extension = extension.toLowerCase();

				if (extension === '.pdf') {
					return '/html/themes/classic/images/file_system/large/pdf.png';
				}

				var musicExtensions = ['.mp3', '.wav', '.m4a', '.wma', '.aac', '.mp4', '.auif', '.bwf', '.flac'];

				if (musicExtensions.indexOf(extension) > -1) {
					return '/html/themes/classic/images/file_system/large/music.png';
				}

				var imageExtensions = ['.jpg', '.jpeg', '.png', '.gif', '.tiff', '.bmp'];

				if (imageExtensions.indexOf(extension) > -1) {
					return '/documents/' + themeDisplay.getScopeGroupId() + '/' + instance.folderId + '/' + fileName;
				}

				var videoExtensions = ['.avi', '.flv', '.m4v', '.mov', '.mpeg', '.mpg', '.mpe', '.ogg', '.wmv'];

				if (videoExtensions.indexOf(extension) > -1) {
					return '/html/themes/classic/images/file_system/large/video.png';
				}

				var compressedExtensions = ['.gz', '.tar', '.zip','.tgz'];

				if (compressedExtensions.indexOf(extension) > -1) {
					return '/html/themes/classic/images/file_system/large/compressed.png';
				}

				return '/html/themes/classic/images/file_system/large/default.png'
			},

			_getValidFiles: function(data) {
				var instance = this;

				var invalidFiles = [];

				var validFiles = A.Array.filter(
					data,
					function (item, index, collection) {
						item.name = item.get('name');

						item.size = item.get('size') || 0;

						var maxFileSize = Math.floor(instance._maxFileSize.replace(/\D/g,''));

						var maxFileNameLength = 240;

						if (item.size > maxFileSize ) {
							invalidFiles.push(item);

							item.errorMessage = Lang.sub(instance._invalidFileSizeText, '240');

							return;
						}
						else if (item.name.length > maxFileNameLength || instance._containsInvalidCharacters(item.name)) {
							invalidFiles.push(item);

							item.errorMessage = Lang.sub(instance._invalidFileNameText, maxFileNameLength);

							return;
						}
						else if (!item.size) {
							invalidFiles.push(item);

							item.errorMessage = Lang.sub(instance._zeroByteFileText);

							return;
						}
						else {
							return item;
						}
					}
				);

				return {
					validFiles: validFiles,
					invalidFiles: invalidFiles
				}
			},

			_handleDragLeave: function(event) {
				var instance = this;

				instance._entriesContainer.removeClass('drag-highlight');
			},

			_handleDragOver: function(event) {
				var instance = this;

				var entriesContainer = instance._entriesContainer;

				entriesContainer.addClass('drag-highlight');

				var syncNotificationContent = A.one('#' + instance.ns('syncNotificationContent'));

				var syncNotificationAnchor = syncNotificationContent.one('a');

				syncNotificationAnchor.setContent('Drop files here or on any folder to upload.');

				try {
					event._event.dataTransfer.dropEffect = 'copy';
				}
				catch (err) {
				}

				event.stopPropagation();
				event.preventDefault();
			},

			_initCallbacks: function() {
				var instance = this;

				var entriesContainer = instance._entriesContainer;

				var docElement = document.documentElement;

				var docElementNode = A.one(docElement);

				var uploader = instance._uploader;

				A.getWin().on('beforeunload', instance._preventPageUnload, instance);

				docElementNode.on('dragover', instance._handleDragOver, instance);
				docElementNode.on('dragleave', instance._handleDragLeave, instance);

				docElementNode.delegate(
					'drop',
					function(event) {
						event.preventDefault();
						event.stopPropagation();

						if (event._event && event._event.dataTransfer) {
							var files = event._event.dataTransfer.files;

							event.fileList = instance._getDragDropFiles(files);

							instance._createNavigationOverlays();

							uploader.fire('fileselect', {_EVT: event});
						}
					},
					'body, .document-container, .aui-overlaymask, .aui-progressbar, [data-folder="true"]'
				);

				entriesContainer.delegate(
					'dragover',
					function(event) {
						var parentElement = event.target.ancestor(CSS_DOCUMENT_DISPLAY_STYLE);

						parentElement.addClass(CSS_ACTIVE_AREA);
					},
					'[data-folder="true"]'
				);

				entriesContainer.delegate(
					'dragleave',
					function(event) {
						var parentElement = event.target.ancestor(CSS_DOCUMENT_DISPLAY_STYLE);

						parentElement.removeClass(CSS_ACTIVE_AREA);
					},
					'[data-folder="true"]'
				);

				uploader.after('fileselect', instance._queueUpload, instance);

				uploader.on('alluploadscomplete', instance._onAllUploadsComplete, instance);

				uploader.after('alluploadscomplete', instance._startNextUpload, instance);

				Liferay.before(
					instance.ns('dataRequest'),
					function(event) {
						var uploaderIsUploading = instance._isUploading();

						if (uploaderIsUploading) {
							event.halt();
						}
					},
					instance
				);
			},

			_initDLUpload: function() {
				var instance = this;

				if (themeDisplay.isSignedIn()) {
					var uploader = instance._initUploader();

					if (uploader) {
						instance._dataset = new A.DataSet();
						instance._maxFileSize = '314572800 B';
						instance._overlayManager = new A.OverlayManager();
						instance._subscriptions = [];

						var documentContainer = A.one('.document-container');

						var invisibleDescriptiveEntry = documentContainer.one('#invisible_descriptive');
						var invisibleIconEntry = documentContainer.one('#invisible_icon');

						var documentContainerParent = documentContainer.ancestor();

						documentContainerParent.append(invisibleDescriptiveEntry);
						documentContainerParent.append(invisibleIconEntry);

						instance._entriesContainer = documentContainer;
						instance._invisibleDescriptiveEntry = invisibleDescriptiveEntry;
						instance._invisibleIconEntry = invisibleIconEntry;
						instance._uploader = uploader;

						instance._initCallbacks();

						instance._invalidFileNameText = Liferay.Language.get('please-enter-a-file-with-a-valid-file-name');
						instance._invalidFileSizeText = Liferay.Language.get('please-enter-a-file-with-a-valid-file-size-no-larger-than-x');
						instance._zeroByteFileText = Liferay.Language.get('the-file-contains-no-data-and-cannot-be-uploaded.-please-use-the-classic-uploader');
					}
				}
			},

			_initUploader: function() {
				var instance = this;

				var uploaderType = A.Uploader.TYPE;

				if (uploaderType && uploaderType === 'html5' && !UA.ios) {
					var folderId = instance.folderId;

					var uploadURL = instance._getFolderURL(folderId);

					var uploader = new A.Uploader(
						{
							appendNewFiles: false,
							fileFieldName: 'file',
							multipleFiles: true,
							simLimit: 2,
							swfURL: Liferay.Util.addParams('ts=' + Lang.now(), themeDisplay.getPathContext() + '/html/js/aui/uploader/assets/flashuploader.swf'),
							uploadURL: Liferay.Util.addParams('ts=' + Lang.now(), uploadURL)
						}
					);

					uploader.render(A.getBody());

					uploader.get('selectFilesButton').hide();

					return uploader;
				}
			},

			_isUploading: function() {
				var instance = this;

				var uploader = instance._uploader;

				var queue = uploader.queue;

				return (queue && queue._currentState && queue._currentState === 'uploading');
			},

			_onAllUploadsComplete: function(upload) {
				var instance = this;

				var dataset = instance._dataset;

				var currentUpload = dataset.get('first') || upload;

				var folderEntry = currentUpload.folderEntry;

				if (folderEntry) {
					var invalidFiles = currentUpload.invalidFiles;

					instance._updateFolderEntry(folderEntry, invalidFiles);
				}

				instance._detachSubscriptions();
			},

			_preventPageUnload: function(event) {
				var instance = this;

				if (instance._isUploading()) {
					event.preventDefault();
				}
			},

			_queueFilesToBottom: function(data) {
				var instance = this;

				var uploader = instance._uploader;

				var queue = uploader.queue;

				A.Array.map(
					data,
					function(item, index, collection) {
						queue.addToQueueBottom(item);
					}
				);
			},

			_queueUpload: function(event) {
				var instance = this;

				var originalEvent = event._EVT;

				var fileList = originalEvent.fileList;

				var files = instance._getValidFiles(fileList);

				var validFileList = files.validFiles;

				var validFileListLength = validFileList.length;

				var target = originalEvent.target;

				var folderEntry = instance._getFolderEntry(target);

				var folderId;

				var overlay = null;

				var progressBar = null;

				if (folderEntry) {
					folderId = folderEntry.id;

					overlay = folderEntry.overlay || null;

					progressBar = folderEntry.progressBar || null;
				}
				else {
					instance._createEntries(files);

					folderId = instance.folderId;
				}

				var dataset = instance._dataset;

				var uploader = instance._uploader;

				var datasetFolderEntry = dataset.item(folderId + '');

				var dataUploading = dataset.get('first') || null;

				var queue = uploader.queue;

				if (dataUploading && dataUploading.folderId === folderId) {
					A.Array.map(
						fileList,
						function(item, index, collection) {
							queue.addToQueueBottom(item);
						}
					);
				}
				else if (datasetFolderEntry && datasetFolderEntry.fileList) {
					var combinedFileLists = instance._combineFileLists(datasetFolderEntry.fileList, fileList);

					datasetFolderEntry.fileList = combinedFileLists;

					dataset.replace(folderId, datasetFolderEntry);
				}
				else {
					dataset.add(
						folderId,
						{
							displayView: instance._appViewSelect._getDisplayStyle(),
							fileList: validFileList,
							folderEntry: folderEntry,
							folderId: folderId,
							invalidFiles: files.invalidFiles,
							overlay: overlay,
							progressBar: progressBar
						}
					);
				}

				if (!queue && !!dataset.length) {
					var nextDatasetEntry = dataset.get('first');

					instance._startUpload(nextDatasetEntry);
				}
			},

			_showFileUploadComplete: function(event) {
				var instance = this;

				var file = event.file;

				var overlay = file.overlay;

				overlay.hide();

				try {
					var responseData = A.JSON.parse(event.data);

					var fileEntryId = responseData.fileEntryId;

					var displayStyle = instance._appViewSelect._getDisplayStyle();

					if (displayStyle !== 'list') {
						instance._updateIconLink(
							fileEntryId,
							{
								entryDocumentLink: file.entryDocumentLink,
								name: file.name
							}
						);
					}
					else {
						instance._updateRowLink(fileEntryId, file.row);
					}
				}
				catch(err) {
				}
			},

			_showFileUploadProgress: function(event) {
				var instance = this;

				var file = event.file;

				var progressBar = file.progressBar;

				progressBar.set('value', event.percentLoaded);
			},

			_showFileUploadStarting: function(event) {
				var instance = this;

				var file = event.file;

				var overlay = file.overlay;

				var progressBar = file.progressBar;

				var overlayContentBox = overlay.get(CONTENT_BOX);
				var overlayBoundingBox = overlay.get(BOUNDING_BOX);

				var progressBarBoundingBox = progressBar.get(BOUNDING_BOX);

				progressBar.render(overlayBoundingBox);

				progressBarBoundingBox.center(overlayContentBox);
			},

			_showFolderUploadProgress: function(event) {
				var instance = this;

				var dataset = instance._dataset;

				var dataUploading = dataset.get('first');

				var folderEntry = event.folderEntry || dataUploading;

				var progressBar = dataUploading.progressBar;

				var percentLoaded = Math.ceil(event.percentLoaded);

				progressBar.set('value', percentLoaded);
			},

			_showFolderUploadStarting: function(event) {
				var instance = this;

				var dataset = instance._dataset;

				var dataUploading = dataset.get('first');

				var overlay = dataUploading.overlay;

				var overlayBoundingBox = overlay.get(BOUNDING_BOX);

				var progressBar = dataUploading.progressBar;

				progressBar.render(overlayBoundingBox);

				var overlayContentBox = overlay.get(CONTENT_BOX);

				var progressBarBoundingBox = progressBar.get(BOUNDING_BOX);

				progressBarBoundingBox.center(overlayContentBox);
			},

			_startNextUpload: function(event) {
				var instance = this;

				var dataset = instance._dataset;

				dataset.removeAt(0);

				if (!!dataset.length) {
					var nextDataUpload = dataset.get('first');

					instance._startUpload(nextDataUpload);
				}
			},

			_startUpload: function(data) {
				var instance = this;

				instance._attachSubscriptions(data);

				var fileList = data.fileList;

				var fileListLength = fileList.length;

				var uploader = instance._uploader;

				if (!!fileListLength) {
					var folderId = data.folderId || instance.folderId;

					var url = instance._getFolderURL(folderId);

					uploader.uploadThese(fileList, url);
				}
				else {
					uploader.fire('alluploadscomplete', {folderEntry: data.folderEntry});
				}
			},

			_updateFolderEntry: function(folderEntry, invalidFiles) {
				var instance = this;

				var displayStyle = instance._appViewSelect._getDisplayStyle();

				var imagePath;

				if (invalidFiles.length) {
					if (displayStyle === 'list') {
						imagePath = themeDisplay.getPathContext() + '/html/themes/classic/images/common/close.png';
					}
					else {
						imagePath = themeDisplay.getPathContext() + '/html/themes/classic/images/document_library/folder_exclamation.png';
					}

					var tooltipBodyContent = '';

					A.Array.map(
						invalidFiles,
						function(item, index, collection) {
							tooltipBodyContent = tooltipBodyContent.concat(item.name, ': ' + item.errorMessage + '<br /><br />');
						}
					);

					new A.Tooltip(
						{
							bodyContent: tooltipBodyContent,
							trigger: folderEntry,
							constrain: true
						}
					).render();
				}
				else {
					if (displayStyle === 'list') {
						imagePath = themeDisplay.getPathContext() + '/html/themes/classic/images/common/checked.png';
					}
					else {
						imagePath = themeDisplay.getPathContext() + '/html/themes/classic/images/document_library/folder_checkmark.png';
					}
				}

				var folderImage = folderEntry.one('img');

				if (folderImage) {
					var src = folderImage.get('src');

					folderImage.set('src', imagePath);

					setTimeout(
						function() {
							folderImage.set('src', src);
						},
						3000
					);
				}

				if (folderEntry.overlay) {
					folderEntry.overlay.hide();
				}
			},

			_updateIconLink: function(id, file) {
				var instance = this;

				var entryDocumentLink = file.entryDocumentLink;

				var imageNode = entryDocumentLink.one('img');

				var fileName = file.name;

				var thumbnailPath = instance._getMediaThumbnail(fileName);

				imageNode.set('src', thumbnailPath);

				var ns_fileEntryId = instance.ns('fileEntryId=') + id;

				var href = entryDocumentLink.get('href');

				var ns_struts_action = instance.ns('struts_action=%2Fdocument_library%2Fview_file_entry');

				href = instance._config.viewFileEntryUrl + '&' + ns_fileEntryId;

				entryDocumentLink.set('href', href);
			},

			_updateRowLink: function(id, row) {
				var instance = this;

				var listRowDownload = instance.ns('ocerSearchContainer_col-title_row-1');

				var anchor = row.one('#' + listRowDownload).one('a');

				var ns_fileEntryId = instance.ns('fileEntryId=') + id;

				var href = anchor.get('href') + '&' + ns_fileEntryId;

				anchor.set('href', href);
			}
		}

		Liferay.DocumentLibraryUpload = DocumentLibraryUpload;
	},
	'',
	{
		requires: ['aui-data-set', 'aui-overlay-manager', 'aui-overlay-mask', 'aui-progressbar', 'aui-tooltip', 'liferay-app-view-folders', 'liferay-app-view-move', 'liferay-app-view-paginator', 'liferay-app-view-select', 'uploader', 'uploader-html5']
	}
);