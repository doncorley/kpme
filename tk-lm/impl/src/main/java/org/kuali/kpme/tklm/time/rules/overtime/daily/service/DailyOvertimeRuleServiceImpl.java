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
package org.kuali.kpme.tklm.time.rules.overtime.daily.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.department.Department;
import org.kuali.kpme.core.job.Job;
import org.kuali.kpme.core.permission.KPMEPermissionTemplate;
import org.kuali.kpme.core.role.KPMERoleMemberAttribute;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.time.rules.overtime.daily.DailyOvertimeRule;
import org.kuali.kpme.tklm.time.rules.overtime.daily.dao.DailyOvertimeRuleDao;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;
import org.kuali.kpme.tklm.time.timehourdetail.TimeHourDetail;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.util.TkTimeBlockAggregate;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

public class DailyOvertimeRuleServiceImpl implements DailyOvertimeRuleService {

    private static final Logger LOG = Logger.getLogger(DailyOvertimeRuleServiceImpl.class);

	private DailyOvertimeRuleDao dailyOvertimeRuleDao = null;

	public void saveOrUpdate(DailyOvertimeRule dailyOvertimeRule) {
		dailyOvertimeRuleDao.saveOrUpdate(dailyOvertimeRule);
	}

	public void saveOrUpdate(List<DailyOvertimeRule> dailyOvertimeRules) {
		dailyOvertimeRuleDao.saveOrUpdate(dailyOvertimeRules);
	}

	@Override
	/**
	 * Search for the valid Daily Overtime Rule, wild cards are allowed on
	 * location
	 * paytype
	 * department
	 * workArea
	 *
	 * asOfDate is required.
	 */
	public DailyOvertimeRule getDailyOvertimeRule(String location, String paytype, String dept, Long workArea, LocalDate asOfDate) {
		DailyOvertimeRule dailyOvertimeRule = null;

		//		l, p, d, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, paytype, dept, workArea, asOfDate);

		//		l, p, d, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, paytype, dept, -1L, asOfDate);

		//		l, p, *, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, paytype, "%", workArea, asOfDate);

		//		l, p, *, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, paytype, "%", -1L, asOfDate);

		//		l, *, d, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, "%", dept, workArea, asOfDate);

		//		l, *, d, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, "%", dept, -1L, asOfDate);

		//		l, *, *, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, "%", "%", workArea, asOfDate);

		//		l, *, *, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule(location, "%", "%", -1L, asOfDate);

		//		*, p, d, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", paytype, dept, workArea, asOfDate);

		//		*, p, d, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", paytype, dept, -1L, asOfDate);

		//		*, p, *, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", paytype, "%", workArea, asOfDate);

		//		*, p, *, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", paytype, "%", -1L, asOfDate);

		//		*, *, d, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", "%", dept, workArea, asOfDate);

		//		*, *, d, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", "%", dept, -1L, asOfDate);

		//		*, *, *, w
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", "%", "%", workArea, asOfDate);

		//		*, *, *, -1
		if (dailyOvertimeRule == null)
			dailyOvertimeRule = dailyOvertimeRuleDao.findDailyOvertimeRule("%", "%", "%", -1L, asOfDate);

		return dailyOvertimeRule;
	}



	public void setDailyOvertimeRuleDao(DailyOvertimeRuleDao dailyOvertimeRuleDao) {
		this.dailyOvertimeRuleDao = dailyOvertimeRuleDao;
	}

	private Assignment getIdentifyingKey(TimeBlock block, LocalDate asOfDate, String principalId) {
		List<Assignment> lstAssign = HrServiceLocator.getAssignmentService().getAssignments(principalId, asOfDate);

		for(Assignment assign : lstAssign){
			if((assign.getJobNumber().compareTo(block.getJobNumber()) == 0) && (assign.getWorkArea().compareTo(block.getWorkArea()) == 0)){
				return assign;
			}
		}
		return null;
	}


