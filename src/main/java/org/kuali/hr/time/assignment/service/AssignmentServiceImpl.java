package org.kuali.hr.time.assignment.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.hr.time.assignment.Assignment;
import org.kuali.hr.time.assignment.AssignmentDescriptionKey;
import org.kuali.hr.time.assignment.dao.AssignmentDao;
import org.kuali.hr.time.cache.CacheResult;
import org.kuali.hr.time.calendar.CalendarEntries;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.timesheet.TimesheetDocument;
import org.kuali.hr.time.util.TKContext;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.hr.time.util.TkConstants;

import java.sql.Date;
import java.util.*;

public class AssignmentServiceImpl implements AssignmentService {

    private static final Logger LOG = Logger.getLogger(AssignmentServiceImpl.class);
    private AssignmentDao assignmentDao;

    public AssignmentDao getAssignmentDao() {
        return assignmentDao;
    }

    public void setAssignmentDao(AssignmentDao assignmentDao) {
        this.assignmentDao = assignmentDao;
    }


    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public List<Assignment> getAssignments(String principalId, Date asOfDate) {
        List<Assignment> assignments;

        if (asOfDate == null) {
            asOfDate = TKUtils.getCurrentDate();
        }

        assignments = assignmentDao.findAssignments(principalId, asOfDate);

        for (Assignment assignment : assignments) {
            populateAssignment(assignment, asOfDate);
        }

        return assignments;
    }

    public List<Assignment> getAssignments(String principalId, Date beginDate, Date endDate) {
        List<Assignment> assignments;

        assignments = assignmentDao.findAssignmentsWithinPeriod(principalId, beginDate, endDate);

        for (Assignment assignment : assignments) {
            populateAssignment(assignment, assignment.getEffectiveDate());
        }

        return assignments;
    }

    public List<Assignment> getAssignmentsByPayEntry(String principalId, CalendarEntries payCalendarEntry) {
        List<Assignment> beginPeriodAssign = getAssignments(principalId, payCalendarEntry.getBeginPeriodDate());
        List<Assignment> endPeriodAssign = getAssignments(principalId, payCalendarEntry.getEndPeriodDate());
        List<Assignment> assignsWithPeriod = getAssignments(principalId, payCalendarEntry.getBeginPeriodDate(), payCalendarEntry.getEndPeriodDate());

        List<Assignment> finalAssignments = new ArrayList<Assignment>();
        Map<String, Assignment> assignKeyToAssignmentMap = new HashMap<String, Assignment>();
        for (Assignment assign : endPeriodAssign) {
            assignKeyToAssignmentMap.put(TKUtils.formatAssignmentKey(assign.getJobNumber(), assign.getWorkArea(), assign.getTask()), assign);
            finalAssignments.add(assign);
        }

        //Compare the begin and end and add any assignments to the end thats are not there
        for (Assignment assign : beginPeriodAssign) {
            String assignKey = TKUtils.formatAssignmentKey(assign.getJobNumber(), assign.getWorkArea(), assign.getTask());
            if (!assignKeyToAssignmentMap.containsKey(assignKey)) {
                finalAssignments.add(assign);
            }
        }

        // Add the assignments within the pay period
        for (Assignment assign : assignsWithPeriod) {
            String assignKey = TKUtils.formatAssignmentKey(assign.getJobNumber(), assign.getWorkArea(), assign.getTask());
            if (!assignKeyToAssignmentMap.containsKey(assignKey)) {
                finalAssignments.add(assign);
            }
        }

        return finalAssignments;

    }

    @Override
    public AssignmentDescriptionKey getAssignmentDescriptionKey(String assignmentKey) {
        return new AssignmentDescriptionKey(assignmentKey);
    }

    @Override
    public Map<String, String> getAssignmentDescriptions(TimesheetDocument td, boolean clockOnlyAssignments) {
        if (td == null) {
            throw new RuntimeException("timesheet document is null.");
        }
        List<Assignment> assignments = td.getAssignments();
//		if(assignments.size() < 1) {
//			throw new RuntimeException("No assignment on the timesheet document.");
//		}

        Map<String, String> assignmentDescriptions = new LinkedHashMap<String, String>();
        for (Assignment assignment : assignments) {
            //if the user is not the same as the timesheet and does not have approver access for the assignment
            //do not add to the display
            if (!StringUtils.equals(TKContext.getTargetPrincipalId(), TKContext.getPrincipalId())) {
                if (!TKContext.getUser().isSystemAdmin() && !TKContext.getUser().getReportingWorkAreas().contains(assignment.getWorkArea())) {
                    continue;
                }
            }

            //only add to the assignment list if they are synchronous assignments
            //or clock only assignments is false
            if (!clockOnlyAssignments || assignment.isSynchronous()) {
                assignmentDescriptions.putAll(TKUtils.formatAssignmentDescription(assignment));
            }
        }

        return assignmentDescriptions;
    }

