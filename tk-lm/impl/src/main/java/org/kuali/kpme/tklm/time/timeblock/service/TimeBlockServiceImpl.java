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
package org.kuali.kpme.tklm.time.timeblock.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.earncode.EarnCode;
import org.kuali.kpme.core.earncode.security.EarnCodeSecurity;
import org.kuali.kpme.core.job.Job;
import org.kuali.kpme.core.paytype.PayType;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.core.util.HrContext;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.common.TkConstants;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;
import org.kuali.kpme.tklm.time.timeblock.TimeBlockHistory;
import org.kuali.kpme.tklm.time.timeblock.dao.TimeBlockDao;
import org.kuali.kpme.tklm.time.timehourdetail.TimeHourDetail;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

public class TimeBlockServiceImpl implements TimeBlockService {

    private static final Logger LOG = Logger.getLogger(TimeBlockServiceImpl.class);
    private TimeBlockDao timeBlockDao;

    public void setTimeBlockDao(TimeBlockDao timeBlockDao) {
        this.timeBlockDao = timeBlockDao;
    }

    //This function is used to build timeblocks that span days
    public List<TimeBlock> buildTimeBlocksSpanDates(Assignment assignment, String earnCode, TimesheetDocument timesheetDocument,
                                                    DateTime beginDateTime, DateTime endDateTime, BigDecimal hours, BigDecimal amount, 
                                                    Boolean getClockLogCreated, Boolean getLunchDeleted, String spanningWeeks, String userPrincipalId) {
        DateTimeZone zone = HrServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
        DateTime beginDt = beginDateTime.withZone(zone);
        DateTime endDt = beginDt.toLocalDate().toDateTime(endDateTime.withZone(zone).toLocalTime(), zone);
        if (endDt.isBefore(beginDt)) endDt = endDt.plusDays(1);
    	
        List<Interval> dayInt = TKUtils.getDaySpanForCalendarEntry(timesheetDocument.getCalendarEntry());
        TimeBlock firstTimeBlock = new TimeBlock();
        List<TimeBlock> lstTimeBlocks = new ArrayList<TimeBlock>();
        
        DateTime endOfFirstDay = null; // KPME-2568
        long diffInMillis = 0; // KPME-2568
        
        for (Interval dayIn : dayInt) {
            if (dayIn.contains(beginDt)) {
                if (dayIn.contains(endDt) || dayIn.getEnd().equals(endDt)) {
                	// KPME-1446 if "Include weekends" check box is checked, don't add Sat and Sun to the timeblock list
                	if (StringUtils.isEmpty(spanningWeeks) && 
                		(dayIn.getStart().getDayOfWeek() == DateTimeConstants.SATURDAY ||dayIn.getStart().getDayOfWeek() == DateTimeConstants.SUNDAY)) {
                		// Get difference in millis for the next time block - KPME-2568
                		endOfFirstDay = endDt.withZone(zone);
                		diffInMillis = endOfFirstDay.minus(beginDt.getMillis()).getMillis();
                	} else {
                        firstTimeBlock = createTimeBlock(timesheetDocument, beginDateTime, endDt, assignment, earnCode, hours, amount, false, getLunchDeleted, userPrincipalId);
                        lstTimeBlocks.add(firstTimeBlock);                		
                	}
                } else {
                    //TODO move this to prerule validation
                    //throw validation error if this case met error
                }
            }
        }

        DateTime endTime = endDateTime.withZone(zone);
        if (firstTimeBlock.getEndDateTime() != null) { // KPME-2568
        	endOfFirstDay = firstTimeBlock.getEndDateTime().withZone(zone);
        	diffInMillis = endOfFirstDay.minus(beginDt.getMillis()).getMillis();
        }
        DateTime currTime = beginDt.plusDays(1);
        while (currTime.isBefore(endTime) || currTime.isEqual(endTime)) {
        	// KPME-1446 if "Include weekends" check box is checked, don't add Sat and Sun to the timeblock list
        	if (StringUtils.isEmpty(spanningWeeks) && 
        		(currTime.getDayOfWeek() == DateTimeConstants.SATURDAY || currTime.getDayOfWeek() == DateTimeConstants.SUNDAY)) {
        		// do nothing
        	} else {
	            TimeBlock tb = createTimeBlock(timesheetDocument, currTime, currTime.plus(diffInMillis), assignment, earnCode, hours, amount, false, getLunchDeleted, userPrincipalId);
	            lstTimeBlocks.add(tb);
        	}
        	currTime = currTime.plusDays(1);
        }
        return lstTimeBlocks;
    }