	public void processDailyOvertimeRules(TimesheetDocument timesheetDocument, TkTimeBlockAggregate timeBlockAggregate){
		Map<DailyOvertimeRule, List<Assignment>> mapDailyOvtRulesToAssignment = new HashMap<DailyOvertimeRule, List<Assignment>>();

		for(Assignment assignment : timesheetDocument.getAssignments()) {
			Job job = assignment.getJob();
			DailyOvertimeRule dailyOvertimeRule = getDailyOvertimeRule(job.getLocation(), job.getHrPayType(), job.getDept(), assignment.getWorkArea(), timesheetDocument.getDocEndDate());

			if(dailyOvertimeRule !=null) {
				if(mapDailyOvtRulesToAssignment.containsKey(dailyOvertimeRule)){
					List<Assignment> lstAssign = mapDailyOvtRulesToAssignment.get(dailyOvertimeRule);
					lstAssign.add(assignment);
					mapDailyOvtRulesToAssignment.put(dailyOvertimeRule, lstAssign);
				}  else {
					List<Assignment> lstAssign = new ArrayList<Assignment>();
					lstAssign.add(assignment);
					mapDailyOvtRulesToAssignment.put(dailyOvertimeRule, lstAssign);
				}
			}
		}

		//Quick bail
		if(mapDailyOvtRulesToAssignment.isEmpty()){
			return;
		}

		// TODO: We iterate Day by Day
		for(List<TimeBlock> dayTimeBlocks : timeBlockAggregate.getDayTimeBlockList()){

			if (dayTimeBlocks.size() == 0)
				continue;

			// 1: ... bucketing by (DailyOvertimeRule -> List<TimeBlock>)
			Map<DailyOvertimeRule,List<TimeBlock>> dailyOvtRuleToDayTotals = new HashMap<DailyOvertimeRule,List<TimeBlock>>();
			for(TimeBlock timeBlock : dayTimeBlocks) {
				Assignment assign = this.getIdentifyingKey(timeBlock, timesheetDocument.getAsOfDate(), timesheetDocument.getPrincipalId());
				for(Map.Entry<DailyOvertimeRule, List<Assignment>> entry : mapDailyOvtRulesToAssignment.entrySet()){
					List<Assignment> lstAssign = entry.getValue();

                    // for this kind of operation to work, equals() and hashCode() need to
                    // be over ridden for the object of comparison.
					if(lstAssign.contains(assign)){
                        // comparison here will always work, because we're comparing
                        // against our existing instantiation of the object.
						if(dailyOvtRuleToDayTotals.get(entry.getKey()) != null){
							List<TimeBlock> lstTimeBlock = dailyOvtRuleToDayTotals.get(entry.getKey());
							lstTimeBlock.add(timeBlock);
							dailyOvtRuleToDayTotals.put(entry.getKey(), lstTimeBlock);
						} else {
							List<TimeBlock> lstTimeBlock = new ArrayList<TimeBlock>();
							lstTimeBlock.add(timeBlock);
							dailyOvtRuleToDayTotals.put(entry.getKey(), lstTimeBlock);
						}
					}
				}
			}

			for(DailyOvertimeRule dr : mapDailyOvtRulesToAssignment.keySet() ){
				Set<String> fromEarnGroup = HrServiceLocator.getEarnCodeGroupService().getEarnCodeListForEarnCodeGroup(dr.getFromEarnGroup(), timesheetDocument.getCalendarEntry().getEndPeriodFullDateTime().toLocalDate());
				List<TimeBlock> blocksForRule = dailyOvtRuleToDayTotals.get(dr);
				if (blocksForRule == null || blocksForRule.size() == 0)
					continue; // skip to next rule and check for valid blocks.
				sortTimeBlocksNatural(blocksForRule);

				// 3: Iterate over the timeblocks, apply the rule when necessary.
				BigDecimal hours = BigDecimal.ZERO;
				List<TimeBlock> applicationList = new LinkedList<TimeBlock>();
				TimeBlock previous = null;
				for (TimeBlock block : blocksForRule) {
					if (exceedsMaxGap(previous, block, dr.getMaxGap())) {
						apply(hours, applicationList, dr, fromEarnGroup);
						applicationList.clear();
						hours = BigDecimal.ZERO;
						previous = null; // reset our chain
					} else {
						previous = block; // build up our chain
					}
                    applicationList.add(block);
					for (TimeHourDetail thd : block.getTimeHourDetails())
						if (fromEarnGroup.contains(thd.getEarnCode()))
							hours = hours.add(thd.getHours(), HrConstants.MATH_CONTEXT);
				}
				// when we run out of blocks, we may have more to apply.
				apply(hours, applicationList, dr, fromEarnGroup);
			}
		}
	}

