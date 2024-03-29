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
package org.kuali.kpme.tklm.leave.accrual;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.kuali.kpme.core.calendar.entry.CalendarEntry;

public class RateRangeAggregate {
	private List<RateRange> rateRanges = new ArrayList<RateRange>();
	private RateRange currentRate;
	private boolean rateRangeChanged;
	private Map<String, List<CalendarEntry>> calEntryMap;
	
	public RateRange getRate(DateTime date) {		
		rateRangeChanged = false;
		if (currentRate == null) {
			currentRate = rateRanges.get(0);
		}
		
		if (currentRate.getRange().contains(date)) {
			for (RateRange rateRange : rateRanges) {
				if (rateRange.getRange().contains(date)) {
					rateRangeChanged = rateRange.getAccrualRatePercentageModifier() != currentRate.getAccrualRatePercentageModifier();
					currentRate = rateRange;
					break;
				}
			}
		}
		return currentRate;
	}
	
	public List<RateRange> getRateRanges() {
		return rateRanges;
	}
	public void setRateRanges(List<RateRange> rateRanges) {
		this.rateRanges = rateRanges;
	}
	public RateRange getCurrentRate() {
		return currentRate;
	}
	public void setCurrentRate(RateRange currentRate) {
		this.currentRate = currentRate;
	}
	public boolean isRateRangeChanged() {
		return rateRangeChanged;
	}
	public void setRateRangeChanged(boolean rateRangeChanged) {
		this.rateRangeChanged = rateRangeChanged;
	}
	
	// return the rateRange on the given day
	public RateRange getRateOnDate(DateTime date) {
		for (RateRange rateRange : rateRanges) {
			if (rateRange.getRange().contains(date)) {
				return rateRange;
			}
		}
		return null;
	}

	public Map<String, List<CalendarEntry>> getCalEntryMap() {
		return calEntryMap;
	}

	public void setCalEntryMap(Map<String, List<CalendarEntry>> calEntryMap) {
		this.calEntryMap = calEntryMap;
	}
	

}
