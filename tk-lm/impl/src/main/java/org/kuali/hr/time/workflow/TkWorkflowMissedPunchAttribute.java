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
package org.kuali.hr.time.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.assignment.AssignmentDescriptionKey;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.tklm.common.TkConstants;
import org.kuali.kpme.tklm.time.missedpunch.MissedPunch;
import org.kuali.kpme.tklm.time.missedpunch.MissedPunchDocument;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.AbstractRoleAttribute;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

@Deprecated
public class TkWorkflowMissedPunchAttribute extends AbstractRoleAttribute {

	private static final long serialVersionUID = 8994254411764426802L;

	@Override
	public List<String> getQualifiedRoleNames(String roleName, DocumentContent documentContent) {
		List<String> roles = new ArrayList<String>();
		roles.add(roleName);
		return roles;
	}

	@Override
	public ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, String roleName, String qualifiedRole) {
		ResolvedQualifiedRole rqr = new ResolvedQualifiedRole();
		List<Id> principals = new ArrayList<Id>();

		try {
			String documentId = routeContext.getDocument().getDocumentId();
			Document document = (MissedPunchDocument) KRADServiceLocatorWeb.getDocumentService().getByDocumentHeaderIdSessionless(documentId);
	        MissedPunchDocument missedPunchDocument = (MissedPunchDocument) document;
	        MissedPunch missedPunch = missedPunchDocument.getMissedPunch();
	
	        AssignmentDescriptionKey assignKey = AssignmentDescriptionKey.get(missedPunch.getAssignmentKey());
	        String tsDocIdString = missedPunch.getTimesheetDocumentId();
	
	        if (tsDocIdString != null && assignKey != null) {
	            TimesheetDocument tdoc = TkServiceLocator.getTimesheetService().getTimesheetDocument(tsDocIdString);
	            if (tdoc != null) {
	                Assignment assignment = tdoc.getAssignment(assignKey);
	                if (assignment != null) {
	            		List<RoleMember> roleMembers = new ArrayList<RoleMember>();
	            		
	            		if (TkConstants.ROLE_TK_APPROVER.equals(roleName)) {
	            	        roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInWorkArea(KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), assignment.getWorkArea(), new DateTime(), true));
	            	        roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInWorkArea(KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), assignment.getWorkArea(), new DateTime(), true));
	            		}
	        	
	        	        for (RoleMember roleMember : roleMembers) {
	        	        	principals.add(new PrincipalId(roleMember.getMemberId()));
	        		    }
	                } else {
	                    throw new RuntimeException("Could not obtain Assignment.");
	                }
	            } else {
	                throw new RuntimeException("Could not obtain TimesheetDocument.");
	            }
	        } else {
	            throw new RuntimeException("Could not obtain Timesheet Document ID or Assignment ID");
	        }
		} catch (WorkflowException we) {
			we.printStackTrace();
        }

		if (principals.size() == 0) {
			throw new RuntimeException("No principals to route to. Push to exception routing.");
		}
		
		rqr.setRecipients(principals);
		
		return rqr;
	}

	@Override
	public List<RoleName> getRoleNames() {
        return Collections.emptyList();
	}
	
}
