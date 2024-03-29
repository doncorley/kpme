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
package org.kuali.kpme.tklm.leave.service.permission;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.service.permission.HrPermissionServiceBase;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.tklm.common.LMConstants;
import org.kuali.kpme.tklm.leave.block.LeaveBlock;
import org.kuali.kpme.tklm.leave.calendar.service.LeaveCalendarService;
import org.kuali.kpme.tklm.leave.request.service.LeaveRequestDocumentService;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;
import org.kuali.kpme.tklm.leave.timeoff.SystemScheduledTimeOff;
import org.kuali.kpme.tklm.leave.workflow.LeaveRequestDocument;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.util.TkContext;
import org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.action.ValidActions;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.krad.util.KRADConstants;

public class LMPermissionServiceImpl extends HrPermissionServiceBase implements LMPermissionService {
	
	private PermissionService permissionService;
	private LeaveCalendarService leaveCalendarService;
	private LeaveRequestDocumentService leaveRequestDocumentService;
	
	@Override
	public boolean isAuthorized(String principalId, String permissionName, DateTime asOfDate) {
		Map<String, String> qualification = new HashMap<String, String>();
		
		return getPermissionService().isAuthorized(principalId, KPMENamespace.KPME_LM.getNamespaceCode(), permissionName, qualification);
	}
	
	@Override
	public boolean isAuthorized(String principalId, String permissionName, Map<String, String> qualification, DateTime asOfDate) {
		return getPermissionService().isAuthorized(principalId, KPMENamespace.KPME_LM.getNamespaceCode(), permissionName, qualification);
	}
    
    @Override
    public boolean canViewLeaveRequest(String principalId, String documentId) {
    	return canSuperUserAdministerLeaveRequest(principalId, documentId) 
    			|| isAuthorizedByTemplate(principalId, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.OPEN_DOCUMENT, documentId);
    }
    
    @Override
    public boolean canEditLeaveRequest(String principalId, String documentId) {
    	return canSuperUserAdministerLeaveRequest(principalId, documentId) 
    			|| isAuthorizedByTemplate(principalId, KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.EDIT_DOCUMENT, documentId);
    }
    
    @Override
    public boolean canSubmitLeaveRequest(String principalId, String documentId) {
        return canSuperUserAdministerLeaveRequest(principalId, documentId) 
        		|| isAuthorizedByTemplate(principalId, KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE, KimConstants.PermissionTemplateNames.ROUTE_DOCUMENT, documentId);
    }
    
    @Override
    public boolean canApproveLeaveRequest(String principalId, String documentId) {
    	boolean canApproveLeaveRequest = false;
    	
    	ValidActions validActions = KewApiServiceLocator.getWorkflowDocumentActionsService().determineValidActions(documentId, principalId);
    	
    	if (validActions.getValidActions() != null) {
    		canApproveLeaveRequest = validActions.getValidActions().contains(ActionType.APPROVE);
    	}
    	
    	return canApproveLeaveRequest;
    }
    
    @Override
    public boolean canSuperUserAdministerLeaveRequest(String principalId, String documentId) {
        return isAuthorizedByTemplate(principalId, KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE, "Administer Routing for Document", documentId);
    }
    
    private boolean isAuthorizedByTemplate(String principalId, String namespaceCode, String permissionTemplateName, String documentId) {
    	boolean isAuthorizedByTemplate = false;

/*    	LeaveRequestDocument lrd = LmServiceLocator.getLeaveRequestDocumentService().getLeaveRequestDocument(documentId);
    	
    	if (lrd != null) {
    		String documentTypeName = LeaveRequestDocument.LEAVE_REQUEST_DOCUMENT_TYPE;
        	DocumentStatus documentStatus = DocumentStatus.fromCode(lrd.getActionCode());
        	
        	isAuthorizedByTemplate = isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName, documentTypeName, documentId, documentStatus);
    	}*/
    	
    	return isAuthorizedByTemplate;
    }
    
