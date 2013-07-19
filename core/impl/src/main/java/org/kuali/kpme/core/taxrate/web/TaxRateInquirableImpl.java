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
package org.kuali.kpme.core.taxrate.web;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.taxrate.TaxRate;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;

public class TaxRateInquirableImpl extends KualiInquirableImpl {

    @Override
    @SuppressWarnings("rawtypes")
    public BusinessObject getBusinessObject(Map fieldValues) {
        TaxRate departmentObj = null;

        if (StringUtils.isNotBlank((String) fieldValues.get("hrDeptId"))) {
            departmentObj = HrServiceLocator.getTaxRateService().getTaxRate((String) fieldValues.get("hrDeptId"));
        } else if (fieldValues.containsKey("country") && fieldValues.containsKey("effectiveDate")) {
            String department = (String) fieldValues.get("country");
            LocalDate effectiveDate = TKUtils.formatDateString((String) fieldValues.get("effectiveDate"));
            departmentObj = HrServiceLocator.getTaxRateService().getTaxRate(department, effectiveDate);
        } else {
            departmentObj = (TaxRate) super.getBusinessObject(fieldValues);
        }

        return departmentObj;
    }
}