alter table BookmarksEntry add userName VARCHAR(75) null;

alter table BookmarksFolder add userName VARCHAR(75) null;

drop index IX_975996C0 on Company;

alter table DLFileEntry add mimeType VARCHAR(75) null;

alter table DLFileVersion add mimeType VARCHAR(75) null;

alter table IGFolder add userName VARCHAR(75) null;

alter table IGImage add userName VARCHAR(75) null;

drop index IX_5ABC2905 on LayoutSet;

alter table MBMailingList add allowAnonymous BOOLEAN;

alter table MBThread add companyId LONG;
alter table MBThread add rootMessageUserId LONG;

create table PortalPreferences (
	portalPreferencesId LONG not null primary key,
	ownerId LONG,
	ownerType INTEGER,
	preferences TEXT null
);

alter table ResourcePermission add ownerId LONG;

COMMIT_TRANSACTION;

create unique index IX_4A1F4402 on ResourcePermission (companyId, name, scope, primKey, roleId, ownerId, actionIds);

alter table SocialEquityLog add extraData VARCHAR(255) null;

alter table Ticket add type_ INTEGER;
alter table Ticket add extraInfo TEXT null;

COMMIT_TRANSACTION;

update Ticket set type_ = 3;

alter table UserGroup add addedByLDAPImport BOOLEAN;

create table UserGroups_Teams (
	userGroupId LONG not null,
	teamId LONG not null,
	primary key (userGroupId, teamId)
);

create table UserNotificationEvent (
	uuid_ VARCHAR(75) null,
	userNotificationEventId LONG not null primary key,
	companyId LONG,
	userId LONG,
	type_ VARCHAR(75) null,
	timestamp LONG,
	deliverBy LONG,
	payload TEXT null
);

create table VirtualHost (
	virtualHostId LONG not null primary key,
	companyId LONG,
	layoutSetId LONG,
	hostname VARCHAR(75) null
);