    public List<TimeBlock> buildTimeBlocks(Assignment assignment, String earnCode, TimesheetDocument timesheetDocument,
    									   DateTime beginDateTime, DateTime endDateTime, BigDecimal hours, BigDecimal amount, 
                                           Boolean getClockLogCreated, Boolean getLunchDeleted, String userPrincipalId) {

        //Create 1 or many timeblocks if the span of timeblocks exceed more than one
        //day that is determined by pay period day(24 hrs + period begin date)
        Interval firstDay = null;
        List<Interval> dayIntervals = TKUtils.getDaySpanForCalendarEntry(timesheetDocument.getCalendarEntry());
        List<TimeBlock> lstTimeBlocks = new ArrayList<TimeBlock>();
        DateTime currentDateTime = beginDateTime;

        for (Interval dayInt : dayIntervals) {
        	// the time period spans more than one day
            if (firstDay != null) {
            	if(!dayInt.contains(endDateTime)){
            		currentDateTime = dayInt.getStart();
            	} else if((dayInt.getStartMillis() - endDateTime.getMillis()) != 0){
            		TimeBlock tb = createTimeBlock(timesheetDocument, dayInt.getStart(), endDateTime, assignment, earnCode, hours, amount, getClockLogCreated, getLunchDeleted, userPrincipalId);
            		lstTimeBlocks.add(tb);
            		break;
            	}            		
            }
            if (dayInt.contains(currentDateTime)) {
                firstDay = dayInt;
                // KPME-361
                // added a condition to handle the time block which ends at 12a, e.g. a 10p-12a timeblock
                // this is the same fix as TkTimeBlockAggregate
                if (dayInt.contains(endDateTime) || (endDateTime.getMillis() == dayInt.getEnd().getMillis())) {
                    //create one timeblock if contained in one day interval
                	TimeBlock tb = createTimeBlock(timesheetDocument, currentDateTime, endDateTime, assignment, earnCode, hours, amount, getClockLogCreated, getLunchDeleted, userPrincipalId);
                    tb.setBeginDateTime(currentDateTime);
                    tb.setEndDateTime(endDateTime);
                    lstTimeBlocks.add(tb);
                    break;
                } else {
                    // create a timeblock that wraps the 24 hr day
                	TimeBlock tb = createTimeBlock(timesheetDocument, currentDateTime, dayInt.getEnd(), assignment, earnCode, hours, amount, getClockLogCreated, getLunchDeleted, userPrincipalId);
                    tb.setBeginDateTime(currentDateTime);
                    tb.setEndDateTime(firstDay.getEnd());
                    lstTimeBlocks.add(tb);
                }
            }
        }
        return lstTimeBlocks;
    }

    public void saveTimeBlocks(List<TimeBlock> oldTimeBlocks, List<TimeBlock> newTimeBlocks, String userPrincipalId) {
        List<TimeBlock> alteredTimeBlocks = new ArrayList<TimeBlock>();
        for (TimeBlock tb : newTimeBlocks) {
            boolean persist = true;
            for (TimeBlock tbOld : oldTimeBlocks) {
                if (tb.equals(tbOld)) {
                    persist = false;
                    break;
                }
            }
            if (persist) {
                alteredTimeBlocks.add(tb);
            }
        }
        
        Set<String> timeBlockIds = new HashSet<String>();
        
        for (TimeBlock timeBlock : alteredTimeBlocks) {
        	if(timeBlock.getTkTimeBlockId() != null) {
        		timeBlockIds.add(timeBlock.getTkTimeBlockId());
        	}
        	TkServiceLocator.getTimeHourDetailService().removeTimeHourDetails(timeBlock.getTkTimeBlockId());
            timeBlock.setUserPrincipalId(userPrincipalId);
        }
        
        List<TimeBlock> savedTimeBlocks = (List<TimeBlock>) KRADServiceLocator.getBusinessObjectService().save(alteredTimeBlocks);
        
        for (TimeBlock timeBlock : savedTimeBlocks) {
        	if(!timeBlockIds.contains(timeBlock.getTkTimeBlockId())) {
	            timeBlock.setTimeBlockHistories(createTimeBlockHistories(timeBlock, TkConstants.ACTIONS.ADD_TIME_BLOCK));
	            KRADServiceLocator.getBusinessObjectService().save(timeBlock.getTimeBlockHistories());
        	} else {
	            timeBlock.setTimeBlockHistories(createTimeBlockHistories(timeBlock, TkConstants.ACTIONS.UPDATE_TIME_BLOCK));
	            KRADServiceLocator.getBusinessObjectService().save(timeBlock.getTimeBlockHistories());
        	}
        }
    }

