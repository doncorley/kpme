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
package org.kuali.hr.time.clock.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.hr.KPMEWebTestCase;
import org.kuali.hr.util.HtmlUnitUtil;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.HrTestConstants;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.common.TkConstants;
import org.kuali.kpme.tklm.time.clocklog.ClockLog;
import org.kuali.kpme.tklm.time.rules.graceperiod.GracePeriodRule;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timeblock.TimeBlock;
import org.kuali.kpme.tklm.time.timehourdetail.TimeHourDetail;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;
import org.kuali.kpme.tklm.time.workflow.TimesheetDocumentHeader;
import org.kuali.kpme.tklm.utils.TkTestConstants;
import org.kuali.rice.krad.service.KRADServiceLocator;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class ClockWebTest extends KPMEWebTestCase {

    private String tbId;

    public Long maxDocumentId() {
        Collection aCol = KRADServiceLocator.getBusinessObjectService().findAll(TimesheetDocumentHeader.class);
        Long maxId = new Long(-1);
        Iterator<TimesheetDocumentHeader> itr = aCol.iterator();
        while (itr.hasNext()) {
            TimesheetDocumentHeader tdh = itr.next();
            Long temp = new Long(tdh.getDocumentId());
            if (temp > maxId) {
                maxId = temp;
            }
        }
        return maxId;
    }

    public Long maxTimeBlockId() {
        Collection aCol = KRADServiceLocator.getBusinessObjectService().findAll(TimeBlock.class);
        Long maxId = new Long(-1);
        Iterator<TimeBlock> itr = aCol.iterator();
        while (itr.hasNext()) {
            TimeBlock tb = itr.next();
            Long temp = new Long(tb.getTkTimeBlockId());
            if (temp > maxId) {
                maxId = temp;
            }
        }
        return maxId;
    }

    public void createTB() {
        TimeBlock timeBlock = new TimeBlock();
        timeBlock.setUserPrincipalId("admin");
        timeBlock.setJobNumber(2L);
        timeBlock.setWorkArea(1234L);
        timeBlock.setTask(1L);
        timeBlock.setEarnCode("RGN");
        timeBlock.setBeginTimestamp(TKUtils.getCurrentTimestamp());
        timeBlock.setEndTimestamp(TKUtils.getCurrentTimestamp());
        TimeHourDetail timeHourDetail = new TimeHourDetail();
        timeHourDetail.setEarnCode("RGN");
        timeHourDetail.setHours(new BigDecimal(2.0));
        timeBlock.getTimeHourDetails().add(timeHourDetail);
        timeBlock.setHours(new BigDecimal(2.0));
        List<TimeBlock> tbList = new ArrayList<TimeBlock>();
        String documentId = this.maxDocumentId().toString();
        timeBlock.setDocumentId(documentId);
        tbList.add(timeBlock);
        TkServiceLocator.getTimeBlockService().saveTimeBlocks(tbList);

        tbId = timeBlock.getTkTimeBlockId();
        TimesheetDocument td = TkServiceLocator.getTimesheetService().getTimesheetDocument(documentId);
        td.setTimeBlocks(tbList);

    }

    @Ignore
    public void testDistributeTB() throws Exception {
        String baseUrl = TkTestConstants.Urls.CLOCK_URL;
        HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), baseUrl);
        Assert.assertNotNull(page);
        Assert.assertTrue("Clock Page contains Distribute Button", page.asText().contains("Distribute Time Blocks"));
        this.createTB();
        updateWebClient();
        HtmlElement element = page.getElementByName("distributeTime");
        Assert.assertNotNull(element);
