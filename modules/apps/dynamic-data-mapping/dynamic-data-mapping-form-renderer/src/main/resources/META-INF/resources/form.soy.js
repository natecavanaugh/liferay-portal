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
  return '\t<link href="/o/dynamic-data-mapping-form-renderer/css/main.css" rel="stylesheet" type="text/css" /><script type="text/javascript">AUI().use( \'liferay-ddm-form-renderer\', \'liferay-ddm-form-renderer-field\', function(A) {Liferay.DDM.Renderer.FieldTypes.register(' + soy.$$filterNoAutoescape(opt_data.fieldTypes) + '); Liferay.component( \'' + soy.$$escapeHtml(opt_data.containerId) + 'DDMForm\', new Liferay.DDM.Renderer.Form({container: \'#' + soy.$$escapeHtml(opt_data.containerId) + '\', definition: ' + soy.$$filterNoAutoescape(opt_data.definition) + ', evaluation: ' + soy.$$filterNoAutoescape(opt_data.evaluation) + ',' + ((opt_data.layout) ? 'layout: ' + soy.$$filterNoAutoescape(opt_data.layout) + ',' : '') + 'portletNamespace: \'' + soy.$$escapeHtml(opt_data.portletNamespace) + '\', readOnly: ' + soy.$$escapeHtml(opt_data.readOnly) + ', submitLabel: \'' + soy.$$escapeHtml(opt_data.submitLabel) + '\', templateNamespace: \'' + soy.$$escapeHtml(opt_data.templateNamespace) + '\', values: ' + soy.$$filterNoAutoescape(opt_data.values) + '}).render() );});<\/script>';
};


ddm.form_rows = function(opt_data, opt_ignored) {
  var output = '\t';
  var rowList49 = opt_data.rows;
  var rowListLen49 = rowList49.length;
  for (var rowIndex49 = 0; rowIndex49 < rowListLen49; rowIndex49++) {
    var rowData49 = rowList49[rowIndex49];
    output += '<div class="row">' + ddm.form_row_columns(soy.$$augmentMap(opt_data, {columns: rowData49.columns})) + '</div>';
  }
  return output;
};


ddm.form_row_column = function(opt_data, opt_ignored) {
  return '\t<div class="col-md-' + soy.$$escapeHtml(opt_data.column.size) + '">' + ddm.fields(soy.$$augmentMap(opt_data, {fields: opt_data.column.fields})) + '</div>';
};


ddm.form_row_columns = function(opt_data, opt_ignored) {
  var output = '\t';
  var columnList64 = opt_data.columns;
  var columnListLen64 = columnList64.length;
  for (var columnIndex64 = 0; columnIndex64 < columnListLen64; columnIndex64++) {
    var columnData64 = columnList64[columnIndex64];
    output += ddm.form_row_column(soy.$$augmentMap(opt_data, {column: columnData64}));
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
    var pageList84 = opt_data.pages;
    var pageListLen84 = pageList84.length;
    for (var pageIndex84 = 0; pageIndex84 < pageListLen84; pageIndex84++) {
      var pageData84 = pageList84[pageIndex84];
      output += '<li ' + ((pageIndex84 == 0) ? 'class="active"' : '') + '><div class="progress-bar-title">' + soy.$$filterNoAutoescape(pageData84.title) + '</div><div class="divider"></div><div class="progress-bar-step">' + soy.$$escapeHtml(pageIndex84 + 1) + '</div></li>';
    }
    output += '</ul></div>';
  }
  output += '<div class="lfr-ddm-form-pages">';
  var pageList98 = opt_data.pages;
  var pageListLen98 = pageList98.length;
  for (var pageIndex98 = 0; pageIndex98 < pageListLen98; pageIndex98++) {
    var pageData98 = pageList98[pageIndex98];
    output += '<div class="' + ((pageIndex98 == 0) ? 'active' : '') + ' lfr-ddm-form-page">' + ((pageData98.title) ? '<h3 class="lfr-ddm-form-page-title">' + soy.$$filterNoAutoescape(pageData98.title) + '</h3>' : '') + ((pageData98.description) ? '<h4 class="lfr-ddm-form-page-description">' + soy.$$filterNoAutoescape(pageData98.description) + '</h4>' : '') + ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData98.showRequiredFieldsWarning, strings: opt_data.strings})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData98.rows})) + '</div>';
  }
  output += '</div></div><div class="lfr-ddm-form-pagination-controls"><button class="btn btn-lg btn-primary hide lfr-ddm-form-pagination-prev" type="button"><i class="icon-angle-left"></i> ' + soy.$$escapeHtml(opt_data.strings.previous) + '</button><button class="btn btn-lg btn-primary' + ((opt_data.pages.length == 1) ? ' hide' : '') + ' lfr-ddm-form-pagination-next pull-right" type="button">' + soy.$$escapeHtml(opt_data.strings.next) + ' <i class="icon-angle-right"></i></button>' + ((! opt_data.readOnly) ? '<button class="btn btn-lg btn-primary' + ((opt_data.pages.length > 1) ? ' hide' : '') + ' lfr-ddm-form-submit pull-right" disabled type="submit">' + soy.$$escapeHtml(opt_data.submitLabel) + '</button>' : '') + '</div></div>';
  return output;
};


ddm.simple_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-fields">';
  var pageList146 = opt_data.pages;
  var pageListLen146 = pageList146.length;
  for (var pageIndex146 = 0; pageIndex146 < pageListLen146; pageIndex146++) {
    var pageData146 = pageList146[pageIndex146];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData146.showRequiredFieldsWarning, strings: opt_data.strings})) + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData146.rows}));
  }
  output += '</div></div>';
  return output;
};


ddm.tabbed_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-form-tabs"><ul class="nav nav-tabs nav-tabs-default">';
  var pageList158 = opt_data.pages;
  var pageListLen158 = pageList158.length;
  for (var pageIndex158 = 0; pageIndex158 < pageListLen158; pageIndex158++) {
    var pageData158 = pageList158[pageIndex158];
    output += '<li><a href="javascript:;">' + soy.$$escapeHtml(pageData158.title) + '</a></li>';
  }
  output += '</ul><div class="tab-content">';
  var pageList164 = opt_data.pages;
  var pageListLen164 = pageList164.length;
  for (var pageIndex164 = 0; pageIndex164 < pageListLen164; pageIndex164++) {
    var pageData164 = pageList164[pageIndex164];
    output += ddm.required_warning_message(soy.$$augmentMap(opt_data, {showRequiredFieldsWarning: pageData164.showRequiredFieldsWarning, strings: opt_data.strings})) + '<div class="tab-pane ' + ((pageIndex164 == 0) ? 'active' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData164.rows})) + '</div>';
  }
  output += '</div></div></div>';
  return output;
};


ddm.settings_form = function(opt_data, opt_ignored) {
  var output = '\t<div class="lfr-ddm-form-container" id="' + soy.$$escapeHtml(opt_data.containerId) + '"><div class="lfr-ddm-settings-form">';
  var pageList182 = opt_data.pages;
  var pageListLen182 = pageList182.length;
  for (var pageIndex182 = 0; pageIndex182 < pageListLen182; pageIndex182++) {
    var pageData182 = pageList182[pageIndex182];
    output += '<div class="lfr-ddm-form-page' + ((pageIndex182 == 0) ? ' active basic' : '') + ((pageIndex182 == pageListLen182 - 1) ? ' advanced' : '') + '">' + ddm.form_rows(soy.$$augmentMap(opt_data, {rows: pageData182.rows})) + '</div>';
  }
  output += '</div></div>';
  return output;
};