    public void saveTimeBlocks(List<TimeBlock> tbList) {
		 for (TimeBlock tb : tbList) {
	         TkServiceLocator.getTimeHourDetailService().removeTimeHourDetails(tb.getTkTimeBlockId());
	         timeBlockDao.saveOrUpdate(tb);
	         for(TimeBlockHistory tbh : tb.getTimeBlockHistories()){
	        	 TkServiceLocator.getTimeBlockHistoryService().saveTimeBlockHistory(tbh);
	         }
	     }
    }
    
    public void updateTimeBlock(TimeBlock tb) {
	         timeBlockDao.saveOrUpdate(tb);
    }


    public TimeBlock createTimeBlock(TimesheetDocument timesheetDocument, DateTime beginDateTime, DateTime endDateTime, Assignment assignment, String earnCode, BigDecimal hours, BigDecimal amount, Boolean clockLogCreated, Boolean lunchDeleted, String userPrincipalId) {
        DateTimeZone timezone = HrServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
        EarnCode earnCodeObj = HrServiceLocator.getEarnCodeService().getEarnCode(earnCode, timesheetDocument.getAsOfDate());

        TimeBlock tb = new TimeBlock();
        tb.setDocumentId(timesheetDocument.getDocumentHeader().getDocumentId());
        tb.setPrincipalId(timesheetDocument.getPrincipalId());
        tb.setJobNumber(assignment.getJobNumber());
        tb.setWorkArea(assignment.getWorkArea());
        tb.setTask(assignment.getTask());
        tb.setEarnCode(earnCode);
        tb.setBeginDateTime(beginDateTime);
        tb.setEndDateTime(endDateTime);
        tb.setBeginTimeDisplay(tb.getBeginDateTime().withZone(timezone));
        tb.setEndTimeDisplay(tb.getEndDateTime().withZone(timezone));
        // only calculate the hours from the time fields if the passed in hour is zero
        if(hours == null || hours.compareTo(BigDecimal.ZERO) == 0) {
        	hours = TKUtils.getHoursBetween(beginDateTime.getMillis(), endDateTime.getMillis());
        }
        tb.setAmount(amount);
        //If earn code has an inflate min hours check if it is greater than zero
        //and compare if the hours specified is less than min hours awarded for this
        //earn code
        if (earnCodeObj.getInflateMinHours() != null) {
            if ((earnCodeObj.getInflateMinHours().compareTo(BigDecimal.ZERO) != 0) &&
                    earnCodeObj.getInflateMinHours().compareTo(hours) > 0) {
                hours = earnCodeObj.getInflateMinHours();
            }
        }
        //If earn code has an inflate factor multiple hours specified by the factor
        if (earnCodeObj.getInflateFactor() != null) {
            if ((earnCodeObj.getInflateFactor().compareTo(new BigDecimal(1.0)) != 0)
            		&& (earnCodeObj.getInflateFactor().compareTo(BigDecimal.ZERO)!= 0) ) {
                hours = earnCodeObj.getInflateFactor().multiply(hours, HrConstants.MATH_CONTEXT).setScale(HrConstants.BIG_DECIMAL_SCALE);
            }
        }

        tb.setEarnCodeType(earnCodeObj.getEarnCodeType());
        tb.setHours(hours);
        tb.setClockLogCreated(clockLogCreated);
        tb.setUserPrincipalId(userPrincipalId);
        tb.setTimestamp(TKUtils.getCurrentTimestamp());
        tb.setLunchDeleted(lunchDeleted);

        tb.setTimeHourDetails(this.createTimeHourDetails(tb.getEarnCode(), tb.getHours(), tb.getAmount(), tb.getTkTimeBlockId()));

        return tb;
    }

