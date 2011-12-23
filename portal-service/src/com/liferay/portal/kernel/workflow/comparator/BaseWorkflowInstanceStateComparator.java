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
import com.liferay.portal.kernel.workflow.WorkflowInstance;

/**
 * @author Brian Wing Shun Chan
 */
public abstract class BaseWorkflowInstanceStateComparator
	extends OrderByComparator {

	public BaseWorkflowInstanceStateComparator() {
		this(false);
	}

	public BaseWorkflowInstanceStateComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		WorkflowInstance workflowInstance1 = (WorkflowInstance)obj1;
		WorkflowInstance workflowInstance2 = (WorkflowInstance)obj2;

		String state1 = workflowInstance1.getState();
		String state2 = workflowInstance2.getState();

		int value = state1.compareTo(state2);

		if (value == 0) {
			Long workflowInstanceId1 =
				workflowInstance1.getWorkflowInstanceId();
			Long workflowInstanceId2 =
				workflowInstance2.getWorkflowInstanceId();

			value = workflowInstanceId1.compareTo(workflowInstanceId2);
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