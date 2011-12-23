/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.kernel.workflow;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
public class DefaultWorkflowInstance implements Serializable, WorkflowInstance {

	public void addChildWorkflowInstance(
		WorkflowInstance childWorkflowInstance) {

		_childrenWorkflowInstances.add(childWorkflowInstance);
	}

	public int getChildrenWorkflowInstanceCount() {
		return _childrenWorkflowInstances.size();
	}

	public List<WorkflowInstance> getChildrenWorkflowInstances() {
		return _childrenWorkflowInstances;
	}

	public Date getEndDate() {
		return _endDate;
	}

	public WorkflowInstance getParentWorkflowInstance() {
		return _parentWorkflowInstance;
	}

	public long getParentWorkflowInstanceId() {
		if (_parentWorkflowInstance != null) {
			return _parentWorkflowInstance.getWorkflowInstanceId();
		}
		else {
			return 0;
		}
	}

	public Date getStartDate() {
		return _startDate;
	}

	public String getState() {
		return _state;
	}

	public Map<String, Serializable> getWorkflowContext() {
		return _workflowContext;
	}

	public String getWorkflowDefinitionName() {
		return _workflowDefinitionName;
	}

	public int getWorkflowDefinitionVersion() {
		return _workflowDefinitionVersion;
	}

	public long getWorkflowInstanceId() {
		return _workflowInstanceId;
	}

	public boolean isComplete() {
		if (getEndDate() != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public void setChildrenWorkflowInstances(
		List<WorkflowInstance> childrenWorkflowInstances) {

		_childrenWorkflowInstances = childrenWorkflowInstances;
	}

	public void setEndDate(Date endDate) {
		_endDate = endDate;
	}

	public void setParentWorkflowInstance(
		WorkflowInstance parentWorkflowInstance) {

		_parentWorkflowInstance = parentWorkflowInstance;
	}

	public void setStartDate(Date startDate) {
		_startDate = startDate;
	}

	public void setState(String state) {
		_state = state;
	}

	public void setWorkflowContext(Map<String, Serializable> workflowContext) {
		_workflowContext = workflowContext;
	}

	public void setWorkflowDefinitionName(String workflowDefinitionName) {
		_workflowDefinitionName = workflowDefinitionName;
	}

	public void setWorkflowDefinitionVersion(int workflowDefinitionVersion) {
		_workflowDefinitionVersion = workflowDefinitionVersion;
	}

	public void setWorkflowInstanceId(long workflowInstanceId) {
		_workflowInstanceId = workflowInstanceId;
	}

	private List<WorkflowInstance> _childrenWorkflowInstances =
		new ArrayList<WorkflowInstance>();
	private Date _endDate;
	private WorkflowInstance _parentWorkflowInstance;
	private Date _startDate;
	private String _state;
	private Map<String, Serializable> _workflowContext;
	private String _workflowDefinitionName;
	private int _workflowDefinitionVersion;
	private long _workflowInstanceId;

}