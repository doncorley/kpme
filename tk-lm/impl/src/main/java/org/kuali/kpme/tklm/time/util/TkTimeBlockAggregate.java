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
package org.kuali.kpme.tklm.time.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.calendar.Calendar;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.earncode.EarnCode;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.leave.block.LeaveBlock;
import org.kuali.kpme.tklm.leave.block.LeaveBlockAggregate;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;
import org.kuali.kpme.tklm.time.flsa.FlsaDay;
import org.kuali.kpme.tklm.time.flsa.FlsaWeek;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;
import org.kuali.kpme.tklm.time.timehourdetail.TimeHourDetail;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader;

public class TkTimeBlockAggregate {
	private List<List<TimeBlock>> dayTimeBlockList = new ArrayList<List<TimeBlock>>();
	private List<List<LeaveBlock>> dayLeaveBlockList = new ArrayList<List<LeaveBlock>>();
	private CalendarEntry payCalendarEntry;
	private Calendar payCalendar;

    /**
     * Defaults to using SYSTEM time zone.
     *
     * @param timeBlocks
     * @param payCalendarEntry
     */
	public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, CalendarEntry payCalendarEntry){
		this(timeBlocks, payCalendarEntry, HrServiceLocator.getCalendarService().getCalendar(payCalendarEntry.getHrCalendarId()));
	}

    /**
     * Defaults to using SYSTEM time zone.
     *
     * @param timeBlocks
     * @param payCalendarEntry
     * @param payCalendar
     */
	public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar) {
        this(timeBlocks, payCalendarEntry, payCalendar, false);
    }

    /**
     * Provides the option to refer to the time zone adjusted time for the current
     * user.
     * @param timeBlocks
     * @param payCalendarEntry
     * @param payCalendar
     * @param useUserTimeZone
     */
    public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar, boolean useUserTimeZone) {
        this(timeBlocks, payCalendarEntry, payCalendar, useUserTimeZone, 
        		useUserTimeZone ? TKUtils.getDaySpanForCalendarEntry(payCalendarEntry) : TKUtils.getDaySpanForCalendarEntry(payCalendarEntry, TKUtils.getSystemDateTimeZone()));
	}

    public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar, boolean useUserTimeZone, List<Interval> dayIntervals) {
    	this.payCalendarEntry = payCalendarEntry;
		this.payCalendar = payCalendar;
		
		for(Interval dayInt : dayIntervals){
			List<TimeBlock> dayTimeBlocks = new ArrayList<TimeBlock>();
			for(TimeBlock timeBlock : timeBlocks){

                // Assumption: Timezones can only be switched at pay period end boundaries.
                // If the above assumption becomes false, the logic below will need to
                // accommodate virtual chopping of time blocks to have them fit nicely
                // in the "days" that are displayed to users.

				DateTime beginTime = useUserTimeZone ? timeBlock.getBeginTimeDisplay() : timeBlock.getBeginDateTime().withZone(TKUtils.getSystemDateTimeZone());
				DateTime endTime = useUserTimeZone ? timeBlock.getEndTimeDisplay() :  timeBlock.getEndDateTime().withZone(TKUtils.getSystemDateTimeZone());
				if(dayInt.contains(beginTime)){
					if(dayInt.contains(endTime) || endTime.compareTo(dayInt.getEnd()) == 0){
						// determine if the time block needs to be pushed forward / backward
						if(beginTime.getHourOfDay() < dayInt.getStart().getHourOfDay()) {
							timeBlock.setPushBackward(true);
						}

						dayTimeBlocks.add(timeBlock);
					}
				}
			}
			dayTimeBlockList.add(dayTimeBlocks);
		}
	}
    
    /**
     * Defaults to using SYSTEM time zone.
     *
     * @param timeBlocks
     * @param leaveBlocks
     * @param payCalendarEntry
     */
	public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, List<LeaveBlock> leaveBlocks, CalendarEntry payCalendarEntry){
		this(timeBlocks, leaveBlocks, payCalendarEntry, HrServiceLocator.getCalendarService().getCalendar(payCalendarEntry.getHrCalendarId()));
	}

    /**
     * Defaults to using SYSTEM time zone.
     *
     * @param timeBlocks
     * @param leaveBlocks
     * @param payCalendarEntry
     * @param payCalendar
     */
	public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, List<LeaveBlock> leaveBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar) {
        this(timeBlocks, leaveBlocks, payCalendarEntry, payCalendar, false);
    }

    /**
     * Provides the option to refer to the time zone adjusted time for the current
     * user.
     * @param timeBlocks
     * @param payCalendarEntry
     * @param payCalendar
     * @param useUserTimeZone
     */
    public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, List<LeaveBlock> leaveBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar, boolean useUserTimeZone) {
        this(timeBlocks, leaveBlocks, payCalendarEntry, payCalendar, useUserTimeZone, 
        		useUserTimeZone ? TKUtils.getDaySpanForCalendarEntry(payCalendarEntry) : TKUtils.getDaySpanForCalendarEntry(payCalendarEntry, TKUtils.getSystemDateTimeZone()));
	}

    public TkTimeBlockAggregate(List<TimeBlock> timeBlocks, List<LeaveBlock> leaveBlocks, CalendarEntry payCalendarEntry, Calendar payCalendar, boolean useUserTimeZone, List<Interval> dayIntervals) {
    	this.payCalendarEntry = payCalendarEntry;
		this.payCalendar = payCalendar;

		for(Interval dayInt : dayIntervals){
			List<TimeBlock> dayTimeBlocks = new ArrayList<TimeBlock>();
			for(TimeBlock timeBlock : timeBlocks){

                // Assumption: Timezones can only be switched at pay period end boundaries.
                // If the above assumption becomes false, the logic below will need to
                // accommodate virtual chopping of time blocks to have them fit nicely
                // in the "days" that are displayed to users.

				DateTime beginTime = useUserTimeZone ? timeBlock.getBeginTimeDisplay() : new DateTime(timeBlock.getBeginTimestamp(), TKUtils.getSystemDateTimeZone());
				DateTime endTime = useUserTimeZone ? timeBlock.getEndTimeDisplay() :  new DateTime(timeBlock.getEndTimestamp(), TKUtils.getSystemDateTimeZone());
				if(dayInt.contains(beginTime)){
					if(dayInt.contains(endTime) || endTime.compareTo(dayInt.getEnd()) == 0){
						// determine if the time block needs to be pushed forward / backward
						if(beginTime.getHourOfDay() < dayInt.getStart().getHourOfDay()) {
							timeBlock.setPushBackward(true);
						}

						dayTimeBlocks.add(timeBlock);
					}
				}
			}
			dayTimeBlockList.add(dayTimeBlocks);

            List<LeaveBlock> dayLeaveBlocks = new ArrayList<LeaveBlock>();
			for(LeaveBlock leaveBlock : leaveBlocks){

                // Assumption: Timezones can only be switched at pay period end boundaries.
                // If the above assumption becomes false, the logic below will need to
                // accommodate virtual chopping of leave blocks to have them fit nicely
                // in the "days" that are displayed to users.
				
				DateTimeZone dateTimeZone = HrServiceLocator.getTimezoneService().getUserTimezoneWithFallback();
				DateTime beginTime = new DateTime(leaveBlock.getLeaveDate(), useUserTimeZone ? dateTimeZone : TKUtils.getSystemDateTimeZone());
				DateTime endTime = new DateTime(leaveBlock.getLeaveDate(), useUserTimeZone ? dateTimeZone : TKUtils.getSystemDateTimeZone());
				if(dayInt.contains(beginTime)){
					if(dayInt.contains(endTime) || endTime.compareTo(dayInt.getEnd()) == 0){
						dayLeaveBlocks.add(leaveBlock);
					}
				}
			}
			dayLeaveBlockList.add(dayLeaveBlocks);
		}
    }
    
	public List<TimeBlock> getFlattenedTimeBlockList(){
		List<TimeBlock> lstTimeBlocks = new ArrayList<TimeBlock>();
		for(List<TimeBlock> timeBlocks : dayTimeBlockList){
			lstTimeBlocks.addAll(timeBlocks);
		}

		Collections.sort(lstTimeBlocks, new Comparator<TimeBlock>() { // Sort the Time Blocks
			public int compare(TimeBlock tb1, TimeBlock tb2) {
				if (tb1 != null && tb2 != null)
					return tb1.getBeginTimestamp().compareTo(tb2.getBeginTimestamp());
				return 0;
			}
		});

		return lstTimeBlocks;
	}
	
	public List<LeaveBlock> getFlattenedLeaveBlockList(){
		List<LeaveBlock> lstLeaveBlocks = new ArrayList<LeaveBlock>();
		for(List<LeaveBlock> leaveBlocks : dayLeaveBlockList){
			lstLeaveBlocks.addAll(leaveBlocks);
		}

		Collections.sort(lstLeaveBlocks, new Comparator<LeaveBlock>() { // Sort the Leave Blocks
			public int compare(LeaveBlock lb1, LeaveBlock lb2) {
				if (lb1 != null && lb2 != null)
					return lb1.getLeaveDate().compareTo(lb2.getLeaveDate());
				return 0;
			}
		});

		return lstLeaveBlocks;
	}

	/**
	 * Provides a way to access all of the time blocks for a given week.
	 *
	 * Outer list is 0 indexed list representing days in a week.
	 * Inner List are all of the time blocks for that day.
	 *
	 * Ex.
	 *
	 * List<List<TimeBlock>> week0 = getWeekTimeBlocks(0);
	 * List<TimeBlock> day0 = week0.get(0);
	 *
	 * @param week
	 * @return
	 */
	public List<List<TimeBlock>> getWeekTimeBlocks(int week){
		int startIndex = week*7;
		int endIndex = (week*7)+7;
		endIndex = endIndex > dayTimeBlockList.size() ? dayTimeBlockList.size() : endIndex;

        // Need to sort each day by clock time.
        List<List<TimeBlock>> wList = dayTimeBlockList.subList(startIndex, endIndex);
        for (List<TimeBlock> dList : wList) {
            Collections.sort(dList, new Comparator<TimeBlock>() { // Sort the Time Blocks
                public int compare(TimeBlock tb1, TimeBlock tb2) {
                    if (tb1 != null && tb2 != null)
                        return tb1.getBeginTimestamp().compareTo(tb2.getBeginTimestamp());
                    return 0;
                }
            });
        }

		return wList;
	}
	
	/**
	 * Provides a way to access all of the leave blocks for a given week.
	 *
	 * Outer list is 0 indexed list representing days in a week.
	 * Inner List are all of the leave blocks for that day.
	 *
	 * Ex.
	 *
	 * List<List<LeaveBlock>> week0 = getWeekLeaveBlocks(0);
	 * List<LeaveBlock> day0 = week0.get(0);
	 *
	 * @param week
	 * @return
	 */
	public List<List<LeaveBlock>> getWeekLeaveBlocks(int week){
		int startIndex = week*7;
		int endIndex = (week*7)+7;
		endIndex = endIndex > dayLeaveBlockList.size() ? dayLeaveBlockList.size() : endIndex;

        // Need to sort each day by clock time.
        List<List<LeaveBlock>> wList = dayLeaveBlockList.subList(startIndex, endIndex);
        for (List<LeaveBlock> dList : wList) {
            Collections.sort(dList, new Comparator<LeaveBlock>() { // Sort the Leave Blocks
                public int compare(LeaveBlock lb1, LeaveBlock lb2) {
                    if (lb1 != null && lb2 != null)
                        return lb1.getLeaveDate().compareTo(lb2.getLeaveDate());
                    return 0;
                }
            });
        }

		return wList;
	}

	/**
	 * When consuming these weeks, you must be aware that you could be on a
	 * virtual day, ie noon to noon schedule and have your FLSA time start
	 * before the virtual day start,
	 * but still have a full 7 days for your week.
     *
     * @param zone The TimeZone to use when constructing this relative sorting.
	 */
	public List<FlsaWeek> getFlsaWeeks(DateTimeZone zone){
		int flsaDayConstant = payCalendar.getFlsaBeginDayConstant();
		Time flsaBeginTime  = payCalendar.getFlsaBeginTime();

		// We can use these to build our interval, we have to make sure we
		// place them on the proper day when we construct it.
		LocalTime flsaBeginLocalTime = LocalTime.fromDateFields(flsaBeginTime);

		// Defines both the start date and the start virtual time.
		// We will add 1 day to this to move over all days.
		//
		// FLSA time is set.  This is an FLSA start date.
        LocalDateTime startLDT = payCalendarEntry.getBeginPeriodLocalDateTime();

		List<FlsaWeek> flsaWeeks = new ArrayList<FlsaWeek>();
		List<TimeBlock> flattenedTimeBlocks = getFlattenedTimeBlockList();
		List<LeaveBlock> flattenedLeaveBlocks = getFlattenedLeaveBlockList();
		FlsaWeek currentWeek = new FlsaWeek(flsaDayConstant, flsaBeginLocalTime, payCalendarEntry.getBeginPeriodFullDateTime().toLocalTime());
		FlsaDay flsaDay = new FlsaDay(startLDT, flattenedTimeBlocks, flattenedLeaveBlocks, zone);
		currentWeek.addFlsaDay(flsaDay);
		flsaWeeks.add(currentWeek);

		for (int i = 1; i < dayTimeBlockList.size(); i++) {
			LocalDateTime currentDate = startLDT.plusDays(i);
			flsaDay = new FlsaDay(currentDate, flattenedTimeBlocks, flattenedLeaveBlocks, zone);

			if (currentDate.getDayOfWeek() == flsaDayConstant) {
				currentWeek = new FlsaWeek(flsaDayConstant, flsaBeginLocalTime, flsaBeginLocalTime);
				flsaWeeks.add(currentWeek);	
			}
			
			currentWeek.addFlsaDay(flsaDay);
		}

		return flsaWeeks;
	}
	
	public List<List<FlsaWeek>> getFlsaWeeks(DateTimeZone zone, String principalId) {
		List<List<FlsaWeek>> flsaWeeks = new ArrayList<List<FlsaWeek>>();
		
		List<FlsaWeek> currentWeeks = getFlsaWeeks(zone);
		
		for (ListIterator<FlsaWeek> weekIterator = currentWeeks.listIterator(); weekIterator.hasNext(); ) {
			List<FlsaWeek> flsaWeek = new ArrayList<FlsaWeek>();
			
			int index = weekIterator.nextIndex();
			FlsaWeek currentWeek = weekIterator.next();
			
			if (index == 0 && !currentWeek.isFirstWeekFull()) {
				CalendarEntry previousCalendarEntry = HrServiceLocator.getCalendarEntryService().getPreviousCalendarEntryByCalendarId(payCalendar.getHrCalendarId(), payCalendarEntry);
				if (previousCalendarEntry != null) {
					TimesheetDocumentHeader timesheetDocumentHeader = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, previousCalendarEntry.getBeginPeriodFullDateTime(), previousCalendarEntry.getEndPeriodFullDateTime());
					if (timesheetDocumentHeader != null) { 
		                TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(timesheetDocumentHeader.getDocumentId());
		                List<String> assignmentKeys = new ArrayList<String>();
		                for(Assignment assignment : timesheetDocument.getAssignments()) {
		                	assignmentKeys.add(assignment.getAssignmentKey());
		                }
		                
						List<TimeBlock> timeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocks(timesheetDocumentHeader.getDocumentId());
						List<LeaveBlock> leaveBlocks = LmServiceLocator.getLeaveBlockService().getLeaveBlocksForTimeCalendar(principalId, previousCalendarEntry.getBeginPeriodFullDateTime().toLocalDate(), previousCalendarEntry.getEndPeriodFullDateTime().toLocalDate(), assignmentKeys);
						if (CollectionUtils.isNotEmpty(timeBlocks)) {
							TkTimeBlockAggregate previousAggregate = new TkTimeBlockAggregate(timeBlocks, leaveBlocks, previousCalendarEntry, payCalendar, true);
							List<FlsaWeek> previousWeek = previousAggregate.getFlsaWeeks(zone);
							if (CollectionUtils.isNotEmpty(previousWeek)) {
								flsaWeek.add(previousWeek.get(previousWeek.size() - 1));
							}
						}
					 }
				}
			}
			
			flsaWeek.add(currentWeek);
			
			if (index == currentWeeks.size() - 1 && !currentWeek.isLastWeekFull()) {
				CalendarEntry nextCalendarEntry = HrServiceLocator.getCalendarEntryService().getNextCalendarEntryByCalendarId(payCalendar.getHrCalendarId(), payCalendarEntry);
				if (nextCalendarEntry != null) {
					TimesheetDocumentHeader timesheetDocumentHeader = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, nextCalendarEntry.getBeginPeriodFullDateTime(), nextCalendarEntry.getEndPeriodFullDateTime());
					if (timesheetDocumentHeader != null) {
		                TimesheetDocument timesheetDocument = TkServiceLocator.getTimesheetService().getTimesheetDocument(timesheetDocumentHeader.getDocumentId());
		                List<String> assignmentKeys = new ArrayList<String>();
		                for(Assignment assignment : timesheetDocument.getAssignments()) {
		                	assignmentKeys.add(assignment.getAssignmentKey());
		                }
		                
						List<TimeBlock> timeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocks(timesheetDocumentHeader.getDocumentId());
						List<LeaveBlock> leaveBlocks = LmServiceLocator.getLeaveBlockService().getLeaveBlocksForTimeCalendar(principalId, nextCalendarEntry.getBeginPeriodFullDateTime().toLocalDate(), nextCalendarEntry.getEndPeriodFullDateTime().toLocalDate(), assignmentKeys);
						if (CollectionUtils.isNotEmpty(timeBlocks)) {
							TkTimeBlockAggregate nextAggregate = new TkTimeBlockAggregate(timeBlocks, leaveBlocks, nextCalendarEntry, payCalendar, true);
							List<FlsaWeek> nextWeek = nextAggregate.getFlsaWeeks(zone);
							if (CollectionUtils.isNotEmpty(nextWeek)) {
								flsaWeek.add(nextWeek.get(0));
							}
						}
					 }
				}
			}
			
			flsaWeeks.add(flsaWeek);
		}

		return flsaWeeks;
	}

	/**
	 * @return the total number of weeks that this object represents.
	 */
	public int numberOfAggregatedWeeks() {
		int weeks = 0;

		if (this.dayTimeBlockList.size() > 0) {
			weeks = this.dayTimeBlockList.size() / 7;
			if (this.dayTimeBlockList.size() % 7 > 0)
				weeks++;
		}

		return weeks;
	}

	public List<List<TimeBlock>> getDayTimeBlockList() {
		return dayTimeBlockList;
	}
	
	public List<List<LeaveBlock>> getDayLeaveBlockList() {
		return dayLeaveBlockList;
	}

	public CalendarEntry getPayCalendarEntry() {
		return payCalendarEntry;
	}

	public void setPayCalendarEntry(CalendarEntry payCalendarEntry) {
		this.payCalendarEntry = payCalendarEntry;
	}

	public Calendar getPayCalendar() {
		return payCalendar;
	}

	public void setPayCalendar(Calendar payCalendar) {
		this.payCalendar = payCalendar;
	}
	
	//Very much a hack to add valid leave blocks to a time block aggregate...
	public static TkTimeBlockAggregate combineTimeAndLeaveAggregates(TkTimeBlockAggregate tbAggregate, LeaveBlockAggregate lbAggregate) {
		if (tbAggregate != null
				&& lbAggregate != null
				&& tbAggregate.getDayTimeBlockList().size() == lbAggregate.getDayLeaveBlockList().size()) {
			for (int i = 0; i < tbAggregate.getDayTimeBlockList().size(); i++) {
				List<LeaveBlock> leaveBlocks = lbAggregate.getDayLeaveBlockList().get(i);
				if (CollectionUtils.isNotEmpty(leaveBlocks)) {
					for (LeaveBlock lb : leaveBlocks) {
						//convert leave block to generic time block and add to list
						//conveniently, we only really need the hours amount
						TimeBlock timeBlock = new TimeBlock();
						timeBlock.setHours(lb.getLeaveAmount().negate());
						timeBlock.setBeginTimestamp(new Timestamp(lb.getLeaveDate().getTime()));
						timeBlock.setEndTimestamp(new Timestamp(new DateTime(lb.getLeaveDate()).plusMinutes(timeBlock.getHours().intValue()).getMillis()));
						timeBlock.setAssignmentKey(lb.getAssignmentKey());
						timeBlock.setEarnCode(lb.getEarnCode());
						
						EarnCode earnCodeObj = HrServiceLocator.getEarnCodeService().getEarnCode(lb.getEarnCode(), lb.getLeaveLocalDate());
						if(earnCodeObj != null) {
							timeBlock.setEarnCodeType(earnCodeObj.getEarnCodeType());
						}
						timeBlock.setPrincipalId(lb.getPrincipalId());
						timeBlock.setWorkArea(lb.getWorkArea());
						TimeHourDetail timeHourDetail = new TimeHourDetail();
						timeHourDetail.setEarnCode(timeBlock.getEarnCode());
						timeHourDetail.setHours(timeBlock.getHours());
						timeHourDetail.setAmount(BigDecimal.ZERO);
						timeBlock.addTimeHourDetail(timeHourDetail);
						tbAggregate.getDayTimeBlockList().get(i).add(timeBlock);
					}
				}

			}
		}
		return tbAggregate;
	}

}
