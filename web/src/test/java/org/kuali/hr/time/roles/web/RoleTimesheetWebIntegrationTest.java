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
package org.kuali.hr.time.roles.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.time.util.TimeDetailTestUtils;
import org.kuali.hr.time.workflow.TimesheetWebTestBase;
import org.kuali.hr.util.HtmlUnitUtil;
import org.kuali.kpme.core.assignment.Assignment;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.earncode.EarnCode;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.time.detail.web.TimeDetailActionFormBase;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.kpme.tklm.time.timesheet.TimesheetDocument;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * See: https://wiki.kuali.org/display/KPME/Role+Security+Grid
 */
public class RoleTimesheetWebIntegrationTest extends TimesheetWebTestBase {

    private static final Logger LOG = Logger.getLogger(RoleTimesheetWebIntegrationTest.class);

    // Non Time Entry users (for this test) who have some access to 'fred's'
    // Time Sheet.
    private List<String> VALID_NON_ENTRY_USERS = new ArrayList<String>() {{
        /*add("testuser6");*/ add("frank"); add("fran"); add("edna"); }};

    // Users with incorrect Department or Work Area for Time Sheet privilege.
    private List<String> INVALID_NON_ENTRY_USERS = new ArrayList<String>(){{
        add("testuser1"); add("testuser2"); add("testuser3"); add("testuser4"); }};

    private TimesheetDocument fredsDocument = null;
    DateTime asOfDate = new DateTime(2011, 3, 1, 12, 0, 0, 0, TKUtils.getSystemDateTimeZone());

