/**
 * Copyright 2004-2013 The Kuali Foundation
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
package org.kuali.kpme.tklm.leave.block;

import java.sql.Timestamp;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.task.Task;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.workarea.WorkArea;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;

public class LeaveBlockHistory extends LeaveBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lmLeaveBlockHistoryId;
	private String action;
	private String principalIdDeleted;
	private Timestamp timestampDeleted;
	
	public LeaveBlockHistory() {
	}

	public LeaveBlockHistory(LeaveBlock lb) {
		this.setLmLeaveBlockId(lb.getLmLeaveBlockId());
		this.setDocumentId(lb.getDocumentId());
		this.setAccrualCategory(lb.getAccrualCategory());
		this.setAccrualGenerated(lb.getAccrualGenerated());
//		this.setApplyToYtdUsed(lb.getApplyToYtdUsed());
		this.setDescription(lb.getDescription());
		this.setLeaveAmount(lb.getLeaveAmount());
		this.setEarnCode(lb.getEarnCode());
		this.setLeaveDate(lb.getLeaveDate());
		this.setPrincipalId(lb.getPrincipalId());
//		this.setPrincipalIdModified(lb.getPrincipalIdModified());
		this.setRequestStatus(lb.getRequestStatus());
		this.setTimestamp(lb.getTimestamp());
//		this.setTkAssignmentId(lb.getTkAssignmentId());
		this.setScheduleTimeOffId(lb.getScheduleTimeOffId());
		this.setLeaveBlockType(lb.getLeaveBlockType());
		this.setBeginTimestamp(lb.getBeginTimestamp());
		this.setEndTimestamp(lb.getEndTimestamp());
	}
	
	
	public String getLmLeaveBlockHistoryId() {
		return lmLeaveBlockHistoryId;
	}

	public void setLmLeaveBlockHistoryId(String lmLeaveBlockHistoryId) {
		this.lmLeaveBlockHistoryId = lmLeaveBlockHistoryId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getPrincipalIdDeleted() {
		return principalIdDeleted;
	}

	public void setPrincipalIdDeleted(String principalIdDeleted) {
		this.principalIdDeleted = principalIdDeleted;
	}

	public Timestamp getTimestampDeleted() {
		return timestampDeleted;
	}

	public void setTimestampDeleted(Timestamp timestampDeleted) {
		this.timestampDeleted = timestampDeleted;
	}

	public String getAssignmentTitle() {
		StringBuilder b = new StringBuilder();
		LeaveBlock lb = LmServiceLocator.getLeaveBlockService().getLeaveBlock(super.getLmLeaveBlockId());
		if(lb != null){
			if (lb.getWorkArea() != null) {
				WorkArea wa = HrServiceLocator.getWorkAreaService().getWorkArea(
					lb.getWorkArea(), LocalDate.now());
				if (wa != null) {
					b.append(wa.getDescription());
				}
				Task task = HrServiceLocator.getTaskService().getTask(
						this.getTask(), this.getLeaveLocalDate());
				if (task != null) {
					// do not display task description if the task is the default
					// one
					// default task is created in getTask() of TaskService
					if (!task.getDescription()
						.equals(HrConstants.TASK_DEFAULT_DESP)) {
						b.append("-" + task.getDescription());
					}
				}
			}
		}
		return b.toString();
	}

}
