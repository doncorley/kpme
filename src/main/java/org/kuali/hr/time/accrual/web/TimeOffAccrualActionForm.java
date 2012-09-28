/**
 * Copyright 2004-2012 The Kuali Foundation
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
package org.kuali.hr.time.accrual.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kuali.hr.time.base.web.TkForm;

public class TimeOffAccrualActionForm extends TkForm {

	private static final long serialVersionUID = 431293610332309616L;
	
	List<Map<String, Object>> timeOffAccrualsCalc = new ArrayList<Map<String, Object>>();

	public List<Map<String, Object>> getTimeOffAccrualsCalc() {
		return timeOffAccrualsCalc;
	}

	public void setTimeOffAccrualsCalc(List<Map<String, Object>> timeOffAccrualsCalc) {
		this.timeOffAccrualsCalc = timeOffAccrualsCalc;
	}

}
