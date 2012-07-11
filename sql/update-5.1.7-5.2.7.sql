##
## Indexes
##

drop index IX_F0A86A4F on ExpandoTable;
drop index IX_DED782CE on ExpandoTable;

drop index IX_D67292FC on JournalArticleImage;

drop index IX_10563688 on TagsEntry;

##
## 5.1.2-5.2.0
##
## Removed WSRP tables.
##

alter table Company add homeURL STRING null;

alter table ExpandoColumn add companyId LONG;
alter table ExpandoColumn add defaultData STRING null;
alter table ExpandoColumn add typeSettings TEXT null;

alter table ExpandoRow add companyId LONG;

alter table ExpandoTable add companyId LONG;

alter table ExpandoValue add companyId LONG;

alter table JournalArticleImage add elInstanceId VARCHAR(75) null;

alter table JournalStructure add parentStructureId VARCHAR(75);

create table MBMailingList (
	uuid_ VARCHAR(75) null,
	mailingListId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	categoryId LONG,
	emailAddress VARCHAR(75) null,
	inProtocol VARCHAR(75) null,
	inServerName VARCHAR(75) null,
	inServerPort INTEGER,
	inUseSSL BOOLEAN,
	inUserName VARCHAR(75) null,
	inPassword VARCHAR(75) null,
	inReadInterval INTEGER,
	outEmailAddress VARCHAR(75) null,
	outCustom BOOLEAN,
	outServerName VARCHAR(75) null,
	outServerPort INTEGER,
	outUseSSL BOOLEAN,
	outUserName VARCHAR(75) null,
	outPassword VARCHAR(75) null,
	active_ BOOLEAN
);

alter table Organization_ add type_ VARCHAR(75);

alter table Role_ add title STRING null;
alter table Role_ add subtype VARCHAR(75);

alter table TagsAsset add visible BOOLEAN;

COMMIT_TRANSACTION;

update TagsAsset set visible = TRUE;

alter table TagsEntry add groupId LONG;
alter table TagsEntry add parentEntryId LONG;
alter table TagsEntry add vocabularyId LONG;

COMMIT_TRANSACTION;

update TagsEntry set groupId = 0;
update TagsEntry set parentEntryId = 0;
update TagsEntry set vocabularyId = 0;

create table TagsVocabulary (
	vocabularyId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	description VARCHAR(75) null,
	folksonomy BOOLEAN
);

alter table User_ add reminderQueryQuestion VARCHAR(75) null;
alter table User_ add reminderQueryAnswer VARCHAR(75) null;

##
## 5.2.0-5.2.1
##

update Organization_ set type_ = 'regular-organization' where type_ = 'regular';

##
## 5.2.2-5.2.3
##
## Most commands were the same as 5.1.4-5.1.5 except the following.
##

alter table JournalArticle add urlTitle VARCHAR(150) null;

COMMIT_TRANSACTION;

update JournalArticle set urlTitle = articleId;

##
## 5.2.4-5.2.5
##

create table UserGroupGroupRole (
	userGroupId LONG not null,
	groupId LONG not null,
	roleId LONG not null,
	primary key (userGroupId, groupId, roleId)
);

##
## 5.2.6-5.2.7
##

alter table Release_ add servletContextName VARCHAR(75);

COMMIT_TRANSACTION;

update Release_ set servletContextName = 'portal';