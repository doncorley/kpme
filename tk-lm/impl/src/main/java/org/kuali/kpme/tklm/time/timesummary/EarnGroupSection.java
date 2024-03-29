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
package org.kuali.kpme.tklm.time.timesummary;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kuali.kpme.core.util.HrConstants;

public class EarnGroupSection implements Serializable {
	private String earnGroup;
	private Map<String, EarnCodeSection> earnCodeToEarnCodeSectionMap = new HashMap<String, EarnCodeSection>();
	private List<EarnCodeSection> earnCodeSections = new ArrayList<EarnCodeSection>();
	private List<BigDecimal> totals = new ArrayList<BigDecimal>();
	public String getEarnGroup() {
		return earnGroup;
	}
	public void setEarnGroup(String earnGroup) {
		this.earnGroup = earnGroup;
	}

	public List<BigDecimal> getTotals() {
		return totals;
	}
	
	public void addEarnCodeSection(EarnCodeSection earnCodeSection, List<Boolean> dayArrangements){
		for(AssignmentRow assignRow : earnCodeSection.getAssignmentsRows()) {
			int i = 0;
			for (AssignmentColumn assignmentColumn : assignRow.getAssignmentColumns()) {
				BigDecimal value = totals.get(i).add(assignmentColumn.getTotal(), HrConstants.MATH_CONTEXT);
				totals.set(i, value.setScale(HrConstants.BIG_DECIMAL_SCALE, HrConstants.BIG_DECIMAL_SCALE_ROUNDING));
				i++;
			}
		}
		earnCodeToEarnCodeSectionMap.put(earnCodeSection.getEarnCode(), earnCodeSection);
		earnCodeSections.add(earnCodeSection);
		
		BigDecimal periodTotal = BigDecimal.ZERO;
		for(int i =0;i<totals.size()-2;i++){
			if(dayArrangements.get(i)){
				periodTotal = periodTotal.add(totals.get(i), HrConstants.MATH_CONTEXT);
			}
		}
		totals.set(totals.size()-1, periodTotal);
	}
	public Map<String, EarnCodeSection> getEarnCodeToEarnCodeSectionMap() {
		return earnCodeToEarnCodeSectionMap;
	}
	public void setEarnCodeToEarnCodeSectionMap(
			Map<String, EarnCodeSection> earnCodeToEarnCodeSectionMap) {
		this.earnCodeToEarnCodeSectionMap = earnCodeToEarnCodeSectionMap;
	}
	
	
	public void addToTotal(int index, BigDecimal hrs){
		BigDecimal total = getTotals().get(index);
		total = total.add(hrs, HrConstants.MATH_CONTEXT);
		getTotals().set(index, total);
	}
	public List<EarnCodeSection> getEarnCodeSections() {
		return earnCodeSections;
	}
	public void setEarnCodeSections(List<EarnCodeSection> earnCodeSections) {
		this.earnCodeSections = earnCodeSections;
	}
}
