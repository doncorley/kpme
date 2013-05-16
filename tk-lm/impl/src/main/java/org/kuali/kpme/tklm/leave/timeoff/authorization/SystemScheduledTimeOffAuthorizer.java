package org.kuali.kpme.tklm.leave.timeoff.authorization;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.kpme.core.authorization.KPMEMaintenanceDocumentAuthorizerBase;
import org.kuali.kpme.core.role.KPMERoleMemberAttribute;
import org.kuali.kpme.tklm.leave.timeoff.SystemScheduledTimeOff;

@SuppressWarnings("deprecation")
public class SystemScheduledTimeOffAuthorizer extends KPMEMaintenanceDocumentAuthorizerBase {

	private static final long serialVersionUID = -3103190348658150171L;

	protected void addRoleQualification(Object dataObject, Map<String, String> attributes) {
		super.addRoleQualification(dataObject, attributes);
		
		String location = StringUtils.EMPTY;
		
		if (dataObject instanceof SystemScheduledTimeOff) {
			SystemScheduledTimeOff systemScheduledTimeOffObj = (SystemScheduledTimeOff) dataObject;
			
			if (systemScheduledTimeOffObj != null) {
				location = systemScheduledTimeOffObj.getLocation();
			}
		}

		attributes.put(KPMERoleMemberAttribute.LOCATION.getRoleMemberAttributeName(), location);
	}
	
}
