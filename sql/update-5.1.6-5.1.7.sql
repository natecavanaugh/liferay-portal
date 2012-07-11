create table Lock_ (
	uuid_ VARCHAR(75) null,
	lockId LONG not null primary key,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	className VARCHAR(75) null,
	key_ VARCHAR(200) null,
	owner VARCHAR(75) null,
	inheritable BOOLEAN,
	expirationDate DATE null
);