    @Override
	public boolean isAuthorizedByTemplate(String principalId, String namespaceCode, String permissionTemplateName, Map<String, String> permissionDetails, DateTime asOfDate) {
		Map<String, String> qualification = new HashMap<String, String>();
		
		return isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName, permissionDetails, qualification, asOfDate);
	}
	
    @Override
	public boolean isAuthorizedByTemplate(String principalId, String namespaceCode, String permissionTemplateName, Map<String, String> permissionDetails, Map<String, String> qualification, DateTime asOfDate) {
		return getPermissionService().isAuthorizedByTemplate(principalId, namespaceCode, permissionTemplateName, permissionDetails, qualification);
	}

    @Override
    public boolean canEditLeaveBlock(String principalId, LeaveBlock leaveBlock) {
        if (principalId != null) {
        	String documentId = leaveBlock.getDocumentId();
        	if (StringUtils.isBlank(documentId)) {
        		TimesheetDocumentHeader timesheetDocumentHeader = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeaderForDate(principalId, leaveBlock.getLeaveLocalDate().toDateTimeAtStartOfDay());
        		if (timesheetDocumentHeader != null) {
        			documentId = timesheetDocumentHeader.getDocumentId();
        		}
        	}
        	if (StringUtils.isNotBlank(documentId)) {
        		DocumentStatus documentStatus = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(documentId);
        		if (DocumentStatus.CANCELED.equals(documentStatus) || DocumentStatus.DISAPPROVED.equals(documentStatus)) {
        			return false;
        		}
        	}
	 	 	 	
            String blockType = leaveBlock.getLeaveBlockType();
            String requestStatus = leaveBlock.getRequestStatus();
            if (StringUtils.equals(HrConstants.REQUEST_STATUS.DISAPPROVED, requestStatus)) {
                return false;
            }
            if (StringUtils.equals(HrConstants.REQUEST_STATUS.APPROVED, requestStatus)) {
            	List<LeaveRequestDocument> docList= LmServiceLocator.getLeaveRequestDocumentService().getLeaveRequestDocumentsByLeaveBlockId(leaveBlock.getLmLeaveBlockId());
            	if(CollectionUtils.isEmpty(docList)) {
            		return false;	// not a leave request. if this is a leave request, do further checking on it
            	}            	
            }
            if (StringUtils.isBlank(blockType)
                    || StringUtils.equals(LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR, blockType)
                    || StringUtils.equals(LMConstants.LEAVE_BLOCK_TYPE.TIME_CALENDAR, blockType)) {

            	if (!TkContext.isDepartmentAdmin()
                        || HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), leaveBlock.getWorkArea(), new DateTime())) {
            		return true;
            	}
            } else if (LMConstants.LEAVE_BLOCK_TYPE.LEAVE_PAYOUT.equals(blockType)
                    || LMConstants.LEAVE_BLOCK_TYPE.DONATION_MAINT.equals(blockType)
                    || LMConstants.LEAVE_BLOCK_TYPE.BALANCE_TRANSFER.equals(blockType)
                    || LMConstants.LEAVE_BLOCK_TYPE.LEAVE_ADJUSTMENT_MAINT.equals(blockType)) {
                if (HrContext.isSystemAdmin()) {
                    return true;
                }
            }
            // kpme-1689
            if(StringUtils.equals(LMConstants.LEAVE_BLOCK_TYPE.ACCRUAL_SERVICE, blockType)
            		&& StringUtils.isNotEmpty(leaveBlock.getScheduleTimeOffId())
            		&& leaveBlock.getLeaveAmount().compareTo(BigDecimal.ZERO) == -1) {
            	if(HrContext.isSystemAdmin()) {
            		return true;
            	}
            	SystemScheduledTimeOff ssto = LmServiceLocator.getSysSchTimeOffService().getSystemScheduledTimeOff(leaveBlock.getScheduleTimeOffId());
            	if(ssto != null && !StringUtils.equals(LMConstants.UNUSED_TIME.NO_UNUSED, ssto.getUnusedTime())) {
            		return true;
            	}
            }
        }

        return false;
    }

    @Override
    public boolean canDeleteLeaveBlock(String principalId, LeaveBlock leaveBlock) {
        if (principalId != null) {
        	String documentId = leaveBlock.getDocumentId();
        	if (StringUtils.isBlank(documentId)) {
        		TimesheetDocumentHeader timesheetDocumentHeader = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeaderForDate(principalId, leaveBlock.getLeaveLocalDate().toDateTimeAtStartOfDay());
        		if (timesheetDocumentHeader != null) {
        			documentId = timesheetDocumentHeader.getDocumentId();
        		}
        	}
        	if (StringUtils.isNotBlank(documentId)) {
        		DocumentStatus documentStatus = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(documentId);
        		if (DocumentStatus.CANCELED.equals(documentStatus) || DocumentStatus.DISAPPROVED.equals(documentStatus)) {
        			return false;
        		}
        	}
        }
    	
    	if(StringUtils.equals(HrConstants.REQUEST_STATUS.DISAPPROVED, leaveBlock.getRequestStatus()))  {
            return false;
        }
    	if(canBankOrTransferSSTOUsage(leaveBlock)) {
    		return true;
    	}
        if (StringUtils.equals(HrConstants.REQUEST_STATUS.APPROVED, leaveBlock.getRequestStatus())) {
        	List<LeaveRequestDocument> docList= LmServiceLocator.getLeaveRequestDocumentService().getLeaveRequestDocumentsByLeaveBlockId(leaveBlock.getLmLeaveBlockId());
        	if(CollectionUtils.isEmpty(docList)) {
        		return false;	// not a leave request
        	}
        }
       
        return canEditLeaveBlock(principalId, leaveBlock);
    }
    
    @Override
	public boolean canBankOrTransferSSTOUsage(LeaveBlock lb) {
		// if it's an accrual generated ssto usage leave block which can be banked or transferred, and on a current leave calendar,
	    // it can be deleted so the accrualed amount can be banked
	    return canBankSSTOUsage(lb) || canTransferSSTOUsage(lb);
	}
    
    @Override
	public boolean canBankSSTOUsage(LeaveBlock lb) {
 	   if(lb.getAccrualGenerated() 
			   && StringUtils.isNotEmpty(lb.getScheduleTimeOffId()) 
			   && lb.getLeaveAmount().compareTo(BigDecimal.ZERO) < 0) {
		   SystemScheduledTimeOff ssto = LmServiceLocator.getSysSchTimeOffService().getSystemScheduledTimeOff(lb.getScheduleTimeOffId());
		   if(ssto != null && StringUtils.equals(ssto.getUnusedTime(), LMConstants.UNUSED_TIME.BANK)) {
			   String viewPrincipal = HrContext.getTargetPrincipalId();
			   CalendarEntry ce = HrServiceLocator.getCalendarEntryService()
						.getCurrentCalendarDatesForLeaveCalendar(viewPrincipal, new LocalDate().toDateTimeAtStartOfDay());
			   if(ce != null) {
				   if(!lb.getLeaveDate().before(ce.getBeginPeriodDate()) && !lb.getLeaveDate().after(ce.getEndPeriodDate())) {
					   return true;
				   }
			   }
			  
		   }
	   }
	   return false;
	}
    
    @Override
	public boolean canTransferSSTOUsage(LeaveBlock lb) {
	   if(lb.getAccrualGenerated() 
			   && StringUtils.isNotEmpty(lb.getScheduleTimeOffId()) 
			   && lb.getLeaveAmount().compareTo(BigDecimal.ZERO) < 0) {
		   SystemScheduledTimeOff ssto = LmServiceLocator.getSysSchTimeOffService().getSystemScheduledTimeOff(lb.getScheduleTimeOffId());
		   if(ssto != null && LMConstants.UNUSED_TIME.TRANSFER.equals(ssto.getUnusedTime())) {
			   String viewPrincipal = HrContext.getTargetPrincipalId();
			   CalendarEntry ce = HrServiceLocator.getCalendarEntryService()
						.getCurrentCalendarDatesForLeaveCalendar(viewPrincipal, new LocalDate().toDateTimeAtStartOfDay());
			   if(ce != null) {
				   if(!lb.getLeaveDate().before(ce.getBeginPeriodDate()) && !lb.getLeaveDate().after(ce.getEndPeriodDate())) {
					   return true;
				   }
			   }
			  
		   }
	   }
	   return false;
	}
	
	public PermissionService getPermissionService() {
		return permissionService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public LeaveCalendarService getLeaveCalendarService() {
		return leaveCalendarService;
	}

	public void setLeaveCalendarService(LeaveCalendarService leaveCalendarService) {
		this.leaveCalendarService = leaveCalendarService;
	}
	
	public LeaveRequestDocumentService getLeaveRequestDocumentService() {
		return leaveRequestDocumentService;
	}

	public void setLeaveRequestDocumentService(LeaveRequestDocumentService leaveRequestDocumentService) {
		this.leaveRequestDocumentService = leaveRequestDocumentService;
	}
	
}
