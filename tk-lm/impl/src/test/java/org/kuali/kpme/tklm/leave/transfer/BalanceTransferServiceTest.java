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
package org.kuali.kpme.tklm.leave.transfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kuali.kpme.core.block.CalendarBlockContract;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.tklm.TKLMUnitTestCase;
import org.kuali.kpme.tklm.leave.block.LeaveBlock;
import org.kuali.kpme.tklm.leave.calendar.LeaveCalendarDocument;
import org.kuali.kpme.tklm.leave.service.LmServiceLocator;
import org.kuali.kpme.tklm.leave.summary.LeaveSummary;
import org.kuali.kpme.tklm.leave.summary.LeaveSummaryRow;
import org.kuali.rice.krad.util.ObjectUtils;

public class BalanceTransferServiceTest extends TKLMUnitTestCase {

	/**
	 * Leave Calendar Document Test data
	 */
	private final String USER_ID = "testUser1";
	
	private LeaveCalendarDocument janLCD;
	private CalendarEntry janEntry;
	private LeaveCalendarDocument decLCD;
	private CalendarEntry decEntry;
	
	private LocalDate janStart;
	private LocalDate decStart;
	
	private final String JAN_ID = "5001";
	private final String DEC_ID = "5000";
	
	/**
	 * Timesheet Document Test Data;
	 */

	/**
	 *  Common data
	 */
	
	private final String OD_XFER = "5000";
	private final String YE_XFER = "5001";
	private final String LA_XFER = "5002";
	private final String OD_XFER_MAC = "5003";
	private final String YE_XFER_MAC = "5004";
	private final String LA_XFER_MAC = "5005";
	private final String OD_LOSE = "5006";
	private final String YE_LOSE = "5007";
	private final String LA_LOSE = "5008";
	private final String OD_LOSE_MAC = "5009";
	private final String YE_LOSE_MAC = "5010";
	private final String LA_LOSE_MAC = "5011";
	private final String YE_XFER_EO = "5012";
	private final LocalDate LM_FROM = TKUtils.formatDateString("11/01/2012");
	private final LocalDate LM_TO = TKUtils.formatDateString("02/01/2013");

	@Before
	public void setUp() throws Exception {
		super.setUp();
		LmServiceLocator.getAccrualService().runAccrual(USER_ID,LM_FROM.toDateTimeAtStartOfDay(),LM_TO.toDateTimeAtStartOfDay(),true,USER_ID);
		janLCD = LmServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(JAN_ID);
		janEntry = janLCD.getCalendarEntry();
		janStart = janEntry.getBeginPeriodFullDateTime().toLocalDate();
		decLCD = LmServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(DEC_ID);
		decEntry = decLCD.getCalendarEntry();
		decStart = decEntry.getBeginPeriodFullDateTime().toLocalDate();
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	/*****************************
	 * Use-case specific testing *
	 ****************************/
	
	//
	// ACTION_AT_MAX_BALANCE = TRANSFER
	//
	
	@Test
	public void testInitializeTransferNullAccrualRule() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();

		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, null, BigDecimal.ZERO, effectiveDate);
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferNullLeaveSummary() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();

		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, null, LocalDate.now());
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferNullAccrualRuleNullLeaveSummary() {
		BalanceTransfer bt = new BalanceTransfer();
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, null, null, LocalDate.now());
		assertNull(bt);
	}
	
	@Test
	public void testInitializeTransferOnDemand() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnDemandWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEnd() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LmServiceLocator.getLeaveBlockService().deleteLeaveBlocksForDocumentId(DEC_ID);
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEndWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApprove() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApproveWithForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(7)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnDemandMaxCarryOver() throws Exception {
		//N/A - Max Carry Over on Year End transfers.
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER_MAC);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnYearEndMaxCarryOver() throws Exception {
		/**
		 * decEntry is not the last calendar entry in the leave plan. Want to check amounts for this action & action frequency
		 * without exceeding the transfer limit.
		 * 
		 * max transfer amount = 10
		 * leave balance = 16
		 * max balance = 15
		 * max carry over = 10
		 * 
		 * all excess should be transferrable. 1 unit of time for excess over max balance, 5 units of time for
		 * excess over max carry over.
		 * 
		 */
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_MAC);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(6)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(3)).longValue(), bt.getAmountTransferred().longValue());
	}
	
