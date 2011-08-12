package org.kuali.hr.time.workflow.web;

import org.kuali.hr.time.roles.UserRoles;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;

public class WorkflowTagSupport {

    public String getDocumentId() {
        return TKContext.getCurrentTimesheetDocumentId();
    }

    public boolean isDisplayingRouteButton() {
      UserRoles roles = TKContext.getUser().getCurrentTargetRoles();
      String docId = TKContext.getCurrentTimesheetDocumentId();
      TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(docId);
      if(tdh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.INITIATED)){
    	  return roles.canSubmitTimesheet(docId);
      }
      return false;
    }

    /**
     * Disable the 'route' button if the document has already been routed.
     * @return true if the route button should render as enabled.
     */
    public boolean isRouteButtonEnabled() {
        String docId = TKContext.getCurrentTimesheetDocumentId();
        TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(docId);
        return (tdh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.INITIATED));
    }

    public boolean isDisplayingApprovalButtons() {

        UserRoles roles = TKContext.getUser().getCurrentTargetRoles();

        String docId = TKContext.getCurrentTimesheetDocumentId();
        TimesheetDocument doc = TkServiceLocator.getTimesheetService().getTimesheetDocument(docId);
        return roles.isApproverForTimesheet(doc);
    }

    public boolean isApprovalButtonsEnabled() {
        String docId = TKContext.getCurrentTimesheetDocumentId();
        TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(docId);
        boolean isEnroute = tdh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.ENROUTE);
        if(isEnroute){
        	DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(Long.parseLong(docId));
        	boolean authorized = KEWServiceLocator.getDocumentSecurityService().routeLogAuthorized(TKContext.getUserSession(), routeHeader, new SecuritySession(TKContext.getUserSession()));
        	return authorized;
        }
        return false;
    }

    public String getRouteAction() { return TkConstants.TIMESHEET_ACTIONS.ROUTE; }
    public String getApproveAction() { return TkConstants.TIMESHEET_ACTIONS.APPROVE; }
    public String getDisapproveAction() { return TkConstants.TIMESHEET_ACTIONS.DISAPPROVE; }
}
