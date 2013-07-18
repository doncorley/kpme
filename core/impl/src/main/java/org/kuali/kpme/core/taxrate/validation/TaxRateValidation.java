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
package org.kuali.kpme.core.taxrate.validation;

import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.kuali.kpme.core.role.KPMERole;
import org.kuali.kpme.core.role.taxrate.TaxRatePrincipalRoleMemberBo;
import org.kuali.kpme.core.service.HrServiceLocator;
import org.kuali.kpme.core.taxrate.TaxRate;
import org.kuali.kpme.core.util.TKUtils;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

@SuppressWarnings("deprecation")
public class TaxRateValidation extends MaintenanceDocumentRuleBase {

	@Override
	protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
		boolean valid = true;

		PersistableBusinessObject pbo = (PersistableBusinessObject) this.getNewBo();
		
		if (pbo instanceof TaxRate) {
			TaxRate department = (TaxRate) pbo;
			
			valid &= validateTaxRate(department);
			valid &= validateRolePresent(department.getRoleMembers(), department.getEffectiveLocalDate());
		}

		return valid;
	}
	
	protected boolean validateTaxRate(TaxRate department) {
		boolean valid = true;

		if (department.getHrTaxRateId() == null) {
			if (department.getCountry() != null && department.getEffectiveDate() != null) {
				TaxRate existingDept = HrServiceLocator.getTaxRateService().getTaxRate(department.getCountry(), department.getEffectiveLocalDate());
				
				if (existingDept != null) {
					if (StringUtils.equalsIgnoreCase(department.getCountry(), existingDept.getCountry())
							&& StringUtils.equalsIgnoreCase(department.getState(), existingDept.getState())) {
						this.putFieldError("country", "error.taxrate.duplicate.exists", department.getCountry());
						valid = false;
					}
				}
			}
		} 
		
		return valid;
	}

	boolean validateRolePresent(List<TaxRatePrincipalRoleMemberBo> roleMembers, LocalDate effectiveDate) {
		boolean valid = true;

		for (ListIterator<TaxRatePrincipalRoleMemberBo> iterator = roleMembers.listIterator(); iterator.hasNext(); ) {
			int index = iterator.nextIndex();
			RoleMemberBo roleMember = iterator.next();
			Role role = KimApiServiceLocator.getRoleService().getRole(roleMember.getRoleId());
				
			valid &= roleMember.isActive();
			
			if (StringUtils.equals(role.getName(), KPMERole.TIME_DEPARTMENT_ADMINISTRATOR.getRoleName())
					|| StringUtils.equals(role.getName(), KPMERole.LEAVE_DEPARTMENT_ADMINISTRATOR.getRoleName())) {
				String prefix = "roleMembers[" + index + "].";
				
				if (roleMember.getActiveToDateValue() != null) {
					if (effectiveDate.compareTo(roleMember.getActiveToDate().toLocalDate()) >= 0
							|| roleMember.getActiveFromDateValue().compareTo(roleMember.getActiveToDateValue()) >= 0) {
						this.putFieldError(prefix + "expirationDate", "error.role.expiration");
						valid = false;
					} else if (TKUtils.getDaysBetween(roleMember.getActiveFromDate().toLocalDate(), roleMember.getActiveToDate().toLocalDate()) > 180) {
						this.putFieldError(prefix + "expirationDate", "error.role.expiration.duration");
						valid = false;
		        	}
				}
			}
		}

		if (!valid) {
			this.putGlobalError("role.required");
		}

		return valid;
	}

}