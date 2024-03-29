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
package org.kuali.kpme.core.accrualcategory.web;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.accrualcategory.AccrualCategory;
import org.kuali.kpme.core.inquirable.KPMEInquirableImpl;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.krad.bo.BusinessObject;

public class AccrualCategoryInquirableImpl extends KPMEInquirableImpl {
	
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
		AccrualCategory ac = null;
		if(StringUtils.isNotBlank((String)fieldValues.get("lmAccrualCategoryId"))) {
			ac = HrServiceLocator.getAccrualCategoryService().getAccrualCategory((String)fieldValues.get("lmAccrualCategoryId"));
			
		} else if(StringUtils.isNotBlank((String)fieldValues.get("accrualCategory"))
					&& StringUtils.isNotBlank((String)fieldValues.get("effectiveDate"))) {
			String accrualCategory = (String)fieldValues.get("accrualCategory");
			LocalDate effectiveDate = TKUtils.formatDateString((String)fieldValues.get("effectiveDate"));
		    ac = HrServiceLocator.getAccrualCategoryService().getAccrualCategory((String)fieldValues.get("accrualCategory"), effectiveDate);
		} else {
			ac = (AccrualCategory) super.getBusinessObject(fieldValues);
		}

		return ac;
	}
}
