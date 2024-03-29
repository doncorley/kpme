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
package org.kuali.kpme.tklm.leave.approval.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hsqldb.lib.StringUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.simple.JSONValue;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.web.KPMEAction;
import org.kuali.kpme.tklm.common.CalendarApprovalForm;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;
import org.kuali.kpme.tklm.leave.workflow.LeaveCalendarDocumentHeader;
import org.kuali.rice.krad.util.GlobalVariables;

public class LeaveApprovalWSAction extends KPMEAction {

	 public ActionForward getLeaveSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LeaveApprovalWSActionForm laaf = (LeaveApprovalWSActionForm) form;
    	String docId = laaf.getDocumentId();
    	LeaveCalendarDocumentHeader lcdh = LmServiceLocator.getLeaveCalendarDocumentHeaderService().getDocumentHeader(docId);
    	if(lcdh != null) {
    		List<Map<String, Object>> detailMap = LmServiceLocator.getLeaveApprovalService().getLeaveApprovalDetailSections(lcdh);
    		
    		String jsonString = JSONValue.toJSONString(detailMap);
    		laaf.setOutputString(jsonString);
    	}
    	
    	return mapping.findForward("ws");
	 }
	 
	  public ActionForward searchApprovalRows(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  LeaveApprovalWSActionForm laaf = (LeaveApprovalWSActionForm) form;
		  List<Map<String, String>> results = new LinkedList<Map<String, String>>();

		  List<String> workAreaList = new ArrayList<String>();
		  if (StringUtil.isEmpty(laaf.getSelectedWorkArea())) {
			  String principalId = GlobalVariables.getUserSession().getPrincipalId();
			
			  Set<Long> workAreas = new HashSet<Long>();
			  workAreas.addAll(HrServiceLocator.getKPMERoleService().getWorkAreasForPrincipalInRole(KPMENamespace.KPME_HR.getNamespaceCode(), principalId, KPMERole.APPROVER.getRoleName(), new DateTime(), true));
			  workAreas.addAll(HrServiceLocator.getKPMERoleService().getWorkAreasForPrincipalInRole(KPMENamespace.KPME_HR.getNamespaceCode(), principalId, KPMERole.APPROVER_DELEGATE.getRoleName(), new DateTime(), true));
			
			  for(Long workArea : workAreas) {
				  workAreaList.add(workArea.toString());
			  }
		  } else {
			  workAreaList.add(laaf.getSelectedWorkArea());
		  }
		  
		  LocalDate endDate = laaf.getCalendarEntry().getEndPeriodFullDateTime().toLocalDate();
		  LocalDate beginDate = laaf.getCalendarEntry().getBeginPeriodFullDateTime().toLocalDate();
	        
		  List<String> principalIds = LmServiceLocator.getLeaveApprovalService()
				  .getLeavePrincipalIdsWithSearchCriteria(workAreaList, laaf.getSelectedPayCalendarGroup(),
						  endDate, beginDate, endDate); 

		  if (StringUtils.equals(laaf.getSearchField(), CalendarApprovalForm.ORDER_BY_PRINCIPAL)) {
			  for (String id : principalIds) {
				  if(StringUtils.contains(id, laaf.getSearchTerm())) {
					  Map<String, String> labelValue = new HashMap<String, String>();
					  labelValue.put("id", id);
					  labelValue.put("result", id);
					  results.add(labelValue);
				  }
			  }
		  } else if (StringUtils.equals(laaf.getSearchField(), CalendarApprovalForm.ORDER_BY_DOCID)) {
			  Map<String, LeaveCalendarDocumentHeader> principalDocumentHeaders =
					  LmServiceLocator.getLeaveApprovalService().getPrincipalDocumentHeader(principalIds, beginDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());
	
			  for (Map.Entry<String,LeaveCalendarDocumentHeader> entry : principalDocumentHeaders.entrySet()) {
				  if (StringUtils.contains(entry.getValue().getDocumentId(), laaf.getSearchTerm())) {
					  Map<String, String> labelValue = new HashMap<String, String>();
//					  labelValue.put("id", entry.getValue().getDocumentId() + " (" + entry.getValue().getPrincipalId() + ")");
					  //removing principalId to make select/submit the result from dropdown work
					  labelValue.put("id", entry.getValue().getDocumentId());
					  labelValue.put("result", entry.getValue().getPrincipalId());
					  results.add(labelValue);
				  }
			  }
		  }
		
	      laaf.setOutputString(JSONValue.toJSONString(results));
	        
	      return mapping.findForward("ws");
	    }
}
