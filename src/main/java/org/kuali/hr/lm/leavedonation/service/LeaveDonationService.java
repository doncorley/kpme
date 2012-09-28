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
package org.kuali.hr.lm.leavedonation.service;

import org.kuali.hr.lm.leavedonation.LeaveDonation;
import org.springframework.cache.annotation.Cacheable;

public interface LeaveDonationService {
    
    /**
     * Fetch LeaveDonation by id
     * @param lmLeaveDonationId
     * @return
     */
    @Cacheable(value= LeaveDonation.CACHE_NAME, key="'lmLeaveDonationId=' + #p0")
    public LeaveDonation getLeaveDonation(String lmLeaveDonationId);

}
