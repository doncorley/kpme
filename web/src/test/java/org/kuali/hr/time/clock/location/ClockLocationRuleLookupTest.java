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
package org.kuali.hr.time.clock.location;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.KPMEWebTestCase;
import org.kuali.hr.util.HtmlUnitUtil;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ClockLocationRuleLookupTest extends KPMEWebTestCase {

    @Test
    public void testLookup() throws Exception{
    	String baseUrl = getBaseURL() + "/kr/lookup.do?__login_user=admin&methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.rules.clocklocation.ClockLocationRule&returnLocation=" + getBaseURL() + "/portal.do&hideReturnLink=true&docFormKey=88888888";
    	HtmlPage page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), baseUrl);
    	Assert.assertNotNull(page);
    	Assert.assertTrue("Could not find text 'Clock Location Rule Lookup' in page.", StringUtils.contains(page.asText(), "Clock Location Rule Lookup"));
    	HtmlForm form = page.getFormByName("KualiForm");
    	Assert.assertNotNull("Search form was missing from page.", form);
    	
    	HtmlInput  input  = HtmlUnitUtil.getInputContainingText(form, "methodToCall.search");
    	Assert.assertNotNull("Could not locate search submit button", input);
    	page = (HtmlPage) input.click();
    	Assert.assertNotNull("Page not returned from click.", page);
    	HtmlUnitUtil.createTempFile(page);
    	Assert.assertTrue("Expected one result.", StringUtils.contains(page.asText(), "One item retrieved"));
    	
    	page = HtmlUnitUtil.gotoPageAndLogin(getWebClient(), baseUrl);
    	form = page.getFormByName("KualiForm");
    	Assert.assertNotNull("Search form was missing from page.", form);
    	HtmlUnitUtil.createTempFile(page);
    	form.getInputByName("dept").setValueAttribute("20");
    	input  = HtmlUnitUtil.getInputContainingText(form, "methodToCall.search");
    	Assert.assertNotNull("Could not locate search submit button", input);
    	page = (HtmlPage) input.click();
    	Assert.assertNotNull("Page not returned from click.", page);
    	Assert.assertTrue("Expected zero results.", StringUtils.contains(page.asText(), "No values match this search."));
    }

}
