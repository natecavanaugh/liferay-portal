<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/html/portlet/css_init.jsp" %>

.portlet-staging-bar {
	.js .controls-hidden .staging-bar {
		display: none;
	}

	.manage-backstages {
		margin-left: 1em;
	}

	.staging-icon-menu-container {
		overflow: hidden;
		position: absolute;
		right: 8px;
		top: -5px;

		a {
			display: block;
			float: right;
			min-height: 15px;

		}

		.staging-icon-menu.lfr-actions .lfr-trigger strong {
			min-width: 0;

			a {
				background-color: #EEE;
				background-position: 50% 4px;
				border-radius: 100%;
				padding: 0 15px 0 0;
			}
		}
	}

	.tab-container {
		position: relative;
	}


	.staging-bar {
		.staging-tabs {
			background-color: #DDD;
			overflow: visible;
			padding-top: 1em;

			.tab {
				background-color: #EEE;
				border: 1px solid #999;
				border-left-color: #CCC;
				border-top-color: #AAA;
				border-bottom: none;
				font-size: 1.5em;
				min-width: 8em;
				padding: 10px 30px 2px 10px;

				-moz-border-radius-topleft: 4px;
				-moz-border-radius-topright: 4px;
				-webkit-border-top-left-radius: 4px;
				-webkit-border-top-right-radius: 4px;
				border-top-left-radius: 4px;
				border-top-right-radius: 4px;

				&.first {
					margin-left: 0.5em;
				}

				&.selected {
					background-color: #777;
					border-right-color: #999;
					border-top-color: #DDD;
					border-left-color: #EEE;
					color: #FFF;
					font-weight: bold;
					padding-bottom: 3px;
				}

				&.live {
					background-color: #339900;
					border-left-color: #CCC;
					border-right-color: #007700;
					border-top-color: #009900;
					color: #FFF;
					padding-left: 20px;

				}
			}
		}

		.staging-tabs-content {
			background-color: #777;
			border-bottom: 1px solid #666;
			color: #EEE;
			padding: 1em;

			&.content-live {
				background-color: #339900;
				border-top: 1px solid #999;
				color: #FFF;
			}

			.staging-icon {
				float: left;
				margin-right: 1em;
			}

			.backstage-info {
            	font-size: 1.1em;
				margin-bottom: 1em;

            	.backstage-description {
                	font-style: italic;
					border-right: 1px solid  #AAA;
					padding: 0 0.5em 0;
				}

				.backstage-pages {
                	padding-left: 0.5em;
				}
			}

			.layout-info {
				.staging-tabs {
					background-color: #777;
					overflow: visible;
					margin-top: 1em;

					.tab {
                    	background-color: #CCC;
						border-top-color: #DDD;
						border-left-color: #EEE;
						color: #333;
						font-size: 1em;

						&.selected {
							background-color: #777;
							color: #FFF;
						}
					}
				}
			}

			.variation-tabs-content {
				border: 1px solid #AAA;
				border-radius: 4px;
				padding: 1em;
			}

			.layout-title {
				font-size: 1.2em;

				label {
					float: left;
				}

				.layout-breadcrumb {
					font-size: 1em;

					.breadcrumbs a {
						color: #EEE;
					}

					.breadcrumbs-horizontal {
						margin-bottom: 0;
						overflow: hidden;
						padding-left: 0.5em;


						li {
							background-image: url(<%= themeImagesPath %>/arrows/09_right.png);

							&.last {
								background-image: none;
								font-weight: bold;
								text-decoration: none;
								position: relative;
								top: -0.2em;
							}
						}

					}
				}
			}

			.manage-page-variations a {
				color: #DDD;
			}

			.layout-actions .manage-page-variations {
				margin-left: 3em;
			}

			.layout-revision-id {
				font-size: 0.6em;
			}

			.last-publication-variation-details {
				font-size: 0.8em;

				.layout-version {
					background: url(<%= themeImagesPath %>/common/pages.png) no-repeat 0 50%;
					padding: 2px 0 2px 20px;
				}

				.variation-name {
					background: url(<%= themeImagesPath %>/common/signal_instance.png) no-repeat 0 50%;
					margin-right: 10px;
					padding: 2px 0 2px 20px;
				}
			}

			.taglib-workflow-status {
				clear: left;
				color: #FFF;
				float: left;
				margin-right: 5em;

				.workflow-status, .workflow-version {
					color: #FFF;
				}

				.workflow-status-approved, .workflow-status-ready-for-publication {
					color: #9F3;
				}

				.workflow-status-draft {
					color: #B8E9FF;
				}

				.workflow-status-expired {
					color: #FF8E8E;
				}
			}

			.last-publication-branch, .staging-live-group-name {
				display: block;
				font-size: 1.3em;
				padding: 5px 0 0;
			}

			.backstage-toolbar {
				padding-left: 1em;
			}
		}
	}

	.layout-set-results {
		min-height: 300px;
	}
}

.webkit .layout-info .staging-tabs .tab {
	padding-bottom: 4px;

	&.selected {
		padding-bottom: 5px;
	}
}

/* ---------- History ---------- */

.variation-selector {
	display: block;
	font-weight: bold;
	font-size: 1.2em;
	margin: 20px;
}

.layout-revision-container {
	margin: 20px;
	padding: 0;

	.aproximate-date {
		color: #999999;
    	font-weight: bold;
	}

	.current-version {
		color: green;
		display: block;
	}

	.current-version-pointer img {
		left: 10px;
		position: absolute;
	}

	.layout-variation-name {
		color: #666;
		font-variant: small-caps;
		margin-bottom: 0.1em;
	}

	.layout-revision-current {
		color: #666;
	}

	.real-date {
		color: #999999;
    	display: block;
    	text-align: right;
	}

	.taglib-workflow-status .workflow-status {
		background: none;
		padding-left: 0;

		.workflow-status-ready-for-publication {
			color: green;
			font-weight: bold;
		}
	}

	.lfr-menu-list {
		z-index: 1002;
	}
}