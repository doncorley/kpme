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
package org.kuali.kpme.tklm.time.service.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.assignment.AssignmentDescriptionKey;
import org.kuali.kpme.core.department.Department;
import org.kuali.kpme.core.earncode.security.EarnCodeSecurity;
import org.kuali.kpme.core.job.Job;
import org.kuali.kpme.core.paytype.PayType;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.service.permission.HrPermissionServiceBase;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.core.workarea.WorkArea;
import org.kuali.kpme.tklm.time.rules.timecollection.TimeCollectionRule;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.timesheet.service.TimesheetService;
import org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.krad.util.GlobalVariables;

public class TKPermissionServiceImpl extends HrPermissionServiceBase implements TKPermissionService {
	
	private PermissionService permissionService;
	private TimesheetService timesheetService;

	@Override
	public boolean isAuthorized(String principalId, String permissionName, DateTime asOfDate) {
		Map<String, String> qualification = new HashMap<String, String>();
		
		return isAuthorized(principalId, permissionName, qualification, asOfDate);
	}

	@Override
	public boolean isAuthorized(String principalId, String permissionName, Map<String, String> qualification, DateTime asOfDate) {
		return getPermissionService().isAuthorized(principalId, KPMENamespace.KPME_TK.getNamespaceCode(), permissionName, qualification);
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
    public boolean canEditTimeBlock(String principalId, TimeBlock timeBlock) {
        if (principalId != null) {

        	// if the sys admin user is working on his own time block, do not grant edit permission without further checking
            if (HrContext.isSystemAdmin() && !timeBlock.getPrincipalId().equals(principalId)) {
            	return true;
            }
            
            if (StringUtils.isNotBlank(timeBlock.getDocumentId())) {
            	DocumentStatus documentStatus = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(timeBlock.getDocumentId());
            	if (DocumentStatus.CANCELED.equals(documentStatus) || DocumentStatus.DISAPPROVED.equals(documentStatus)) {
            		return false;
            	}
            }
            
            Job job = HrServiceLocator.getJobService().getJob(
                    HrContext.getTargetPrincipalId(), timeBlock.getJobNumber(),
                    timeBlock.getEndDateTime().toLocalDate());
            PayType payType = HrServiceLocator.getPayTypeService().getPayType(
                    job.getHrPayType(), timeBlock.getEndDateTime().toLocalDate());
            
        	if (HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.REVIEWER.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), timeBlock.getWorkArea(), new DateTime())) {
        		
                if (StringUtils.equals(payType.getRegEarnCode(),
                		timeBlock.getEarnCode())) {
                    TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),timeBlock.getWorkArea(),timeBlock.getBeginDateTime().toLocalDate());

                    if (tcr == null || tcr.isClockUserFl()) {
                        //if there is only 1 assignment here, it isn't editable.
                        TimesheetDocument td = TkServiceLocator.getTimesheetService().getTimesheetDocument(timeBlock.getDocumentId());
                        Map<String, String> assignments = td.getAssignmentDescriptions(false);
                        if (assignments.size() <= 1) {
                            return false;
                        }
                    }
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            		timeBlock.getEarnCode())) {
                        return true;
                    }
                }
            }

            //taking out for KPME-2427
            // if the time block is generated by clock actions, do not allow it to be edited/deleted
			//if(timeBlock.getClockLogCreated()) {
			//		return false;
			//}

            if (principalId.equals(HrContext.getTargetPrincipalId())) {

                if (StringUtils.equals(payType.getRegEarnCode(), timeBlock.getEarnCode())) {
                    //If you are a clock user and you have only one assignment you should not be allowed to change the assignment
                    //TODO eventually move this logic to one concise place for editable portions of the timeblock
                    List<Assignment> assignments = HrServiceLocator.getAssignmentService().getAssignments(HrContext.getPrincipalId(),timeBlock.getBeginDateTime().toLocalDate());
                    if (assignments.size() == 1) {
                    	TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),timeBlock.getWorkArea(),job.getHrPayType(),timeBlock.getBeginDateTime().toLocalDate());
                        return tcr != null && !tcr.isClockUserFl();
                    } else {
                        return true;
                    }
                }

                List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isEmployee()
                            && StringUtils.equals(dec.getEarnCode(),
                            		timeBlock.getEarnCode())) {
                        return true;
                    }
                }
            }

        }

        return false;
    }
    
    @Override
    public boolean canEditTimeBlockAllFields(String principalId, TimeBlock timeBlock) {
        if (principalId != null) {

            if (HrContext.isSystemAdmin()) {
                return true;
            }
            
            if (StringUtils.isNotBlank(timeBlock.getDocumentId())) {
            	DocumentStatus documentStatus = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(timeBlock.getDocumentId());
            	if (DocumentStatus.CANCELED.equals(documentStatus) || DocumentStatus.DISAPPROVED.equals(documentStatus)) {
            		return false;
            	}
            }

            Job job = HrServiceLocator.getJobService().getJob(
                    HrContext.getTargetPrincipalId(), timeBlock.getJobNumber(),
                    timeBlock.getEndDateTime().toLocalDate());
            PayType payType = job.getPayTypeObj();

        	if (HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.REVIEWER.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), timeBlock.getWorkArea(), new DateTime())) {
        		
                if (StringUtils.equals(payType.getRegEarnCode(), timeBlock.getEarnCode())) {
                    TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),timeBlock.getWorkArea(),timeBlock.getBeginDateTime().toLocalDate());
                    
                    if (tcr != null && !tcr.isClockUserFl()) {
                    	return true;
                    } else {
                        return false;
                    }
                }

                List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            		timeBlock.getEarnCode())) {
                        return true;
                    }
                }
            }

            if (principalId.equals(HrContext.getTargetPrincipalId())
                    && timeBlock.getClockLogCreated()) {
                if (StringUtils.equals(payType.getRegEarnCode(),
                        timeBlock.getEarnCode())) {
                    return false;
                }
            }

            if (principalId.equals(HrContext.getTargetPrincipalId())
            		&& !timeBlock.getClockLogCreated()) {
            	if (StringUtils.equals(payType.getRegEarnCode(),
            			timeBlock.getEarnCode())) {
            		return true;
            	}
            	
                List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isEmployee()
                            && StringUtils.equals(dec.getEarnCode(),
                            		timeBlock.getEarnCode())) {
                        return true;
                    }
                }
            }

        }

        return false;
    }

    @Override
    public boolean canDeleteTimeBlock(String principalId, TimeBlock timeBlock) {
        if (principalId != null) {

        	// if the sys admin user is working on his own time block, do not grant delete permission without further checking
            if (HrContext.isSystemAdmin()&& !timeBlock.getPrincipalId().equals(principalId)) {
            	return true;
            }
            
            if (StringUtils.isNotBlank(timeBlock.getDocumentId())) {
            	DocumentStatus documentStatus = KewApiServiceLocator.getWorkflowDocumentService().getDocumentStatus(timeBlock.getDocumentId());
            	if (DocumentStatus.CANCELED.equals(documentStatus) || DocumentStatus.DISAPPROVED.equals(documentStatus)) {
            		return false;
            	}
            }
            
            Job job = HrServiceLocator.getJobService().getJob(
                    HrContext.getTargetPrincipalId(), timeBlock.getJobNumber(),
                    timeBlock.getEndDateTime().toLocalDate());
            PayType payType = HrServiceLocator.getPayTypeService().getPayType(
                    job.getHrPayType(), timeBlock.getEndDateTime().toLocalDate());

        	if (HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.REVIEWER.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), timeBlock.getWorkArea(), new DateTime())) {

                if (StringUtils.equals(payType.getRegEarnCode(),
                		timeBlock.getEarnCode())) {
                    return true;
                }

                List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                        .getEarnCodeSecurityService().getEarnCodeSecurities(
                                job.getDept(), job.getHrSalGroup(),
                                job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
                for (EarnCodeSecurity dec : deptEarnCodes) {
                    if (dec.isApprover()
                            && StringUtils.equals(dec.getEarnCode(),
                            		timeBlock.getEarnCode())) {
                        return true;
                    }
                }
            }

//            // If the timeblock was created by the employee himeself and is a sync timeblock,
//            // the user can't delete the timeblock
//            if (userId.equals(HrContext.getTargetPrincipalId())
//                    && tb.getClockLogCreated()) {
//                return false;
//            // But if the timeblock was created by the employee himeself and is an async timeblock,
//            // the user should be able to delete that timeblock
//            } else if (userId.equals(HrContext.getTargetPrincipalId()) && !tb.getClockLogCreated() ) {
//                return true;
//            } else {
            
            // if the time block is generated by clock actions, do not allow it to be edited/deleted
			if(timeBlock.getClockLogCreated()) {
					return false;
			}

            //if on a regular earncode and the user is a clock user and this is the users timesheet, do not allow to be deleted
            if (StringUtils.equals(payType.getRegEarnCode(), timeBlock.getEarnCode())) {
            	TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(job.getDept(),timeBlock.getWorkArea(),payType.getPayType(),timeBlock.getEndDateTime().toLocalDate());
            	
            	if (tcr == null || tcr.isClockUserFl()) {
            		if (StringUtils.equals(principalId,HrContext.getTargetPrincipalId())) {
	                    return false;
	                }  else {
	                    return true;
	                }
                }
            }
            
            
            //KPME-2264 -
            // EE's should be able to remove timeblocks added via the time detail calendar only after checking prior conditions,
            if (principalId.equals(HrContext.getTargetPrincipalId())) {
            	return true;
            }      

            List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator
                    .getEarnCodeSecurityService().getEarnCodeSecurities(
                            job.getDept(), job.getHrSalGroup(),
                            job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
            for (EarnCodeSecurity dec : deptEarnCodes) {
            	if (HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.REVIEWER.getRoleName(), timeBlock.getWorkArea(), new DateTime())
            			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), timeBlock.getWorkArea(), new DateTime())
            			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), timeBlock.getWorkArea(), new DateTime())) {
	                
            		if (dec.isEmployee()
	                        && StringUtils.equals(dec.getEarnCode(),
	                        		timeBlock.getEarnCode())) {
	                    return true;
	                }
            	}
            }

        }

        return false;
    }
    
    @Override
    public boolean canEditOvertimeEarnCode(TimeBlock timeBlock) {
        String principalId = GlobalVariables.getUserSession().getPrincipalId();
        Long workArea = timeBlock.getWorkArea();
    	WorkArea workAreaObj = HrServiceLocator.getWorkAreaService().getWorkArea(workArea, timeBlock.getEndDateTime().toLocalDate());
    	String department = workAreaObj.getDept();
    	Department departmentObj = HrServiceLocator.getDepartmentService().getDepartment(department, LocalDate.now());
		String location = departmentObj != null ? departmentObj.getLocation() : null;
		
    	if (StringUtils.equals(workAreaObj.getOvertimeEditRole(), "Employee")) {
            return true;
        } else if (StringUtils.equals(workAreaObj.getOvertimeEditRole(), KPMERole.APPROVER.getRoleName()) ||
                StringUtils.equals(workAreaObj.getOvertimeEditRole(), KPMERole.APPROVER_DELEGATE.getRoleName())) {
            return HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), workArea, new DateTime())
            		|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(principalId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), workArea, new DateTime());
        } else {
            return HrServiceLocator.getKPMERoleService().principalHasRoleInDepartment(principalId, KPMENamespace.KPME_TK.getNamespaceCode(), KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInDepartment(principalId, KPMENamespace.KPME_LM.getNamespaceCode(), KPMERole.LEAVE_DEPARTMENT_ADMINISTRATOR.getRoleName(), department, new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInLocation(principalId, KPMENamespace.KPME_TK.getNamespaceCode(), KPMERole.TIME_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInLocation(principalId, KPMENamespace.KPME_LM.getNamespaceCode(), KPMERole.LEAVE_LOCATION_ADMINISTRATOR.getRoleName(), location, new DateTime());
        }
    }
    
    /*
     * @see org.kuali.kpme.tklm.time.permissions.TkPermissionsService#canEditRegEarnCode(org.kuali.kpme.tklm.time.timeblock.TimeBlock)
     * this method is used in calendar.tag
     * it's only used when a user is working on its own timesheet, regular earn code cannot be editable on clock entered time block
     */
    @Override
    public boolean canEditRegEarnCode(TimeBlock tb) {
    	AssignmentDescriptionKey adk = new AssignmentDescriptionKey(tb.getJobNumber(), tb.getWorkArea(), tb.getTask());
        Assignment anAssignment = HrServiceLocator.getAssignmentService().getAssignment(adk, tb.getBeginDateTime().toLocalDate());
        if(anAssignment != null) {
        	// use timesheet's end date to get Time Collection Rule
        	TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(tb.getDocumentId());
        	DateTime aDate =  tb.getBeginDateTime();
        	if(tdh != null && tdh.getEndDate() != null) {
        		aDate = tdh.getEndDateTime();
        	}
        	
        	TimeCollectionRule tcr = TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(anAssignment.getDept(), anAssignment.getWorkArea(), anAssignment.getJob().getHrPayType(), aDate.toLocalDate());
        	if (tcr == null || tcr.isClockUserFl()) {
        		// use assignment to get the payType object, then check if the regEarnCode of the paytyep matches the earn code of the timeblock
        		// if they do match, then return false
        		PayType pt = HrServiceLocator.getPayTypeService().getPayType(anAssignment.getJob().getHrPayType(), anAssignment.getJob().getEffectiveLocalDate());
        		if(pt != null && pt.getRegEarnCode().equals(tb.getEarnCode())) {
        			return false;
        		}
        	}
        }
    	return true;
    }

	public PermissionService getPermissionService() {
		return permissionService;
	}

	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}

	public TimesheetService getTimesheetService() {
		return timesheetService;
	}

	public void setTimesheetService(TimesheetService timesheetService) {
		this.timesheetService = timesheetService;
	}
	
}
