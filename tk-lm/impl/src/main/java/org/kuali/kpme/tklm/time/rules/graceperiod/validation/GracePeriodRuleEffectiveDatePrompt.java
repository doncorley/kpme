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
package org.kuali.kpme.tklm.time.rules.graceperiod.validation;

import org.kuali.kpme.core.util.TKUtils;
import org.kuali.kpme.core.web.KpmeEffectiveDatePromptBase;
import org.kuali.kpme.tklm.time.rules.graceperiod.GracePeriodRule;
import org.kuali.kpme.tklm.time.service.TkServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

public class GracePeriodRuleEffectiveDatePrompt extends KpmeEffectiveDatePromptBase {
    
	@Override
    protected boolean futureEffectiveDateExists(PersistableBusinessObject pbo) {
    	boolean futureEffectiveDateExists = false;
    	
        GracePeriodRule gracePeriodRule = (GracePeriodRule) pbo;
        GracePeriodRule lastGracePeriodRule = TkServiceLocator.getGracePeriodService().getGracePeriodRule(TKUtils.END_OF_TIME);
        if (lastGracePeriodRule != null && lastGracePeriodRule.getEffectiveLocalDate() != null && gracePeriodRule.getEffectiveLocalDate() != null) {
        	futureEffectiveDateExists = lastGracePeriodRule.getEffectiveLocalDate().isAfter(gracePeriodRule.getEffectiveLocalDate());
        }
        
        return futureEffectiveDateExists;
    }
	
}