	/**
	 * Reverse sorts blocks and applies hours to matching earn codes in the
	 * time hour detail entries.
	 *
	 * @param hours Total number of Daily Overtime Hours to apply.
	 * @param blocks Time blocks found to need rule application.
	 * @param rule The rule we are applying.
	 * @param earnGroup Earn group we've already loaded for this rule.
	 */
	private void apply(BigDecimal hours, List<TimeBlock> blocks, DailyOvertimeRule rule, Set<String> earnGroup) {
		sortTimeBlocksInverse(blocks);
		if (blocks != null && blocks.size() > 0)
			if (hours.compareTo(rule.getMinHours()) >= 0) {
                BigDecimal remaining = hours.subtract(rule.getMinHours(), HrConstants.MATH_CONTEXT);
				for (TimeBlock block : blocks) {
					remaining = applyOvertimeToTimeBlock(block, rule.getEarnCode(), earnGroup, remaining);
                }
                if (remaining.compareTo(BigDecimal.ZERO) > 0) {
                    LOG.warn("Hours remaining that were unapplied in DailyOvertimeRule.");
                }
            }
	}


	/**
	 * Method to apply (if applicable) overtime additions to the indiciated TimeBlock.  TimeBlock
	 * earn code is checked against the convertFromEarnCodes Set.
	 *
	 * @param block
	 * @param otEarnCode
	 * @param convertFromEarnCodes
	 * @param otHours
	 *
	 * @return The amount of overtime hours remaining to be applied.  (BigDecimal is immutable)
	 */
	private BigDecimal applyOvertimeToTimeBlock(TimeBlock block, String otEarnCode, Set<String> convertFromEarnCodes, BigDecimal otHours) {
		BigDecimal applied = BigDecimal.ZERO;

		if (otHours.compareTo(BigDecimal.ZERO) <= 0)
			return BigDecimal.ZERO;

		List<TimeHourDetail> details = block.getTimeHourDetails();
		List<TimeHourDetail> addDetails = new LinkedList<TimeHourDetail>();
		for (TimeHourDetail detail : details) {
			if (convertFromEarnCodes.contains(detail.getEarnCode())) {
				// n = detailHours - otHours
				BigDecimal n = detail.getHours().subtract(otHours, HrConstants.MATH_CONTEXT);
				// n >= 0 (meaning there are greater than or equal amount of Detail hours vs. OT hours, so apply all OT hours here)
				// n < = (meaning there were more OT hours than Detail hours, so apply only the # of hours in detail and update applied.
				if (n.compareTo(BigDecimal.ZERO) >= 0) {
					// if
					applied = otHours;
				} else {
					applied = detail.getHours();
				}

				// Make a new TimeHourDetail with the otEarnCode with "applied" hours
				TimeHourDetail timeHourDetail = new TimeHourDetail();
				timeHourDetail.setHours(applied);
				timeHourDetail.setEarnCode(otEarnCode);
				timeHourDetail.setTkTimeBlockId(block.getTkTimeBlockId());

				// Decrement existing matched FROM earn code.
				detail.setHours(detail.getHours().subtract(applied, HrConstants.MATH_CONTEXT));
				addDetails.add(timeHourDetail);
			}
		}

		if (addDetails.size() > 0) {
			details.addAll(addDetails);
			block.setTimeHourDetails(details);
		}

		return otHours.subtract(applied);
	}


