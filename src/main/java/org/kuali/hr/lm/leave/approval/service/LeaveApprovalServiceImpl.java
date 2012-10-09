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
package org.kuali.hr.lm.leave.approval.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.kuali.hr.job.Job;
import org.kuali.hr.lm.LMConstants;
import org.kuali.hr.lm.accrual.AccrualCategory;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.approval.web.ApprovalLeaveSummaryRow;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.person.TKPerson;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.principal.dao.PrincipalHRAttributesDao;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class LeaveApprovalServiceImpl implements LeaveApprovalService{
	public static final int DAYS_WINDOW_DELTA = 31;
    private PrincipalHRAttributesDao principalHRAttributesDao;

    public void setPrincipalHRAttributesDao(PrincipalHRAttributesDao principalHRAttributesDao) {
        this.principalHRAttributesDao = principalHRAttributesDao;
    }
	
	@Override
	public List<ApprovalLeaveSummaryRow> getLeaveApprovalSummaryRows(List<TKPerson> persons, CalendarEntries payCalendarEntries, List<String> headers) {
		Date payBeginDate = payCalendarEntries.getBeginPeriodDate();
		Date payEndDate = payCalendarEntries.getEndPeriodDate();
		List<ApprovalLeaveSummaryRow> rowList = new ArrayList<ApprovalLeaveSummaryRow>();		
		
		for(TKPerson aPerson : persons) {
			String principalId = aPerson.getPrincipalId();
			ApprovalLeaveSummaryRow aRow = new ApprovalLeaveSummaryRow();
			aRow.setName(aPerson.getPrincipalName());
			aRow.setPrincipalId(aPerson.getPrincipalId());
			
			String lastApprovedString = "No previous approved leave calendar information";
			LeaveCalendarDocumentHeader lastApprovedDoc = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getMaxEndDateApprovedLeaveCalendar(principalId);
			if(lastApprovedDoc != null) {
				lastApprovedString = "Last Approved: " + (new SimpleDateFormat("MMM yyyy")).format(lastApprovedDoc.getBeginDate());
			}
			aRow.setLastApproveMessage(lastApprovedString);
			
			LeaveCalendarDocumentHeader aDoc = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
			if(aDoc != null) {
				aRow.setDocumentId(aDoc.getDocumentId());
				aRow.setApprovalStatus(TkConstants.DOC_ROUTE_STATUS.get(aDoc.getDocumentStatus()));
			}
			List<LeaveCalendarDocumentHeader> docList = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getAllDelinquentDocumentHeadersForPricipalId(principalId);
			if(docList.size() > LMConstants.DELINQUENT_LEAVE_CALENDARS_LIMIT ) {
				aRow.setMoreThanOneCalendar(true);
			}
			
			List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principalId, payBeginDate, payEndDate);
			aRow.setLeaveBlockList(leaveBlocks);
			Map<String, Map<String, BigDecimal>> leaveHoursToPayLabelMap = TkServiceLocator.getLeaveApprovalService().getLeaveHoursToPayDayMap(leaveBlocks, headers);
			aRow.setLeaveHoursToPayLabelMap(leaveHoursToPayLabelMap);
			
			rowList.add(aRow);
		}
		
		return rowList;
	}
	

	public Map<String, LeaveCalendarDocumentHeader> getLeaveDocumehtHeaderMap(List<TKPerson> persons, Date payBeginDate, Date payEndDate) {
		Map<String, LeaveCalendarDocumentHeader> leaveDocumentHeaderMap = new LinkedHashMap<String, LeaveCalendarDocumentHeader>();
		if (CollectionUtils.isNotEmpty(persons)) {
			for (TKPerson person : persons) {
				String principalId = person.getPrincipalId();
				LeaveCalendarDocumentHeader aHeader = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
				if(aHeader != null) {
					leaveDocumentHeaderMap.put(principalId, aHeader);
				}
				
			}
		}
		return leaveDocumentHeaderMap;
	}
	
	@Override
	public Map<String, Map<String, BigDecimal>> getLeaveHoursToPayDayMap(List<LeaveBlock> leaveBlocks,List<String> headers) {
		Map<String, Map<String, BigDecimal>> leaveHoursToPayLabelMap = new LinkedHashMap<String, Map<String, BigDecimal>>();
		
		for(String aString : headers) {
			leaveHoursToPayLabelMap.put(aString, null);
		}
		if(CollectionUtils.isNotEmpty(leaveBlocks)) {
			for(LeaveBlock lb : leaveBlocks) {
				Map<String, BigDecimal> dayHoursMap =  new LinkedHashMap<String, BigDecimal>();
				LocalDateTime leaveDate = (new DateTime(lb.getLeaveDate())).toLocalDateTime();
				String dateString = leaveDate.toString(TkConstants.DT_JUST_DAY_FORMAT);
				
				AccrualCategory ac = TkServiceLocator.getAccrualCategoryService().getAccrualCategory(lb.getAccrualCategoryId());
				if(ac != null && ac.getShowOnGrid().equals("Y")) {
					BigDecimal amount = lb.getLeaveAmount();
					if(leaveHoursToPayLabelMap.get(dateString) != null ) {
						dayHoursMap = leaveHoursToPayLabelMap.get(dateString);
						if(leaveHoursToPayLabelMap.get(dateString).get(ac.getAccrualCategory()) != null) {
							amount = leaveHoursToPayLabelMap.get(dateString).get(ac.getAccrualCategory()).add(lb.getLeaveAmount());
						}
					}
					dayHoursMap.put(ac.getAccrualCategory(), amount);
				}
				
				leaveHoursToPayLabelMap.put(dateString, dayHoursMap);
			}
		}
		return leaveHoursToPayLabelMap;
	}
	
	@Override
	public List<Map<String, Object>> getLaveApprovalDetailSections(LeaveCalendarDocumentHeader lcdh) {
		
		List<Map<String, Object>> acRows = new ArrayList<Map<String, Object>>();
		
		String principalId = lcdh.getPrincipalId();
		CalendarEntries calendarEntry = TkServiceLocator.getCalendarEntriesService().getCalendarEntriesByBeginAndEndDate(lcdh.getBeginDate(), lcdh.getEndDate());
		if(calendarEntry != null) {
			Date beginDate = calendarEntry.getBeginPeriodDate();
			Date endDate = calendarEntry.getEndPeriodDate();
			
			List<String> headers = TkServiceLocator.getLeaveSummaryService().getHeaderForSummary(calendarEntry);
			List<LeaveBlock> leaveBlocks = TkServiceLocator.getLeaveBlockService().getLeaveBlocks(principalId, beginDate, endDate);
			Map<String, Map<String, BigDecimal>> leaveHoursToPayLabelMap = TkServiceLocator.getLeaveApprovalService().getLeaveHoursToPayDayMap(leaveBlocks, headers);

			//get all accrual categories of this employee
			PrincipalHRAttributes pha = TkServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, endDate);
			if(pha != null) {
				List<AccrualCategory> acList = TkServiceLocator.getAccrualCategoryService()
					.getActiveLeaveAccrualCategoriesForLeavePlan(pha.getLeavePlan(), new java.sql.Date(endDate.getTime()));
				
				for(AccrualCategory ac : acList) {
					List<BigDecimal> acDayDetails = new ArrayList<BigDecimal>();
					Map<String, Object> displayMap = new HashMap<String, Object>();
					BigDecimal totalAmount = BigDecimal.ZERO;
					displayMap.put("accrualCategory", ac.getAccrualCategory());
					int index = 0;
					for(String aDateString : headers) {
						acDayDetails.add(index, null);
						if(leaveHoursToPayLabelMap.get(aDateString) != null && leaveHoursToPayLabelMap.get(aDateString).containsKey(ac.getAccrualCategory())) {
							BigDecimal amount =  leaveHoursToPayLabelMap.get(aDateString).get(ac.getAccrualCategory());
							totalAmount = totalAmount.add(amount);
							acDayDetails.set(index, amount);
						}
						index++;
					}
					displayMap.put("periodUsage", totalAmount);
					displayMap.put("availableBalance", BigDecimal.ZERO);
					displayMap.put("daysDetail", acDayDetails);
					displayMap.put("daysSize", acDayDetails.size());
					acRows.add(displayMap);
				}
			}
			
		}
		return acRows;
	}
	
	@Override
	public List<String> getUniqueLeavePayGroups() {
		String sql = "SELECT DISTINCT P.leave_calendar FROM hr_principal_attributes_t P WHERE P.active = 'Y'";
		SqlRowSet rs = TkServiceLocator.getTkJdbcTemplate().queryForRowSet(sql);
		List<String> pyGroups = new LinkedList<String>();
		while (rs.next()) {
			pyGroups.add(rs.getString("leave_calendar"));
		}
		return pyGroups;
	}

    @Override
    public List<String> getUniqueLeavePayGroupsForPrincipalIds(List<String> principalIds) {
        return principalHRAttributesDao.getUniqueLeavePayGroupsForPrincipalIds(principalIds);
    }
	
	@Override
	public List<CalendarEntries> getAllLeavePayCalendarEntriesForApprover(String principalId, Date currentDate) {
		TKUser tkUser = TKContext.getUser();
		Set<String> principals = new HashSet<String>();
		DateTime minDt = new DateTime(currentDate,
				TKUtils.getSystemDateTimeZone());
		minDt = minDt.minusDays(DAYS_WINDOW_DELTA);
		Set<Long> approverWorkAreas = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getPrincipalId()).getApproverWorkAreas();

		// Get all of the principals within our window of time.
		for (Long waNum : approverWorkAreas) {
			List<Assignment> assignments = TkServiceLocator
					.getAssignmentService().getActiveAssignmentsForWorkArea(waNum, TKUtils.getTimelessDate(currentDate));

			if (assignments != null) {
				for (Assignment assignment : assignments) {
					principals.add(assignment.getPrincipalId());
				}
			}
		}
		List<LeaveCalendarDocumentHeader> documentHeaders = new ArrayList<LeaveCalendarDocumentHeader>();
		for(String pid : principals) {
			documentHeaders.addAll(TkServiceLocator.getLeaveCalendarDocumentHeaderService().getAllDocumentHeadersForPricipalId(pid));
		}
		Set<CalendarEntries> payPeriodSet = new HashSet<CalendarEntries>();
		for(LeaveCalendarDocumentHeader lcdh : documentHeaders) {
    		CalendarEntries pe = TkServiceLocator.getCalendarEntriesService().getCalendarEntriesByBeginAndEndDate(lcdh.getBeginDate(), lcdh.getEndDate());
    		if(pe != null) {
    			payPeriodSet.add(pe);
    		}
        }
		List<CalendarEntries> ppList = new ArrayList<CalendarEntries>(payPeriodSet);
        
		return ppList;
	}
	@Override
	public void removeNonLeaveEmployees(List<String> principalIds) {
		if(CollectionUtils.isNotEmpty(principalIds)) {
			java.sql.Date asOfDate = TKUtils.getTimelessDate(null);
			List<String> idList = new ArrayList<String>();
			idList.addAll(principalIds);
	     	for(String principalId: idList) {
	     		boolean leaveFlag = false;
	     		List<Assignment> activeAssignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, asOfDate);
	     		if(CollectionUtils.isNotEmpty(activeAssignments)) {
	         		for(Assignment assignment : activeAssignments) {
	         			if(assignment != null && assignment.getJob() != null && assignment.getJob().isEligibleForLeave()) {
	         				leaveFlag = true;
	         				break;
	         			}
	         		}
	         		if(!leaveFlag) {  // employee is not eligible for leave, remove the id from principalIds
	         			principalIds.remove(principalId);
	         		}
	         	}
	     	}
		}
	}
	
	@Override
	public List<String> getPrincipalIdsByDeptWorkAreaRolename(String roleName,
			String department, String workArea, java.sql.Date payBeginDate,
			java.sql.Date payEndDate, String calGroup) {
		List<String> principalIds = getPrincipalIdsWithActiveAssignmentsForCalendarGroupByDeptAndWorkArea(
				roleName, department, workArea, calGroup, payEndDate,
				payBeginDate, payEndDate);
		return principalIds;
	}
	
	protected List<String> getPrincipalIdsWithActiveAssignmentsForCalendarGroupByDeptAndWorkArea(
		      String roleName, String department, String workArea,
		      String leaveCalendarGroup, java.sql.Date effdt,
		      java.sql.Date beginDate, java.sql.Date endDate) {
	    String sql = null;

      List<Job> jobs = TkServiceLocator.getJobService().getJobs(TKUser.getCurrentTargetPerson().getPrincipalId(), effdt);
      String jobPositionNumbersList = "'";
      for (Job job : jobs) {
                      jobPositionNumbersList += job.getPositionNumber() + "','";
      }
      /* the sql statement will enclose this string in single quotes, so we do not want the leading quote, or the trailing quote, comma, and quote. */
      if (jobPositionNumbersList.length() > 3) {
          jobPositionNumbersList = jobPositionNumbersList.substring(1, jobPositionNumbersList.length()-3) ;
      } else {
          jobPositionNumbersList = jobPositionNumbersList.substring(1);
      }

	    if (department == null || department.isEmpty()) {
	      return new ArrayList<String>();
	    } else {
	      List<String> principalIds = new ArrayList<String>();
	      SqlRowSet rs = null;
        sql = "SELECT DISTINCT A0.PRINCIPAL_ID FROM TK_ASSIGNMENT_T A0, HR_ROLES_T R0, TK_WORK_AREA_T W0, HR_PRINCIPAL_ATTRIBUTES_T P0  WHERE "
      		  + "((A0.EFFDT =  (SELECT MAX(EFFDT)  FROM TK_ASSIGNMENT_T  WHERE PRINCIPAL_ID = A0.PRINCIPAL_ID  AND EFFDT <= ? AND WORK_AREA = A0.WORK_AREA  AND TASK = A0.TASK AND JOB_NUMBER = A0.JOB_NUMBER) AND "
                + "A0.TIMESTAMP =  (SELECT MAX(TIMESTAMP)  FROM TK_ASSIGNMENT_T  WHERE PRINCIPAL_ID = A0.PRINCIPAL_ID  AND EFFDT = A0.EFFDT AND WORK_AREA = A0.WORK_AREA AND TASK = A0.TASK AND JOB_NUMBER = A0.JOB_NUMBER) AND "
                + "A0.ACTIVE = 'Y') OR (A0.ACTIVE = 'N'  AND A0.EFFDT >= ? AND A0.EFFDT <= ?)) AND "
                + "R0.WORK_AREA = A0.WORK_AREA AND "
                + "R0.ROLE_NAME IN ('TK_APPROVER', 'TK_APPROVER_DELEGATE', 'TK_REVIEWER') AND "
                + "R0.ACTIVE = 'Y' AND "
                + "( (R0.PRINCIPAL_ID = ? AND "
                + "R0.EFFDT = (SELECT MAX(EFFDT)  FROM HR_ROLES_T  WHERE ROLE_NAME = R0.ROLE_NAME AND PRINCIPAL_ID = R0.PRINCIPAL_ID AND EFFDT <= ? AND WORK_AREA = R0.WORK_AREA) AND "
                + "R0.TIMESTAMP = (SELECT MAX(TIMESTAMP)  FROM HR_ROLES_T  WHERE ROLE_NAME = R0.ROLE_NAME AND PRINCIPAL_ID = R0.PRINCIPAL_ID AND EFFDT = R0.EFFDT AND WORK_AREA = R0.WORK_AREA) "
                + ") or ("
                + "R0.POSITION_NBR in (?) AND "
                + "R0.EFFDT = (SELECT MAX(EFFDT)  FROM HR_ROLES_T  WHERE ROLE_NAME = R0.ROLE_NAME AND POSITION_NBR = R0.POSITION_NBR AND EFFDT <= ? AND WORK_AREA = R0.WORK_AREA) AND "
                + "R0.TIMESTAMP = (SELECT MAX(TIMESTAMP)  FROM HR_ROLES_T  WHERE ROLE_NAME = R0.ROLE_NAME AND POSITION_NBR = R0.POSITION_NBR AND EFFDT = R0.EFFDT AND WORK_AREA = R0.WORK_AREA) "
                + ") ) AND "
                + "W0.WORK_AREA = A0.WORK_AREA AND "
                + "W0.DEPT = ? AND "
                + "W0.EFFDT = (SELECT MAX(EFFDT) FROM TK_WORK_AREA_T WHERE EFFDT <= ? AND WORK_AREA = W0.WORK_AREA) AND "
                + "W0.TIMESTAMP =  (SELECT MAX(TIMESTAMP)  FROM TK_WORK_AREA_T  WHERE WORK_AREA = W0.WORK_AREA  AND EFFDT = W0.EFFDT) AND "
                + "W0.ACTIVE = 'Y' AND "
                + "P0.PRINCIPAL_ID = A0.PRINCIPAL_ID AND "
                + "P0.LEAVE_CALENDAR = ?";


	       int[] params = null;
	       Object[] values = null;
	       if (workArea != null) {
	          sql += " AND A0.WORK_AREA = ? ";
	          params = new int[] {java.sql.Types.DATE,
	              java.sql.Types.DATE,
	              java.sql.Types.DATE,
	              java.sql.Types.VARCHAR, 
	              java.sql.Types.DATE,
                java.sql.Types.VARCHAR,
                java.sql.Types.DATE,
	              java.sql.Types.VARCHAR,
	              java.sql.Types.DATE,
	              java.sql.Types.VARCHAR,
	              java.sql.Types.INTEGER };
	          values = new Object[] {effdt, beginDate, endDate, TKUser.getCurrentTargetPerson().getPrincipalId(), effdt, jobPositionNumbersList, effdt, department, effdt, leaveCalendarGroup, workArea };
	        }else {
	          params = new int[] {java.sql.Types.DATE,
	              java.sql.Types.DATE,
	              java.sql.Types.DATE,
	              java.sql.Types.VARCHAR, 
	              java.sql.Types.DATE,
                java.sql.Types.VARCHAR,
                java.sql.Types.DATE,
	              java.sql.Types.VARCHAR,
	              java.sql.Types.DATE,
	              java.sql.Types.VARCHAR};
	          values = new Object[] {effdt, beginDate, endDate, TKUser.getCurrentTargetPerson().getPrincipalId(), effdt, jobPositionNumbersList, effdt, department, effdt, leaveCalendarGroup};
	        }
	        rs = TkServiceLocator.getTkJdbcTemplate().queryForRowSet(
	            sql, values, params);
	      while (rs.next()) {
	        principalIds.add(rs.getString("principal_id"));
	      }
	      return principalIds;
	    }
	}
	
	@Override
	public Map<String, LeaveCalendarDocumentHeader> getPrincipalDocumehtHeader(List<TKPerson> persons, Date payBeginDate, Date payEndDate) {
		Map<String, LeaveCalendarDocumentHeader> principalDocumentHeader = new LinkedHashMap<String, LeaveCalendarDocumentHeader>();
		for (TKPerson person : persons) {
			String principalId = person.getPrincipalId();
			LeaveCalendarDocumentHeader lcdh = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
			if(lcdh != null) {
				principalDocumentHeader.put(principalId, lcdh);	
			}
		}
		return principalDocumentHeader;
	}

}
