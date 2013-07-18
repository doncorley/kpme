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
package org.kuali.kpme.core.taxrate.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.core.taxrate.TaxRate;
import org.springframework.cache.annotation.Cacheable;

public interface TaxRateService {
	
	/**
	 * Calculate the Income Tax this this employee.
	 * @param country - Two letter country code
	 * @param state - Two letter state/region code
	 * @param city - Four letter IATA city code
	 * @param maritalStatus - (S)ingle/(M)arried/(H)ead of Household/Married (F)iling separately
	 * @param date - Pay date
	 * @param days - Length of pay period in days
	 * @param amount - Taxable amount
	 * @return The calculated tax
	 */
	public BigDecimal getTax(String country, String state, String city, String maritalStatus, Date date, int days, BigDecimal amount);

	/**
     * Fetch department by id
     * @param hrDeptId
     * @return
     */
    @Cacheable(value=TaxRate.CACHE_NAME, key="'hrDeptId=' + #p0")
    TaxRate getTaxRate(String hrDeptId);
    
    List<TaxRate> getTaxRates(String userPrincipalId, String department, String location, String descr, String active, String showHistory);
    
    /**
	 * get count of department with given department
	 * @param department
	 * @return int
	 */
	int getTaxRateCount(String department);
	
	/**
	 * Get Department as of a particular date passed in
	 * @param department
	 * @param asOfDate
	 * @return
	 */
    @Cacheable(value=TaxRate.CACHE_NAME, key="'department=' + #p0 + '|' + 'asOfDate=' + #p1")
	TaxRate getTaxRate(String department, LocalDate asOfDate);

    /**
     * Fetches a list of Department objects as of the specified date all of which
     * belong to the indicated location.
     *
     * @param location The search criteria
     * @param asOfDate Effective date
     * @return A List<Department> object.
     */
    @Cacheable(value=TaxRate.CACHE_NAME, key="'chart=' + #p0 + '|' + 'asOfDate=' + #p1")
    List<TaxRate> getTaxRates(String location, LocalDate asOfDate);

}