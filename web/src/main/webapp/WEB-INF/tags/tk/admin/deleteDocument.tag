<%--
 Copyright 2007-2009 The Kuali Foundation
 
 Licensed under the Educational Community License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.opensource.org/licenses/ecl2.php
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ include file="/rice-portal/jsp/sys/riceTldHeader.jsp"%>

<c:set var="systemAdmin" value='<%=org.kuali.kpme.core.util.HrContext.isSystemAdmin()%>' />

<c:if test="${systemAdmin}">
    <channel:portalChannelTop channelTitle="Delete Timesheet / Leave Calendar" />
    <div class="body">
        <div id="content">
            <html:form action="/deleteDocument" method="post">
                <html:text property="deleteDocumentId" />
                <html:submit property="methodToCall.deleteDocument" value="Submit" />
            </html:form>
        </div>
    </div>
    <channel:portalChannelBottom />
</c:if>