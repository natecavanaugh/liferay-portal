<%--
/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

<%
int loanAmount = ParamUtil.get(request, "loanAmount", 200000);
double interest = ParamUtil.get(request, "interest", 7.00);
int years = ParamUtil.get(request, "years", 30);
int paymentsPerYear = ParamUtil.get(request, "paymentsPerYear", 12);

double tempValue = Math.pow((1 + (interest / 100 / paymentsPerYear)), (years * paymentsPerYear));
double amountPerPayment = (loanAmount * tempValue * (interest / 100 / paymentsPerYear)) / (tempValue - 1);
double totalPaid = amountPerPayment * years * paymentsPerYear;
double interestPaid = totalPaid - loanAmount;

NumberFormat doubleFormat = NumberFormat.getNumberInstance(locale);

doubleFormat.setMaximumFractionDigits(2);
doubleFormat.setMinimumFractionDigits(2);

NumberFormat integerFormat = NumberFormat.getNumberInstance(locale);

integerFormat.setMaximumFractionDigits(0);
integerFormat.setMinimumFractionDigits(0);

NumberFormat percentFormat = NumberFormat.getPercentInstance(locale);
%>

<style type="text/css">
	.interest-details {
		margin: 1em 0;
	}

	.interest-details dt {
		font-weight: bold;
	}
</style>

<liferay-portlet:renderURL windowState="<%= LiferayWindowState.EXCLUSIVE.toString() %>" varImpl="loanCalculatorURL"><portlet:param name="struts_action" value="/loan_calculator/view" /></liferay-portlet:renderURL>

<aui:form action="<%= loanCalculatorURL %>" name="fm" onSubmit="event.preventDefault();">
	<aui:column cssClass="aui-fieldset" columnWidth="25">
		<aui:input label="loan-amount" name="loanAmount" size="5" value="<%= integerFormat.format(loanAmount) %>" data-myAttribute="test" />

		<aui:input label="interest-rate" name="interest" size="5" value="<%= doubleFormat.format(interest) %>" />

		<aui:input label="years" name="years" size="5" value="<%= years %>" />

		<dl class="interest-details">
			<dt><liferay-ui:message key="monthly-payment" /></dt>
			<dd>
				<%= integerFormat.format(amountPerPayment) %>
			</dd>
			<dt><liferay-ui:message key="interest-paid" /></dt>
			<dd>
				<%= integerFormat.format(interestPaid) %>
			</dd>
			<dt><liferay-ui:message key="total-paid" /></dt>
			<dd>
				<%= integerFormat.format(totalPaid) %>
			</dd>
		</dl>
	</aui:column>
	<aui:column columnWidth="75" id="chart"></aui:column>

	<aui:button-row>
		<aui:button value="calculate" type="submit" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-chart,aui-io-request,aui-parse-content">
	new A.PieChart(
		{
			dataField: 'count',
			dataSource: (new A.DataSource.Local(
					{
						source: [
							{ response: 'Principle', count: <%= totalPaid - interestPaid %> },
							{ response: 'Interest', count: <%= interestPaid %> }
						]
					}
				).plug(
					{
						fn: A.DataSourceJSONSchema,
						cfg: {
							resultFields: ['response', 'count']
						}
					}
				)),
			categoryField: 'response',
			width: 400,
			height: 300,
			dataTipFunction: function(item, index, series) {
				var instance = this;

				var data = series.data;

				var total = 0;
				var current = item.count;

				for (var i = 0; i < data.length; i++) {
					total += data[i].count;
				}

				var percentage = (current / total) * 100;

				return item.response + '\n' + (Math.round(percentage * 100) / 100) + '%';
			},
			style: {
				legend: {
					display: 'right',
					padding: 10,
					spacing: 5,
					font: {
						family: 'Arial',
						size: 13
					}
				}
			}
		}
	).render('#chart');

	var form = A.one('#<portlet:namespace />fm');
	var parentNode = form.get('parentNode');

	parentNode.plug(A.Plugin.ParseContent);

	form.on(
		'submit',
		function(event) {
			var uri = form.getAttribute('action');

			A.io.request(
				uri,
				{
					form: {
						id: form
					},
					on: {
						success: function(event, id, obj) {
							var responseData = this.get('responseData');

							parentNode.setContent(responseData);
						}
					}
				}
			);

			event.halt();
		}
	);
</aui:script>