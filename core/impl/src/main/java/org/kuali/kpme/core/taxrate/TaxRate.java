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
package org.kuali.kpme.core.taxrate;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.kuali.kpme.core.bo.HrBusinessObject;
import org.kuali.kpme.core.kfs.coa.businessobject.Chart;
import org.kuali.kpme.core.kfs.coa.businessobject.Organization;
import org.kuali.kpme.core.location.Location;
import org.kuali.kpme.core.role.taxrate.TaxRatePrincipalRoleMemberBo;
import org.kuali.kpme.core.util.HrConstants;

import com.google.common.collect.ImmutableList;

public class TaxRate extends HrBusinessObject {

	private static final long serialVersionUID = 5476378484272246487L;

	public static final String CACHE_NAME = HrConstants.CacheNamespace.NAMESPACE_PREFIX + "TaxRate";
	//KPME-2273/1965 Primary Business Keys List.		
	public static final ImmutableList<String> EQUAL_TO_FIELDS = new ImmutableList.Builder<String>()
            .add("country")
            .add("state")
            .build();

    private String hrTaxRateId;
    private String country;
    private String city;
    private String state;
    private String chart;
    private String org;
    private String history;
    private boolean payrollApproval;	

    private Location stateObj;
    private Chart chartObj;
    private Organization orgObj;
    
    @Transient
    private List<TaxRatePrincipalRoleMemberBo> roleMembers = new ArrayList<TaxRatePrincipalRoleMemberBo>();
    
    @Transient
    private List<TaxRatePrincipalRoleMemberBo> inactiveRoleMembers = new ArrayList<TaxRatePrincipalRoleMemberBo>();
    
	@Override
	public String getUniqueKey() {
		return getCountry() + "_" + getOrg() + "_" + getChart();
	}
    
	@Override
	public String getId() {
		return getHrTaxRateId();
	}

	@Override
	public void setId(String id) {
		setHrTaxRateId(id);
	}
	
	public String getHrTaxRateId() {
		return hrTaxRateId;
	}

	public void setHrTaxRateId(String hrTaxRateId) {
		this.hrTaxRateId = hrTaxRateId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
    
	public Location getStateObj() {
		return stateObj;
	}

	public void setStateObj(Location stateObj) {
		this.stateObj = stateObj;
	}

	public Chart getChartObj() {
		return chartObj;
	}

	public void setChartObj(Chart chartObj) {
		this.chartObj = chartObj;
	}

	public Organization getOrgObj() {
		return orgObj;
	}

	public void setOrgObj(Organization orgObj) {
		this.orgObj = orgObj;
	}

	public List<TaxRatePrincipalRoleMemberBo> getRoleMembers() {
		return roleMembers;
	}
	
	public void addRoleMember(TaxRatePrincipalRoleMemberBo roleMemberBo) {
		roleMembers.add(roleMemberBo);
	}
	
	public void removeRoleMember(TaxRatePrincipalRoleMemberBo roleMemberBo) {
		roleMembers.remove(roleMemberBo);
	}
	
	public void setRoleMembers(List<TaxRatePrincipalRoleMemberBo> roleMembers) {
		this.roleMembers = roleMembers;
	}

	public List<TaxRatePrincipalRoleMemberBo> getInactiveRoleMembers() {
		return inactiveRoleMembers;
	}
	
	public void addInactiveRoleMember(TaxRatePrincipalRoleMemberBo inactiveRoleMemberBo) {
		inactiveRoleMembers.add(inactiveRoleMemberBo);
	}
	
	public void removeInactiveRoleMember(TaxRatePrincipalRoleMemberBo inactiveRoleMemberBo) {
		inactiveRoleMembers.remove(inactiveRoleMemberBo);
	}

	public void setInactiveRoleMembers(List<TaxRatePrincipalRoleMemberBo> inactiveRoleMembers) {
		this.inactiveRoleMembers = inactiveRoleMembers;
	}
	
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

	public boolean isPayrollApproval() {
		return payrollApproval;
	}

	public void setPayrollApproval(boolean payrollApproval) {
		this.payrollApproval = payrollApproval;
	}
}