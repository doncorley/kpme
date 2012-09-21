package org.kuali.hr.lm.approval.web;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;
import org.kuali.hr.lm.workflow.LeaveCalendarDocumentHeader;
import org.kuali.hr.time.approval.web.ApprovalLeaveSummaryRow;
import org.kuali.hr.time.approval.web.ApprovalTimeSummaryRow;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.base.web.TkAction;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.person.TKPerson;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;

public class LeaveApprovalAction extends TkAction{
	
	public ActionForward searchResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LeaveApprovalActionForm laaf = (LeaveApprovalActionForm)form;
		String searchField = laaf.getSearchField();
		String searchTerm = laaf.getSearchTerm();
		String principalId;
		
        if(StringUtils.equals("documentId", searchField)){
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(searchTerm);
        	principalId = tdh.getPrincipalId();
        } else {
        	principalId = searchTerm;
        }
    	laaf.setSearchField("principalId");
    	laaf.setSearchTerm(principalId);
       
        List<String> principalIds = new ArrayList<String>();
        principalIds.add(principalId);
        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
        CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(laaf.getHrPyCalendarEntriesId());
        if (persons.isEmpty()) {
        	laaf.setLeaveApprovalRows(new ArrayList<ApprovalLeaveSummaryRow>());
        	laaf.setResultSize(0);
        } else {
        	this.setApprovalTables(laaf, principalIds, request, payCalendarEntries);
        	
   	        laaf.setPayCalendarEntries(payCalendarEntries);
   	        laaf.setLeaveCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        	
	        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, payCalendarEntries.getEndPeriodDate());
	        if(!assignments.isEmpty()){
	        	 for(Long wa : laaf.getWorkAreaDescr().keySet()){
	        		for (Assignment assign : assignments) {
		             	if (assign.getWorkArea().toString().equals(wa.toString())) {
		             		laaf.setSelectedWorkArea(wa.toString());
		             		break;
		             	}
	        		}
	             }
	        }
        }
 
		return mapping.findForward("basic");
	}
	
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
       
        List<ApprovalLeaveSummaryRow> lstLeaveRows = laaf.getLeaveApprovalRows();
        for (ApprovalLeaveSummaryRow ar : lstLeaveRows) {
            if (ar.isApprovable() && StringUtils.equals(ar.getSelected(), "on")) {
                String documentNumber = ar.getDocumentId();
                LeaveCalendarDocumentHeader lcd = TkServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(documentNumber);
                //TODO: approve the selected leave calendar documents, logic does not exist in LeaveCalendarDocumentHeaderService yet
            }
        }  
        
        return mapping.findForward("basic");
    }
        
	public ActionForward selectNewDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LeaveApprovalActionForm laaf = (LeaveApprovalActionForm)form;
		laaf.setSearchField(null);
		laaf.setSearchTerm(null);

        CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(laaf.getHrPyCalendarEntriesId());
        laaf.setPayCalendarEntries(payCalendarEntries);
        laaf.setLeaveCalendarLabels(TkServiceLocator.getLeaveSummaryService().getHeaderForSummary(payCalendarEntries));

		laaf.getWorkAreaDescr().clear();
    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(laaf.getSelectedDept(), new java.sql.Date(laaf.getPayBeginDate().getTime()));
        for(WorkArea wa : workAreas){
        	if (TKContext.getUser().getApproverWorkAreas().contains(wa.getWorkArea())
        			|| TKContext.getUser().getReviewerWorkAreas().contains(wa.getWorkArea())) {
        		laaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
        	}
        }
	
    	List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(laaf.getRoleName(), laaf.getSelectedDept(), laaf.getSelectedWorkArea(), new java.sql.Date(laaf.getPayBeginDate().getTime()), new java.sql.Date(laaf.getPayEndDate().getTime()), laaf.getSelectedPayCalendarGroup());
    	this.setApprovalTables(laaf, principalIds, request, payCalendarEntries);
    	
    	this.populateCalendarAndPayPeriodLists(request, laaf);
		return mapping.findForward("basic");
	}
	
	public ActionForward selectNewWorkArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LeaveApprovalActionForm laaf = (LeaveApprovalActionForm)form;
		laaf.setSearchField(null);
		laaf.setSearchTerm(null);

	    CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(laaf.getHrPyCalendarEntriesId());
        laaf.setLeaveCalendarLabels(TkServiceLocator.getLeaveSummaryService().getHeaderForSummary(payCalendarEntries));
        
        List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(laaf.getRoleName(), laaf.getSelectedDept(), laaf.getSelectedWorkArea(), new java.sql.Date(laaf.getPayBeginDate().getTime()), new java.sql.Date(laaf.getPayEndDate().getTime()), laaf.getSelectedPayCalendarGroup());
        this.setApprovalTables(laaf, principalIds, request, payCalendarEntries);
        
		return mapping.findForward("basic");
	}
	public ActionForward selectNewApprovalType(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LeaveApprovalActionForm laaf = (LeaveApprovalActionForm)form;
		laaf.setSearchField(null);
		laaf.setSearchTerm(null);
		List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(laaf.getRoleName(), laaf.getSelectedDept(), laaf.getSelectedWorkArea(), new java.sql.Date(laaf.getPayBeginDate().getTime()), new java.sql.Date(laaf.getPayEndDate().getTime()), laaf.getSelectedPayCalendarGroup());
		CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(laaf.getHrPyCalendarEntriesId());
		this.setApprovalTables(laaf, principalIds, request, payCalendarEntries);
		return mapping.findForward("basic");
	}
	
	private void setApprovalTables(LeaveApprovalActionForm laaf, List<String> principalIds, HttpServletRequest request, CalendarEntries payCalendarEntries) {
		if (principalIds.isEmpty()) {
			laaf.setLeaveApprovalRows(new ArrayList<ApprovalLeaveSummaryRow>());
			laaf.setResultSize(0);
		}
		else {
			List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
		    Collections.sort(persons);
			List<ApprovalLeaveSummaryRow> leaveRows = new ArrayList<ApprovalLeaveSummaryRow>();
		    leaveRows = this.getApprovalLeaveRows(laaf, getSubListPrincipalIds(request, persons)); 
		    laaf.setLeaveApprovalRows(leaveRows);
		    laaf.setLeaveCalendarLabels(TkServiceLocator.getLeaveSummaryService().getHeaderForSummary(payCalendarEntries));
		    laaf.setResultSize(persons.size());
		}
	}
	
	
	
	
	public ActionForward loadApprovalTab(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		ActionForward fwd = mapping.findForward("basic");
		TKUser user = TKContext.getUser();
        LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
        Date currentDate = null;
        CalendarEntries payCalendarEntries = null;
        Calendar currentPayCalendar = null;
        String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        
        //reset state
        if(StringUtils.isBlank(laaf.getSelectedDept())){
        	resetState(form, request);
        }
        // Set calendar groups
        List<String> calGroups = TkServiceLocator.getLeaveApprovalService().getUniqueLeavePayGroups();
        laaf.setPayCalendarGroups(calGroups);

        if (StringUtils.isBlank(laaf.getSelectedPayCalendarGroup())) {
            laaf.setSelectedPayCalendarGroup(calGroups.get(0));
        }
        
        // Set current pay calendar entries if present. Decide if the current date should be today or the end period date
        if (laaf.getHrPyCalendarEntriesId() != null) {
        	payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(laaf.getHrPyCalendarEntriesId());
            currentDate = payCalendarEntries.getEndPeriodDate();
        } else {
            currentDate = TKUtils.getTimelessDate(null);
            currentPayCalendar = TkServiceLocator.getCalendarService().getCalendarByGroup(laaf.getSelectedPayCalendarGroup());
            payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCurrentCalendarEntriesByCalendarId(currentPayCalendar.getHrCalendarId(), currentDate);
        }
        laaf.setPayCalendarEntries(payCalendarEntries);
        
        
        if(laaf.getPayCalendarEntries() != null) {
	        populateCalendarAndPayPeriodLists(request, laaf);
        }
        setupDocumentOnFormContext(request,laaf,payCalendarEntries, page);
        return fwd;
	}

	private void populateCalendarAndPayPeriodLists(HttpServletRequest request, LeaveApprovalActionForm laaf) {
		// set calendar year list
		Set<String> yearSet = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// if selected calendar year is passed in
		if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
			laaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
		} else {
			laaf.setSelectedCalendarYear(sdf.format(laaf.getPayCalendarEntries().getBeginPeriodDate()));
		}
		
		List<CalendarEntries> pcListForYear = new ArrayList<CalendarEntries>();
		List<CalendarEntries> pceList =  new ArrayList<CalendarEntries>();
		pceList.addAll(TkServiceLocator.getLeaveApprovalService()
			.getAllLeavePayCalendarEntriesForApprover(TKContext.getPrincipalId(), TKUtils.getTimelessDate(null)));
		
	    for(CalendarEntries pce : pceList) {
	    	yearSet.add(sdf.format(pce.getBeginPeriodDate()));
	    	if(sdf.format(pce.getBeginPeriodDate()).equals(laaf.getSelectedCalendarYear())) {
	    		pcListForYear.add(pce);
	    	}
	    }
	    List<String> yearList = new ArrayList<String>(yearSet);
	    Collections.sort(yearList);
	    laaf.setCalendarYears(yearList);
		
		// set pay period list contents
		if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
			laaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
		} else {
			laaf.setSelectedPayPeriod(laaf.getPayCalendarEntries().getHrCalendarEntriesId());
			laaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
		if(laaf.getPayPeriodsMap().isEmpty()) {
		    laaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
	}

	private void setupDocumentOnFormContext(HttpServletRequest request,LeaveApprovalActionForm laaf, CalendarEntries payCalendarEntries, String page) {
		if(payCalendarEntries == null) {
			return;
		}
		laaf.setHrPyCalendarId(payCalendarEntries.getHrCalendarId());
		laaf.setHrPyCalendarEntriesId(payCalendarEntries.getHrCalendarEntriesId());
		laaf.setPayBeginDate(payCalendarEntries.getBeginPeriodDateTime());
		laaf.setPayEndDate(payCalendarEntries.getEndPeriodDateTime());
		
		CalendarEntries prevPayCalendarEntries = TkServiceLocator.getCalendarEntriesService().getPreviousCalendarEntriesByCalendarId(laaf.getHrPyCalendarId(), payCalendarEntries);
		if (prevPayCalendarEntries != null) {
		    laaf.setPrevPayCalendarId(prevPayCalendarEntries.getHrCalendarEntriesId());
		} else {
		    laaf.setPrevPayCalendarId(null);
		}
		
		CalendarEntries nextPayCalendarEntries = TkServiceLocator.getCalendarEntriesService().getNextCalendarEntriesByCalendarId(laaf.getHrPyCalendarId(), payCalendarEntries);
		if (nextPayCalendarEntries != null) {
		    laaf.setNextPayCalendarId(nextPayCalendarEntries.getHrCalendarEntriesId());
		} else {
		    laaf.setNextPayCalendarId(null);
		}
		laaf.setLeaveCalendarLabels(TkServiceLocator.getLeaveSummaryService().getHeaderForSummary(payCalendarEntries));
				
		if (StringUtils.isBlank(page)) {
		    List<String> depts = new ArrayList<String>(TKContext.getUser().getReportingApprovalDepartments().keySet());
		    if ( depts.isEmpty() ) {
		    	return;
		    }
		    Collections.sort(depts);
		    laaf.setDepartments(depts);
		    
		    if (laaf.getDepartments().size() == 1 || laaf.getSelectedDept() != null) {
		    	if (StringUtils.isEmpty(laaf.getSelectedDept()))
		    		laaf.setSelectedDept(laaf.getDepartments().get(0));
		    	
		    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(laaf.getSelectedDept(), new java.sql.Date(laaf.getPayBeginDate().getTime()));
		        for(WorkArea wa : workAreas){
		        	if (TKContext.getUser().getApproverWorkAreas().contains(wa.getWorkArea())
		        			|| TKContext.getUser().getReviewerWorkAreas().contains(wa.getWorkArea())) {
		        		laaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
		        	}
		        }
		    }
		}

		List<String> principalIds = new ArrayList<String>();
		principalIds = TkServiceLocator.getLeaveApprovalService().getPrincipalIdsByDeptWorkAreaRolename(laaf.getRoleName(), laaf.getSelectedDept(), laaf.getSelectedWorkArea(), new java.sql.Date(laaf.getPayBeginDate().getTime()), new java.sql.Date(laaf.getPayEndDate().getTime()), laaf.getSelectedPayCalendarGroup());
		TkServiceLocator.getLeaveApprovalService().removeNonLeaveEmployees(principalIds);
     	
		this.setApprovalTables(laaf, principalIds, request, payCalendarEntries);
		laaf.setOnCurrentPeriod(ActionFormUtils.getOnCurrentPeriodFlag(laaf.getPayCalendarEntries()));
	}
	
	public ActionForward selectNewPayCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LeaveApprovalActionForm laaf = (LeaveApprovalActionForm)form;
		laaf.setSearchField(null);
		laaf.setSearchTerm(null);
		laaf.setSelectedWorkArea(null);
		laaf.setSelectedDept(null);
		laaf.setPayBeginDate(null);
		laaf.setPayEndDate(null);
		laaf.setHrPyCalendarEntriesId(null);
        // KPME-909
        laaf.setLeaveApprovalRows(new ArrayList<ApprovalLeaveSummaryRow>());
		
		return loadApprovalTab(mapping, form, request, response);
	}
	   
    protected List<ApprovalLeaveSummaryRow> getApprovalLeaveRows(LeaveApprovalActionForm laaf, List<TKPerson> assignmentPrincipalIds) {
        return TkServiceLocator.getLeaveApprovalService().getLeaveApprovalSummaryRows
        	(assignmentPrincipalIds, laaf.getPayCalendarEntries(), laaf.getLeaveCalendarLabels());
    }
	
    public void resetState(ActionForm form, HttpServletRequest request) {
    	  LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
 	      String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
 	      
 	      if (StringUtils.isBlank(page)) {
 			  laaf.getDepartments().clear();
 			  laaf.getWorkAreaDescr().clear();
 			  laaf.setLeaveApprovalRows(new ArrayList<ApprovalLeaveSummaryRow>());
 			  laaf.setSelectedDept(null);
 			  laaf.setSearchField(null);
 			  laaf.setSearchTerm(null);
 	      }
	}
    @Override
    protected void checkTKAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
        if (!TKContext.getUser().isTimesheetReviewer() && !TKContext.getUser().isAnyApproverActive() && !TKContext.getUser().isSystemAdmin() 
        		&& !TKContext.getUser().isLocationAdmin() && !TKContext.getUser().isGlobalViewOnly() && !TKContext.getUser().isDeptViewOnly() 
        		&& !TKContext.getUser().isDepartmentAdmin()) {
            throw new AuthorizationException(GlobalVariables.getUserSession().getPrincipalId(), "TimeApprovalAction", "");
        }
    }
    
    protected String getSortField(HttpServletRequest request) {
        return request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_SORT)));
    }

    protected Boolean isAscending(HttpServletRequest request) {
        // returned value 1 = ascending; 2 = descending
        String ascending = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_ORDER)));
        return StringUtils.equals(ascending, "1") ? true : false;
    }

    // move this to the service layer
    protected List<TKPerson> getSubListPrincipalIds(HttpServletRequest request, List<TKPerson> assignmentPrincipalIds) {
        String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        // The paging index begins from 1, but the sublist index begins from 0.
        // So the logic below sets the sublist begin index to 0 if the page number is null or equals 1
        Integer beginIndex = StringUtils.isBlank(page) || StringUtils.equals(page, "1") ? 0 : (Integer.parseInt(page) - 1)*TkConstants.PAGE_SIZE;
        Integer endIndex = beginIndex + TkConstants.PAGE_SIZE > assignmentPrincipalIds.size() ? assignmentPrincipalIds.size() : beginIndex + TkConstants.PAGE_SIZE;

        return assignmentPrincipalIds.subList(beginIndex, endIndex);
    } 
    
    public ActionForward gotoCurrentPayPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
         
    	LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
    	Date currentDate = TKUtils.getTimelessDate(null);
        Calendar currentPayCalendar = TkServiceLocator.getCalendarService().getCalendarByGroup(laaf.getSelectedPayCalendarGroup());
        CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCurrentCalendarEntriesByCalendarId(currentPayCalendar.getHrCalendarId(), currentDate);
        laaf.setPayCalendarEntries(payCalendarEntries);
        laaf.setSelectedCalendarYear(new SimpleDateFormat("yyyy").format(payCalendarEntries.getBeginPeriodDate()));
        laaf.setSelectedPayPeriod(payCalendarEntries.getHrCalendarEntriesId());
        populateCalendarAndPayPeriodLists(request, laaf);
        setupDocumentOnFormContext(request, laaf, payCalendarEntries, page);
        return mapping.findForward("basic");
    }
    
    // Triggered by changes of calendar year drop down list, reloads the pay period list
    public ActionForward changeCalendarYear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
    	if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
    		laaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
    		populateCalendarAndPayPeriodLists(request, laaf);
    	}
    	return mapping.findForward("basic");
    }
    
    // Triggered by changes of pay period drop down list, reloads the whole page based on the selected pay period
    public ActionForward changePayPeriod(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
      String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
      LeaveApprovalActionForm laaf = (LeaveApprovalActionForm) form;
  	  if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
  		  laaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
  		  CalendarEntries pce = TkServiceLocator.getCalendarEntriesService()
  		  	.getCalendarEntries(request.getParameter("selectedPP").toString());
  		  if(pce != null) {
  			  laaf.setPayCalendarEntries(pce);
  			  setupDocumentOnFormContext(request, laaf, pce, page);
  		  }
  	  }
  	  return mapping.findForward("basic");
	}

}