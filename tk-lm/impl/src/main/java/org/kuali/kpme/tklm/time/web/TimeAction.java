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
package org.kuali.kpme.tklm.time.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.job.Job;
import org.kuali.kpme.core.principal.PrincipalHRAttributes;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.core.web.KPMEAction;
import org.kuali.kpme.core.web.KPMEForm;
import org.kuali.kpme.tklm.time.rules.timecollection.TimeCollectionRule;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.util.TkContext;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;

public class TimeAction extends KPMEAction {

    @Override
    protected void checkTKAuthorization(ActionForm form, String methodToCall) throws AuthorizationException {
        KPMEForm kPMEForm = (KPMEForm) form;

        if (StringUtils.equals(methodToCall, "targetEmployee") || StringUtils.equals(methodToCall, "changeEmployee") || StringUtils.equals(methodToCall, "clearBackdoor") || StringUtils.equals(methodToCall, "clearChangeUser")) {
            // Handle security validation in targetEmployee action, we may need
            // to check the document for validity, since the user may not
            // necessarily be a system administrator.
        } else {
        	String principalId = GlobalVariables.getUserSession().getPrincipalId();
        	TimesheetDocument tdoc = TkServiceLocator.getTimesheetService().getTimesheetDocument(kPMEForm.getDocumentId());
            if (!HrContext.isSystemAdmin()
        			&& !TkContext.isLocationAdmin()
        			&& !TkContext.isDepartmentAdmin()
        			&& !HrContext.isGlobalViewOnly()
        			&& !TkContext.isDepartmentViewOnly()
        			&& (kPMEForm.getDocumentId() != null && !HrServiceLocator.getHRPermissionService().canApproveCalendarDocument(principalId, tdoc))
        			&& (kPMEForm.getDocumentId() != null && !HrServiceLocator.getHRPermissionService().canViewCalendarDocument(principalId, tdoc)))  {
                throw new AuthorizationException("", "TimeAction", "");
            }
        }
    }


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //boolean synch = TKUser.isSynchronous();
        String principalId = HrContext.getTargetPrincipalId();
        if (HrContext.isSystemAdmin()) {
            return new ActionRedirect("/portal.do");
        }
        PrincipalHRAttributes phra = HrServiceLocator.getPrincipalHRAttributeService().getPrincipalCalendar(principalId, LocalDate.now());
        if (phra == null) {
            return new ActionRedirect("/PersonInfo.do");
        }
        Job job = HrServiceLocator.getJobService().getPrimaryJob(principalId, LocalDate.now());
        boolean activeAssignments = false;
        if (job != null) {
            String flsa = job.getFlsaStatus();
            List<Assignment> assignments = HrServiceLocator.getAssignmentService().getActiveAssignmentsForJob(principalId, job.getJobNumber(), LocalDate.now());
            for (Assignment asmnt : assignments) {
                if (asmnt.isActive()) {
                    if (job.getFlsaStatus().equals(HrConstants.FLSA_STATUS_NON_EXEMPT)) {
                        TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(asmnt.getJob().getDept(), asmnt.getWorkArea(), LocalDate.now());
                        if (tcr.isClockUserFl()) {
                            return new ActionRedirect("/Clock.do");
                        } else {
                            return new ActionRedirect("/TimeDetail.do");
                        }
                    } else {
                        if (job.isEligibleForLeave()) {
                            return new ActionRedirect("/LeaveCalendar.do");
                        }
                    }
                }
            }
        }

        return new ActionRedirect("/PersonInfo.do");
    }
}
