package org.kuali.hr.pm.test;

import org.kuali.hr.time.test.HtmlUnitUtil;

public final class PmTestConstants {
	
	public static class Urls {
		public static String BASE_URL = HtmlUnitUtil.getBaseURL();
		
		public static final String POSITION_REPORT_TYPE_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.pm.positionreporttype.PositionReportType&methodToCall=start";
		
		public static final String POSITION_REPORT_GROUP_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.pm.positionreportgroup.PositionReportGroup&methodToCall=start";
		
		public static final String POSITION_REPORT_CAT_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.pm.positionreportcat.PositionReportCategory&methodToCall=start";
		
		public static final String POSITION_REPORT_SUB_CAT_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.pm.positionreportsubcat.PositionReportSubCategory&methodToCall=start";
		
		public static final String INSTITUTION_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.core.bo.institution.Institution&methodToCall=start";

		public static final String PAY_STEP_MAINT_NEW_URL = BASE_URL + "/kr/maintenance.do?businessObjectClassName=org.kuali.kpme.core.bo.paystep.PayStep&methodToCall=start";

		
	}

}
