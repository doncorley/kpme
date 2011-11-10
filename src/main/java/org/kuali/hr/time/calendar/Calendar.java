package org.kuali.hr.time.calendar;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeConstants;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Calendar extends PersistableBusinessObjectBase {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private Long hrCalendarId;
	private String calendarName;
	private String calendarDescriptions;

	private String flsaBeginDay;
	private Time flsaBeginTime;
	private String calendarTypes;
	private int flsaBeginDayConstant = -1;

	private List<CalendarEntries> calendarEntries = new ArrayList<CalendarEntries>();

	public Calendar() {

	}
	
	public Long getHrCalendarId() {
		return hrCalendarId;
	}



	public void setHrCalendarId(Long hrCalendarId) {
		this.hrCalendarId = hrCalendarId;
	}



	public String getCalendarName() {
		return calendarName;
	}



	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}



	public String getCalendarTypes() {
		return calendarTypes;
	}



	public void setCalendarTypes(String calendarTypes) {
		this.calendarTypes = calendarTypes;
	}



	public List<CalendarEntries> getCalendarEntries() {
		return calendarEntries;
	}



	public void setCalendarEntries(List<CalendarEntries> calendarEntries) {
		this.calendarEntries = calendarEntries;
	}



	public void setFlsaBeginDayConstant(int flsaBeginDayConstant) {
		this.flsaBeginDayConstant = flsaBeginDayConstant;
	}

	public String getCalendarDescriptions() {
		return calendarDescriptions;
	}

	public void setCalendarDescriptions(String calendarDescriptions) {
		this.calendarDescriptions = calendarDescriptions;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		return null;
	}

	public String getFlsaBeginDay() {
		return flsaBeginDay;
	}

	public void setFlsaBeginDay(String flsaBeginDay) {
		this.flsaBeginDay = flsaBeginDay;
		this.setFlsaBeinDayConstant(flsaBeginDay);
	}

	public Time getFlsaBeginTime() {
		return flsaBeginTime;
	}

	public void setFlsaBeginTime(Time flsaBeginTime) {
		this.flsaBeginTime = flsaBeginTime;
	}

	/**
	 * This method sets a constant matching those listed in
	 * org.joda.time.DateTimeConstants for day comparisons.
	 *
	 * Currently this is 'hard-coded' to be English specific, it would
	 * be trivial to change and support more than one language/day naming
	 * convention.
	 *
	 * @param day
	 */
	private void setFlsaBeinDayConstant(String day) {
		if (!StringUtils.isEmpty(day)) {
			day = day.toLowerCase().trim();

			if (day.startsWith("m")) {
				this.flsaBeginDayConstant = DateTimeConstants.MONDAY;
			} else if (day.startsWith("tu")) {
				this.flsaBeginDayConstant = DateTimeConstants.TUESDAY;
			} else if (day.startsWith("w")) {
				this.flsaBeginDayConstant = DateTimeConstants.WEDNESDAY;
			} else if (day.startsWith("th")) {
				this.flsaBeginDayConstant = DateTimeConstants.THURSDAY;
			} else if (day.startsWith("f")) {
				this.flsaBeginDayConstant = DateTimeConstants.FRIDAY;
			} else if (day.startsWith("sa")) {
				this.flsaBeginDayConstant = DateTimeConstants.SATURDAY;
			} else if (day.startsWith("su")) {
				this.flsaBeginDayConstant = DateTimeConstants.SUNDAY;
			}
		}
	}

	/**
	 * org.joda.time.DateTimeConstants.MONDAY
	 * ...
	 * org.joda.time.DateTimeConstants.SUNDAY
	 *
	 * @return an int representing the FLSA start day, sourced from
	 * org.joda.time.DateTimeConstants in the interval [1,7].
	 */
	public int getFlsaBeginDayConstant() {
		if (flsaBeginDayConstant < 0) {
			this.setFlsaBeinDayConstant(this.getFlsaBeginDay());
		}
		return flsaBeginDayConstant;
	}

    @Override
    public boolean equals(Object o) {
        if (o instanceof Calendar) {
            Calendar pc = (Calendar)o;
            return this.getHrCalendarId().compareTo(pc.getHrCalendarId()) == 0;
        } else {
            return false;
        }
    }
}