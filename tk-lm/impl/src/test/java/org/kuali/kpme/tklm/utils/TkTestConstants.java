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
package org.kuali.kpme.tklm.utils;

import org.kuali.kpme.core.util.HrTestConstants;

public final class TkTestConstants {
	
	private static String BASE_URL = HrTestConstants.BASE_URL;

	public static class Urls {

		public static final String TIME_COLLECTION_RULE_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.rules.timecollection.TimeCollectionRule&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String SHIFT_DIFFERENTIAL_RULE_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.rules.shiftdifferential.ShiftDifferentialRule&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String WEEKLY_OVERTIME_RULE_MAINT_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.time.rules.overtime.weekly.WeeklyOvertimeRuleGroup&tkWeeklyOvertimeRuleGroupId=1&returnLocation="+
		BASE_URL + "/portal.do&methodToCall=edit";

		public static final String DAILY_OVERTIME_RULE_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.rules.overtime.daily.DailyOvertimeRule&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String CLOCK_LOG_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.clocklog.ClockLog&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String DEPT_LUNCH_RULE_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.rules.lunch.department.DeptLunchRule&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String DOC_HEADER_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String EMPLOYEE_OVERRIDE_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.override.EmployeeOverride&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";
		
		public static final String LEAVE_ADJUSTMENT_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.adjustment.LeaveAdjustment&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";
		
		public static final String TIME_OFF_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.timeoff.SystemScheduledTimeOff&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";
		
		public static final String LEAVE_DONATION_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.donation.LeaveDonation&returnLocation="+
		BASE_URL + "/portal.do&hideReturnLink=true&docFormKey=88888888";

		public static final String WEEKLY_OVERTIME_RULE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.time.rules.overtime.weekly.WeeklyOvertimeRuleGroup&tkWeeklyOvertimeRuleGroupId=1&returnLocation=" + BASE_URL + "/portal.do&methodToCall=edit";
		
		public static final String SHIFT_DIFFERENTIAL_RULE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.time.rules.shiftdifferential.ShiftDifferentialRule&methodToCall=start#topOfForm";

		public static final String EMPLOYEE_OVERRIDE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.leave.override.EmployeeOverride&methodToCall=start";
		
		public static final String LEAVE_ADJUSTMENT_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.leave.adjustment.LeaveAdjustment&methodToCall=start";
		
		public static final String TIME_OFF_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.leave.timeoff.SystemScheduledTimeOff&methodToCall=start";

		public static final String LEAVE_DONATION_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.leave.donation.LeaveDonation&methodToCall=start";
		
        public static final String DELETE_TIMESHEET_URL = BASE_URL + "/deleteTimesheet.do";
		
		public static final String CLOCK_URL = BASE_URL + "/Clock.do";

		public static final String TIME_DETAIL_URL = BASE_URL + "/TimeDetail.do";

		public static final String PERSON_INFO_URL = BASE_URL + "/PersonInfo.do";
        
        public static final String LEAVE_REQUEST_PAGE_URL = BASE_URL + "/LeaveRequest.do";
        
        public static final String LEAVE_BLOCK_DISPLAY_URL = BASE_URL + "/LeaveBlockDisplay.do";
        
        public static final String LEAVE_CALENDAR_URL = BASE_URL + "/LeaveCalendar.do";
        
        public static final String LEAVE_CALENDAR_SUBMIT_URL = BASE_URL + "/LeaveCalendarSubmit.do";
        
        public static final String BALANCE_TRANSFER_MAINT_NEW_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.transfer.BalanceTransfer&returnLocation="+
        BASE_URL+"/portal.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y";
        
        public static final String BALANCE_TRANSFER_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.transfer.BalanceTransfer&returnLocation="+
        BASE_URL+"http://ci.kpme.kuali.org:80/kpme-trunk/portal.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y";

        public static final String LEAVE_PAYOUT_MAINT_NEW_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.payout.LeavePayout&returnLocation="+
                BASE_URL+"/portal.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y";

        public static final String LEAVE_PAYOUT_MAINT_URL = BASE_URL + "/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.leave.payout.LeavePayout&returnLocation="+
                BASE_URL+"http://ci.kpme.kuali.org:80/kpme-trunk/portal.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y";
        public static final String LOG_OUT_URL = BASE_URL + "/SessionInvalidateAction.do?methodToCall=userLogout";
    
        public static final String TIME_COLLECTION_RULE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.time.rules.timecollection.TimeCollectionRule&methodToCall=start";
	
		public static final String DAILY_OVERTIME_RULE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.tklm.time.rules.overtime.daily.DailyOvertimeRule&methodToCall=start";
	}

}
