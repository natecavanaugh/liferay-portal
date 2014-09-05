<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portlet/loan_calculator/init.jsp" %>

<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/activate.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_article.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_drive.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_folder.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_instance.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_location.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_portlet_display_template.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_template.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_template_display.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_template_form.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/add_user.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/all_pages.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/assign.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/assign_user_group_roles.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/assign_user_roles.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/attributes.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/back.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/bottom.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/calendar.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/category.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/check.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/clip.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/close.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/configuration.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/configuration_white.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/control_panel.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/conversation.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/copy.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/date.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/deactivate.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/define_permissions.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/delete.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/delete_attachment.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/desktop.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/download.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/drive.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/drive_error.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/edit.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/edit_template_white.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/edit_white.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/expire.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/export.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/feed.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/folder_full_document.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/forward.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/global.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/group.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/guest_icon.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/help.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/history.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/home.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/install_more.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/join.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/key.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/key_small.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/layout.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/leave.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/links.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/lock.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/manage_nodes.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/manage_task.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/maximize.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/message.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/minimize.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/my_places_private.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/my_places_public.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/news.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/open_window.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/organization_icon.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/organization_icon_white.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/page.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/page_template.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/pages.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/permissions.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/portlet.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/post.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/print.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/quote.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/recent_changes.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/remove.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/reply.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/revision.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/rss.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/saved_in_database.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/search.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/services.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/sharing.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/sign_out.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/signal_instance.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/site_icon.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/site_template.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/sitemap.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/staging.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/status_offline.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/status_online.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/submit.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/subscribe.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/tag.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/team_icon.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/telephone.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/telephone_mobile.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/time.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/tool.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/top.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/trash.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/unassign_user.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/unassign_user_group.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/undo.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/unlink.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/unlock.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/unsubscribe.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/user_icon.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/user_icon_white.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_articles.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_instances.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_locations.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_structures.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_tasks.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_templates.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/view_users.svg" %>' />
<liferay-ui:icon src='<%= themeDisplay.getPathThemeImages() + "/common/welcome_message.svg" %>' />