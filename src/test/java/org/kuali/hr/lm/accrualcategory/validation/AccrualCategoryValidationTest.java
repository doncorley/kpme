package org.kuali.hr.lm.accrualcategory.validation;

import org.junit.Test;
import org.kuali.hr.time.test.HtmlUnitUtil;
import org.kuali.hr.time.test.TkTestCase;
import org.kuali.hr.time.test.TkTestConstants;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class AccrualCategoryValidationTest extends TkTestCase{
	private static final String ACCRUAL_CATEGORY = "testAC";
	
	@Test
	public void testValidateStartEndUnits() throws Exception {
		HtmlPage accrualCategoryLookup = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.ACCRUAL_CATEGORY_MAINT_URL);
		accrualCategoryLookup = HtmlUnitUtil.clickInputContainingText(accrualCategoryLookup, "search");
		assertTrue("Page contains test AccrualCategory", accrualCategoryLookup.asText().contains(ACCRUAL_CATEGORY));
		HtmlUnitUtil.createTempFile(accrualCategoryLookup);
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(accrualCategoryLookup, "edit", "lmAccrualCategoryId=3000");
		assertTrue("Maintenance Page contains test AccrualCategory",maintPage.asText().contains(ACCRUAL_CATEGORY));
	}
	
	@Test
	public void testMinWorkPercentageField() throws Exception {
		HtmlPage accrualCategoryLookup = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.ACCRUAL_CATEGORY_MAINT_URL);
		accrualCategoryLookup = HtmlUnitUtil.clickInputContainingText(accrualCategoryLookup, "search");
		assertTrue("Page contains test AccrualCategory", accrualCategoryLookup.asText().contains(ACCRUAL_CATEGORY));
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(accrualCategoryLookup, "edit", "lmAccrualCategoryId=3000");
		HtmlUnitUtil.createTempFile(maintPage);
		assertTrue("Maintenance Page contains test AccrualCategory",maintPage.asText().contains("Min Percent Worked to Earn Accrual"));
	}
	
	//@Test
	/*public void testValidateStartEndUnitsForErrorMessages() throws Exception {		
		HtmlPage accrualCategoryLookup = HtmlUnitUtil.gotoPageAndLogin(TkTestConstants.Urls.ACCRUAL_CATEGORY_MAINT_URL);
		accrualCategoryLookup = HtmlUnitUtil.clickInputContainingText(accrualCategoryLookup, "search");
		assertTrue("Page contains test Accrual Category", accrualCategoryLookup.asText().contains(ACCRUAL_CATEGORY));
		HtmlPage maintPage = HtmlUnitUtil.clickAnchorContainingText(accrualCategoryLookup, "edit", "lmAccrualCategoryId=3000");
		assertTrue("Maintenance Page contains test AccrualCategory",maintPage.asText().contains(ACCRUAL_CATEGORY));
		
		HtmlForm form = maintPage.getFormByName("KualiForm");
	  	assertNotNull("Search form was missing from page.", maintPage);
	  	
		HtmlInput inputForDescription = HtmlUnitUtil.getInputContainingText(maintPage, "* Document Description");
		inputForDescription.setValueAttribute("Test_KPME1252");

		setFieldValue(maintPage, "document.newMaintainableObject.effectiveDate", "04/01/2011");
		
        HtmlInput inputForStartUnit = HtmlUnitUtil.getInputContainingText(maintPage, "document.newMaintainableObject.add.accrualCategoryRules.start");
        inputForStartUnit.setValueAttribute("10");

        HtmlInput inputForEndUnit = HtmlUnitUtil.getInputContainingText(maintPage, "document.newMaintainableObject.add.accrualCategoryRules.end");
        inputForEndUnit.setValueAttribute("24");

        HtmlInput inputForAccrualRate = HtmlUnitUtil.getInputContainingText(maintPage, "document.newMaintainableObject.add.accrualCategoryRules.accrualRate");
        inputForAccrualRate.setValueAttribute("0.5");

        HtmlInput inputForMaxBalance = HtmlUnitUtil.getInputContainingText(maintPage, "document.newMaintainableObject.add.accrualCategoryRules.maxBalance");
        inputForMaxBalance.setValueAttribute("300");
        
        HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) HtmlUnitUtil.getInputContainingText(maintPage, "document.newMaintainableObject.add.accrualCategoryRules.active");
        checkbox.setChecked(true);
        
	  	HtmlElement element = maintPage.getElementByName("methodToCall.route");
	  	maintPage = element.click();
	  	HtmlUnitUtil.createTempFile(maintPage);
        HtmlPage resultantPageAfterEdit = HtmlUnitUtil.clickInputContainingText(maintPage, "submit");
        
        /*HtmlUnitUtil.createTempFile(resultantPageAfterEdit);
        */
		//assertTrue("Maintenance Page contains test startEndOverLapErrormessage", resultantPageAfterEdit.asText().contains("Start and End units should not have gaps or overlaps."));
	//}
}