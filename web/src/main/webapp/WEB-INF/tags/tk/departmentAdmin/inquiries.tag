<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp" %>

<div class="portlet">
    <div class="header">Inquiries</div>
    <div class="body">
        <ul>
            <li>
                <a href="${ConfigProperties.application.url}/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.clocklog.ClockLog&returnLocation=${ConfigProperties.application.url}/DepartmentAdmin.do&hideReturnLink=true&docFormKey=88888888&active=Y">Clock Log</a>
            </li>
            <li>
                <a href="${ConfigProperties.application.url}/kpme/lookup?methodToCall=start&dataObjectClassName=org.kuali.kpme.tklm.time.missedpunch.MissedPunch&returnLocation=${ConfigProperties.application.url}/portal.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y">Missed Punch</a>
            </li>
            <li>
                <a href="${ConfigProperties.application.url}/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.timeblock.TimeBlock&returnLocation=${ConfigProperties.application.url}/DepartmentAdmin.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y">Time Block Inquiry</a>
            </li>
            <li>
                <a href="${ConfigProperties.application.url}/kr/lookup.do?methodToCall=start&businessObjectClassName=org.kuali.kpme.tklm.time.timeblock.TimeBlockHistory&returnLocation=${ConfigProperties.application.url}/DepartmentAdmin.do&showMaintenanceLinks=true&hideReturnLink=true&docFormKey=88888888&active=Y">Time Block History Inquiry</a>
            </li>
        </ul>
    </div>
</div>