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
package org.kuali.kpme.core.taxrate;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.kpme.core.CoreUnitTestCase;
import org.kuali.kpme.core.service.HrServiceLocator;

public class TaxRateServiceImplTest extends CoreUnitTestCase {
	
	@Test
	public void testCheckTaxRates() throws Exception {
		
		Date date = new Date();
		BigDecimal amount = new BigDecimal(200.00);
		BigDecimal tax = HrServiceLocator.getTaxRateService().getTax("US", "CA", "", "S", date, 7, amount);
		Assert.assertEquals("Tax rate was not calculated correctly.", BigDecimal.ZERO, tax);
		
		// TODO Add more tests here.
	}

}