//	  	HtmlPage testPage1 = element.click();
//	  	assertTrue("Distribute Page contains Close button", testPage1.asText().contains("Close"));
//        assertTrue("Distribute Page contains Close button", testPage1.asText().contains("Edit"));

        // timeDistribute.jsp
        String distributeUrl = baseUrl + "?methodToCall=distributeTimeBlocks";
        HtmlPage page1 = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), distributeUrl);
        Assert.assertTrue("Distribute Page contains Close button", page1.asText().contains("Close"));
        Assert.assertTrue("Distribute Page contains Close button", page1.asText().contains("Edit"));

        element = page1.getElementByName("editTimeBlock");
        Assert.assertNotNull(element);
        Assert.assertTrue("Onclick attribute of Edit button contains", element.getAttribute("onclick").contains("Clock.do?methodToCall=editTimeBlock&editTimeBlockId="));

        if (tbId == null) {
            tbId = this.maxTimeBlockId().toString();
        }

        //editTimeBlock.jsp
        String editUrl = baseUrl + "?methodToCall=editTimeBlock&editTimeBlockId=" + tbId;
        HtmlPage page3 = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), editUrl);

        // editTimeBlock.jsp
        Assert.assertTrue("Edit Time Blocks Page contains Cancel button", page3.asText().contains("Add"));
        Assert.assertTrue("Edit Time Blocks Page contains Save button", page3.asText().contains("Save"));
        Assert.assertTrue("Edit Time Blocks Page contains Cancel button", page3.asText().contains("Cancel"));

        element = page3.getElementByName("addTimeBlock");
        Assert.assertNotNull(element);
        Assert.assertTrue("Onclick attribute of Add button contains", element.getAttribute("onclick").contains("javascript: addTimeBlockRow(this.form);"));

        updateWebClient();

