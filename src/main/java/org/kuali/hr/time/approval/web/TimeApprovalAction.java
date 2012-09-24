package org.kuali.hr.time.approval.web;

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
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.base.web.ApprovalAction;
import org.kuali.hr.time.base.web.ApprovalForm;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.person.TKPerson;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUser;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;

public class TimeApprovalAction extends ApprovalAction{
	
	public ActionForward searchResult(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		String searchField = taaf.getSearchField();
		String searchTerm = taaf.getSearchTerm();
		String principalId;
		
        if(StringUtils.equals("documentId", searchField)){
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(searchTerm);
        	principalId = tdh.getPrincipalId();
        } else {
        	principalId = searchTerm;
        }
    	taaf.setSearchField("principalId");
    	taaf.setSearchTerm(principalId);
       
        List<String> principalIds = new ArrayList<String>();
        principalIds.add(principalId);
        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
        if (persons.isEmpty()) {
        	taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
        	taaf.setResultSize(0);
        } else {
        	taaf.setResultSize(persons.size());	
	        taaf.setApprovalRows(getApprovalRows(taaf, persons));
	        
        	CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
   	        taaf.setPayCalendarEntries(payCalendarEntries);
   	        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        	
	        List<Assignment> assignments = TkServiceLocator.getAssignmentService().getAssignments(principalId, payCalendarEntries.getEndPeriodDate());
	        if(!assignments.isEmpty()){
	        	 for(Long wa : taaf.getWorkAreaDescr().keySet()){
	        		for (Assignment assign : assignments) {
		             	if (assign.getWorkArea().toString().equals(wa.toString())) {
		             		taaf.setSelectedWorkArea(wa.toString());
		             		break;
		             	}
	        		}
	             }
	        }
        }
 
		return mapping.findForward("basic");
	}
	
