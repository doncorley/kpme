package org.kuali.hr.time.approval.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.hr.time.approval.web.ApprovalTimeSummaryRow;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.cache.CacheResult;
import org.kuali.hr.time.calendar.Calendar;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.clocklog.ClockLog;
import org.kuali.hr.time.flsa.FlsaDay;
import org.kuali.hr.time.flsa.FlsaWeek;
import org.kuali.hr.time.principal.PrincipalHRAttributes;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.timesummary.TimeSummary;
import org.kuali.hr.time.util.*;
import org.kuali.hr.time.workarea.WorkArea;
import org.kuali.hr.time.workflow.TimesheetDocumentHeader;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeApproveServiceImpl implements TimeApproveService {

    private static final Logger LOG = Logger.getLogger(TimeApproveServiceImpl.class);

    public static final int DAYS_WINDOW_DELTA = 31;


    public Map<String, CalendarEntries> getCalendarEntriesForDept(String dept, Date currentDate) {
        DateTime minDt = new DateTime(currentDate, TkConstants.SYSTEM_DATE_TIME_ZONE);
        minDt = minDt.minusDays(DAYS_WINDOW_DELTA);
        java.sql.Date windowDate = TKUtils.getTimelessDate(minDt.toDate());

        Map<String, CalendarEntries> pceMap = new HashMap<String, CalendarEntries>();
        Set<String> principals = new HashSet<String>();
        List<WorkArea> workAreasForDept = TkServiceLocator.getWorkAreaService().getWorkAreas(dept, new java.sql.Date(currentDate.getTime()));
        // Get all of the principals within our window of time.
        for (WorkArea workArea : workAreasForDept) {
            Long waNum = workArea.getWorkArea();
            List<Assignment> assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(waNum, TKUtils.getTimelessDate(currentDate));

            if (assignments != null) {
                for (Assignment assignment : assignments) {
                    principals.add(assignment.getPrincipalId());
                }
            } else {
                assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(waNum, windowDate);
                if (assignments != null) {
                    for (Assignment assignment : assignments) {
                        principals.add(assignment.getPrincipalId());
                    }
                }
            }
        }

        // Get the pay calendars
        Set<Calendar> cals = new HashSet<Calendar>();
        for (String pid : principals) {
            PrincipalHRAttributes pc = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(pid, currentDate);
            if (pc == null)
                pc = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(pid, windowDate);

            if (pc != null) {
                cals.add(pc.getCalendar());
            } else {
                LOG.warn("PrincipalCalendar null for principal: '" + pid + "'");
            }
        }

        // Grab the pay calendar entries + groups
        for (Calendar pc : cals) {
            CalendarEntries pce = TkServiceLocator.getCalendarEntriesSerivce().getCurrentCalendarEntriesByCalendarId(pc.getHrCalendarId(), currentDate);
            pceMap.put(pc.getCalendarName(), pce);
        }

        return pceMap;
    }

    @Override
    public Map<String, CalendarEntries> getCalendarEntriesForApprover(String principalId, Date currentDate, String dept) {
        TKUser tkUser = TKContext.getUser();

        Map<String, CalendarEntries> pceMap = new HashMap<String, CalendarEntries>();
        Set<String> principals = new HashSet<String>();
        DateTime minDt = new DateTime(currentDate, TkConstants.SYSTEM_DATE_TIME_ZONE);
        minDt = minDt.minusDays(DAYS_WINDOW_DELTA);
        java.sql.Date windowDate = TKUtils.getTimelessDate(minDt.toDate());
        Set<Long> approverWorkAreas = tkUser.getCurrentRoles().getApproverWorkAreas();

        // Get all of the principals within our window of time.
        for (Long waNum : approverWorkAreas) {
            List<Assignment> assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(waNum, TKUtils.getTimelessDate(currentDate));

            if (assignments != null) {
                for (Assignment assignment : assignments) {
                    principals.add(assignment.getPrincipalId());
                }
            }
//            else {
//                assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(waNum, windowDate);
//                if (assignments != null) {
//                    for (Assignment assignment : assignments) {
//                        principals.add(assignment.getPrincipalId());
//                    }
//                }
//            }
        }

        // Get the pay calendars
        Set<Calendar> cals = new HashSet<Calendar>();
        for (String pid : principals) {
            PrincipalHRAttributes pc = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(pid, currentDate);
            if (pc == null)
                pc = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(pid, windowDate);

            if (pc != null) {
                cals.add(pc.getCalendar());
            } else {
                LOG.warn("PrincipalCalendar null for principal: '" + pid + "'");
            }
        }

        // Grab the pay calendar entries + groups
        for (Calendar cal : cals) {
            CalendarEntries ce = TkServiceLocator.getCalendarEntriesSerivce().getCurrentCalendarEntriesByCalendarId(cal.getHrCalendarId(), currentDate);
            pceMap.put(cal.getCalendarName(), ce);
        }

        return pceMap;
    }

    public SortedSet<String> getApproverCalendarGroups(Date payBeginDate, Date payEndDate) {
        SortedSet<String> pcg = new TreeSet<String>();

        TKUser tkUser = TKContext.getUser();
        Set<Long> approverWorkAreas = tkUser.getCurrentRoles().getApproverWorkAreas();
        List<Assignment> assignments = new ArrayList<Assignment>();

        for (Long workArea : approverWorkAreas) {
            if (workArea != null) {
                assignments.addAll(TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(workArea, new java.sql.Date(payBeginDate.getTime())));
            }
        }
        if (!assignments.isEmpty()) {
            for (Assignment assign : assignments) {
                String principalId = assign.getPrincipalId();
                TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
                if (tdh != null) {
                    String pyCalendarGroup = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(principalId, payBeginDate).getPayCalendar();
                    pcg.add(pyCalendarGroup);
                }
            }
        }

        // Handle the situation where pay calendar is null. This shouldn't happen but just in case.
//        if (pcg.size() == 0) {
//            throw new RuntimeException("Pay calendar group is null");
//        }

        return pcg;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<ApprovalTimeSummaryRow> getApprovalSummaryRows(Date payBeginDate, Date payEndDate, String calGroup, List<String> principalIds) {
        List<ApprovalTimeSummaryRow> rows = new LinkedList<ApprovalTimeSummaryRow>();
        Map<String, TimesheetDocumentHeader> principalDocumentHeader = getPrincipalDocumehtHeader(principalIds, payBeginDate, payEndDate);

        for (String principalId : principalIds) {
            TimesheetDocumentHeader tdh = new TimesheetDocumentHeader();
            String documentId = "";
            if (principalDocumentHeader.containsKey(principalId)) {
                tdh = principalDocumentHeader.get(principalId);
                documentId = principalDocumentHeader.get(principalId).getDocumentId();
            }
            List<TimeBlock> timeBlocks = new ArrayList<TimeBlock>();
            List notes = new ArrayList();
            List<String> warnings = new ArrayList<String>();

//            TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
            if (StringUtils.isNotBlank(documentId)) {
                timeBlocks = TkServiceLocator.getTimeBlockService().getTimeBlocks(Long.parseLong(documentId));
                notes = this.getNotesForDocument(documentId);
                warnings = TkServiceLocator.getWarningService().getWarnings(documentId);
            }

            Person person = KIMServiceLocator.getPersonService().getPerson(principalId);
            CalendarEntries calendarEntry = TkServiceLocator.getCalendarSerivce().getCalendarDatesByPayEndDate(principalId, TKUtils.getTimelessDate(payEndDate));

            List<String> pyCalendarLabels = TkServiceLocator.getTimeSummaryService().getHeaderForSummary(calendarEntry, new ArrayList<Boolean>());

            Map<String, BigDecimal> hoursToPayLabelMap = getHoursToPayDayMap(principalId, payEndDate, pyCalendarLabels, timeBlocks, null);

            ApprovalTimeSummaryRow approvalSummaryRow = new ApprovalTimeSummaryRow();
            approvalSummaryRow.setName(person.getName());
            approvalSummaryRow.setPrincipalId(person.getPrincipalId());
            approvalSummaryRow.setCalendarGroup(calGroup);
            approvalSummaryRow.setDocumentId(documentId);
            approvalSummaryRow.setLstTimeBlocks(timeBlocks);
            if (principalDocumentHeader.containsKey(principalId)) {
                approvalSummaryRow.setApprovalStatus(tdh.getDocumentStatus());
                approvalSummaryRow.setApprovalStatusMessage(TkConstants.DOC_ROUTE_STATUS.get(tdh.getDocumentStatus()));
                TimesheetDocument td = TkServiceLocator.getTimesheetService().getTimesheetDocument(tdh.getDocumentId());
                TimeSummary ts = TkServiceLocator.getTimeSummaryService().getTimeSummary(td);
                approvalSummaryRow.setTimeSummary(ts);
            }
            approvalSummaryRow.setHoursToPayLabelMap(hoursToPayLabelMap);
            approvalSummaryRow.setPeriodTotal(hoursToPayLabelMap.get("Period Total"));
            approvalSummaryRow.setNotes(notes);
            approvalSummaryRow.setWarnings(warnings);

            // Compare last clock log versus now and if > threshold
            // highlight entry
            ClockLog lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog(principalId);
            approvalSummaryRow.setClockStatusMessage(createLabelForLastClockLog(lastClockLog));
            if (lastClockLog != null &&
                    (StringUtils.equals(lastClockLog.getClockAction(), TkConstants.CLOCK_IN) || StringUtils.equals(lastClockLog.getClockAction(), TkConstants.LUNCH_IN))) {
                DateTime startTime = new DateTime(lastClockLog.getClockTimestamp().getTime());
                DateTime endTime = new DateTime(System.currentTimeMillis());

                Hours hour = Hours.hoursBetween(startTime, endTime);
                if (hour != null) {
                    int elapsedHours = hour.getHours();
                    if (elapsedHours >= TkConstants.NUMBER_OF_HOURS_CLOCKED_IN_APPROVE_TAB_HIGHLIGHT) {
                        approvalSummaryRow.setClockedInOverThreshold(true);
                    }
                }

            }
            rows.add(approvalSummaryRow);
        }

        return rows;
    }

    public List<TimesheetDocumentHeader> getDocumentHeadersByPrincipalIds(Date payBeginDate, Date payEndDate, List<String> principalIds) {
        List<TimesheetDocumentHeader> headers = new LinkedList<TimesheetDocumentHeader>();
        for (String principalId : principalIds) {
            TimesheetDocumentHeader tdh = TkServiceLocator.getTimesheetDocumentHeaderService().getDocumentHeader(principalId, payBeginDate, payEndDate);
            if (tdh != null) {
                headers.add(tdh);
            }
        }

        return headers;
    }

    /**
     * Get pay calendar labels for approval tab
     */
    @CacheResult
    public List<String> getCalendarLabelsForApprovalTab(Date payBeginDate, Date payEndDate) {
        // :) http://stackoverflow.com/questions/111933/why-shouldnt-i-use-hungarian-notation
        List<String> lstCalendarLabels = new ArrayList<String>();
        DateTime payBegin = new DateTime(payBeginDate.getTime());
        DateTime payEnd = new DateTime(payEndDate.getTime());
        DateTime currTime = payBegin;
        int dayCounter = 1;
        int weekCounter = 1;

        while (currTime.isBefore(payEnd)) {
            String labelForDay = createLabelForDay(currTime);
            lstCalendarLabels.add(labelForDay);
            currTime = currTime.plusDays(1);
            if ((dayCounter % 7) == 0) {
                lstCalendarLabels.add("Week " + weekCounter);
                weekCounter++;
            }
            dayCounter++;
        }
        lstCalendarLabels.add("Total Hours");
        return lstCalendarLabels;
    }

    /**
     * Create label for a given pay calendar day
     *
     * @param fromDate
     * @return
     */
    private String createLabelForDay(DateTime fromDate) {
        DateMidnight dateMidnight = new DateMidnight(fromDate);
        if (dateMidnight.compareTo(fromDate) == 0) {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM/dd");
            return fmt.print(fromDate);
        }
        DateTime toDate = fromDate.plusDays(1);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM/dd k:m:s");
        return fmt.print(fromDate) + "-" + fmt.print(toDate);
    }

    /**
     * Create label for the last clock log
     *
     * @param cl
     * @return
     */
    private String createLabelForLastClockLog(ClockLog cl) {
//    	return sdf.format(dt);
        if (cl == null) {
            return "No previous clock information";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String dateTime = sdf.format(new java.sql.Date(cl.getClockTimestamp().getTime()));
        if (StringUtils.equals(cl.getClockAction(), TkConstants.CLOCK_IN)) {
            return "Clocked in since: " + dateTime;
        } else if (StringUtils.equals(cl.getClockAction(), TkConstants.LUNCH_IN)) {
            return "At Lunch since: " + dateTime;
        } else if (StringUtils.equals(cl.getClockAction(), TkConstants.LUNCH_OUT)) {
            return "Returned from Lunch : " + dateTime;
        } else if (StringUtils.equals(cl.getClockAction(), TkConstants.CLOCK_OUT)) {
            return "Clocked out since: " + dateTime;
        } else {
            return "No previous clock information";
        }

    }

    public List<Map<String, Map<String, BigDecimal>>> getHoursByDayAssignmentBuckets(TkTimeBlockAggregate aggregate, List<Assignment> approverAssignments, List<String> calendarLabels) {
        Map<String, Assignment> mappedAssignments = mapAssignmentsByAssignmentKey(approverAssignments);
        List<List<TimeBlock>> blocksByDay = aggregate.getDayTimeBlockList();

        // (assignment_key, <List of Hours Summed by Day>)
        Map<String, List<BigDecimal>> approverHours = new HashMap<String, List<BigDecimal>>();
        Map<String, List<BigDecimal>> otherHours = new HashMap<String, List<BigDecimal>>();
        for (int day = 0; day < blocksByDay.size(); day++) {
            List<TimeBlock> dayBlocks = blocksByDay.get(day);
            for (TimeBlock block : dayBlocks) {
                List<BigDecimal> hours;
                // Approver vs. Other :: Set our day-hour-list object
                if (mappedAssignments.containsKey(block.getAssignmentKey())) {
                    hours = approverHours.get(block.getAssignmentKey());
                    if (hours == null) {
                        hours = new ArrayList<BigDecimal>();
                        approverHours.put(block.getAssignmentKey(), hours);
                    }
                } else {
                    hours = otherHours.get(block.getAssignmentKey());
                    if (hours == null) {
                        hours = new ArrayList<BigDecimal>();
                        otherHours.put(block.getAssignmentKey(), hours);
                    }
                }

                // Fill in zeroes for days with 0 hours / no time blocks
                for (int fill = hours.size(); fill <= day; fill++) {
                    hours.add(TkConstants.BIG_DECIMAL_SCALED_ZERO);
                }

                // Add time from time block to existing time.
                BigDecimal timeToAdd = hours.get(day);
                timeToAdd = timeToAdd.add(block.getHours(), TkConstants.MATH_CONTEXT);
                hours.set(day, timeToAdd);
            }
        }

        // Compute Weekly / Period Summary Totals for each Assignment.
        // assignment row, each row has a map of pay calendar label -> big decimal totals.
        Map<String, Map<String, BigDecimal>> approverAssignToPayHourTotals = new HashMap<String, Map<String, BigDecimal>>();
        Map<String, Map<String, BigDecimal>> otherAssignToPayHourTotals = new HashMap<String, Map<String, BigDecimal>>();

        // Pass by Reference
        generateSummaries(approverAssignToPayHourTotals, approverHours, calendarLabels);
        generateSummaries(otherAssignToPayHourTotals, otherHours, calendarLabels);

        // Add to our return list, "virtual" tuple.
        List<Map<String, Map<String, BigDecimal>>> returnTuple = new ArrayList<Map<String, Map<String, BigDecimal>>>(2);
        returnTuple.add(approverAssignToPayHourTotals);
        returnTuple.add(otherAssignToPayHourTotals);

        return returnTuple;
    }

    // Helper method for above method.
    private void generateSummaries(Map<String, Map<String, BigDecimal>> payHourTotals, Map<String, List<BigDecimal>> assignmentToHours, List<String> calendarLabels) {
        for (String key : assignmentToHours.keySet()) {
            // for every Assignment
            Map<String, BigDecimal> hoursToPayLabelMap = new LinkedHashMap<String, BigDecimal>();
            List<BigDecimal> dayTotals = assignmentToHours.get(key);
            int dayCount = 0;
            BigDecimal weekTotal = new BigDecimal(0.00);
            BigDecimal periodTotal = new BigDecimal(0.00);
            for (String calendarLabel : calendarLabels) {
                if (StringUtils.contains(calendarLabel, "Week")) {
                    hoursToPayLabelMap.put(calendarLabel, weekTotal);
                    weekTotal = new BigDecimal(0.00);
                } else if (StringUtils.contains(calendarLabel, "Total Hours")) {
                    hoursToPayLabelMap.put(calendarLabel, periodTotal);
                } else {
                    BigDecimal dayTotal = TkConstants.BIG_DECIMAL_SCALED_ZERO;
                    if (dayCount < dayTotals.size())
                        dayTotal = dayTotals.get(dayCount);

                    hoursToPayLabelMap.put(calendarLabel, dayTotal);
                    weekTotal = weekTotal.add(dayTotal, TkConstants.MATH_CONTEXT);
                    periodTotal = periodTotal.add(dayTotal);
                    dayCount++;
                }
            }
            payHourTotals.put(key, hoursToPayLabelMap);
        }
    }

    private Map<String, Assignment> mapAssignmentsByAssignmentKey(List<Assignment> assignments) {
        Map<String, Assignment> assignmentMap = new HashMap<String, Assignment>();
        for (Assignment assignment : assignments) {
            assignmentMap.put(AssignmentDescriptionKey.getAssignmentKeyString(assignment), assignment);
        }
        return assignmentMap;
    }

    /**
     * Aggregate TimeBlocks to hours per day and sum for week
     *
     * @param principalId
     * @param payEndDate
     * @param calendarLabels
     * @param lstTimeBlocks
     * @param workArea
     * @return
     */
    @Override
    public Map<String, BigDecimal> getHoursToPayDayMap(String principalId, Date payEndDate, List<String> calendarLabels, List<TimeBlock> lstTimeBlocks, Long workArea) {
        Map<String, BigDecimal> hoursToPayLabelMap = new LinkedHashMap<String, BigDecimal>();
        List<BigDecimal> dayTotals = new ArrayList<BigDecimal>();
        CalendarEntries calendarEntry = TkServiceLocator.getCalendarSerivce().getCalendarDatesByPayEndDate(principalId, payEndDate);
        // TODO: we should just pass in the timeblocks instead of calling TkTimeBlockAggregate twice..
        TkTimeBlockAggregate tkTimeBlockAggregate = new TkTimeBlockAggregate(lstTimeBlocks, calendarEntry, TkServiceLocator.getCalendarSerivce().getCalendar(calendarEntry.getHrCalendarId()), true);
        for (FlsaWeek week : tkTimeBlockAggregate.getFlsaWeeks(TkServiceLocator.getTimezoneService().getUserTimezoneWithFallback())) {
            for (FlsaDay day : week.getFlsaDays()) {
                BigDecimal total = new BigDecimal(0.00);
                for (TimeBlock tb : day.getAppliedTimeBlocks()) {
                    if (workArea != null) {
                        if (tb.getWorkArea().compareTo(workArea) == 0) {
                            total = total.add(tb.getHours(), TkConstants.MATH_CONTEXT);
                        } else {
                            total = total.add(new BigDecimal("0"), TkConstants.MATH_CONTEXT);
                        }
                    } else {
                        total = total.add(tb.getHours(), TkConstants.MATH_CONTEXT);
                    }
                }
                dayTotals.add(total);
            }
        }

        int dayCount = 0;
        BigDecimal weekTotal = new BigDecimal(0.00);
        BigDecimal periodTotal = new BigDecimal(0.00);
        for (String calendarLabel : calendarLabels) {
            if (StringUtils.contains(calendarLabel, "Week")) {
                hoursToPayLabelMap.put(calendarLabel, weekTotal);
                weekTotal = new BigDecimal(0.00);
            } else if (StringUtils.contains(calendarLabel, "Period Total")) {
                hoursToPayLabelMap.put(calendarLabel, periodTotal);
            } else {
                hoursToPayLabelMap.put(calendarLabel, dayTotals.get(dayCount));
                weekTotal = weekTotal.add(dayTotals.get(dayCount), TkConstants.MATH_CONTEXT);
                periodTotal = periodTotal.add(dayTotals.get(dayCount));
                dayCount++;

            }

        }
        return hoursToPayLabelMap;
    }

    public boolean doesApproverHavePrincipalsForCalendarGroup(Date asOfDate, String calGroup) {
        TKUser tkUser = TKContext.getUser();
        Set<Long> approverWorkAreas = tkUser.getCurrentRoles().getApproverWorkAreas();
        for (Long workArea : approverWorkAreas) {
            List<Assignment> assignments = TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(workArea, new java.sql.Date(asOfDate.getTime()));
            List<String> principalIds = new ArrayList<String>();
            for (Assignment assign : assignments) {
                if (principalIds.contains(assign.getPrincipalId())) {
                    continue;
                }
                principalIds.add(assign.getPrincipalId());
            }

            for (String principalId : principalIds) {
                PrincipalHRAttributes principalCal = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(principalId, asOfDate);
                if (StringUtils.equals(principalCal.getPayCalendar(), calGroup)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public List getNotesForDocument(String documentNumber) {
        List notes = KEWServiceLocator.getNoteService().getNotesByRouteHeaderId(Long.parseLong(documentNumber));
        return notes;
    }

    private static final String UNIQUE_PY_GROUP_SQL = "select distinct calendar_name from hr_calendar_t";

    @Override
    public List<String> getUniquePayGroups() {
        SqlRowSet rs = TkServiceLocator.getTkJdbcTemplate().queryForRowSet(UNIQUE_PY_GROUP_SQL);
        List<String> pyGroups = new LinkedList<String>();
        while (rs.next()) {
            pyGroups.add(rs.getString("calendar_name"));
        }

        return pyGroups;
    }

    @Override
    public List<String> getPrincipalIdsByAssignment(Set<Long> workAreas, java.sql.Date payEndDate, String calGroup, Integer start, Integer end) {
        List<String> list = getPrincipalIdsByAssignment(workAreas, payEndDate, calGroup);
        return list.subList(start, end);
    }

    @Override
    public List<String> getPrincipalIdsByAssignment(Set<Long> workAreas, java.sql.Date payEndDate, String calGroup) {
        //        List<Assignment> activeAssignments = new ArrayList<Assignment>(); //getActiveAssignmentsAndPrincipalCalendars(workAreas, payEndDate);
        List<Assignment> activeAssignments = getActiveAssignmentsAndPrincipalCalendars(workAreas, payEndDate);
        List<String> principalIds = new LinkedList<String>();

//        for (Long aWorkArea : workAreas) {
//            activeAssignments.addAll(TkServiceLocator.getAssignmentService().getActiveAssignmentsForWorkArea(aWorkArea, new java.sql.Date(payEndDate.getTime())));
//        }
        if (!activeAssignments.isEmpty()) {
            for (Assignment assign : activeAssignments) {
                PrincipalHRAttributes principalCalendar = TkServiceLocator.getPrincipalHRAttributesService().getPrincipalCalendar(assign.getPrincipalId(), payEndDate);
                //TODO remove this comparision sometiem
                if (!principalIds.contains(assign.getPrincipalId()) && StringUtils.equals(principalCalendar.getPayCalendar(), calGroup)) {
                    principalIds.add(assign.getPrincipalId());
                }
            }
        }

        return principalIds;
    }

    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    private List<Assignment> getActiveAssignmentsAndPrincipalCalendars(Set<Long> approverWorkAres, java.sql.Date effdt) {
        // We didn't need to select the max effdt and max timestamp here,
        // because we only need the list of the principal ids and it doesn't matter if we select the latest row or not
        String sql =
                "SELECT DISTINCT " +
                        "A0.principal_id,A0.work_area,C0.calendar_name " +
                        "FROM tk_assignment_t A0,hr_principal_hr_attributes_t C0 " +
                        "WHERE (###) " +
                        "AND A0.effdt <= ? " +
                        "AND C0.principal_id = A0.principal_id";

        // prepare the OR statement for query
        StringBuilder workAreas = new StringBuilder();
        for (long workarea : approverWorkAres) {
            workAreas.append("work_area = " + workarea + " or ");
        }
        String workAresForQuery = workAreas.substring(0, workAreas.length() - 3);
        sql = sql.replaceAll("###", workAresForQuery);

        List<Assignment> assignments = new ArrayList<Assignment>();
        SqlRowSet rs = TkServiceLocator.getTkJdbcTemplate().queryForRowSet(sql, new Object[]{effdt}, new int[]{Types.DATE});
        while (rs.next()) {
            Assignment assignment = new Assignment();
            assignment.setPrincipalId(rs.getString("principal_id"));
            assignment.setWorkArea(rs.getLong("work_area"));
            assignment.setCalGroup(rs.getString("calendar_name"));

            assignments.add(assignment);
        }

        return assignments;
    }

    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public Map<String, TimesheetDocumentHeader> getPrincipalDocumehtHeader(List<String> principalIds, Date payBeginDate, Date payEndDate) {

        if (principalIds.size() == 0) {
            return new LinkedHashMap<String, TimesheetDocumentHeader>();
        }

        String sql = "SELECT document_id, principal_id, document_status " +
                "FROM tk_document_header_t " +
                "WHERE (###) AND pay_begin_dt >= ? AND pay_end_dt <= ?";
        StringBuilder ids = new StringBuilder();
        for (String principalId : principalIds) {
            ids.append("principal_id = '" + principalId + "' or ");
        }
        String idsForQuery = ids.substring(0, ids.length() - 4);
        sql = sql.replaceAll("###", idsForQuery);
        Map<String, TimesheetDocumentHeader> principalDocumentHeader = new LinkedHashMap<String, TimesheetDocumentHeader>();
        SqlRowSet rs = TkServiceLocator.getTkJdbcTemplate().queryForRowSet(sql,
                new Object[]{payBeginDate, payEndDate}, new int[]{Types.DATE, Types.DATE});
        while (rs.next()) {
            TimesheetDocumentHeader tdh = new TimesheetDocumentHeader();
            tdh.setPrincipalId(rs.getString("principal_id"));
            String docId = StringUtils.isBlank(rs.getString("document_id")) ? "" : rs.getString("document_id");
            tdh.setDocumentId(docId);
            tdh.setDocumentStatus(rs.getString("document_status"));

            principalDocumentHeader.put(rs.getString("principal_id"), tdh);
        }

        return principalDocumentHeader;
    }
}

