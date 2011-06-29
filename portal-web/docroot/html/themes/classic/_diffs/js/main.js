AUI().ready(
	'liferay-hudcrumbs',
	function(A) {
		var navigation = A.one('#navigation');

		var siteBreadcrumbs = A.one('.site-breadcrumbs');

		if (siteBreadcrumbs) {
			siteBreadcrumbs.plug(A.Hudcrumbs);
		}
	}
);