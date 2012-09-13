package org.kuali.hr.time.workflow.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.core.document.CalendarDocumentHeaderContract;
import org.kuali.hr.core.document.calendar.CalendarDocumentContract;
import org.kuali.hr.lm.leavecalendar.LeaveCalendarDocument;
import org.kuali.hr.time.roles.TkUserRoles;
import org.kuali.hr.time.roles.UserRoles;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.rice.kew.doctype.SecuritySession;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

public class WorkflowTagSupport {

    public String getTimesheetDocumentId() {
        return TKContext.getCurrentTimesheetDocumentId();
    }

    public String getLeaveCalendarDocumentId() {
        return TKContext.getCurrentLeaveCalendarDocumentId();
    }

    public boolean isDisplayingTimesheetRouteButton() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        return isDisplayingRouteButton(doc);
    }

    public boolean isDisplayingLeaveRouteButton() {
        LeaveCalendarDocument doc = TKContext.getCurrentLeaveCalendarDocument();
        return isDisplayingRouteButton(doc);
    }

    private boolean isDisplayingRouteButton(CalendarDocumentContract doc) {
        TkUserRoles roles = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getActualPerson().getPrincipalId());
        CalendarDocumentHeaderContract docHeader = doc.getDocumentHeader();
        if(docHeader.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.SAVED)
                || docHeader.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.INITIATED)){
            return roles.canSubmitTimesheet(doc);
        }
        return false;
    }

    /**
     * Disable the 'route' button if the document has already been routed.
     * @return true if the route button should render as enabled.
     */
    public boolean isRouteTimesheetButtonEnabled() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        return isRouteButtonEnabled(doc);
    }

    /**
     * Disable the 'route' button if the document has already been routed.
     * @return true if the route button should render as enabled.
     */
    public boolean isRouteLeaveButtonEnabled() {
        LeaveCalendarDocument doc = TKContext.getCurrentLeaveCalendarDocument();
        return isRouteButtonEnabled(doc);
    }

    private boolean isRouteButtonEnabled(CalendarDocumentContract doc) {
        CalendarDocumentHeaderContract dh = doc.getDocumentHeader();
        return (dh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.INITIATED)
                || dh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.SAVED));
    }

    public boolean isDisplayingTimesheetApprovalButtons() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        return isDisplayingApprovalButtons(doc);
    }

    public boolean isDisplayingLeaveApprovalButtons() {
        LeaveCalendarDocument doc = TKContext.getCurrentLeaveCalendarDocument();
        return isDisplayingApprovalButtons(doc);
    }

    private boolean isDisplayingApprovalButtons(CalendarDocumentContract doc) {
        UserRoles roles = TkUserRoles.getUserRoles(GlobalVariables.getUserSession().getPrincipalId());
        boolean tookActionAlready = KEWServiceLocator.getActionTakenService().hasUserTakenAction(TKContext.getPrincipalId(), doc.getDocumentHeader().getDocumentId());
        return !tookActionAlready
                && roles.isApproverForTimesheet(doc)
                && !StringUtils.equals(doc.getDocumentHeader().getDocumentStatus(), TkConstants.ROUTE_STATUS.FINAL);
    }

    public boolean isApprovalTimesheetButtonsEnabled() {
        TimesheetDocument doc = TKContext.getCurrentTimesheetDocument();
        return isApprovalButtonsEnabled(doc);
    }

    public boolean isApprovalLeaveButtonsEnabled() {
        LeaveCalendarDocument doc = TKContext.getCurrentLeaveCalendarDocument();
        return isApprovalButtonsEnabled(doc);
    }

    private boolean isApprovalButtonsEnabled(CalendarDocumentContract doc) {
        CalendarDocumentHeaderContract dh = doc.getDocumentHeader();
        boolean isEnroute = dh.getDocumentStatus().equals(TkConstants.ROUTE_STATUS.ENROUTE);
        if(isEnroute){
            DocumentRouteHeaderValue routeHeader = KEWServiceLocator.getRouteHeaderService().getRouteHeader(dh.getDocumentId());
            boolean authorized = KEWServiceLocator.getDocumentSecurityService().routeLogAuthorized(TKContext.getPrincipalId(), routeHeader, new SecuritySession(TKContext.getPrincipalId()));
            boolean tookActionAlready = KEWServiceLocator.getActionTakenService().hasUserTakenAction(TKContext.getPrincipalId(), dh.getDocumentId());
            return !tookActionAlready && authorized;
        }
        return false;
    }

    public String getRouteAction() { return TkConstants.DOCUMENT_ACTIONS.ROUTE; }
    public String getApproveAction() { return TkConstants.DOCUMENT_ACTIONS.APPROVE; }
    public String getDisapproveAction() { return TkConstants.DOCUMENT_ACTIONS.DISAPPROVE; }
}
