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
package org.kuali.kpme.core.block;

import java.sql.Timestamp;
import java.util.Date;

public interface CalendarBlockContract {

	public String getDocumentId();

	public void setDocumentId(String documentId);

	public abstract String getPrincipalIdModified();

	public abstract void setPrincipalIdModified(String principalIdModified);

	public String getPrincipalId();

	public void setPrincipalId(String principalId);

	public Timestamp getTimestamp();

	public void setTimestamp(Timestamp timestamp);

	public Date getBeginTimestamp();

	public void setBeginTimestamp(Date beginTimestamp);

	public Date getEndTimestamp();

	public void setEndTimestamp(Date endTimestamp);

}