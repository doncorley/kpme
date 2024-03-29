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
package org.kuali.hr.time.missedpunch;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.hr.KPMEWebTestCase;
import org.kuali.hr.util.HtmlUnitUtil;
import org.kuali.kpme.tklm.utils.TkTestConstants;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.google.common.collect.Lists;

public class MissedPunchDocumentTest extends KPMEWebTestCase {
    private static final Logger LOG = Logger.getLogger(MissedPunchDocumentTest.class);

	@Before
	public void setUp() throws Exception {
		super.setUp();	
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void updateWebClient() {
        WebClient webClient = getWebClient();
		webClient.setJavaScriptEnabled(true);
		webClient.setThrowExceptionOnScriptError(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(10000);
	}

	@Ignore
	@Test
	public void testMissedPunch() throws Exception {

		String baseUrl = TkTestConstants.Urls.CLOCK_URL;
		HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), baseUrl);
		Assert.assertNotNull(page);
		Assert.assertTrue("Clock Page contains Missed Punch Button", page.asText()
				.contains("Missed Punch"));
		updateWebClient();

		LOG.debug("Page is : " + page.asText());

		// getting Assignment options
		HtmlSelect assOption = (HtmlSelect) page
				.getElementByName("selectedAssignment");
		Iterable<DomElement> it = assOption.getChildElements();
		List<DomElement> assList = Lists.newLinkedList(it);
		
		LOG.debug("Second ele is : "+assList.get(2));

		// set Assignment value
		HtmlOption ho = null;
		if(assList.size() > 2) {
			ho = (HtmlOption) assList.get(2);
		} else {
			ho= (HtmlOption) assList.get(0);
		}
		LOG.debug("ho is : " + ho.asText());
		assOption.setSelectedAttribute(ho, true);

				
		// get tdocid 
		HtmlTableDataCell htmlTable = (HtmlTableDataCell) page.getByXPath(
				"//tbody//tr//td").get(9);
		LOG.debug("htmlTable.getTextContent();"
				+ htmlTable.getTextContent());

		String docId = htmlTable.getTextContent();
		
		// redirect to missed punch page
		HtmlPage mPunchPage = HtmlUnitUtil
				.gotoPageAndLogin(getWebClient(), getBaseURL()
						+ "/missedPunch?methodToCall=start&viewId=MissedPunch-SubmitView&missedPunch.timesheetDocumentId="
						+ docId);
		Assert.assertNotNull(mPunchPage);
		
		// clock action and assignment are drop down lists that are not readonly
		HtmlElement element = (HtmlElement)mPunchPage.getElementById("document.clockAction");
		Assert.assertNotNull(element);
		element = (HtmlElement)mPunchPage.getElementById("document.assignment");
		Assert.assertNotNull(element);

        LOG.debug("Page1 is : " + mPunchPage.asText());

		// set Invalid action time
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.documentHeader.documentDescription",
				"Missed Punch- test");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.clockAction", "CO");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionDate", "01/16/2012");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionTime", "5:5");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.assignment", "2_1234_2");

		HtmlElement elementSubmit = mPunchPage
				.getElementByName("methodToCall.route");
		mPunchPage = elementSubmit.click();

        LOG.debug("After first click >>>>> page is : "
				+ mPunchPage.asText());

		Assert.assertTrue("page text:\n" + mPunchPage.asText() + "\n does not contain:\n",
				mPunchPage.asText().contains("5:5 is not a valid time."));

		// set Future time 
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionDate", "01/20/2020");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionTime", "5:50 AM");

		elementSubmit = mPunchPage.getElementByName("methodToCall.route");
		mPunchPage = elementSubmit.click();

		Assert.assertTrue(
				"page text:\n" + mPunchPage.asText() + "\n does not contain:\n",
				mPunchPage.asText().contains(
						"Missed Punch Action Date cannot be a future date"));

        LOG.debug("After second click >>>>> page is : "
				+ mPunchPage.asText());

		// before last clock in time
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.clockAction", "CI");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionDate", "12/20/2009");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionTime", "5:50 AM");
//		HtmlUnitUtil.setFieldValue(mPunchPage, "document.assignment", "2_1234_2");

		elementSubmit = mPunchPage.getElementByName("methodToCall.route");

		mPunchPage = elementSubmit.click();
        LOG.debug("After third click >>>>> page is : "
				+ mPunchPage.asText());

		Assert.assertTrue(
				"page text:\n" + mPunchPage.asText() + "\n does not contain:\n",
				mPunchPage.asText()
						.contains(
								"Assignment is not effective as of date chosen."));

	
		// set proper value
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionDate", "01/19/2012");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.actionTime", "5:50 AM");
		HtmlUnitUtil.setFieldValue(mPunchPage, "document.clockAction", "CI");
//		HtmlUnitUtil.setFieldValue(mPunchPage, "document.assignment", "2_1234_2");

		HtmlElement elementSubmit1 = mPunchPage.getElementByName("methodToCall.route");
		mPunchPage = elementSubmit1.click();

        LOG.debug("After forth click >>>>> page is : "
				+ mPunchPage.asText());

		Assert.assertTrue("page text:\n" + mPunchPage.asText() + "\n does not contain:\n",
				mPunchPage.asText().contains("Document was successfully submitted."));
		
		// open another missed punch doc for clock out
		mPunchPage = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), getBaseURL()
				+ "/missedPunch.do?methodToCall=start&viewId=MissedPunch-SubmitView&missedPunch.timesheetDocumentId="
				+ docId);
		Assert.assertNotNull(mPunchPage);
		element = (HtmlElement)mPunchPage.getElementById("document.clockAction");
		Assert.assertNotNull(element);
		// element not found for assignment since it is a readonly field now
		element = (HtmlElement)mPunchPage.getElementById("document.assignment");
		Assert.assertNull(element);
		Assert.assertTrue("page text:does not contain: \n", 
				mPunchPage.asText().contains("Assignment: 	 work area description : $0.00 Rcd 2 TEST-DEPT description 2"));
		
	}
}
