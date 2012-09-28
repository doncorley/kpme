/**
 * Copyright 2004-2012 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.hr.lm.approval.web;

import java.util.ArrayList;
import java.util.List;

import org.kuali.hr.time.approval.web.ApprovalLeaveSummaryRow;
import org.kuali.hr.time.base.web.ApprovalForm;

public class LeaveApprovalActionForm extends ApprovalForm {
	
	private static final long serialVersionUID = 1L;
	
	private List<ApprovalLeaveSummaryRow> leaveApprovalRows;
	private List<String> leaveCalendarLabels = new ArrayList<String>();
	
	public List<ApprovalLeaveSummaryRow> getLeaveApprovalRows() {
		return leaveApprovalRows;
	}
	public void setLeaveApprovalRows(List<ApprovalLeaveSummaryRow> leaveApprovalRows) {
		this.leaveApprovalRows = leaveApprovalRows;
	}
	public List<String> getLeaveCalendarLabels() {
		return leaveCalendarLabels;
	}
	public void setLeaveCalendarLabels(List<String> leaveCalendarLabels) {
		this.leaveCalendarLabels = leaveCalendarLabels;
	}
}
