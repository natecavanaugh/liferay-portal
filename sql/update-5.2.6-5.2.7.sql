alter table Release_ add servletContextName VARCHAR(75);

COMMIT_TRANSACTION;

update Release_ set servletContextName = 'portal';