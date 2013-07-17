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
package org.kuali.kpme.core.taxrate.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.taxrate.TaxRate;
import org.kuali.kpme.core.util.OjbSubQueryUtil;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import com.google.common.collect.ImmutableList;

public class TaxRateDaoOjbImpl extends PlatformAwareDaoBaseOjb implements TaxRateDao {
    
	@Override
	public void saveOrUpdate(TaxRate dept) {
		this.getPersistenceBrokerTemplate().store(dept);
	}

	@Override
	public TaxRate getTaxRate(String department, LocalDate asOfDate) {
		Criteria root = new Criteria();

		root.addEqualTo("country", department);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(TaxRate.class, asOfDate, TaxRate.EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(TaxRate.class, TaxRate.EQUAL_TO_FIELDS, false));

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		Query query = QueryFactory.newQuery(TaxRate.class, root);

		TaxRate d = (TaxRate)this.getPersistenceBrokerTemplate().getObjectByQuery(query);

		return d;
	}

    @Override
    public List<TaxRate> getTaxRates(String location, LocalDate asOfDate) {
		Criteria root = new Criteria();

		root.addEqualTo("state", location);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(TaxRate.class, asOfDate, TaxRate.EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(TaxRate.class, TaxRate.EQUAL_TO_FIELDS, false));

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);


		Query query = QueryFactory.newQuery(TaxRate.class, root);

        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		List<TaxRate> d = new ArrayList<TaxRate>(c.size());
        d.addAll(c);

		return d;
    }

	@Override
	@SuppressWarnings("unchecked")
    public List<TaxRate> getTaxRates(String dept, String location, String departmentDescr, String active, String showHistory) {
        List<TaxRate> results = new ArrayList<TaxRate>();

        Criteria root = new Criteria();

        if (StringUtils.isNotBlank(dept)) {
            root.addLike("country", dept);
        }

        if (StringUtils.isNotBlank(location)) {
            root.addLike("state", location);
        }

        if (StringUtils.isNotBlank(departmentDescr)) {
            root.addLike("city", departmentDescr);
        }

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
            root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQueryWithoutFilter(TaxRate.class, TaxRate.EQUAL_TO_FIELDS, false));
            root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(TaxRate.class, TaxRate.EQUAL_TO_FIELDS, false));
        }

        Query query = QueryFactory.newQuery(TaxRate.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));

        return results;
    }

	@Override
	public TaxRate getTaxRate(String hrDeptId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("hrDeptId", hrDeptId);
		
		Query query = QueryFactory.newQuery(TaxRate.class, crit);
		return (TaxRate)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	@Override
	public int getTaxRateCount(String department) {
		Criteria crit = new Criteria();
		crit.addEqualTo("country", department);
		Query query = QueryFactory.newQuery(TaxRate.class, crit);
		return this.getPersistenceBrokerTemplate().getCount(query);
	}
}
