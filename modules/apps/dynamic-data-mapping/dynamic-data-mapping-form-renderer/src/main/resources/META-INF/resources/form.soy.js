// This file was automatically generated from form.soy.
// Please don't edit this file by hand.

if (typeof ddm == 'undefined') { var ddm = {}; }


ddm.field = function(opt_data, opt_ignored) {
  return '\t' + ((opt_data.field != null) ? soy.$$filterNoAutoescape(opt_data.field) : '');
};


ddm.fields = function(opt_data, opt_ignored) {
  var output = '\t';
  var fieldList10 = opt_data.fields;
  var fieldListLen10 = fieldList10.length;
  for (var fieldIndex10 = 0; fieldIndex10 < fieldListLen10; fieldIndex10++) {
    var fieldData10 = fieldList10[fieldIndex10];
    output += ddm.field(soy.$$augmentMap(opt_data, {field: fieldData10}));
  }
  return output;
};


ddm.form_renderer_js = function(opt_data, opt_ignored) {
  return '\t<link href="/o/dynamic-data-mapping-form-renderer/css/main.css" rel="stylesheet" type="text/css" /><script type="text/javascript">AUI().use( \'liferay-ddm-form-renderer\', \'liferay-ddm-form-renderer-field\', function(A) {Liferay.DDM.Renderer.FieldTypes.register(' + soy.$$filterNoAutoescape(opt_data.fieldTypes) + '); Liferay.component( \'' + soy.$$escapeHtml(opt_data.containerId) + 'DDMForm\', new Liferay.DDM.Renderer.Form({container: \'#' + soy.$$escapeHtml(opt_data.containerId) + '\', definition: ' + soy.$$filterNoAutoescape(opt_data.definition) + ', evaluation: ' + soy.$$filterNoAutoescape(opt_data.evaluation) + ',' + ((opt_data.layout) ? 'layout: ' + soy.$$filterNoAutoescape(opt_data.layout) + ',' : '') + 'portletNamespace: \'' + soy.$$escapeHtml(opt_data.portletNamespace) + '\', readOnly: ' + soy.$$escapeHtml(opt_data.readOnly) + ', showRequiredFieldsWarning: ' + soy.$$escapeHtml(opt_data.showRequiredFieldsWarning) + ', submitLabel: \'' + soy.$$escapeHtml(opt_data.submitLabel) + '\', templateNamespace: \'' + soy.$$escapeHtml(opt_data.templateNamespace) + '\', values: ' + soy.$$filterNoAutoescape(opt_data.values) + '}).render() );});<\/script>';
};


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '\t';
  var rowList51 = opt_data.rows;
  var rowListLen51 = rowList51.length;
  for (var rowIndex51 = 0; rowIndex51 < rowListLen51; rowIndex51++) {
    var rowData51 = rowList51[rowIndex51];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData51.columns})) + '</div>';
  }
  return output;
};


ddm.form_row_column = function(opt_data, opt_ignored) {
  return '\t<div class="col-md-' + soy.$$escapeHtml(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};


ddm.form_row_columns = function(opt_data, opt_ignored) {
  var output = '\t';
  var columnList66 = opt_data.columns;
  var columnListLen66 = columnList66.length;
  for (var columnIndex66 = 0; columnIndex66 < columnListLen66; columnIndex66++) {
    var columnData66 = columnList66[columnIndex66];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData66}));
  }
  return output;
};


ddm.required_warning_message = function(opt_data, opt_ignored) {
  return '\t' + ((opt_data.showRequiredFieldsWarning) ? '<label class="required-warning">' + soy.$$escapeHtml(opt_data.strings.requiredFieldsWarningMessage) + '<span class="icon-asterisk text-warning"><span class="hide-accessible">' + soy.$$escapeHtml(opt_data.strings.required) + '</span></span></label>' : '');
};


ddm.paginated_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-content">';
  if (opt_data.pages.length > 1) {
    output += '<div class="lfr-ddm-form-wizard"><ul class="multi-step-progress-bar">';
    var pageList86 = opt_data.pages;
    var pageListLen86 = pageList86.length;
    for (var pageIndex86 = 0; pageIndex86 < pageListLen86; pageIndex86++) {
      var pageData86 = pageList86[pageIndex86];
      output += '<li ' + ((pageIndex86 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData86.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex86 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages">';
  var pageList100 = opt_data.pages;
  var pageListLen100 = pageList100.length;
  for (var pageIndex100 = 0; pageIndex100 < pageListLen100; pageIndex100++) {
    var pageData100 = pageList100[pageIndex100];
    output += '<div class="' + ((pageIndex100 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData100.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData100.title) + '</h3>' : '') + ((pageData100.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData100.description) + '</h4>' : '') + ddm.required_warning_message(opt_data) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData100.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((! opt_data.readOnly) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '">' + ddm.required_warning_message(opt_data) + '<div class="lfr-ddm-form-fields">';
  var pageList152 = opt_data.pages;
  var pageListLen152 = pageList152.length;
  for (var pageIndex152 = 0; pageIndex152 < pageListLen152; pageIndex152++) {
    var pageData152 = pageList152[pageIndex152];
    output += ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData152.rows}));
  }
  output += '</div></div>';
  return output;
};


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav nav-tabs nav-tabs-default">';
  var pageList161 = opt_data.pages;
  var pageListLen161 = pageList161.length;
  for (var pageIndex161 = 0; pageIndex161 < pageListLen161; pageIndex161++) {
    var pageData161 = pageList161[pageIndex161];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData161.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList167 = opt_data.pages;
  var pageListLen167 = pageList167.length;
  for (var pageIndex167 = 0; pageIndex167 < pageListLen167; pageIndex167++) {
    var pageData167 = pageList167[pageIndex167];
    output += ddm.required_warning_message(opt_data) + '<div class="tab-pane ' + ((pageIndex167 == 0) ? 'active' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData167.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList185 = opt_data.pages;
  var pageListLen185 = pageList185.length;
  for (var pageIndex185 = 0; pageIndex185 < pageListLen185; pageIndex185++) {
    var pageData185 = pageList185[pageIndex185];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex185 == 0) ? ' active basic' : '') + ((pageIndex185 == pageListLen185 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData185.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
