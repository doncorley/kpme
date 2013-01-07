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
package org.kuali.hr.lm.accrual.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.lm.accrual.AccrualCategory;
import org.kuali.hr.time.util.TKUtils;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class AccrualCategoryDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements AccrualCategoryDao {
    
    @Override
    public AccrualCategory getAccrualCategory(String accrualCategory, Date asOfDate) {
    	AccrualCategory accrlCategory = null;
		Criteria root = new Criteria();
        Criteria effdt = new Criteria();
        Criteria timestamp = new Criteria();
        
		root.addEqualTo("accrualCategory", accrualCategory);
		root.addLessOrEqualThan("effectiveDate", asOfDate);
        effdt.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
        effdt.addLessOrEqualThan("effectiveDate", asOfDate);
        ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, effdt);
        effdtSubQuery.setAttributes(new String[]{"max(effectiveDate)"});

        timestamp.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
        timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
        ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, timestamp);
        timestampSubQuery.setAttributes(new String[]{"max(timestamp)"});
        
		root.addEqualTo("active",true);
        root.addEqualTo("effectiveDate", effdtSubQuery);
        root.addEqualTo("timestamp", timestampSubQuery);
		
		Query query = QueryFactory.newQuery(AccrualCategory.class, root);
		Object obj = this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		if (obj != null) {
			accrlCategory = (AccrualCategory) obj;
		}

		return accrlCategory;
    }
    
    @Override
    public void saveOrUpdate(AccrualCategory accrualCategory) {
    	this.getPersistenceBrokerTemplate().store(accrualCategory);
    }

	@Override
	public AccrualCategory getAccrualCategory(String lmAccrualCategoryId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("lmAccrualCategoryId", lmAccrualCategoryId);
		
		Query query = QueryFactory.newQuery(AccrualCategory.class, crit);
		return (AccrualCategory)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		
	}

	@Override
	// KPME-1011
    public List<AccrualCategory> getActiveAccrualCategories(Date asOfDate) {
		List<AccrualCategory> accrualCategories = new ArrayList<AccrualCategory>();
		Criteria root = new Criteria();
		Criteria timestampSubCrit = new Criteria();
		
		timestampSubCrit.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, timestampSubCrit);
		timestampSubQuery.setAttributes(new String[]{ "max(timestamp)" });
		
		root.addLessOrEqualThan("effectiveDate", asOfDate);
		root.addEqualTo("timestamp", timestampSubQuery);
		root.addEqualTo("active",true);
		
		Query query = QueryFactory.newQuery(AccrualCategory.class, root);
		
		Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		
		if (c != null) {
			accrualCategories.addAll(c);
		}

		return accrualCategories;
    }

	@Override
    @SuppressWarnings("unchecked")
	public List<AccrualCategory> getAccrualCategories(String accrualCategory, String descr, String leavePlan, String accrualEarnInterval, 
													  String unitOfTime, String minPercentWorked, Date fromEffdt, Date toEffdt, String active, 
													  String showHistory) {
        
        List<AccrualCategory> results = new ArrayList<AccrualCategory>();
    	
    	Criteria root = new Criteria();

        if (StringUtils.isNotBlank(accrualCategory)) {
            root.addLike("accrualCategory", accrualCategory);
        }
        
        if (StringUtils.isNotBlank(descr)) {
            root.addLike("descr", descr);
        }
        
        if (StringUtils.isNotBlank(leavePlan)) {
        	root.addLike("leavePlan", leavePlan);
        }
        
        if (StringUtils.isNotBlank(accrualEarnInterval)) {
        	root.addLike("accrualEarnInterval", accrualEarnInterval);
        }
        
        if (StringUtils.isNotBlank(unitOfTime)) {
        	root.addLike("unitOfTime", unitOfTime);
        }
        
        if (StringUtils.isNotBlank(minPercentWorked)) {
        	root.addLike("minPercentWorked", minPercentWorked);
        }
        
        Criteria effectiveDateFilter = new Criteria();
        if (fromEffdt != null) {
            effectiveDateFilter.addGreaterOrEqualThan("effectiveDate", fromEffdt);
        }
        if (toEffdt != null) {
            effectiveDateFilter.addLessOrEqualThan("effectiveDate", toEffdt);
        }
        if (fromEffdt == null && toEffdt == null) {
            effectiveDateFilter.addLessOrEqualThan("effectiveDate", TKUtils.getCurrentDate());
        }
        root.addAndCriteria(effectiveDateFilter);
        
        if (StringUtils.isNotBlank(active)) {
        	Criteria activeFilter = new Criteria();
            if (StringUtils.equals(active, "Y")) {
                activeFilter.addEqualTo("active", true);
            } else if (StringUtils.equals(active, "N")) {
                activeFilter.addEqualTo("active", false);
            }
            root.addAndCriteria(activeFilter);
        }

        if (StringUtils.equals(showHistory, "N")) {
            Criteria effdt = new Criteria();
            effdt.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
            effdt.addAndCriteria(effectiveDateFilter);
            ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, effdt);
            effdtSubQuery.setAttributes(new String[]{"max(effectiveDate)"});
            root.addEqualTo("effectiveDate", effdtSubQuery);
            
            Criteria timestamp = new Criteria();
            timestamp.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
            timestamp.addAndCriteria(effectiveDateFilter);
            ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, timestamp);
            timestampSubQuery.setAttributes(new String[]{"max(timestamp)"});
            root.addEqualTo("timestamp", timestampSubQuery);
        }

        Query query = QueryFactory.newQuery(AccrualCategory.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));

        return results;
    }
    
	public List<AccrualCategory> getActiveAccrualCategories(String leavePlan, Date asOfDate){
		List<AccrualCategory> accrualCategories = new ArrayList<AccrualCategory>();
		
		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();

		effdt.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("accrualCategory", Criteria.PARENT_QUERY_PREFIX + "accrualCategory");
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(AccrualCategory.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });

		root.addEqualTo("leavePlan", leavePlan);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
		
		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);
		
		
		Query query = QueryFactory.newQuery(AccrualCategory.class, root);
		Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);

		if (c != null) {
			accrualCategories.addAll(c);
		}
		return accrualCategories;
	}
    
    @Override
	public List<AccrualCategory> getActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate){
		Criteria root = new Criteria();
		root.addEqualTo("leavePlan", leavePlan);
		root.addLessOrEqualThan("effectiveDate", asOfDate);
		root.addNotEqualTo("accrualEarnInterval", "N");
		root.addEqualTo("active", true);
		
		Query query = QueryFactory.newQuery(AccrualCategory.class, root);
		Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		List<AccrualCategory> accrualCategories = new ArrayList<AccrualCategory>();

		if (c != null) {
			accrualCategories.addAll(c);
		}
		return accrualCategories;
	}
	
	@Override
	public List <AccrualCategory> getInActiveLeaveAccrualCategoriesForLeavePlan(String leavePlan, Date asOfDate) {
		Criteria root = new Criteria();
		root.addEqualTo("leavePlan", leavePlan);
		root.addNotEqualTo("accrualEarnInterval", "N");
		root.addLessOrEqualThan("effectiveDate", asOfDate);
		root.addEqualTo("active", false);
		Query query = QueryFactory.newQuery(AccrualCategory.class, root);
		Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		List<AccrualCategory> accrualCategories = new ArrayList<AccrualCategory>();

		if (c != null) {
			accrualCategories.addAll(c);
		}
		return accrualCategories;
	}
}
