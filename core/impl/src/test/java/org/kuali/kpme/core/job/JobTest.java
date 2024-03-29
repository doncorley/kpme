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
package org.kuali.kpme.core.job;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.kpme.core.CoreUnitTestCase;
import org.kuali.kpme.core.calendar.Calendar;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;
import org.kuali.kpme.core.paytype.PayType;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * This class needs refactored - the name job test implies that it should unit test on the Job object, especially considering it's package location.
 *
 *
 */
public class JobTest extends CoreUnitTestCase {

	private static final String CALENDAR_GROUP = "BWN-CAL";
	private static Long jobId = 23L;//id entered in the bootstrap SQL
	private static Long jobNumber = 5L;//number entered in the bootstrap SQL
	public static final String TEST_USER = "admin";


	@Test
	public void testInsertPayCalendar() throws Exception {
		Calendar payCalendar = new Calendar();
		payCalendar.setHrCalendarId("1001");
		payCalendar.setCalendarName(CALENDAR_GROUP);

		payCalendar.setFlsaBeginDay("Sun");
		payCalendar.setFlsaBeginTime(Time.valueOf("0:00:00"));
        payCalendar.setCalendarDescriptions("Test Description");

		KRADServiceLocator.getBusinessObjectService().save(payCalendar);
		Assert.assertTrue(HrServiceLocator.getCalendarService().getCalendar(payCalendar.getHrCalendarId()) != null);

	}

	@Test
	public void testInsertPayCalendarDates() throws Exception {
		CalendarEntry payCalendarDates = new CalendarEntry();
		payCalendarDates.setHrCalendarEntryId("1001");
		payCalendarDates.setHrCalendarId("1001");

		DateTime beginPeriodDateTime = new DateTime(2010, 7, 1, 0, 0, 0);
		DateTime endPeriodDateTime = new DateTime(2010, 7, 15, 0, 0, 0);

		payCalendarDates.setBeginPeriodFullDateTime(beginPeriodDateTime);
		payCalendarDates.setEndPeriodFullDateTime(endPeriodDateTime);
		payCalendarDates.setCalendarName(CALENDAR_GROUP);

		KRADServiceLocator.getBusinessObjectService().save(payCalendarDates);
		Assert.assertTrue(HrServiceLocator.getCalendarEntryService().getCalendarEntry(payCalendarDates.getHrCalendarEntryId()) != null);

	}

	@Test
	public void testInsertPayType() throws Exception {
		PayType payType = new PayType();
		payType.setHrPayTypeId("1001");
		payType.setPayType("BW");
		payType.setRegEarnCode("RGN");
		payType.setEffectiveLocalDate(LocalDate.now());
		payType.setTimestamp(new Timestamp(System.currentTimeMillis()));
		// KPME-2252
		payType.setLocation("*");
		payType.setInstitution("*");
		payType.setFlsaStatus("NE");
		payType.setPayFrequency("M");

		payType = (PayType) KRADServiceLocator.getBusinessObjectService().save(payType);
		Assert.assertTrue(HrServiceLocator.getPayTypeService().getPayType(payType.getPayType(), payType.getEffectiveLocalDate()) != null);
		KRADServiceLocator.getBusinessObjectService().delete(payType);
	}

	@Ignore
	@Test
	public void testGetJobs() {
		/**
		 * This test is conducted in JobServiceImplTest.java
		 */
		DateTime payPeriodEndDate = new DateTime(2010,7,30,1,0,0,0, TKUtils.getSystemDateTimeZone());
		List<Job> jobs = HrServiceLocator.getJobService().getJobs(TEST_USER, payPeriodEndDate.toLocalDate());
		Assert.assertNotNull("Jobs was null", jobs);
		Assert.assertEquals("Incorrect number of jobs", 2, jobs.size());
	}


	@Test
	public void testSaveAndFetchObject() throws Exception{
		//Confirming the save and fetch
		//save an object and confirm that it can be fetched
	}
}

