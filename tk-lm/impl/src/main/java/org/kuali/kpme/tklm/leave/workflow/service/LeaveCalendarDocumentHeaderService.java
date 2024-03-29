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
package org.kuali.kpme.tklm.leave.workflow.service;

import java.util.List;

import org.joda.time.DateTime;
import org.kuali.kpme.tklm.leave.workflow.LeaveCalendarDocumentHeader;

public interface LeaveCalendarDocumentHeaderService {
    LeaveCalendarDocumentHeader getDocumentHeader(String documentId);
    LeaveCalendarDocumentHeader getDocumentHeader(String principalId, DateTime beginDate, DateTime endDate);
    void saveOrUpdate(LeaveCalendarDocumentHeader leaveCalendarDocumentHeader);
    
    public List<LeaveCalendarDocumentHeader> getDocumentHeaders(DateTime beginDate, DateTime endDate);
    /*
     * return the latest approved leave calendar for given principalId
     */
    public LeaveCalendarDocumentHeader getMaxEndDateApprovedLeaveCalendar(String principalId);
    /*
     * return the earliest pending leave calendar for given principalId
     */
    public LeaveCalendarDocumentHeader getMinBeginDatePendingLeaveCalendar(String principalId);
    /*
     * returns List of LeaveCalendarDocumentHeader for the given principalId
     * Used to populate the lists Of calendar year and calendar entries on Approval page
     */
	public List<LeaveCalendarDocumentHeader> getAllDocumentHeadersForPricipalId(String principalId);
	
	public List<LeaveCalendarDocumentHeader> getSubmissionDelinquentDocumentHeaders(String principalId, DateTime beforeDate);
	
	public List<LeaveCalendarDocumentHeader> getApprovalDelinquentDocumentHeaders(String principalId);

    public void deleteLeaveCalendarHeader(String documentId);
    
    /**
     * Get list of LeaveCalendarDocumentHeader for given principalId and date range
     * @param principalId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<LeaveCalendarDocumentHeader> getAllDocumentHeadersInRangeForPricipalId(String principalId, DateTime beginDate, DateTime endDate);
}

