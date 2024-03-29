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
package org.kuali.kpme.core.kfs.coa.businessobject.options;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kuali.kpme.core.kfs.coa.businessobject.Chart;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * This class returns list of chart key value pairs.
 */
public class ChartValuesFinder extends KeyValuesBase {

    /**
     * Creates a list of {@link Chart} using their code as the key and their code "-" description
     * 
     * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
     */

	
	
    public List getKeyValues() {
    	
    	Collection<Chart> chartCodes = KRADServiceLocator.getKeyValuesService().findAll(Chart.class);

    	List<KeyValue> chartKeyLabels = new ArrayList<KeyValue>();
        chartKeyLabels.add(new ConcreteKeyValue("", ""));

        for (Chart element : chartCodes) {
            if(element.isActive()) {
                chartKeyLabels.add(new ConcreteKeyValue(element.getChartOfAccountsCode(), element.getCodeAndDescription()));
            }
        }

        //TODO remove temp hard coded values
        //chartKeyLabels.add(new KeyLabelPair("SC", "SC Description Temp"));
        return chartKeyLabels;
    	
    }

}
