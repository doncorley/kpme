package org.kuali.hr.time.timeblock.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.Interval;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timeblock.TimeHourDetail;
import org.kuali.hr.time.timeblock.dao.TimeBlockDao;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;

public class TimeBlockServiceImpl implements TimeBlockService {
	
	private static final Logger LOG = Logger.getLogger(TimeBlockServiceImpl.class);
	private TimeBlockDao timeBlockDao;

	public void setTimeBlockDao(TimeBlockDao timeBlockDao) {
		this.timeBlockDao = timeBlockDao;
	}

	
	public List<TimeBlock> buildTimeBlocks(Assignment assignment, String earnCode, TimesheetDocument timesheetDocument, 
						Timestamp beginTimestamp, Timestamp endTimestamp){
		//Create 1 or many timeblocks if the span of timeblocks exceed more than one 
		//day that is determined by pay period day(24 hrs + period begin date)
		Interval firstDay = null;
		List<Interval> dayIntervals = TKUtils.getDaySpanForPayCalendarEntry(timesheetDocument.getPayCalendarEntry());
		List<TimeBlock> lstTimeBlocks = new ArrayList<TimeBlock>();
		for(Interval dayInt : dayIntervals){
			//on second day of span so safe to assume doesnt go furthur than this
			if(firstDay != null){
				TimeBlock tb = new TimeBlock();
				tb.setBeginTimestamp(new Timestamp(dayInt.getStartMillis()));
				tb.setEndTimestamp(endTimestamp);
				lstTimeBlocks.add(tb);
				break;
			}
			if(dayInt.contains(beginTimestamp.getTime()) ){
				firstDay = dayInt;
				if(dayInt.contains(endTimestamp.getTime())){
					//create one timeblock if contained in one day interval
					TimeBlock tb = createTimeBlock(timesheetDocument, beginTimestamp, endTimestamp, assignment, earnCode);
					tb.setBeginTimestamp(beginTimestamp);
					tb.setEndTimestamp(endTimestamp);
					lstTimeBlocks.add(tb);
					break;
				} else {
					//create a timeblock that wraps the 24 hr day
					TimeBlock tb = createTimeBlock(timesheetDocument, beginTimestamp, endTimestamp, assignment, earnCode);
					tb.setBeginTimestamp(beginTimestamp);
					tb.setEndTimestamp(new Timestamp(firstDay.getEndMillis()));
					lstTimeBlocks.add(tb);
				}
			}
		}
		return lstTimeBlocks;
	}
	
	public void saveTimeBlocks(List<TimeBlock> oldTimeBlocks, List<TimeBlock> newTimeBlocks){
		List<TimeBlock> alteredTimeBlocks = new ArrayList<TimeBlock>();
		boolean persist = false;
		for(TimeBlock tb : newTimeBlocks){
			for(TimeBlock tbOld : oldTimeBlocks){
				if(tb.equals(tbOld)){
					persist = true;
					break;
				}
			}
			if(persist){
				alteredTimeBlocks.add(tb);
				persist = false;
			}
		}
		for(TimeBlock tb : alteredTimeBlocks){
			timeBlockDao.saveOrUpdate(tb);
		}
		
	}

	
	private TimeBlock createTimeBlock(TimesheetDocument timesheetDocument, Timestamp beginTime, Timestamp endTime, Assignment assignment, String earnCode){
		TimeBlock tb = new TimeBlock();
    	tb.setDocumentId(timesheetDocument.getDocumentHeader().getDocumentId().toString());
    	tb.setJobNumber(assignment.getJobNumber());
    	tb.setWorkArea(assignment.getWorkArea());
    	tb.setTask(assignment.getTask());
    	tb.setTkWorkAreaId(assignment.getWorkAreaObj().getTkWorkAreaId());
    	tb.setHrJobId(assignment.getJob().getHrJobId());
	    Long tkTaskId = null;
	    for(Task task : assignment.getWorkAreaObj().getTasks()) {
	    	if(task.getTask().compareTo(assignment.getTask()) == 0 ) {
	    		tkTaskId = task.getTkTaskId();
	    		break;
	    	}
	    }
    	tb.setTkTaskId(tkTaskId);
    	tb.setEarnCode(earnCode);
    	tb.setBeginTimestamp(beginTime);
    	//TODO add timezeon things
//    	tb.setBeginTimestampTimezone(clockLog.getClockTimestampTimezone());
    	tb.setEndTimestamp(endTime);
//    	tb.setEndTimestampTimezone(clockLog.getClockTimestampTimezone());
    	tb.setClockLogCreated(true);
    	tb.setHours(TKUtils.getHoursBetween(beginTime.getTime(), endTime.getTime()));
    	tb.setUserPrincipalId(TKContext.getUser().getPrincipalId());
    	tb.setTimestamp(new Timestamp(System.currentTimeMillis()));
    	
    	return tb;
	}

	public TimeBlock getTimeBlock(String timeBlockId) {
		return timeBlockDao.getTimeBlock(timeBlockId);
	}
	
	
	@Override
	public List<Map<String,Object>> getTimeBlocksForOurput(List<TimeBlock> timeBlocks) {
		List<Map<String,Object>> timeBlockList = new LinkedList<Map<String,Object>>();

		for(TimeBlock timeBlock : timeBlocks) {
			Map<String,Object> timeBlockMap = new LinkedHashMap<String, Object>();

			timeBlockMap.put("title", "HRMS Java Team : " + timeBlock.getEarnCode());
			timeBlockMap.put("start", new java.util.Date(timeBlock.getBeginTimestamp().getTime()).toString());
			timeBlockMap.put("end", new java.util.Date(timeBlock.getEndTimestamp().getTime()).toString());
			timeBlockMap.put("id", timeBlock.getTkTimeBlockId().toString());
			timeBlockMap.put("hours", timeBlock.getHours());

			timeBlockList.add(timeBlockMap);
		}
		
		return timeBlockList;
	
	}

	@Override
	public void deleteTimeBlock(TimeBlock timeBlock) {
		timeBlockDao.deleteTimeBlock(timeBlock);
		
	}
	
	public List<TimeBlock> resetTimeHourDetail(List<TimeBlock> origTimeBlocks){
		for(TimeBlock tb : origTimeBlocks){
			List<TimeHourDetail> timeHourDetails = new ArrayList<TimeHourDetail>();
			TimeHourDetail timeHourDetail = new TimeHourDetail();
			timeHourDetail.setEarnCode(tb.getEarnCode());
			timeHourDetail.setHours(tb.getHours());
			timeHourDetail.setTkTimeBlockId(tb.getTkTimeBlockId());
			timeHourDetails.add(timeHourDetail);
			tb.setTimeHourDetails(timeHourDetails);
		}
		
		return origTimeBlocks;
	}
	
	public List<TimeBlock> getTimeBlocks(Long documentId){
		return timeBlockDao.getTimeBlocks(documentId);
	}

}
