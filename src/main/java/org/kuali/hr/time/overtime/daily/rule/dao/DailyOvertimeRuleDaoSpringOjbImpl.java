/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.overtime.daily.rule.dao;

import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.hr.time.overtime.daily.rule.DailyOvertimeRule;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

public class DailyOvertimeRuleDaoSpringOjbImpl extends PlatformAwareDaoBaseOjb implements DailyOvertimeRuleDao {

	@Override
	public DailyOvertimeRule findDailyOvertimeRule(String location, String paytype, String dept, Long workArea, Date asOfDate) {
		DailyOvertimeRule dailyOvertimeRule;

		Criteria root = new Criteria();
		Criteria effdt = new Criteria();
		Criteria timestamp = new Criteria();

		effdt.addEqualToField("location", Criteria.PARENT_QUERY_PREFIX + "location");
		effdt.addEqualToField("paytype", Criteria.PARENT_QUERY_PREFIX + "paytype");
		effdt.addEqualToField("dept", Criteria.PARENT_QUERY_PREFIX + "dept");
		effdt.addEqualToField("workArea", Criteria.PARENT_QUERY_PREFIX + "workArea");
		
		effdt.addLessOrEqualThan("effectiveDate", asOfDate);
		//effdt.addEqualTo("active", true);
		ReportQueryByCriteria effdtSubQuery = QueryFactory.newReportQuery(DailyOvertimeRule.class, effdt);
		effdtSubQuery.setAttributes(new String[] { "max(effdt)" });

		timestamp.addEqualToField("location", Criteria.PARENT_QUERY_PREFIX + "location");
		timestamp.addEqualToField("paytype", Criteria.PARENT_QUERY_PREFIX + "paytype");
		timestamp.addEqualToField("dept", Criteria.PARENT_QUERY_PREFIX + "dept");
		timestamp.addEqualToField("workArea", Criteria.PARENT_QUERY_PREFIX + "workArea");
		
		timestamp.addEqualToField("effectiveDate", Criteria.PARENT_QUERY_PREFIX + "effectiveDate");
		//timestamp.addEqualTo("active", true);
		ReportQueryByCriteria timestampSubQuery = QueryFactory.newReportQuery(DailyOvertimeRule.class, timestamp);
		timestampSubQuery.setAttributes(new String[] { "max(timestamp)" });

		root.addEqualTo("dept", dept);
		root.addEqualTo("workArea", workArea);
		root.addEqualTo("location", location);
		root.addEqualTo("paytype", paytype);
		root.addEqualTo("effectiveDate", effdtSubQuery);
		root.addEqualTo("timestamp", timestampSubQuery);
		//root.addEqualTo("active", true);

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		
		Query query = QueryFactory.newQuery(DailyOvertimeRule.class, root);
		dailyOvertimeRule = (DailyOvertimeRule)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
		
		return dailyOvertimeRule;
	}

	@Override
	public void saveOrUpdate(DailyOvertimeRule dailyOvertimeRule) {
		this.getPersistenceBrokerTemplate().store(dailyOvertimeRule);
	}

	@Override
	public void saveOrUpdate(List<DailyOvertimeRule> dailyOvertimeRules) {
		for (DailyOvertimeRule dor : dailyOvertimeRules) {
			saveOrUpdate(dor);
		}
	}

	@Override
	public DailyOvertimeRule getDailyOvertimeRule(String tkDailyOvertimeRuleId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("tkDailyOvertimeRuleId", tkDailyOvertimeRuleId);
		
		Query query = QueryFactory.newQuery(DailyOvertimeRule.class, crit);
		return (DailyOvertimeRule)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}

}
