package org.kuali.hr.time.roles.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.hr.time.HrEffectiveDateActiveLookupableHelper;
import org.kuali.hr.time.roles.TkRoleGroup;
import org.kuali.hr.time.service.base.TkServiceLocator;
import org.kuali.hr.time.util.TKContext;
import org.kuali.rice.kim.bo.Person;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kns.bo.BusinessObject;
import org.kuali.rice.kns.lookup.HtmlData;

public class TkRoleLookupableHelper extends HrEffectiveDateActiveLookupableHelper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "serial" })
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject,
			List pkNames) {
		List<HtmlData> customActionUrls = super.getCustomActionUrls(
				businessObject, pkNames);
		if (TKContext.getUser().getCurrentRoles().isSystemAdmin()) {
			TkRoleGroup tkRoleGroup = (TkRoleGroup) businessObject;
			final String className = this.getBusinessObjectClass().getName();
			final String principalId = tkRoleGroup.getPrincipalId();
			HtmlData htmlData = new HtmlData() {

				@Override
				public String constructCompleteHtmlTag() {
					return "<a target=\"_blank\" href=\"inquiry.do?businessObjectClassName="
							+ className
							+ "&methodToCall=start&principalId="
							+ principalId + "\">view</a>";
				}
			};
			customActionUrls.add(htmlData);
		} else if (customActionUrls.size() != 0) {
			customActionUrls.remove(0);
		}
		return customActionUrls;
	}

	@Override
	public List<? extends BusinessObject> getSearchResults(
			Map<String, String> fieldValues) {
		List<BusinessObject> roleGroupList = new ArrayList<BusinessObject>();
		String principalId = fieldValues.get("principalId");
		if(principalId!=""){
			Person person = KIMServiceLocator.getPersonService().getPerson(principalId);
			roleGroupList.add(getRoleGroupFromPerson(person));
		}else{
			List<Person> personList = KIMServiceLocator.getPersonService().findPeople(null);
			for(Person person : personList){
				roleGroupList.add(getRoleGroupFromPerson(person));
			}
		}
		for(BusinessObject businessObject : roleGroupList){
			TkRoleGroup roleGroup = (TkRoleGroup)businessObject;
			TkRoleGroup tkRoleGroup = TkServiceLocator.getTkRoleGroupService().getRoleGroup(roleGroup.getPrincipalId());
			if(tkRoleGroup == null){
				TkServiceLocator.getTkRoleGroupService().saveOrUpdate(roleGroup);
			}
		}
		return roleGroupList;
	}
	
	private TkRoleGroup getRoleGroupFromPerson(Person person){
		TkRoleGroup tkRoleGroup = new TkRoleGroup();
		tkRoleGroup.setPerson(person);
		tkRoleGroup.setPrincipalId(person.getPrincipalId());
		return tkRoleGroup;
	}

}
