<%@ include file="/WEB-INF/jsp/TkTldHeader.jsp"%>

<jsp:useBean id="tagSupport" class="org.kuali.hr.time.util.TagSupport"/>
<%@ attribute name="leaveSummary" required="true" type="org.kuali.hr.lm.leaveSummary.LeaveSummary"%>
 
<div id="leave-summary">
	<c:if test="${not empty leaveSummary.leaveSummaryRows}">
		<div class="summaryTitle" style="clear:botd; text-align:center; font-weight: bold; margin-bottom: 5px;">Summary </div>
			<div id="leave-summary-table">
				<table>
					<thead>
						<tr class="noborder" >
							<td></td>
							<td></td>
							<td colspan="2" class="${empty leaveSummary.ytdDatesString ? '' : 'withborder' }">${leaveSummary.ytdDatesString}</td>
							<td></td>
							<td colspan="2" class="${empty leaveSummary.pendingDatesString ? '' : 'withborder' }">${leaveSummary.pendingDatesString}</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
						<tr class="ui-state-default">
							<td scope="col">Accrual Category</td>
							<td scope="col">Carryover</td>
							<td scope="col">Ytd Accrued Balance</td>
							<td scope="col">Ytd Approved Usage</td>
							<td scope="col">Leave Balance</td>
							<td scope="col">Pending Leave Accrual</td>
							<td scope="col">Pending Leave Requests/Usage</td>
							<td scope="col">Pending Leave Balance</td>
							<td scope="col">Pending Available Usage</td>
							<td scope="col">Usage Limit</td>
							<td scope="col">FMLA Usage</td>
						</tr>
					</thead>
			    	<tbody>
			    		<c:forEach items="${leaveSummary.leaveSummaryRows}" var="row">
							<tr style="border-bottom-style: double; font-weight: bold;">
								<td>${row.accrualCategory}</td>	
								<td>${row.carryOver}</td>
								<td>${row.ytdAccruedBalance}</td>
								<td>${row.ytdApprovedUsage}</td>
								<td>${row.leaveBalance}</td>
								<td>${row.pendingLeaveAccrual}</td>
								<td>${row.pendingLeaveRequests}</td>
								<td>${row.pendingLeaveBalance}</td>
								<td>${row.pendingAvailableUsage}</td>
								<td>${row.usageLimit}</td>
								<td>${row.fmlaUsage}</td>								
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</c:if>
</div>
	