    @Override
    /**
     * This code is called before each test below - allows us to control how far
     * into the routing process we are for any given level of testing.
     */
    public void setUp() throws Exception {
        super.setUp();

        String userId = "fred";
        DateTimeZone dateTimeZone = DateTimeZone.forID(HrServiceLocator.getTimezoneService().getUserTimezone(userId));
        
        CalendarEntry pcd = HrServiceLocator.getCalendarEntryService().getCurrentCalendarDates(userId, asOfDate);
        Assert.assertNotNull("No PayCalendarDates", pcd);
        fredsDocument = TkServiceLocator.getTimesheetService().openTimesheetDocument(userId, pcd);
        String tdocId = fredsDocument.getDocumentId();

        // Verify the non time entry logins.
        verifyLogins(fredsDocument);

        // Verify Fred, and Add Timeblocks
        HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), userId, tdocId, true);
        Assert.assertTrue("Calendar not loaded.", page.asText().contains("March 2011"));

        HtmlForm form = page.getFormByName("TimeDetailActionForm");
        Assert.assertNotNull(form);
        List<Assignment> assignments = HrServiceLocator.getAssignmentService().getAssignments(userId, JAN_AS_OF_DATE.toLocalDate());
        Assignment assignment = assignments.get(0);

        List<EarnCode> earnCodes = TkServiceLocator.getTimesheetService().getEarnCodesForTime(assignment, JAN_AS_OF_DATE.toLocalDate());
        EarnCode earnCode = earnCodes.get(0);
        Assert.assertEquals("There should be no existing time blocks.", 0, fredsDocument.getTimeBlocks().size());

        // 2. Set Timeblock Start and End time
        // 3/02/2011 - 8:00a to 6:00pm
        // OVT - 0 Hrs Expected
        final DateTime start = new DateTime(2011, 3, 2, 8, 0, 0, 0, dateTimeZone);
        final DateTime end = new DateTime(2011, 3, 2, 13, 0, 0, 0, dateTimeZone);
        TimeDetailActionFormBase tdaf = TimeDetailTestUtils.buildDetailActionForm(fredsDocument, assignment, earnCode, start, end, null, false, null, true);
        List<String> errors = TimeDetailTestUtils.setTimeBlockFormDetails(form, tdaf);
        Assert.assertEquals("There should be no errors in this time detail submission", 0, errors.size());
        page = TimeDetailTestUtils.submitTimeDetails(getWebClient(), getTimesheetDocumentUrl(tdocId), tdaf);
        Assert.assertNotNull(page);

        String dataText = page.getElementById("timeBlockString").getFirstChild().getNodeValue();
        JSONArray jsonData = (JSONArray) JSONValue.parse(dataText);
        final JSONObject jsonDataObject = (JSONObject) jsonData.get(0);
        Assert.assertTrue("TimeBlock Data Missing.", checkJSONValues(new JSONObject() {{ put("outer", jsonDataObject); }},
                new ArrayList<Map<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("earnCode", "RGN");
                        put("hours", "5.0");
                        put("amount", null);
                    }});
                }},
                new HashMap<String, Object>() {{
                    put("earnCode", "RGN");
                    start.toString();
                    put("startNoTz", start.toString("yyyy-MM-dd'T'HH:mm:ss"));
                    put("endNoTz", end.toString("yyyy-MM-dd'T'HH:mm:ss"));
                    put("title", "SDR1 Work Area");
                    put("assignment", "30_30_30");
                }}
        ));

        // Set freds timesheet to have updated info.
        fredsDocument = TkServiceLocator.getTimesheetService().openTimesheetDocument(userId, pcd);
    }

    /*
     * Tests while Timesheet is in INITIATED state.
     */

    @Test
    public void testInitiatedTimesheetIsVisibleByAll() throws Exception {
        // test valid users
        for (String uid : VALID_NON_ENTRY_USERS) {
            LOG.info("Testing visibility for " + uid);
            HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), uid, fredsDocument.getDocumentId(), true);
            Assert.assertTrue("Calendar not loaded.", page.asText().contains("March 2011"));
        }
    }

    @Test
    public void testInitiatedTimesheetIsNotVisible() throws Exception {
        for (String uid : INVALID_NON_ENTRY_USERS) {
            LOG.info("Testing visibility for " + uid);
            HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), uid, fredsDocument.getDocumentId(), false);
            //HtmlUnitUtil.createTempFile(page, "badlogin");
            Assert.assertTrue("Should not have access", page.asText().contains("You are not authorized to access this portion of the application."));
        }
    }

    public void testInitiatedTimesheetEditable(String userId) throws Exception {
        // admin, add one timeblock
        String tdocId = fredsDocument.getDocumentId();
        HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), userId, tdocId, true);
        LOG.info(page.asText());
        //HtmlUnitUtil.createTempFile(page, "loggedin");
        Assert.assertTrue("Calendar not loaded.", page.asText().contains("March 2011"));

        HtmlForm form = page.getFormByName("TimeDetailActionForm");
        Assert.assertNotNull(form);
        List<Assignment> assignments = HrServiceLocator.getAssignmentService().getAssignments("fred", JAN_AS_OF_DATE.toLocalDate());
        Assignment assignment = assignments.get(0);

        List<EarnCode> earnCodes = TkServiceLocator.getTimesheetService().getEarnCodesForTime(assignment, JAN_AS_OF_DATE.toLocalDate());
        EarnCode earnCode = earnCodes.get(0);

        Assert.assertEquals("There should be one existing time block.", 1, fredsDocument.getTimeBlocks().size());

        DateTimeZone dateTimeZone = DateTimeZone.forID(HrServiceLocator.getTimezoneService().getUserTimezone(userId));
        final DateTime start = new DateTime(2011, 3, 4, 8, 0, 0, 0, dateTimeZone);
        final DateTime end = new DateTime(2011, 3, 4, 13, 0, 0, 0, dateTimeZone);
        TimeDetailActionFormBase tdaf = TimeDetailTestUtils.buildDetailActionForm(fredsDocument, assignment, earnCode, start, end, null, false, null, true);
        List<String> errors = TimeDetailTestUtils.setTimeBlockFormDetails(form, tdaf);
        Assert.assertEquals("There should be no errors in this time detail submission", 0, errors.size());
        page = TimeDetailTestUtils.submitTimeDetails(getWebClient(), getTimesheetDocumentUrl(tdocId), tdaf);
        Assert.assertNotNull(page);
        HtmlUnitUtil.createTempFile(page, "initiatetest");

        String dataText = page.getElementById("timeBlockString").getFirstChild().getNodeValue();
        JSONArray jsonData = (JSONArray) JSONValue.parse(dataText);
        final JSONObject jsonDataObject = (JSONObject) jsonData.get(1);
        Assert.assertTrue("TimeBlock Data Missing.", checkJSONValues(new JSONObject() {{ put("outer", jsonDataObject); }},
                new ArrayList<Map<String, Object>>() {{
                    add(new HashMap<String, Object>() {{
                        put("earnCode", "RGN");
                        put("hours", "5.0");
                        put("amount", null);
                    }});
                }},
                new HashMap<String, Object>() {{
                    put("earnCode", "RGN");
                    put("startNoTz", start.toString("yyyy-MM-dd'T'HH:mm:ss"));
                    put("endNoTz", end.toString("yyyy-MM-dd'T'HH:mm:ss"));
                    put("title", "SDR1 Work Area");
                    put("assignment", "30_30_30");
                }}
        ));
    }

    public void testInitiatedTimesheetNotEditable(String userId) throws Exception {
        // admin, add one timeblock
        String tdocId = fredsDocument.getDocumentId();
        HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), userId, tdocId, true);
        //HtmlUnitUtil.createTempFile(page, "loggedin");
        Assert.assertTrue("Calendar not loaded.", page.asText().contains("March 2011"));

        HtmlForm form = page.getFormByName("TimeDetailActionForm");
        Assert.assertNotNull(form);
        List<Assignment> assignments = HrServiceLocator.getAssignmentService().getAssignments("fred", JAN_AS_OF_DATE.toLocalDate());
        Assignment assignment = assignments.get(0);

        List<EarnCode> earnCodes = TkServiceLocator.getTimesheetService().getEarnCodesForTime(assignment, JAN_AS_OF_DATE.toLocalDate());
        EarnCode earnCode = earnCodes.get(0);

        Assert.assertEquals("There should be one existing time block.", 1, fredsDocument.getTimeBlocks().size());

        DateTimeZone dateTimeZone = DateTimeZone.forID(HrServiceLocator.getTimezoneService().getUserTimezone(userId));
        DateTime start = new DateTime(2011, 3, 4, 8, 0, 0, 0, dateTimeZone);
        DateTime end = new DateTime(2011, 3, 4, 13, 0, 0, 0, dateTimeZone);
        TimeDetailActionFormBase tdaf = TimeDetailTestUtils.buildDetailActionForm(fredsDocument, assignment, earnCode, start, end, null, false, null, true);
        List<String> errors = TimeDetailTestUtils.setTimeBlockFormDetails(form, tdaf);
        Assert.assertEquals("There should be no errors in this time detail submission", 0, errors.size());
        page = TimeDetailTestUtils.submitTimeDetails(getWebClient(), userId, getTimesheetDocumentUrl(tdocId), tdaf);
        Assert.assertNotNull(page);
        HtmlUnitUtil.createTempFile(page, "aftertdadd");
        Assert.assertTrue("Should not have access", page.asText().contains("You are not authorized to access this portion of the application."));
    }

    @Test
    public void testInitiatedTimesheetIsEditableByAdmin() throws Exception {
        testInitiatedTimesheetEditable("admin");
    }

    @Test
    public void testInitiatedTimesheetIsEditableByApprover() throws Exception {
        testInitiatedTimesheetEditable("fran");
    }

    @Test
    public void testInitiatedTimesheetIsEditableByReviewer() throws Exception {
        testInitiatedTimesheetEditable("frank");
    }

    @Test
    public void testInitiatedTimesheetIs_NOT_EditableByViewOnly() throws Exception {
        testInitiatedTimesheetNotEditable("edna");
    }

    @Test
    public void testInitiatedTimesheetIs_NOT_EditableByDeptAdmin() throws Exception {
        testInitiatedTimesheetNotEditable("testuser6");
    }


    @Test
    public void testInitiatedTimesheetSubmitUser() throws Exception {
        // User,
        // Check for submit button.
        // Click Button
    }

    @Test
    public void testInitiatedTimesheetSubmitAdmin() throws Exception {
        // Admin
        // Check for submit button.
        // Click Button
    }

    @Test
    public void testInitiatedTimesheetSubmitApprover() throws Exception {
        // Approver
        // Check for submit button.
        // Click Button
    }


    @Test
    public void testInitiatedTimesheetIs_NOT_SubmittableByUsers() throws Exception {
        // DeptAdmin, View Only, Reviewer
        // Check that submit button is not present.
    }

    /*
     * Test for ENROUTE state.
     */

    @Test
    public void testEnrouteTimesheetIsVisibleByAll() throws Exception {
        // test valid users
    }

    @Test
    public void testEnrouteTimesheetIsNotVisible() throws Exception {
        // make sure invalid users do not have access
    }

    @Test
    public void testEnrouteTimesheetIsEditableByAdmin() throws Exception {
        // admin, add one timeblock
    }

    @Test
    public void testEnrouteTimesheetIsEditableByApprover() throws Exception {
        // approver, add one timeblock
    }

    @Test
    public void testEnrouteTimesheetIsEditableByReviewer() throws Exception {
        // reviewer add one timeblock.
    }

    @Test
    public void testEnrouteTimesheetIs_NOT_EditableByViewOnly() throws Exception {
    }

    @Test
    public void testEnrouteTimesheetIs_NOT_EditableByDeptAdmin() throws Exception {
    }


    @Test
    public void testEnrouteTimesheet_NOT_Approvable() throws Exception {
        // User, Reviewer, View Only, Dept Admin
        // Check for approve button
    }

    @Test
    public void testEnrouteTimesheetApproveAdmin() throws Exception {
        // Admin
        // Check for approve button.
        // Click Button
    }

    @Test
    public void testEnrouteTimesheetApproveApprover() throws Exception {
        // Approver
        // Check for approve button.
        // Click Button
    }

    @Test
    public void testEnrouteTimesheetIs_NOT_SubmittableByUsers() throws Exception {
        // DeptAdmin, View Only, Reviewer
        // Check that submit button is not present.
    }


    /*
     * Final State
     */

    @Test
    public void testFinalTimesheetIsVisibleByAll() throws Exception {
        // test valid users
    }

    @Test
    public void testFinalTimesheetIsNotVisible() throws Exception {
        // make sure invalid users do not have access
    }

    @Test
    public void testFinalTimesheetIsNotEditable() throws Exception {
        // by everyone but admin
    }

    @Test
    public void testFinalTimesheetIsAdminEditable() throws Exception {
        // admin, add timeblock.
    }

    /**
     * Verifies that each admin/approver/reviewer login is active for this test.
     */
    private void verifyLogins(TimesheetDocument tdoc) throws Exception {
        for (String userId : VALID_NON_ENTRY_USERS) {
            String tdocId = tdoc.getDocumentId();
            HtmlPage page = loginAndGetTimeDetailsHtmlPage(getWebClient(), userId, tdocId, true);
            Assert.assertTrue("Calendar not loaded.", page.asText().contains("March 2011"));
        }
    }

}