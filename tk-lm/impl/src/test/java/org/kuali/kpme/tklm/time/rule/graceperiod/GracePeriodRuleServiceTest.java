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
package org.kuali.kpme.tklm.time.rule.graceperiod;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.TKLMUnitTestCase;
import org.kuali.kpme.tklm.time.rules.graceperiod.GracePeriodRule;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocator;

public class GracePeriodRuleServiceTest extends TKLMUnitTestCase{

	@Test
	public void testGracePeriodRuleFetch() throws Exception{
		GracePeriodRule gpr = new GracePeriodRule();
		gpr.setActive(true);
		gpr.setEffectiveLocalDate(LocalDate.now());
		gpr.setHourFactor(new BigDecimal(0.1));
		
		KRADServiceLocator.getBusinessObjectService().save(gpr);
		gpr = TkServiceLocator.getGracePeriodService().getGracePeriodRule(LocalDate.now());
		Assert.assertTrue("fetched one rule", gpr != null);

        //cleanup
        KRADServiceLocator.getBusinessObjectService().delete(gpr);
	}
	
	@Test
    @Ignore
	public void testGracePeriodFetchValidation() throws Exception{
		//TODO: Sai - confirm maintenance page renders
		//TODO: Sai - confirm if hour factor is less than or equal 0 and greater than 1 it throws 
		//appropriate error
		
	}
	
	@Test
	public void testGracePeriodRuleTest() throws Exception{
		GracePeriodRule gpr = new GracePeriodRule();
		gpr.setActive(true);
		gpr.setEffectiveLocalDate(LocalDate.now());
		gpr.setHourFactor(new BigDecimal(3));
		
		KRADServiceLocator.getBusinessObjectService().save(gpr);
		gpr = TkServiceLocator.getGracePeriodService().getGracePeriodRule(LocalDate.now());
		Assert.assertTrue("fetched one rule", gpr != null);

		DateTime beginDateTime = new DateTime(2012, 10, 16, 12, 3, 0, 0, TKUtils.getSystemDateTimeZone());
		DateTime derivedDateTime = TkServiceLocator.getGracePeriodService().processGracePeriodRule(beginDateTime, LocalDate.now());

		Assert.assertTrue("rounded to 1:03", derivedDateTime.getMinuteOfHour() == 3);
		
		beginDateTime = new DateTime(2012, 10, 16, 12, 56, 0, 0, TKUtils.getSystemDateTimeZone());
		derivedDateTime = TkServiceLocator.getGracePeriodService().processGracePeriodRule(beginDateTime, LocalDate.now());

		Assert.assertTrue("rounded to 1:56", derivedDateTime.getMinuteOfHour() == 57);

        //cleanup
        KRADServiceLocator.getBusinessObjectService().delete(gpr);
	}
}