	// TODO : Refactor this Copy-Pasta mess to util/comparator classes.

	private void sortTimeBlocksInverse(List<TimeBlock> blocks) {
		Collections.sort(blocks, new Comparator<TimeBlock>() { // Sort the Time Blocks
			public int compare(TimeBlock tb1, TimeBlock tb2) {
				if (tb1 != null && tb2 != null)
					return -1 * tb1.getBeginTimestamp().compareTo(tb2.getBeginTimestamp());
				return 0;
			}
		});
	}


	private void sortTimeBlocksNatural(List<TimeBlock> blocks) {
		Collections.sort(blocks, new Comparator<TimeBlock>() { // Sort the Time Blocks
			public int compare(TimeBlock tb1, TimeBlock tb2) {
				if (tb1 != null && tb2 != null)
					return tb1.getBeginTimestamp().compareTo(tb2.getBeginTimestamp());
				return 0;
			}
		});
	}

	/**
	 * Does the difference between the previous time blocks clock out time and the
	 * current time blocks clock in time exceed the max gap?
	 *
	 * @param previous If null, false is returned.
	 * @param current
	 * @param maxGap
	 * @return
	 */
	boolean exceedsMaxGap(TimeBlock previous, TimeBlock current, BigDecimal maxGap) {
		if (previous == null)
			return false;

		long difference = current.getBeginTimestamp().getTime() - previous.getEndTimestamp().getTime();
		BigDecimal gapHours = TKUtils.convertMillisToHours(difference);
		BigDecimal cmpGapHrs = TKUtils.convertMinutesToHours(maxGap);
		return (gapHours.compareTo(cmpGapHrs) > 0);
	}

	@Override
	public DailyOvertimeRule getDailyOvertimeRule(String tkDailyOvertimeRuleId) {
		return dailyOvertimeRuleDao.getDailyOvertimeRule(tkDailyOvertimeRuleId);
	}
	
	@Override
	public List<DailyOvertimeRule> getDailyOvertimeRules(String userPrincipalId, String dept, String workArea, String location, LocalDate fromEffdt, LocalDate toEffdt, String active, String showHist) {
		List<DailyOvertimeRule> results = new ArrayList<DailyOvertimeRule>();
        
    	List<DailyOvertimeRule> dailyOvertimeRuleObjs = dailyOvertimeRuleDao.getDailyOvertimeRules(dept, workArea, location, fromEffdt, toEffdt, active, showHist);
	
    	for (DailyOvertimeRule dailyOvertimeRuleObj : dailyOvertimeRuleObjs) {
        	String department = dailyOvertimeRuleObj.getDept();
        	Department departmentObj = HrServiceLocator.getDepartmentService().getDepartment(department, dailyOvertimeRuleObj.getEffectiveLocalDate());
        	String loc = departmentObj != null ? departmentObj.getLocation() : null;
        	
        	Map<String, String> roleQualification = new HashMap<String, String>();
        	roleQualification.put(KimConstants.AttributeConstants.PRINCIPAL_ID, userPrincipalId);
        	roleQualification.put(KPMERoleMemberAttribute.DEPARTMENT.getRoleMemberAttributeName(), department);
        	roleQualification.put(KPMERoleMemberAttribute.LOCATION.getRoleMemberAttributeName(), loc);
        	
        	if (!KimApiServiceLocator.getPermissionService().isPermissionDefinedByTemplate(KPMENamespace.KPME_WKFLW.getNamespaceCode(),
    				KPMEPermissionTemplate.VIEW_KPME_RECORD.getPermissionTemplateName(), new HashMap<String, String>())
    		  || KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(userPrincipalId, KPMENamespace.KPME_WKFLW.getNamespaceCode(),
    				  KPMEPermissionTemplate.VIEW_KPME_RECORD.getPermissionTemplateName(), new HashMap<String, String>(), roleQualification)) {
        		results.add(dailyOvertimeRuleObj);
        	}
    	}
    	
    	return results;
	}
}
