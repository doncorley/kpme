package org.kuali.hr.time.assignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.kuali.hr.time.util.TkConstants;

public class AssignmentDescriptionKey {
	
	private Long jobNumber;
	private Long workArea;
	private Long task;
	
	@Override
	public String toString() {
		return "jobNumber: " + jobNumber + "; workArea: " + workArea + "; task: " + task;
	}
	
	public String toAssignmentKeyString() {
		return jobNumber + TkConstants.ASSIGNMENT_KEY_DELIMITER + workArea + TkConstants.ASSIGNMENT_KEY_DELIMITER + task; 
	}
	
	public AssignmentDescriptionKey(String jobNumer, String workArea, String task) {
		this.jobNumber = Long.parseLong(jobNumer);
		this.workArea = Long.parseLong(workArea);
		this.task = Long.parseLong(task);
	}
	public AssignmentDescriptionKey(Long jobNumer, Long workArea, Long task) {
		this.jobNumber = jobNumer;
		this.workArea = workArea;
		this.task = task;
	}
	public AssignmentDescriptionKey(String assignmentKey) {
		
		Pattern keyPattern = Pattern.compile("^\\d{1,}_\\d{1,}_\\d{1,}");
		Matcher match = keyPattern.matcher(assignmentKey);
		if(!match.matches()) {
			throw new RuntimeException("the format of the assignment key is wrong. it should be jobNubmer_workArea_task");
		}
		
		String[] key = assignmentKey.split(TkConstants.ASSIGNMENT_KEY_DELIMITER);
		
		this.jobNumber = Long.parseLong(key[0]);
		this.workArea = Long.parseLong(key[1]);
		this.task = Long.parseLong(key[2]);
		
	}
	public Long getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(Long jobNumber) {
		this.jobNumber = jobNumber;
	}
	public Long getWorkArea() {
		return workArea;
	}
	public void setWorkArea(Long workArea) {
		this.workArea = workArea;
	}
	public Long getTask() {
		return task;
	}
	public void setTask(Long task) {
		this.task = task;
	}
}
