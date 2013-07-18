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
package org.kuali.kpme.core.taxrate.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.KPMENamespace;
import org.kuali.kpme.core.taxrate.TaxRate;
import org.kuali.kpme.core.taxrate.dao.TaxRateDao;
import org.kuali.kpme.core.permission.KPMEPermissionTemplate;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.role.KPMERoleMemberAttribute;
import org.kuali.kpme.core.role.taxrate.TaxRatePrincipalRoleMemberBo;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleMemberBo;

public class TaxRateServiceImpl implements TaxRateService {

	private TaxRateDao taxRateDao;
	
	public TaxRateDao getTaxRateDao() {
		return taxRateDao;
	}
	
	public void setTaxRateDao(TaxRateDao departmentDao) {
		this.taxRateDao = departmentDao;
	}
	
	@Override
	public TaxRate getTaxRate(String hrDeptId) {
		TaxRate departmentObj = taxRateDao.getTaxRate(hrDeptId);
		
		if (departmentObj != null) {
			populateTaxRateRoleMembers(departmentObj, departmentObj.getEffectiveLocalDate());
		}
		
		return departmentObj;
	}
	
	/**
	 * Calculate the Income Tax this this employee.
	 * @param country - Two letter country code
	 * @param state - Two letter state/region code
	 * @param city - Four letter IATA city code
	 * @param maritalStatus - (S)ingle/(M)arried/(H)ead of Household/Married (F)iling separately
	 * @param date - Pay date
	 * @param days - Length of pay period in days
	 * @param amount - Taxable amount
	 * @return The calculated tax
	 */
	public BigDecimal getTax(String country, String state, String city, String maritalStatus, Date date, int days, BigDecimal amount)
	{
		BigDecimal taxes = BigDecimal.ZERO;
		
		// TODO - Read though the TaxRate file and calculate the tax amount
		
		return taxes;
	}


	@Override
    public List<TaxRate> getTaxRates(String userPrincipalId, String department, String location, String descr, String active, String showHistory) {
    	List<TaxRate> results = new ArrayList<TaxRate>();
    	
    	List<TaxRate> departmentObjs = taxRateDao.getTaxRates(department, location, descr, active, showHistory);
        
    	for (TaxRate departmentObj : departmentObjs) {
        	Map<String, String> roleQualification = new HashMap<String, String>();
        	roleQualification.put(KimConstants.AttributeConstants.PRINCIPAL_ID, userPrincipalId);
        	roleQualification.put(KPMERoleMemberAttribute.TAX_RATE.getRoleMemberAttributeName(), departmentObj.getCountry());
        	roleQualification.put(KPMERoleMemberAttribute.LOCATION.getRoleMemberAttributeName(), departmentObj.getState());
        	
        	if (!KimApiServiceLocator.getPermissionService().isPermissionDefinedByTemplate(KPMENamespace.KPME_WKFLW.getNamespaceCode(),
    				KPMEPermissionTemplate.VIEW_KPME_RECORD.getPermissionTemplateName(), new HashMap<String, String>())
    		  || KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(userPrincipalId, KPMENamespace.KPME_WKFLW.getNamespaceCode(),
    				  KPMEPermissionTemplate.VIEW_KPME_RECORD.getPermissionTemplateName(), new HashMap<String, String>(), roleQualification)) {
        		results.add(departmentObj);
        	}
    	}
    	
        for (TaxRate result : results) {
        	populateTaxRateRoleMembers(result, result.getEffectiveLocalDate());
        }
        
        return results;
    }
    
	@Override
	public int getTaxRateCount(String department) {
		return taxRateDao.getTaxRateCount(department);
	}
	
    @Override
	public TaxRate getTaxRate(String department, LocalDate asOfDate) {
        TaxRate departmentObj = taxRateDao.getTaxRate(department, asOfDate);
        
        if (departmentObj != null) {
        	populateTaxRateRoleMembers(departmentObj, asOfDate);
        }

		return departmentObj;
	}

    @Override
    public List<TaxRate> getTaxRates(String location, LocalDate asOfDate) {
        List<TaxRate> departmentObjs = taxRateDao.getTaxRates(location, asOfDate);

        for (TaxRate departmentObj : departmentObjs) {
        	populateTaxRateRoleMembers(departmentObj, departmentObj.getEffectiveLocalDate());
        }

        return departmentObjs;
    }

    private void populateTaxRateRoleMembers(TaxRate department, LocalDate asOfDate) {
    	if (department != null && asOfDate != null 
    			&& CollectionUtils.isEmpty(department.getRoleMembers()) && CollectionUtils.isEmpty(department.getInactiveRoleMembers())) {
    		Set<RoleMember> roleMembers = new HashSet<RoleMember>();
    		
	    	roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInDepartment(KPMENamespace.KPME_TK.getNamespaceCode(), KPMERole.TIME_DEPARTMENT_VIEW_ONLY.getRoleName(), department.getCountry(), asOfDate.toDateTimeAtStartOfDay(), false));
	    	roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInDepartment(KPMENamespace.KPME_TK.getNamespaceCode(), KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName(), department.getCountry(), asOfDate.toDateTimeAtStartOfDay(), false));
	    	roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInDepartment(KPMENamespace.KPME_LM.getNamespaceCode(), KPMERole.LEAVE_DEPARTMENT_VIEW_ONLY.getRoleName(), department.getCountry(), asOfDate.toDateTimeAtStartOfDay(), false));
	    	roleMembers.addAll(HrServiceLocator.getKPMERoleService().getRoleMembersInDepartment(KPMENamespace.KPME_LM.getNamespaceCode(), KPMERole.LEAVE_DEPARTMENT_ADMINISTRATOR.getRoleName(), department.getCountry(), asOfDate.toDateTimeAtStartOfDay(), false));
	
	    	for (RoleMember roleMember : roleMembers) {
	    		RoleMemberBo roleMemberBo = RoleMemberBo.from(roleMember);
	    		
	    		if (roleMemberBo.isActive()) {
	    			department.addRoleMember(TaxRatePrincipalRoleMemberBo.from(roleMemberBo, roleMember.getAttributes()));
	    		} else {
	    			department.addInactiveRoleMember(TaxRatePrincipalRoleMemberBo.from(roleMemberBo, roleMember.getAttributes()));
	    		}
	    	}
    	}
    }

}