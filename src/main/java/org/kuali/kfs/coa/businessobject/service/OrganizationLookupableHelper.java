package org.kuali.kfs.coa.businessobject.service;

import java.util.List;

import org.kuali.hr.time.util.TKContext;
import org.kuali.kfs.coa.businessobject.Organization;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;

public class OrganizationLookupableHelper extends
		KualiLookupableHelperServiceImpl {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject,
			List pkNames) {
		List<HtmlData> customActionUrls = super.getCustomActionUrls(
				businessObject, pkNames);
		if (TKContext.getUser().getCurrentRoles().isSystemAdmin()) {
			Organization organization = (Organization) businessObject;
			final String className = this.getBusinessObjectClass().getName();
			final String organizationCode = organization.getOrganizationCode();
			final String chartOfAccountsCode = organization
					.getChartOfAccountsCode();
			HtmlData htmlData = new HtmlData() {

				@Override
				public String constructCompleteHtmlTag() {
					return "<a target=\"_blank\" href=\"inquiry.do?businessObjectClassName="
							+ className
							+ "&methodToCall=start&organizationCode="
							+ organizationCode
							+ "&chartOfAccountsCode="
							+ chartOfAccountsCode + "\">view</a>";
				}
			};
			customActionUrls.add(htmlData);
		} else if (customActionUrls.size() != 0) {
			customActionUrls.remove(0);
		}
		return customActionUrls;
	}
}