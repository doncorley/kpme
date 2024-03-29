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
package org.kuali.kpme.tklm.leave.transfer.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.LocalDate;
import org.kuali.kpme.tklm.leave.transfer.BalanceTransfer;
import org.kuali.rice.kew.api.exception.WorkflowException;

public interface BalanceTransferService {

	//Data access
	public List<BalanceTransfer> getAllBalanceTransfersForPrincipalId(String principalId);
	public List<BalanceTransfer> getAllBalanceTransferForPrincipalIdAsOfDate(String principalId, LocalDate effectiveDate);
	public List<BalanceTransfer> getAllBalanceTransferByEffectiveDate(LocalDate effectiveDate);
	public void saveOrUpdate(BalanceTransfer balanceTransfer);
	
	//@Cacheable(value= LeaveDonation.CACHE_NAME, key="'balanceTransferId=' + #p0")
	public BalanceTransfer getBalanceTransferById(String balanceTransferId);
	
	//Use-Case specific service providers. Could be combined if max carry over applies to all use cases.
	/**
	 * A service that instantiates and returns BalanceTransfer objects that follow the given accrual category rule.
	 * 
	 * @param principalId	The principal this transfer pertains to.
	 * @param accrualCategoryRule	The accrual category rule that contains the max balance information.
	 * @param accruedBalance	Holds balance information needed for transfer.
	 * @param effectiveDate 
	 * @return A BalanceTransfer object conforming to @param accrualCategoryRule, if one exists. Null otherwise.
	 * 
	 * The transfer amount will be the minimum of:
	 *  
	 *  	1.) the accrual category rule's maximum transfer amount, adjusted for the employees FTE.
	 *  	2.) the number of time units exceeding the maximum balance 
	 *
	 */
	public BalanceTransfer initializeTransfer(String principalId, String accrualCategoryRule, BigDecimal accruedBalance, LocalDate effectiveDate);

	/**
	 * Consumes a BalanceTransfer object, creating up to three leave blocks.
	 * @param balanceTransfer The BalanceTransfer object to use for transfer.
	 * @return The same BalanceTransfer object, but with associated leave block ids.
	 */
	public BalanceTransfer transfer(BalanceTransfer balanceTransfer);
	
	/**
	 * Helper Services
	 */
	
	/**
	 * Determines which accrual categories within the given leave calendar document, are TRANSFERABLE for the given action frequency.
	 * Includes accrual categories for which ACTION_AT_MAX_BALANCE = LOSE.
	 * 
	 * @param document The LeaveCalendarDocument to use in gathering transfer eligible accrual categories.
	 * @param actionFrequency One of HrConstants.MAX_BAL_ACTION_FREQ
	 * @return A List of accrualCategoryRuleId's in {@param document}'s leave summary with MAX_BAL_ACTION_FREQUENCY = {@param actionFrequency} 
	 * @throws Exception
	 */
/*	public Map<String,ArrayList<String>> getEligibleTransfers(CalendarEntry calendarEntry, String principalId) throws Exception;
*/	
	public void submitToWorkflow(BalanceTransfer balanceTransfer) throws WorkflowException;
	
	/**
	 * transfer system scheduled time off
	 * @param balanceTransfer
	 * @return
	 */
	public BalanceTransfer transferSsto(BalanceTransfer balanceTransfer);
	
	public List<BalanceTransfer> getBalanceTransfers(String viewPrincipal, LocalDate beginPeriodDate, LocalDate endPeriodDate);


    public List<BalanceTransfer> getBalanceTransfers(String principalId, String fromAccrualCategory, String transferAmount, String toAccrualCategory, String amountTransferred, String forfeitedAmount, LocalDate fromEffdt, LocalDate toEffdt);
}
