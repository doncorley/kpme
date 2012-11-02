/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.util;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.lm.leaveblock.LeaveBlock;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.detail.web.ActionFormUtils;
import org.kuali.hr.time.earncode.EarnCode;
import org.kuali.hr.time.service.base.TkServiceLocator;

public class ActionFormUtilsTest extends KPMETestCase {
	
	@Test
	public void testFmlaWarningTextForLeaveBlocks() throws Exception {
		// create two leave blocks with two different earn codes
		// earn code "ECA" has fmla=Y
		// earn Code "ECB" has fmla = N
		
		List<LeaveBlock> leaveBlocs = new ArrayList<LeaveBlock>();
		LeaveBlock lbA = new LeaveBlock();
		lbA.setEarnCode("ECA");
		lbA.setLeaveDate(TKUtils.getCurrentDate());
		leaveBlocs.add(lbA);
		
		LeaveBlock lbB = new LeaveBlock();
		lbB.setEarnCode("ECB");
		lbA.setLeaveDate(TKUtils.getCurrentDate());
		leaveBlocs.add(lbB);
		
		List<String> warningMess = ActionFormUtils.fmlaWarningTextForLeaveBlocks(leaveBlocs);
		Assert.assertTrue("There should be 1 warning message ", warningMess.size()== 1);
		String warning = warningMess.get(0);
		Assert.assertTrue("Warning message should be 'Test Message'", warning.equals("Test Message"));
		
	}
	@Test
	public void testGetUnitOfTimeForEarnCode() throws Exception {
		// earn code with an existing Accrual category
		EarnCode earnCode = TkServiceLocator.getEarnCodeService().getEarnCodeById("5000");
		String unitOfTime = ActionFormUtils.getUnitOfTimeForEarnCode(earnCode);
		Assert.assertTrue("Unit of Time should be 'D', not " + unitOfTime, unitOfTime.equals("D"));
		// earn code without an existing accrual category
		earnCode = TkServiceLocator.getEarnCodeService().getEarnCodeById("5002");
		unitOfTime = ActionFormUtils.getUnitOfTimeForEarnCode(earnCode);
		Assert.assertTrue("Unit of Time should be 'H', not " + unitOfTime, unitOfTime.equals("H"));
		
	}
	
	@Test
	public void testGetLeaveBlocksJson() {
		List<LeaveBlock> lbList = new ArrayList<LeaveBlock>();
		LeaveBlock lb = new LeaveBlock();
		lb.setAssignmentTitle("testAssignment");
		lb.setAssignmentKey("0-123-0");
		lb.setEarnCode("EarnCode");
		lb.setLmLeaveBlockId("1111");
		lb.setLeaveAmount(new BigDecimal(3));
		lb.setLeaveDate(new Date((new DateTime(2012, 2, 20, 0, 0, 0, 0, TKUtils.getSystemDateTimeZone())).getMillis()));	
		lbList.add(lb);
		
		String jsonString = ActionFormUtils.getLeaveBlocksJson(lbList);
		String expectedString = "[{\"title\":\"\",\"assignment\":\"0-123-0\",\"earnCode\":\"EarnCode\",\"lmLeaveBlockId\":\"1111\",\"leaveAmount\":\"3\",\"leaveDate\":\"02\\/20\\/2012\",\"id\":\"1111\"}]";
		Assert.assertTrue("Leave Block Json should include assignment", jsonString.equals(expectedString));
		
		
	}
	

}
