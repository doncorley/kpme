package org.kuali.kpme.pm.positionreportsubcat.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.bo.utils.OjbSubQueryUtil;
import org.kuali.kpme.core.util.HrConstants;
import org.kuali.kpme.pm.positionreportsubcat.PositionReportSubCategory;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class PositionReportSubCatDaoObjImpl extends PlatformAwareDaoBaseOjb  implements PositionReportSubCatDao {
	 public PositionReportSubCategory getPositionReportSubCatById(
			String pmPositionReportSubCatId) {
		Criteria crit = new Criteria();
        crit.addEqualTo("pmPositionReportSubCatId", pmPositionReportSubCatId);

        Query query = QueryFactory.newQuery(PositionReportSubCategory.class, crit);
        return (PositionReportSubCategory) this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	public List<PositionReportSubCategory> getPositionReportSubCat(String pstnRptSubCat, String institution, String campus, LocalDate asOfDate) {
		List<PositionReportSubCategory> prscList = new ArrayList<PositionReportSubCategory>();
		Criteria root = new Criteria();
		if(StringUtils.isNotEmpty(pstnRptSubCat) 
				&& !StringUtils.equals(pstnRptSubCat, HrConstants.WILDCARD_CHARACTER)) {
			root.addEqualTo("positionReportSubCat", pstnRptSubCat); 
		}
		if(StringUtils.isNotEmpty(institution) 
				&& !StringUtils.equals(institution, HrConstants.WILDCARD_CHARACTER)) {
			root.addEqualTo("institution", institution); 
		}
		if(StringUtils.isNotEmpty(campus) 
				&& !StringUtils.equals(campus, HrConstants.WILDCARD_CHARACTER)) {
			root.addEqualTo("campus", campus); 
		}
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(PositionReportSubCategory.class, asOfDate, PositionReportSubCategory.EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(PositionReportSubCategory.class, PositionReportSubCategory.EQUAL_TO_FIELDS, false));
        
        Criteria activeFilter = new Criteria();
        activeFilter.addEqualTo("active", true);
        root.addAndCriteria(activeFilter);
        Query query = QueryFactory.newQuery(PositionReportSubCategory.class, root);
        
        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		if(!c.isEmpty())
			prscList.addAll(c);
		
		return prscList;
	}

}