    @Override
    public Map<String, String> getAssignmentDescriptions(Assignment assignment) {
        if (assignment == null) {
            throw new RuntimeException("Assignment is null");
        }

        Map<String, String> assignmentDescriptions = new LinkedHashMap<String, String>();
        assignmentDescriptions.putAll(TKUtils.formatAssignmentDescription(assignment));

        return assignmentDescriptions;

    }

    @Override
    public Assignment getAssignment(TimesheetDocument timesheetDocument, String assignmentKey) {
        List<Assignment> assignments = timesheetDocument.getAssignments();
        AssignmentDescriptionKey desc = getAssignmentDescriptionKey(assignmentKey);

        for (Assignment assignment : assignments) {
            if (assignment.getJobNumber().compareTo(desc.getJobNumber()) == 0 &&
                    assignment.getWorkArea().compareTo(desc.getWorkArea()) == 0 &&
                    assignment.getTask().compareTo(desc.getTask()) == 0) {
                return assignment;
            }
        }

        //No assignment found so fetch the inactive ones for this payBeginDate
        Assignment assign = TkServiceLocator.getAssignmentService().getAssignment(desc, timesheetDocument.getPayCalendarEntry().getBeginPeriodDate());
        if (assign != null) {
            return assign;
        }


        LOG.warn("no matched assignment found");
        return new Assignment();
    }

    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public Assignment getAssignment(String tkAssignmentId) {
        return getAssignmentDao().getAssignment(tkAssignmentId);
    }


    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public List<Assignment> getActiveAssignmentsForWorkArea(Long workArea, Date asOfDate) {
        List<Assignment> assignments = assignmentDao.getActiveAssignmentsInWorkArea(workArea, asOfDate);
        for (Assignment assignment : assignments) {
            populateAssignment(assignment, asOfDate);
        }
        return assignments;
    }

    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public List<Assignment> getActiveAssignments(Date asOfDate) {
        return assignmentDao.getActiveAssignments(asOfDate);
    }

    private void populateAssignment(Assignment assignment, Date asOfDate) {
        assignment.setJob(TkServiceLocator.getJobSerivce().getJob(assignment.getPrincipalId(), assignment.getJobNumber(), asOfDate));
        assignment.setTimeCollectionRule(TkServiceLocator.getTimeCollectionRuleService().getTimeCollectionRule(assignment.getJob().getDept(), assignment.getWorkArea(), assignment.getJob().getHrPayType(),asOfDate));
        assignment.setWorkAreaObj(TkServiceLocator.getWorkAreaService().getWorkArea(assignment.getWorkArea(), asOfDate));
        assignment.setDeptLunchRule(TkServiceLocator.getDepartmentLunchRuleService().getDepartmentLunchRule(assignment.getJob().getDept(),
                assignment.getWorkArea(), assignment.getPrincipalId(), assignment.getJobNumber(), asOfDate));
    }

    public Assignment getAssignment(String principalId, AssignmentDescriptionKey key, Date asOfDate) {
        Assignment a = null;

        if (key != null) {
            a = assignmentDao.getAssignment(principalId, key.getJobNumber(), key.getWorkArea(), key.getTask(), asOfDate);
        }

        return a;
    }

    @Override
    public Assignment getAssignment(AssignmentDescriptionKey key, Date asOfDate) {
        Assignment a = null;

        if (key != null) {
            a = assignmentDao.getAssignment(key.getJobNumber(), key.getWorkArea(), key.getTask(), asOfDate);
        }

        return a;
    }

    /**
     * KPME-1129 Kagata
     * Get a list of active assignments based on principalId and jobNumber as of a particular date
     */
    @Override
    @CacheResult(secondsRefreshPeriod = TkConstants.DEFAULT_CACHE_TIME)
    public List<Assignment> getActiveAssignmentsForJob(String principalId, Long jobNumber, Date asOfDate) {
        List<Assignment> assignments = assignmentDao.getActiveAssignmentsForJob(principalId, jobNumber, asOfDate);

        return assignments;
    }

}
