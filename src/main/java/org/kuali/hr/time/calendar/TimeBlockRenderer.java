package org.kuali.hr.time.calendar;

import org.apache.commons.lang.StringUtils;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.task.Task;
import org.kuali.hr.time.timeblock.TimeBlock;
import org.kuali.hr.time.timeblock.TimeHourDetail;
import org.kuali.hr.time.util.TkConstants;
import org.kuali.hr.time.workarea.WorkArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Render helper to handle timeblock and time hour details display
 */
public class TimeBlockRenderer {

    private TimeBlock timeBlock;
    private List<TimeHourDetailRenderer> detailRenderers = new ArrayList<TimeHourDetailRenderer>();
    private String assignmentClass;
    private String lunchLabel;
    private String lunchLabelId;

    public TimeBlockRenderer(TimeBlock b) {
        this.timeBlock = b;
        for (TimeHourDetail detail : timeBlock.getTimeHourDetails()) {
            detailRenderers.add(new TimeHourDetailRenderer(detail));
        }
    }

    public List<TimeHourDetailRenderer> getDetailRenderers() {
        return detailRenderers;
    }

    public TimeBlock getTimeBlock() {
        return timeBlock;
    }

    public String getTimeRange() {
        StringBuilder b = new StringBuilder();
        if(StringUtils.equals(timeBlock.getEarnCodeType(), TkConstants.EARN_CODE_TIME)) {
            b.append(timeBlock.getBeginTimeDisplay().toString(TkConstants.DT_BASIC_TIME_FORMAT));
            b.append(" - ");
            b.append(timeBlock.getEndTimeDisplay().toString(TkConstants.DT_BASIC_TIME_FORMAT));
        }

        return b.toString();
    }

    public String getTitle() {
        StringBuilder b = new StringBuilder();

        WorkArea wa = TkServiceLocator.getWorkAreaService().getWorkArea(timeBlock.getTkWorkAreaId());
        b.append(wa.getDescription());
        Task task = TkServiceLocator.getTaskService().getTask(timeBlock.getTask(), timeBlock.getBeginDate());
        if(task != null) {
        	// do not display task description if the task is the default one
        	// default task is created in getTask() of TaskService
        	if(!task.getDescription().equals(TkConstants.TASK_DEFAULT_DESP)) {
        		b.append("-" + task.getDescription());
        	}
        }
        return b.toString();
    }

    public String getAmount() {
    	if(StringUtils.equals(timeBlock.getEarnCodeType(), TkConstants.EARN_CODE_AMOUNT)) {
    		if(timeBlock.getAmount() != null) {
    			return timeBlock.getEarnCode() + ": $" + timeBlock.getAmount().toString();
    		} else {
    			return timeBlock.getEarnCode() + ": $0.00";
    		}
	    }
    	return "";
    }

    public String getEarnCodeType() {
    	return timeBlock.getEarnCodeType();
    }

	public String getAssignmentClass() {
		return assignmentClass;
	}

	public void setAssignmentClass(String assignmentClass) {
		this.assignmentClass = assignmentClass;
	}

	public String getLunchLabel() {
		return lunchLabel;
	}

	public void setLunchLabel(String lunchLabel) {
		this.lunchLabel = lunchLabel;
	}

    public String getLunchLabelId() {
        return lunchLabelId;
    }

    public void setLunchLabelId(String lunchLabelId) {
        this.lunchLabelId = lunchLabelId;
    }
}