/*	@Test
	public void testInitializeTransferUnderMaxBalanceOnYearEndMaxCarryOver() throws Exception {
		//Create a leave block that will bring the available balance for january down to 14.
		//this balance would be under the max available balance (15), but over the max annual carry over amount.
		//i.o.w., this transfer would not due to max balance limit, but max annual carry over.
		//could also simply change the accrual amount.
		LeaveBlock usage = new LeaveBlock();
		usage.setAccrualCategory(YE_XFER_MAC);
		usage.setLeaveDate(DateUtils.addDays(janStart,5));
		usage.setLeaveAmount(new BigDecimal(-18));
		usage.setPrincipalId(USER_ID);
		usage.setAccrualGenerated(false);
		usage.setRequestStatus(HrConstants.REQUEST_STATUS.APPROVED);
		usage.setDocumentId(janLCD.getDocumentId());
		usage.setLmLeaveBlockId("99999");
		usage.setEarnCode("EC5");
		usage.setBlockId(0L);
		usage.setLeaveBlockType(LMConstants.LEAVE_BLOCK_TYPE.LEAVE_CALENDAR);
		List<LeaveBlock> leaveBlocks = new ArrayList<LeaveBlock>();
		leaveBlocks.add(usage);
		LmServiceLocator.getLeaveBlockService().saveLeaveBlocks(leaveBlocks);
		
		BalanceTransfer bt = new BalanceTransfer();
		janLCD = LmServiceLocator.getLeaveCalendarService().getLeaveCalendarDocument(JAN_ID);
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janLCD.getCalendarEntry());
		Date effectiveDate = DateUtils.addDays(janStart,3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(4)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(2)).longValue(), bt.getAmountTransferred().longValue());
	}*/
	
	@Test
	public void testInitializeTransferOnYearEndMaxCarryOverWithForfeiture() throws Exception {
		//max bal limit reached and max annual carry over triggererd.
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_MAC);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(10)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount", (new BigDecimal(12)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferOnLeaveApproveMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_XFER_MAC);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_XFER_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(1)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(0)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0.5)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	//
	// ACTION_AT_MAX_BALANCE = LOSE
	//
	@Test
	public void testInitializeLoseOnDemand() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_LOSE);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(1)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnYearEnd() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnLeaveApprove() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_LOSE);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_LOSE, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnDemandMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_LOSE_MAC);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnYearEndMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE_MAC);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(22)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeLoseOnLeaveApproveMaxCarryOver() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(LA_LOSE_MAC);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, LA_LOSE_MAC, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(0)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(17)).longValue(), bt.getForfeitedAmount().longValue());
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(0)).longValue(), bt.getAmountTransferred().longValue());
	}
	
	@Test
	public void testInitializeTransferWithOverrides() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER_EO);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER_EO, aRow.getAccruedBalance(), effectiveDate);
		assertEquals("transferOnDemand transfer amount", (new BigDecimal(7)).longValue(), bt.getTransferAmount().longValue());
		assertEquals("transferOnDemand forfeited amount",(new BigDecimal(20)).longValue(), bt.getForfeitedAmount().longValue());
		// max balance transfer conversion factor is undefined for YE_XFER_EO
		assertEquals("transferOnDemand amount transferred", (new BigDecimal(7)).longValue(), bt.getAmountTransferred().longValue());
	}
	/**
	 * End Use-case testing
	 */
	
	@Test
	public void testTransferNullBalanceTransfer() {
		BalanceTransfer balanceTransfer = null;
		try {
			balanceTransfer = LmServiceLocator.getBalanceTransferService().transfer(balanceTransfer);
		} catch (RuntimeException re) {
			assertTrue(re.getMessage().contains("did not supply a valid BalanceTransfer object"));
		}
	}
	
	@Test
	public void testTransferWithZeroTransferAmount() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		bt = LmServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		CalendarBlockContract accruedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		CalendarBlockContract debitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect", (new BigDecimal(-17)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("accrued leave block should not exist",ObjectUtils.isNull(accruedLeaveBlock));
		assertTrue("debited leave block should not exist",ObjectUtils.isNull(debitedLeaveBlock));
	}
	
	@Test
	public void testTransferWithNoAmountTransferred() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_LOSE);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_LOSE, aRow.getAccruedBalance(), effectiveDate);
		bt.setAmountTransferred(null);
		bt = LmServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		CalendarBlockContract accruedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		CalendarBlockContract debitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect",(new BigDecimal(-17)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("accrued leave block should not exist",ObjectUtils.isNull(accruedLeaveBlock));
		assertTrue("debited leave block should not exist",ObjectUtils.isNull(debitedLeaveBlock));	
	}
	
	@Test
	public void testTransferWithZeroForfeiture() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, decEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(OD_XFER);
		LocalDate effectiveDate = decStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, OD_XFER, aRow.getAccruedBalance(), effectiveDate);
		bt = LmServiceLocator.getBalanceTransferService().transfer(bt);
		CalendarBlockContract forfeitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("accrued leave block leave amount incorrect", (new BigDecimal(0.5)).longValue(), accruedLeaveBlock.getLeaveAmount().longValue());
		assertTrue("forfeited leave block should not exist",ObjectUtils.isNull(forfeitedLeaveBlock));
		assertEquals("transfered leave block leave amount incorrect", (new BigDecimal(-1)).longValue(), debitedLeaveBlock.getLeaveAmount().longValue());
	}
	
	@Test
	public void testTransferWithThreeLeaveBlocks() throws Exception {
		BalanceTransfer bt = new BalanceTransfer();
		LeaveSummary summary = LmServiceLocator.getLeaveSummaryService().getLeaveSummary(USER_ID, janEntry);
		LeaveSummaryRow aRow = summary.getLeaveSummaryRowForAccrualCategory(YE_XFER);
		LocalDate effectiveDate = janStart.plusDays(3);
		bt = LmServiceLocator.getBalanceTransferService().initializeTransfer(USER_ID, YE_XFER, aRow.getAccruedBalance(), effectiveDate);
		bt = LmServiceLocator.getBalanceTransferService().transfer(bt);
		LeaveBlock forfeitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getForfeitedLeaveBlockId());
		LeaveBlock accruedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getAccruedLeaveBlockId());
		LeaveBlock debitedLeaveBlock = LmServiceLocator.getLeaveBlockService().getLeaveBlock(bt.getDebitedLeaveBlockId());
		assertEquals("forfeited leave block leave amount incorrect", (new BigDecimal(-7)).longValue(), forfeitedLeaveBlock.getLeaveAmount().longValue());
		assertEquals((new BigDecimal(5)).longValue(), accruedLeaveBlock.getLeaveAmount().longValue());
		assertEquals((new BigDecimal(-10)).longValue(), debitedLeaveBlock.getLeaveAmount().longValue());
	}
	
	//TODO: write tests for adjusted max balance cases - i.e. FTE < 1, employee override's w/ type MAX_BALANCE

	@Test
	public void testSubmitToWorkflow() {
		assertNull(null);
	}

}
