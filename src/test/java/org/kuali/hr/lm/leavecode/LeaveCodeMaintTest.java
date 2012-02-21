package org.kuali.hr.lm.leavecode;

import org.junit.Test;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestConstants;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class LeaveCodeMaintTest extends TkTestCase{
	
	@Test
	public void testLookupPage() throws Exception {	 
		HtmlPage lcLookup = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.LEAVE_CODE_MAINT_URL);
		lcLookup = HtmlUnitUtil.clickInputContainingText(lcLookup, "search");
		assertTrue("Page contains test Leave Code", lcLookup.asText().contains("testLC"));
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(lcLookup, "edit");
		assertTrue("Maintenance Page contains test Leave Code",maintPage.asText().contains("testLC"));	 
	}
	
	@Test
	public void testFutureEffectiveDate() throws Exception {
		this.futureEffectiveDateValidation(TkTestConstants.Urls.LEAVE_CODE_MAINT_NEW_URL);
	}

	@Test
	public void testAddNew() throws Exception {
	  	String baseUrl = TkTestConstants.Urls.LEAVE_CODE_MAINT_NEW_URL;
	  	HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(baseUrl);
	  	assertNotNull(page);
	 
	  	HtmlForm form = page.getFormByName("KualiForm");
	  	assertNotNull("Search form was missing from page.", form);
	  	assertTrue("page text contains:\n" + "Leave Code Maintenance", page.asText().contains("Leave Code Maintenance"));
	  	
	  	setFieldValue(page, "document.documentHeader.documentDescription", "Leave Code - test");
	    setFieldValue(page, "document.newMaintainableObject.defaultAmountofTime", "25"); // a wrong default amount of time
	    setFieldValue(page, "document.newMaintainableObject.effectiveDate", "02/21/2012"); // jira1360
	    setFieldValue(page, "document.newMaintainableObject.accrualCategory", "AC1"); // jira1360
	    
	    HtmlElement element = page.getElementByName("methodToCall.route");
	  	page = element.click();
	  	HtmlUnitUtil.createTempFile(page);
	  	assertTrue("page text contains:\n" + "should be between 0 and 24", page.asText().contains("should be between 0 and 24"));
	  	assertTrue("page text contains:\n" + "IU-SM", page.asText().contains("IU-SM"));
	}
}