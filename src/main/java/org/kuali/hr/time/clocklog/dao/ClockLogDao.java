package org.kuali.hr.time.clocklog.dao;

import java.util.List;

import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.clocklog.ClockLog;

public interface ClockLogDao {

	/**
	 * Save or update ClockLog passed in
	 * @param clockLog
	 */
    public void saveOrUpdate(ClockLog clockLog);
    /**
     * Save or update List of ClockLogs passed in
     * @param clockLogList
     */
    public void saveOrUpdate(List<ClockLog> clockLogList);
    /**
     * Get last ClockLog for a given principalId
     * @param principalId
     * @return
     */
    public ClockLog getLastClockLog(String principalId);
    /**
     * Get last ClockLog for a given principalId and clockAction
     * @param principalId
     * @param clockAction
     * @return
     */
	public ClockLog getLastClockLog(String principalId, String clockAction);
	
	/**
	 * Return a list of all open clock logs
	 * @param calendarEntry
	 * @return
	 */
	public List<ClockLog> getOpenClockLogs(CalendarEntries calendarEntry);
	
	/**
	 * Fetch clock log by id
	 * @param tkClockLogId
	 * @return
	 */
	public ClockLog getClockLog(Long tkClockLogId);
}
