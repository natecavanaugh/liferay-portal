;(function() {
	var A = AUI();
	var Lang = A.Lang;

	var LiferayUtil = Liferay.Util;

	var entities = A.merge(
		LiferayUtil.MAP_HTML_CHARS_ESCAPED,
		{
			'[': '&#91;',
			']': '&#93;',
			'(': '&#40;',
			')': '&#41;'
		}
	);

	var BBCodeUtil = Liferay.namespace('BBCodeUtil');

	BBCodeUtil.escape = A.rbind('escapeHTML', Lang.String, true, entities);
	BBCodeUtil.unescape = A.rbind('unescapeHTML', Lang.String, entities);
}());