    public ActionForward approve(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        List<ApprovalTimeSummaryRow> lstApprovalRows = taaf.getApprovalRows();
        for (ApprovalTimeSummaryRow ar : lstApprovalRows) {
            if (ar.isApprovable() && StringUtils.equals(ar.getSelected(), "on")) {
                String documentNumber = ar.getDocumentId();
                TimesheetDocument tDoc = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentNumber);
                TkServiceLocator.getTimesheetService().approveTimesheet(TKContext.getPrincipalId(), tDoc);
            }
        }
        return mapping.findForward("basic");
    }
    
	public ActionForward selectNewDept(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		taaf.setSearchField(null);
		taaf.setSearchTerm(null);

        CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarEntries(payCalendarEntries);
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));

		taaf.getWorkAreaDescr().clear();
    	List<WorkArea> workAreas = TkServiceLocator.getWorkAreaService().getWorkAreas(taaf.getSelectedDept(), new java.sql.Date(taaf.getPayBeginDate().getTime()));
        for(WorkArea wa : workAreas){
        	if (TKContext.getUser().getApproverWorkAreas().contains(wa.getWorkArea())
        			|| TKContext.getUser().getReviewerWorkAreas().contains(wa.getWorkArea())) {
        		taaf.getWorkAreaDescr().put(wa.getWorkArea(),wa.getDescription()+"("+wa.getWorkArea()+")");
        	}
        }
	
    	List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
    	if (principalIds.isEmpty()) {
    		taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
    		taaf.setResultSize(0);
    	}
    	else {
	        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
	        Collections.sort(persons);
	        taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
	        taaf.setResultSize(persons.size());
    	}
    	
    	this.populateCalendarAndPayPeriodLists(request, taaf);
		return mapping.findForward("basic");
	}
	
	public ActionForward selectNewWorkArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		taaf.setSearchField(null);
		taaf.setSearchTerm(null);

	    CalendarEntries payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
        taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
        
        List<String> principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		}
		else {
	        List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
	        Collections.sort(persons);
	        taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
	        taaf.setResultSize(persons.size());
		}
		return mapping.findForward("basic");
	}
	
	public ActionForward loadApprovalTab(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		ActionForward fwd = mapping.findForward("basic");
		TKUser user = TKContext.getUser();
        TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
        Date currentDate = null;
        CalendarEntries payCalendarEntries = null;
        Calendar currentPayCalendar = null;
        String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
        
        //reset state
        if(StringUtils.isBlank(taaf.getSelectedDept())){
        	resetState(form, request);
        }
        // Set calendar groups
        List<String> calGroups = TkServiceLocator.getTimeApproveService().getUniquePayGroups();
        taaf.setPayCalendarGroups(calGroups);

        if (StringUtils.isBlank(taaf.getSelectedPayCalendarGroup())) {
            taaf.setSelectedPayCalendarGroup(calGroups.get(0));
        }
        
        // Set current pay calendar entries if present. Decide if the current date should be today or the end period date
        if (taaf.getHrPyCalendarEntriesId() != null) {
        	payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCalendarEntries(taaf.getHrPyCalendarEntriesId());
            currentDate = payCalendarEntries.getEndPeriodDate();
        } else {
            currentDate = TKUtils.getTimelessDate(null);
            currentPayCalendar = TkServiceLocator.getCalendarService().getCalendarByGroup(taaf.getSelectedPayCalendarGroup());
            payCalendarEntries = TkServiceLocator.getCalendarEntriesService().getCurrentCalendarEntriesByCalendarId(currentPayCalendar.getHrCalendarId(), currentDate);
        }
        taaf.setPayCalendarEntries(payCalendarEntries);
        
        
        if(taaf.getPayCalendarEntries() != null) {
	        populateCalendarAndPayPeriodLists(request, taaf);
        }
        setupDocumentOnFormContext(request,taaf,payCalendarEntries, page);
        return fwd;
	}

	@Override
	protected void setupDocumentOnFormContext(HttpServletRequest request,ApprovalForm form, CalendarEntries payCalendarEntries, String page) {
		super.setupDocumentOnFormContext(request, form, payCalendarEntries, page);
		TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
		taaf.setPayCalendarLabels(TkServiceLocator.getTimeSummaryService().getHeaderForSummary(payCalendarEntries, new ArrayList<Boolean>()));
		List<String> principalIds = new ArrayList<String>();
		principalIds = TkServiceLocator.getTimeApproveService().getPrincipalIdsByDeptWorkAreaRolename(taaf.getRoleName(), taaf.getSelectedDept(), taaf.getSelectedWorkArea(), new java.sql.Date(taaf.getPayBeginDate().getTime()), new java.sql.Date(taaf.getPayEndDate().getTime()), taaf.getSelectedPayCalendarGroup());
		if (principalIds.isEmpty()) {
			taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
			taaf.setResultSize(0);
		}
		else {
		    List<TKPerson> persons = TkServiceLocator.getPersonService().getPersonCollection(principalIds);
		    Collections.sort(persons);
		    taaf.setApprovalRows(getApprovalRows(taaf, getSubListPrincipalIds(request, persons)));
		    taaf.setResultSize(persons.size());
		}
		
		taaf.setOnCurrentPeriod(ActionFormUtils.getOnCurrentPeriodFlag(taaf.getPayCalendarEntries()));
	}
	
	public ActionForward selectNewPayCalendar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// resets the common fields for approval pages
		super.resetMainFields(form);
		TimeApprovalActionForm taaf = (TimeApprovalActionForm)form;
		// KPME-909
        taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
		return loadApprovalTab(mapping, form, request, response);
	}
	
    /**
     * Helper method to modify / manage the list of records needed to display approval data to the user.
     *
     * @param taaf
     * @return
     */
    protected List<ApprovalTimeSummaryRow> getApprovalRows(TimeApprovalActionForm taaf, List<TKPerson> assignmentPrincipalIds) {
        return TkServiceLocator.getTimeApproveService().getApprovalSummaryRows(taaf.getPayBeginDate(), taaf.getPayEndDate(), taaf.getSelectedPayCalendarGroup(), assignmentPrincipalIds, taaf.getPayCalendarLabels(), taaf.getPayCalendarEntries());
    }
	
    public void resetState(ActionForm form, HttpServletRequest request) {
    	  TimeApprovalActionForm taaf = (TimeApprovalActionForm) form;
 	      String page = request.getParameter((new ParamEncoder(TkConstants.APPROVAL_TABLE_ID).encodeParameterName(TableTagParameters.PARAMETER_PAGE)));
 	      
 	      if (StringUtils.isBlank(page)) {
 			  taaf.getDepartments().clear();
 			  taaf.getWorkAreaDescr().clear();
 			  taaf.setApprovalRows(new ArrayList<ApprovalTimeSummaryRow>());
 			  taaf.setSelectedDept(null);
 			  taaf.setSearchField(null);
 			  taaf.setSearchTerm(null);
 	      }
	}
	
    @Override
    protected void populateCalendarAndPayPeriodLists(HttpServletRequest request, ApprovalForm taf) {
    	TimeApprovalActionForm taaf = (TimeApprovalActionForm)taf;
		// set calendar year list
		Set<String> yearSet = new HashSet<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		// if selected calendar year is passed in
		if(!StringUtils.isEmpty(request.getParameter("selectedCY"))) {
			taaf.setSelectedCalendarYear(request.getParameter("selectedCY").toString());
		} else {
			taaf.setSelectedCalendarYear(sdf.format(taaf.getPayCalendarEntries().getBeginPeriodDate()));
		}
		
		List<CalendarEntries> pcListForYear = new ArrayList<CalendarEntries>();
		List<CalendarEntries> pceList = TkServiceLocator.getTimeApproveService()
			.getAllPayCalendarEntriesForApprover(TKContext.getPrincipalId(), TKUtils.getTimelessDate(null));
	    for(CalendarEntries pce : pceList) {
	    	yearSet.add(sdf.format(pce.getBeginPeriodDate()));
	    	if(sdf.format(pce.getBeginPeriodDate()).equals(taaf.getSelectedCalendarYear())) {
	    		pcListForYear.add(pce);
	    	}
	    }
	    List<String> yearList = new ArrayList<String>(yearSet);
	    Collections.sort(yearList);
	    taaf.setCalendarYears(yearList);
		
		// set pay period list contents
		if(!StringUtils.isEmpty(request.getParameter("selectedPP"))) {
			taaf.setSelectedPayPeriod(request.getParameter("selectedPP").toString());
		} else {
			taaf.setSelectedPayPeriod(taaf.getPayCalendarEntries().getHrCalendarEntriesId());
			taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
		if(taaf.getPayPeriodsMap().isEmpty()) {
		    taaf.setPayPeriodsMap(ActionFormUtils.getPayPeriodsMap(pcListForYear));
		}
	}
}
