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
package org.kuali.kpme.tklm.time.rules.lunch.department.dao;

import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.tklm.time.rules.lunch.department.DeptLunchRule;

public interface DepartmentLunchRuleDao {
	public DeptLunchRule getDepartmentLunchRule(String dept, Long workArea, String principalId, Long jobNumber, LocalDate asOfDate);
	
	public DeptLunchRule getDepartmentLunchRule(String tkDeptLunchRuleId);

    List<DeptLunchRule> getDepartmentLunchRules(String dept, String workArea, String principalId, String jobNumber, 
    											LocalDate fromEffdt, LocalDate toEffdt, String active, String showHistory);
}
