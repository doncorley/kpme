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
package org.kuali.hr.lm.leave.approval;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.hr.KPMEWebTestCase;
import org.kuali.hr.util.HtmlUnitUtil;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.tklm.leave.calendar.LeaveCalendarDocument;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;
import org.kuali.kpme.tklm.utils.TkTestConstants;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LeaveCalendarWebTest extends KPMEWebTestCase {
	
	private String documentId;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		CalendarEntry firstCalendarEntry = HrServiceLocator.getCalendarEntryService().getCalendarEntry("202");
        LeaveCalendarDocument firstLeaveCalendarDocument = LmServiceLocator.getLeaveCalendarService().openLeaveCalendarDocument("admin", firstCalendarEntry);
        documentId = firstLeaveCalendarDocument.getDocumentId();
        
        CalendarEntry secondCalendarEntry = HrServiceLocator.getCalendarEntryService().getCalendarEntry("203");
        LmServiceLocator.getLeaveCalendarService().openLeaveCalendarDocument("admin", secondCalendarEntry);
        
        CalendarEntry thirdCalendarEntry = HrServiceLocator.getCalendarEntryService().getCalendarEntry("204");
        LmServiceLocator.getLeaveCalendarService().openLeaveCalendarDocument("admin", thirdCalendarEntry);
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

    @Override
	public void setWebClient(WebClient webClient) {
        super.setWebClient(webClient);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(10000);
	}

	@Test
	public void testLeaveCalendarPage() throws Exception {
		// get the page and Login
		HtmlPage leaveCalendarPage = HtmlUnitUtil
				.gotoPageAndLogin(getWebClient(), TkTestConstants.Urls.LEAVE_CALENDAR_URL+"?documentId=" + documentId, true);
		Assert.assertNotNull("Leave Request page not found" ,leaveCalendarPage);

		//this.setWebClient(leaveCalendarPage.getWebClient());

		Assert.assertTrue("Page does not have Current calendar ", leaveCalendarPage.asText().contains("March 2012"));

        // Check for next document
        HtmlButton nextButton = (HtmlButton) leaveCalendarPage
                .getElementById("nav_next_lc");
        Assert.assertNotNull(nextButton);
        //TODO: click not working.  Not even getting to the 'execute' method in LeaveCalendarAction
        HtmlPage page = nextButton.click();
        Assert.assertNotNull(page);
        
        synchronized (page) {
            page.wait(5000);
        }
        String pageText = page.asText();
        LOG.info(pageText);
		// Check for previous document
		HtmlButton prevButton = (HtmlButton) page
				.getElementById("nav_prev_lc");
		Assert.assertNotNull(prevButton);
        page = prevButton.click();
        Assert.assertNotNull(page);

	}

}
