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
package org.kuali.kpme.tklm.time.clock.log;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.kpme.tklm.TKLMUnitTestCase;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;

public class ClockLogServiceTest extends TKLMUnitTestCase {
	
	@Test
	public void testGetUnapprovedIPWarning() throws Exception {
		List<TimeBlock> tbLists = new ArrayList<TimeBlock>();
		TimeBlock timeBlock = new TimeBlock();
        timeBlock.setUserPrincipalId("testUser");
        timeBlock.setClockLogCreated(true);
        timeBlock.setClockLogEndId("5000");
        tbLists.add(timeBlock);		
		
		List<String> warningList = TkServiceLocator.getClockLogService().getUnapprovedIPWarning(tbLists);
		Assert.assertTrue("There should be 1 warning message ", warningList.size()== 1);
		String warning = warningList.get(0);
		Assert.assertTrue("Warning message should be 'Warning: Action 'Clock Out' taken at 2012-03-01 08:08:08.0 was from an unapproved IP address - TEST', not " + warning, 
				warning.equals("Warning: Action 'Clock Out' taken at 2012-03-01 08:08:08.0 was from an unapproved IP address - TEST"));
		
	}
}