    public TimeBlock getTimeBlock(String tkTimeBlockId) {
        return timeBlockDao.getTimeBlock(tkTimeBlockId);
    }

    @Override
    public void deleteTimeBlock(TimeBlock timeBlock) {
        timeBlockDao.deleteTimeBlock(timeBlock);

    }

    public void resetTimeHourDetail(List<TimeBlock> origTimeBlocks) {
        for (TimeBlock tb : origTimeBlocks) {
            tb.setTimeHourDetails(createTimeHourDetails(tb.getEarnCode(), tb.getHours(), tb.getAmount(), tb.getTkTimeBlockId()));
            //reset time block history details
            for(TimeBlockHistory tbh : tb.getTimeBlockHistories()) {
            	TkServiceLocator.getTimeBlockHistoryService().addTimeBlockHistoryDetails(tbh,tb);
            }
        }
    }

    private List<TimeHourDetail> createTimeHourDetails(String earnCode, BigDecimal hours, BigDecimal amount, String timeBlockId) {
        List<TimeHourDetail> timeHourDetails = new ArrayList<TimeHourDetail>();

        TimeHourDetail timeHourDetail = new TimeHourDetail();
        timeHourDetail.setEarnCode(earnCode);
        timeHourDetail.setHours(hours);
        timeHourDetail.setAmount(amount);
        timeHourDetail.setTkTimeBlockId(timeBlockId);
        timeHourDetails.add(timeHourDetail);

        return timeHourDetails;
    }

    public List<TimeBlockHistory> createTimeBlockHistories(TimeBlock tb, String actionHistory) {
        List<TimeBlockHistory> tbhs = new ArrayList<TimeBlockHistory>();

        TimeBlockHistory tbh = new TimeBlockHistory(tb);
        tbh.setActionHistory(actionHistory);
        // add time block history details to this time block history
        TkServiceLocator.getTimeBlockHistoryService().addTimeBlockHistoryDetails(tbh, tb);
        
        tbhs.add(tbh);

        return tbhs;
    }
    
    // This method now translates time based on timezone settings.
    //
    public List<TimeBlock> getTimeBlocks(String documentId) {
    	List<TimeBlock> timeBlocks = timeBlockDao.getTimeBlocks(documentId);
        DateTimeZone timezone = HrServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
        for(TimeBlock tb : timeBlocks) {
            String earnCodeType = HrServiceLocator.getEarnCodeService().getEarnCodeType(tb.getEarnCode(), tb.getBeginDateTime().toLocalDate());
            tb.setEarnCodeType(earnCodeType);
			if(ObjectUtils.equals(timezone, TKUtils.getSystemDateTimeZone())){
				tb.setBeginTimeDisplay(tb.getBeginDateTime());
				tb.setEndTimeDisplay(tb.getEndDateTime());
			}
			else {
				tb.setBeginTimeDisplay(tb.getBeginDateTime().withZone(timezone));
				tb.setEndTimeDisplay(tb.getEndDateTime().withZone(timezone));
			}
        }

        return timeBlocks;
    }

    public List<TimeBlock> getTimeBlocksForAssignment(Assignment assign) {
    	List<TimeBlock> timeBlocks = new ArrayList<TimeBlock>();
    	if(assign != null) {
        	timeBlocks = timeBlockDao.getTimeBlocksForAssignment(assign);
    	}
        DateTimeZone timezone = HrServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
    	 for(TimeBlock tb : timeBlocks) {
             String earnCodeType = HrServiceLocator.getEarnCodeService().getEarnCodeType(tb.getEarnCode(), tb.getBeginDateTime().toLocalDate());
             tb.setEarnCodeType(earnCodeType);
 			if(ObjectUtils.equals(timezone, TKUtils.getSystemDateTimeZone())){
				tb.setBeginTimeDisplay(tb.getBeginDateTime());
				tb.setEndTimeDisplay(tb.getEndDateTime());
			}
			else {
				tb.setBeginTimeDisplay(tb.getBeginDateTime().withZone(timezone));
				tb.setEndTimeDisplay(tb.getEndDateTime().withZone(timezone));
			}
         }
    	return timeBlocks;
    }


	@Override
	public void deleteTimeBlocksAssociatedWithDocumentId(String documentId) {
		timeBlockDao.deleteTimeBlocksAssociatedWithDocumentId(documentId);
	}

