package org.kuali.hr.time.accrual.service;

import java.util.List;

import org.kuali.hr.time.accrual.TimeOffAccrual;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;

public class TimeOffAccrualLookupableHelper extends
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
		TimeOffAccrual timeOffAccrual = (TimeOffAccrual) businessObject;
		final String className = this.getBusinessObjectClass().getName();
		final Long laAccrualId = timeOffAccrual.getLaAccrualId();
		HtmlData htmlData = new HtmlData() {

			@Override
			public String constructCompleteHtmlTag() {
				return "<a target=\"_blank\" href=\"inquiry.do?businessObjectClassName="
						+ className + "&methodToCall=start&laAccrualId=" + laAccrualId
						+ "\">view</a>";
			}
		};
		customActionUrls.add(htmlData);
		return customActionUrls;
	}
}
