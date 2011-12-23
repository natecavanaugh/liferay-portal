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

package com.liferay.portal.kernel.workflow.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.workflow.WorkflowTask;

/**
 * @author Shuyang Zhou
 */
public class BaseWorkflowTaskNameComparator extends OrderByComparator {

	public BaseWorkflowTaskNameComparator() {
		this(false);
	}

	public BaseWorkflowTaskNameComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		WorkflowTask workflowTask1 = (WorkflowTask)obj1;
		WorkflowTask workflowTask2 = (WorkflowTask)obj2;

		String name1 = workflowTask1.getName();
		String name2 = workflowTask2.getName();

		int value = name1.compareTo(name2);

		if (value == 0) {
			Long workflowTaskId1 = workflowTask1.getWorkflowTaskId();
			Long workflowTaskId2 = workflowTask2.getWorkflowTaskId();

			value = workflowTaskId1.compareTo(workflowTaskId2);
		}

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private boolean _ascending;

}