//	  	HtmlPage page4 = element.click();
//	  	assertTrue("Edit Time Blocks Page contains Cancel button", page4.asText().contains("Add"));

    }

    public void updateWebClient() {
        WebClient webClient = getWebClient();
        webClient.setJavaScriptEnabled(true);
        webClient.setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(10000);
    }

    /**
     * This test is to :
     * 1) make sure the seconds on clockTimestamp and timestamp are preserved when there is no grace period rule.
     * 2) the clock in / out button is correctly rendered
     *
     * @throws Exception
     */
    @Test
    public void testClockActionWithoutGracePeriodRule() throws Exception {
        // Make sure there is no active grace period rule
        GracePeriodRule gpr = TkServiceLocator.getGracePeriodService().getGracePeriodRule(LocalDate.now());
        if (gpr != null && gpr.isActive()) {
            gpr.setActive(false);
            KRADServiceLocator.getBusinessObjectService().save(gpr);
        }

        // Clock in
        clockIn();
        // Make sure clock out button is rendered
        ClockLog lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog("admin");
        // Make sure both timestamps preserve seconds
        Assert.assertTrue("The seconds on clock timestamp should be preserved", lastClockLog.getClockDateTime().getSecondOfMinute() != 0);
        Assert.assertTrue("The seconds on timestamp should be preserved", new DateTime(lastClockLog.getTimestamp().getTime()).getSecondOfMinute() != 0);

        // Clock out
        clockOut();
        // Make sure both timestamps preserve seconds
        lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog("admin");
        Assert.assertTrue("The seconds on clock timestamp should be preserved", lastClockLog.getClockDateTime().getSecondOfMinute() != 0);
        Assert.assertTrue("The seconds on timestamp should be preserved", new DateTime(lastClockLog.getTimestamp().getTime()).getSecondOfMinute() != 0);
    }

    @Test
    public void testClockActionWithGracePeriodRule() throws Exception {
        //clean clock logs
        KRADServiceLocator.getBusinessObjectService().deleteMatching(ClockLog.class, Collections.singletonMap("principalId", "admin"));
        GracePeriodRule gpr = new GracePeriodRule();
        //gpr.setTkGracePeriodRuleId("1");
        gpr.setEffectiveLocalDate(new LocalDate(2010, 1, 1));
        gpr.setHourFactor(new BigDecimal(3));
        gpr.setTimestamp(TKUtils.getCurrentTimestamp());
        
        gpr.setActive(true);
        KRADServiceLocator.getBusinessObjectService().save(gpr);

        // Clock in
        clockIn();
        // Make sure clock out button is rendered
        ClockLog lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog("admin");
        // Make sure both timestamps preserve seconds
        Assert.assertTrue("The seconds on clock timestamp should NOT be preserved", lastClockLog.getClockDateTime().getSecondOfMinute() == 0);
        Assert.assertTrue("The seconds on timestamp should be preserved", new DateTime(lastClockLog.getTimestamp().getTime()).getSecondOfMinute() != 0);

        // Clock out
        clockOut();
        // Make sure both timestamps preserve seconds
        lastClockLog = TkServiceLocator.getClockLogService().getLastClockLog("admin");
        Assert.assertTrue("The seconds on clock timestamp should NOT be preserved", lastClockLog.getClockDateTime().getSecondOfMinute() == 0);
        Assert.assertTrue("The seconds on timestamp should be preserved", new DateTime(lastClockLog.getTimestamp().getTime()).getSecondOfMinute() != 0);


    }
    
    
	@Test
	public void testClockInOutWithTimezone() throws Exception {
		HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), TkTestConstants.Urls.CLOCK_URL,true);
		Assert.assertNotNull(page);
    	
    	Map<String, Object> criteria = new LinkedHashMap<String, Object>();
    	criteria.put("selectedAssignment", new String[]{HrTestConstants.FormElementTypes.DROPDOWN, "30_30_30"});
    	// choose the first assignment from the drop down
    	page = HtmlUnitUtil.fillOutForm(page, criteria);
    	Assert.assertNotNull(page);
    	// clock in
    	page = HtmlUnitUtil.clickButton(page, "clockAction");
    	// clock out 
    	page = HtmlUnitUtil.clickButton(page, "clockAction");
    	HtmlUnitUtil.createTempFile(page);
    	
    	DateTimeZone dateTimeZone = DateTimeZone.forID(HrServiceLocator.getTimezoneService().getUserTimezone());
    	DateTime dateTime = new DateTime().withZone(dateTimeZone);
    	String timeZone = dateTime.toString("zzzz");
    	Assert.assertTrue("Time zone information is incorrect", page.asText().contains(timeZone));
	}

    private HtmlPage clockIn() throws Exception {

        // Clock in
        HtmlPage page = clockAction(TkConstants.CLOCK_IN);

        // Make sure clock in button is rendered
        HtmlUnitUtil.createTempFile(page);
        Assert.assertTrue("The clock out button should have displayed", page.asText().contains("Clock Out"));
        Assert.assertTrue("The clock out button should have displayed", page.asText().contains("Take Lunch"));

        return page;
    }

    private HtmlPage clockOut() throws Exception {
        DateTime dateTime = new DateTime();
        if (dateTime.getSecondOfMinute() >= 58
                || dateTime.getSecondOfMinute() == 0) {
            Thread.sleep(4000);
        }
        // Clock out
        HtmlPage page = clockAction(TkConstants.CLOCK_OUT);

        // Make sure clock in button is rendered
        HtmlUnitUtil.createTempFile(page);
        Assert.assertTrue("The clock out button should have displayed", page.asText().contains("Clock In"));

        return page;
    }

    /**
     * This method is used for clocking in and out.
     * For some reason, htmlunit couldn't click the clock action button correctly.
     * It's probably because we bind a onClick event to the button instead of submitting the form.
     *
     * @param clockAction
     * @return HtmlPage page
     */
    private HtmlPage clockAction(String clockAction) throws Exception {
        DateTime dateTime = new DateTime();
        if (dateTime.getSecondOfMinute() >= 58
                || dateTime.getSecondOfMinute() == 0) {
            Thread.sleep(4000);
        }
        String baseUrl = TkTestConstants.Urls.CLOCK_URL;
        String actionUrl = baseUrl + "?methodToCall=clockAction&selectedAssignment=30_30_30&currentClockAction=" + clockAction;
        HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), actionUrl);
        Assert.assertNotNull("The login page shouldn't be null", page);
        Thread.sleep(3000);
        return page;
    }


}
