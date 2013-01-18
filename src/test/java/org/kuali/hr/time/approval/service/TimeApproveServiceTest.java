package org.kuali.hr.time.approval.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.hr.test.KPMETestCase;
import org.kuali.hr.time.service.base.TkServiceLocator;

public class TimeApproveServiceTest extends KPMETestCase {
	
	@Test
	public void testGetTimePrincipalIdsWithSearchCriteria() throws ParseException {
		List<String> workAreaList = new ArrayList<String>();
		String calendarGroup = "payCal";
		DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");
		java.sql.Date beginDate = new java.sql.Date(DATE_FORMAT.parse("03/01/2012").getTime());
		java.sql.Date endDate = new java.sql.Date(DATE_FORMAT.parse("03/30/2012").getTime());
		
		List<String> idList = TkServiceLocator.getTimeApproveService()
								.getTimePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 0 principal ids when searching with empty workarea list, not " + idList.size(), idList.isEmpty());
		
		workAreaList.add("1111");
		workAreaList.add("2222");
		idList = TkServiceLocator.getTimeApproveService()
					.getTimePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);	
		// there's an principal id '1033' in setup that does not have jobs with flsaStatus = NE, so it should not be in the search results
		Assert.assertTrue("There should be 2 principal ids when searching with both workareas, not " + idList.size(), idList.size() == 2);
		for(String anId : idList) {
			if(!(anId.equals("1011") || anId.equals("1022"))) {
				Assert.fail("PrincipalIds searched with both workareas should be either '1011' or '1022', not " + anId);
			}
		}
		
		workAreaList = new ArrayList<String>();
		workAreaList.add("1111");
		idList = TkServiceLocator.getTimeApproveService()
					.getTimePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);		
		Assert.assertTrue("There should be 1 principal ids for workArea '1111', not " + idList.size(), idList.size() == 1);
		Assert.assertTrue("Principal id for workArea '1111' should be principalA, not " + idList.get(0), idList.get(0).equals("1011"));
		
		workAreaList = new ArrayList<String>();
		workAreaList.add("2222");
		idList = TkServiceLocator.getTimeApproveService()
						.getTimePrincipalIdsWithSearchCriteria(workAreaList, calendarGroup, endDate, beginDate, endDate);	
		Assert.assertTrue("There should be 1 principal ids for workArea '2222', not " + idList.size(), idList.size() == 1);
		Assert.assertTrue("Principal id for workArea '2222' should be principalB, not " + idList.get(0), idList.get(0).equals("1022"));
	}

}
