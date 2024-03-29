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
package org.kuali.kpme.core.paystep.valuesfinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.kpme.core.util.HrConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;

public class ServiceUnitKeyValueFinder extends UifKeyValuesFinderBase {

	@Override
	public List<KeyValue> getKeyValues(ViewModel model) {
		List<KeyValue> values = new ArrayList<KeyValue>();
		for(Map.Entry entry : HrConstants.SERVICE_UNIT_OF_TIME.entrySet()) {
			values.add(new ConcreteKeyValue((String)entry.getKey(), (String)entry.getValue()));
		}
		return values;
	}

}
