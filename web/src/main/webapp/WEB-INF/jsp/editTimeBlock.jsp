<%--

    Copyright 2004-2013 The Kuali Foundation

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
<%@include file="/WEB-INF/jsp/TkTldHeader.jsp"%>
<c:set var="Form" value="${ClockActionForm}" scope="request"/>
<c:set var="timeBlockHours"><fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${Form.currentTimeBlock.hours}" /></c:set>

<tk:tkHeader tabId="editTimeBlock" >
	<html:form action="/Clock.do" styleId="editTimeBlockForm">
		<div style="clear:both; text-align:center; font-weight: bold; margin-bottom: 5px;">Distribute Hours</div>
 			<html:hidden property="methodToCall" value="" styleId="methodToCall"/>
 			<html:hidden property="newAssignDesCol" value="" styleId="newAssignDesCol"/>
 			<html:hidden property="newBDCol" value="" styleId="newBDCol"/>
 			<html:hidden property="newBTCol" value="" styleId="newBTCol"/>
 			<html:hidden property="newEDCol" value="" styleId="newEDCol"/>
 			<html:hidden property="newETCol" value="" styleId="newETCol"/>
 			<html:hidden property="newHrsCol" value="" styleId="newHrsCol"/>
 			<html:hidden property="tsDocId" value="${Form.documentId}" styleId="tsDocId"/>
 			<html:hidden property="tbId" value="${Form.editTimeBlockId}"  styleId="tbId"/>
 			<html:hidden property="originHrs" value="${timeBlockHours}"  styleId="originHrs"/>
			<html:hidden property="assignmentList" value="${Form.assignDescriptionsList}"/>
			<html:hidden property="distributeAssignList" value="${Form.distributeAssignList}" />
			<html:hidden property="originalAssignment" value="${Form.currentAssignmentDescription}"/>
			<html:hidden property="beginTimestamp" value="${Form.currentTimeBlock.beginTimeDisplayDate}"/>
			<html:hidden property="endTimestamp" value="${Form.currentTimeBlock.endTimeDisplayDate}"/>			
			<html:hidden property="beginDateOnly" value="${Form.currentTimeBlock.beginTimeDisplayDateOnlyString}"/>
			<html:hidden property="beginTimeOnly" value="${Form.currentTimeBlock.beginTimeDisplayTimeOnlyString}"/>
			<html:hidden property="endDateOnly" value="${Form.currentTimeBlock.endTimeDisplayDateOnlyString}"/>
			<html:hidden property="endTimeOnly" value="${Form.currentTimeBlock.endTimeDisplayTimeOnlyString}"/>			
			<html:hidden property="hours" value="${timeBlockHours}"/>
			
			<div id="clock" style="border-bottom: 1px solid gray;">
				<table style="border-collapse:collapse;border: 1px solid #000;">
					<thead>
						<tr class="ui-state-default">
							<td>Assignment</td>
							<td>Begin Date/Time</td>
							<td>End Date/Time</td>
							<td>Hours</td>
						</tr>
					</thead>
					<tbody>
							<tr>
								<td><c:out value="${Form.currentAssignmentDescription}" /></td>
								<td><c:out value="${Form.currentTimeBlock.beginTimeDisplayDateOnlyString} ${Form.currentTimeBlock.beginTimeDisplayTimeOnlyString}" /></td>
								<td><c:out value="${Form.currentTimeBlock.endTimeDisplayDateOnlyString} ${Form.currentTimeBlock.endTimeDisplayTimeOnlyString}" /></td>
								<td><c:out value="${Form.currentTimeBlock.hours}" /></td>
							</tr>
					</tbody>
				</table>
		</div>

		<div id="clock">
		 	<p id="validation" class="validation">All form fields are required.</p>
		 	<div id="edit-panel">
				<table id="tblNewTimeBlocks" style="border-collapse:collapse;border: 1px solid #000;">
                    <thead>
						<tr class="ui-state-default">
							<td>Assignment</td>
							<td>Begin Date</td>
							<td>Begin Time</td>
							<td></td>
							<td>End Date</td>
							<td>End Time</td>
							<td></td>
							<td>Hours</td>
							<td>Action</td>
						</tr>
                    </thead>
                    <tbody>
                        <tr>
							<td>
								 <select id="assignmentRowFirst" class="assignmentRow">
									<c:forEach var="assignment" items="${Form.desList}">
										<c:choose>
											<c:when test='${assignment.value == Form.currentAssignmentDescription}'>
												<option value="${assignment.key}" selected>${assignment.value}</option>
											</c:when>
											<c:otherwise>
												<option value="${assignment.key}" >${assignment.value}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td>
								<input disabled type="text" id="bdRowFirst" class="bdRow" size="10" value="${Form.currentTimeBlock.beginTimeDisplayDateOnlyString}" />
							</td>
							<td colspan="2" align="left">
								<input disabled type="text" id="btRowFirst" class="btRow" size="10" value="${Form.currentTimeBlock.beginTimeDisplayTimeOnlyString}" />
                            </td>
							<td>
								<input disabled type="text" id="edRowFirst" class="edRow" size="10" value="${Form.currentTimeBlock.beginTimeDisplayDateOnlyString}" />
							</td>
							<td colspan="2">
								<input id="etRowFirst" class="etRow" size="10" /><input type="button" style="width: 20px; height: 23px;" class="endTimeHelp" value="?"
									title="Supported formats:<br/>9a, 9 am, 9 a.m.,  9:00a, 9:45a, 3p, 15:30, 2:30" />
							</td>
							<td>
								<input disabled id="hrRowFirst" class="hrRow" disabled="disabled" size="5" />
							</td>
							<td>
                                <input class="button" value="Add" type="button" name="addTimeBlock" id="addTimeBlock"
                                    onclick="javascript: addTimeBlockRow(this.form);" />
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <select id="assignmentRowLast" class="assignmentRow">
                                    <c:forEach var="assignment" items="${Form.desList}">
										<c:choose>
											<c:when test='${assignment.value == Form.currentAssignmentDescription}'>
												<option value="${assignment.key}" selected>${assignment.value}</option>
											</c:when>
											<c:otherwise>
												<option value="${assignment.key}" >${assignment.value}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td>
								<input disabled type="text" id="bdRowLast" class="bdRow" size="10" value="${Form.currentTimeBlock.beginTimeDisplayDateOnlyString }" />
							</td>
							<td colspan="2">
								<input id="btRowLast" class="btRow" size="10" /><input type="button" style="width: 20px; height: 23px;" class="beginTimeHelp" value="?"
									title="Supported formats:<br/>9a, 9 am, 9 a.m.,  9:00a, 9:45a, 3p, 15:30, 2:30" />
							</td>
							<td>
								<input disabled type="text" id="edRowLast" class="edRow" size="10" value="${Form.currentTimeBlock.endTimeDisplayDateOnlyString }" />
							</td>
							<td colspan="2" align="left">
								<input disabled id="etRowLast" class="etRow" size="10" value="${Form.currentTimeBlock.endTimeDisplayTimeOnlyString}" />
							</td>
							<td>
								<input disabled id="hrRowLast" class="hrRow" size="5" />
							</td>
                            <td></td>
                        </tr>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="7" />
							<td>Total Hours:</td>
							<td>
								<input name="hrsTotal" id="hrsTotal" disabled="disabled" size="5"/>
							</td>
							<td></td>
						</tr>
					</tfoot>
				</table>
			</div>
		</div>


		<div align="center">
			 <input type="button" class="button" value="Save" name="saveTimeBlock" id="saveTimeBlock"/>
			<input type="submit" class="button" value="Cancel" name="cancel"
			onclick="javascript: window.close();return false;"/>
		</div>

	</html:form>
</tk:tkHeader>

<tk:note/>

