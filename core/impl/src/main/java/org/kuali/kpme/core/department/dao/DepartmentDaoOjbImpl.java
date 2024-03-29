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
package org.kuali.kpme.core.department.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.department.Department;
import org.kuali.kpme.core.util.OjbSubQueryUtil;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import com.google.common.collect.ImmutableList;

public class DepartmentDaoOjbImpl extends PlatformAwareDaoBaseOjb implements DepartmentDao {
    
	@Override
	public void saveOrUpdate(Department dept) {
		this.getPersistenceBrokerTemplate().store(dept);
	}

	@Override
	public Department getDepartment(String department, LocalDate asOfDate) {
		Criteria root = new Criteria();

		root.addEqualTo("dept", department);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Department.class, asOfDate, Department.EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Department.class, Department.EQUAL_TO_FIELDS, false));

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);

		Query query = QueryFactory.newQuery(Department.class, root);

		Department d = (Department)this.getPersistenceBrokerTemplate().getObjectByQuery(query);

		return d;
	}

    @Override
    public List<Department> getDepartments(String location, LocalDate asOfDate) {
		Criteria root = new Criteria();

		root.addEqualTo("location", location);
        root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQuery(Department.class, asOfDate, Department.EQUAL_TO_FIELDS, false));
        root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Department.class, Department.EQUAL_TO_FIELDS, false));

		Criteria activeFilter = new Criteria(); // Inner Join For Activity
		activeFilter.addEqualTo("active", true);
		root.addAndCriteria(activeFilter);


		Query query = QueryFactory.newQuery(Department.class, root);

        Collection c = this.getPersistenceBrokerTemplate().getCollectionByQuery(query);
		List<Department> d = new ArrayList<Department>(c.size());
        d.addAll(c);

		return d;
    }

	@Override
	@SuppressWarnings("unchecked")
    public List<Department> getDepartments(String dept, String location, String departmentDescr, String active, String showHistory) {
        List<Department> results = new ArrayList<Department>();

        Criteria root = new Criteria();

        if (StringUtils.isNotBlank(dept)) {
            root.addLike("dept", dept);
        }

        if (StringUtils.isNotBlank(location)) {
            root.addLike("location", location);
        }

        if (StringUtils.isNotBlank(departmentDescr)) {
            root.addLike("description", departmentDescr);
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
            root.addEqualTo("effectiveDate", OjbSubQueryUtil.getEffectiveDateSubQueryWithoutFilter(Department.class, Department.EQUAL_TO_FIELDS, false));
            root.addEqualTo("timestamp", OjbSubQueryUtil.getTimestampSubQuery(Department.class, Department.EQUAL_TO_FIELDS, false));
        }

        Query query = QueryFactory.newQuery(Department.class, root);
        results.addAll(getPersistenceBrokerTemplate().getCollectionByQuery(query));

        return results;
    }

	@Override
	public Department getDepartment(String hrDeptId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("hrDeptId", hrDeptId);
		
		Query query = QueryFactory.newQuery(Department.class, crit);
		return (Department)this.getPersistenceBrokerTemplate().getObjectByQuery(query);
	}
	
	@Override
	public int getDepartmentCount(String department) {
		Criteria crit = new Criteria();
		crit.addEqualTo("dept", department);
		Query query = QueryFactory.newQuery(Department.class, crit);
		return this.getPersistenceBrokerTemplate().getCount(query);
	}
}