	@Override
	// figure out if the user has permission to edit/delete the time block
	public Boolean getTimeBlockEditable(TimeBlock timeBlock) {
		String userId = GlobalVariables.getUserSession().getPrincipalId();

    	if(userId != null) {

			if(HrContext.isSystemAdmin()) {
				return true;
			}

        	if (HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(userId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.REVIEWER.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(userId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER_DELEGATE.getRoleName(), timeBlock.getWorkArea(), new DateTime())
        			|| HrServiceLocator.getKPMERoleService().principalHasRoleInWorkArea(userId, KPMENamespace.KPME_HR.getNamespaceCode(), KPMERole.APPROVER.getRoleName(), timeBlock.getWorkArea(), new DateTime())) {

				Job job = HrServiceLocator.getJobService().getJob(HrContext.getTargetPrincipalId(),timeBlock.getJobNumber(), timeBlock.getEndDateTime().toLocalDate());
				PayType payType = HrServiceLocator.getPayTypeService().getPayType(job.getHrPayType(), timeBlock.getEndDateTime().toLocalDate());
				if(StringUtils.equals(payType.getRegEarnCode(), timeBlock.getEarnCode())){
					return true;
				}

				List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator.getEarnCodeSecurityService().getEarnCodeSecurities(job.getDept(), job.getHrSalGroup(), job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
				for(EarnCodeSecurity dec : deptEarnCodes){
					if(dec.isApprover() && StringUtils.equals(dec.getEarnCode(), timeBlock.getEarnCode())){
						return true;
					}
				}
			}

			if(userId.equals(HrContext.getTargetPrincipalId())) {
				Job job = HrServiceLocator.getJobService().getJob(HrContext.getTargetPrincipalId(),timeBlock.getJobNumber(), timeBlock.getEndDateTime().toLocalDate());
				PayType payType = HrServiceLocator.getPayTypeService().getPayType(job.getHrPayType(), timeBlock.getEndDateTime().toLocalDate());
				if(StringUtils.equals(payType.getRegEarnCode(), timeBlock.getEarnCode())){
					return true;
				}

				List<EarnCodeSecurity> deptEarnCodes = HrServiceLocator.getEarnCodeSecurityService().getEarnCodeSecurities(job.getDept(), job.getHrSalGroup(), job.getLocation(), timeBlock.getEndDateTime().toLocalDate());
				for(EarnCodeSecurity dec : deptEarnCodes){
					if(dec.isEmployee() && StringUtils.equals(dec.getEarnCode(), timeBlock.getEarnCode())){
						return true;
					}
				}
				// if the user is the creator of this time block
			}


		}
		return false;
	}

	@Override
	public List<TimeBlock> getTimeBlocksForClockLogEndId(String tkClockLogId) {
		return timeBlockDao.getTimeBlocksForClockLogEndId(tkClockLogId);
	}
	@Override
	public List<TimeBlock> getTimeBlocksForClockLogBeginId(String tkClockLogId) {
		return timeBlockDao.getTimeBlocksForClockLogBeginId(tkClockLogId);
	}

	@Override
	public List<TimeBlock> getLatestEndTimestampForEarnCode(String earnCode){
		return timeBlockDao.getLatestEndTimestampForEarnCode(earnCode);
	}

    @Override
    public List<TimeBlock> getOvernightTimeBlocks(String clockLogEndId) {
        return timeBlockDao.getOvernightTimeBlocks(clockLogEndId);
    }
    
    @Override
    public void deleteLunchDeduction(String tkTimeHourDetailId) {
        TimeHourDetail thd = TkServiceLocator.getTimeHourDetailService().getTimeHourDetail(tkTimeHourDetailId);
        TimeBlock tb = getTimeBlock(thd.getTkTimeBlockId());
        
        // mark the lunch deleted as Y
        tb.setLunchDeleted(true);
        // save the change
        timeBlockDao.saveOrUpdate(tb);
        // remove the related time hour detail row with the lunch deduction
        TkServiceLocator.getTimeHourDetailService().removeTimeHourDetail(thd.getTkTimeHourDetailId());
    }
    @Override
    public List<TimeBlock> getTimeBlocksWithEarnCode(String earnCode, DateTime effDate) {
    	return timeBlockDao.getTimeBlocksWithEarnCode(earnCode, effDate);
    }
}
