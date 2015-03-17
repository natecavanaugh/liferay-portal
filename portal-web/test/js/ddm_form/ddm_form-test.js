'use strict';

var assert = chai.assert;

describe('DDM Form Test Suite', function() {
    this.timeout(5000);

    before(function(done) {
        var self = this;

        AUI().use('aui-io-request', 'liferay-ddm-form', function(A) {
            var getTestData = function(name) {
                var definition, html;

                A.io.request(
                    '/base/test/js/ddm_form/assets/' + name + '-definition.json',
                    {
                        dataType: 'json',
                        on: {
                            success: function() {
                                definition = this.get('responseData');
                            }
                        },
                        sync: true
                    }
                );

                A.io.request(
                    '/base/test/js/ddm_form/assets/' + name + '-definition.html',
                    {
                        on: {
                            success: function() {
                                html = this.get('responseData');
                            }
                        },
                        sync: true
                    }
                );

                return { definition: definition, html: html };
            };

            assert.ok(Liferay.DDM.Form);

            var data = getTestData('simple');

            document.body.innerHTML = data.html;

            self.ddmForm = new Liferay.DDM.Form(
                {
                    container: '#ddmContainer',
                    ddmFormValuesInput: '#_167_ddmFormValues',
                    definition: data.definition,
                    doAsGroupId: 20180,
                    fieldsNamespace: '',
                    mode: 'null',
                    p_l_id: 20173,
                    portletNamespace: '_167_',
                    repeatable: true
                }
            );

            assert.ok(self.ddmForm);

            done();
        });
    });

    it('should serialize a simple DDM Form with one unlocalizable text field', function(done) {
        var self = this,
            ddmForm = self.ddmForm;

        var textField = ddmForm.get('fields')[0];

        assert.ok(textField);

        var textFieldInputNode = textField.getInputNode();

        assert.ok(textFieldInputNode);

        var value = 'simple text';

        textFieldInputNode.attr('value', value);

        var json = ddmForm.toJSON();

        assert.ok(json);

        assert.strictEqual(value, json.fieldValues[0].value);

        done();
    });
});