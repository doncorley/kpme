/*
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

    
$(function () {
	
	 /**
	 * ====================
	 * Models
	 * ====================
	 */
	/**
     * leave request employee row
     */
		
    // create a LeaveRequestApproval view
    var LeaveRequestApprovalView = Backbone.View.extend({
    	 el : $("body"),
    	 events : {
	    	 "click input[id^=takeAction]" : "takeActionOnEmployee",
	         "click input[id^=checkAllApprove]" : "checkAllApprove",
	         "change input:radio[id^=action]" : "changeReasonStyle"
         },
         initialize : function () {
        	 return this;
         },
         render : function () {
             return this;
         },
      
	    /*
	     * The logic in following functions heavily depends on the id of the fields in LeaveRequestApproval.jsp
	     * If you need to make changes to the name or id of any fields in the jsp, make sure they are changed in these functions too 
	     */
         takeActionOnEmployee : function (e) {
        	
        	var key = _(e).parseActionKey();
        	var principalId = key.principalId;	// get the principalId from the id of the "take action" button        	
        	// get all the checked leave request, build multiple lists and submit them to action
        	var approveList = '';
        	var disapproveList = '';
        	var deferList = '';
        	var validationEle = $("#validation_"+principalId);	// display error messages
        	var radioCells = $("input:radio[id^=action_" + principalId + "]");
        	var docSeparator = "----";  // separater doc actions
        	var idSeparator = "____";	// four "_", separator for for documentId and reason string
        	var errors = "";
        	// reset all error fields
        	this.resetErrorFields(principalId);
        	
        	// client side validation
        	radioCells.each(function() {
        		if($(this).attr('checked') == "checked") {
        			var radioKey = _(this).parseRadioEleKey();
        			var reasonEle = $("#reason_" + principalId + "_" + radioKey.documentId);
        			// if value = noAction, ignore this row
        			if($(this).attr('value') == "noAction") {
        				// do nothing
        			} else if($(this).attr('value') == "approve") {
        				// add this row to the approve list
        				approveList += radioKey.documentId + idSeparator + reasonEle.val() + docSeparator;
        			} else if($(this).attr('value') == "disapprove") {
        				// check if reason field is empty
        				if(!reasonEle.val()) {
        					reasonEle.addClass('ui-state-error');
        					errors = "Reason needed for Disapprove action.";
        					return false;
        				} else {
        					disapproveList += radioKey.documentId + idSeparator + reasonEle.val() + docSeparator;
        				}
        			} else if($(this).attr('value') == "defer") {
        				// check if reason field is empty
        				if(!reasonEle.val()) {
        					reasonEle.addClass('ui-state-error');
        					errors = "Reason needed for Defer action.";
        					return false;
        				} else {
        					deferList += radioKey.documentId + idSeparator + reasonEle.val() + docSeparator;
        				}
        			}
        		}
        	});
        	if(errors.length != 0) {
        		this.updateValidation(validationEle, errors);
        		return false;
        	}
        	// if no action found, display error and return back to GUI
        	if(approveList.length ==0 && disapproveList.length == 0 && deferList.length == 0) {
        		this.updateValidation(validationEle, "No action selected!");
        		return false;
        	}
        	// submit the request to form
        	var params = {};
            params['principalId'] = principalId;	// may not be needed
            params['approveList'] = approveList;
            params['disapproveList'] = disapproveList;
            params['deferList'] = deferList;       

            var errorMsgs = "";
            $.ajax({
                url: "LeaveRequestApproval.do?methodToCall=validateActions",	// server side validation
                data: params,
                cache: false,
                async : false,
                success: function(data) {
                    var json = jQuery.parseJSON(data);
                    // if there is no error message, submit the form and save the new time blocks
                    if (json == null || json.length == 0) {
                    	$.ajax({
                            url: "LeaveRequestApproval.do?methodToCall=takeActionOnEmployee",
                            data: params,
                            cache: false,
                            async : false,
                            success : function(data) {
                                // successful
                                return;
                            },
                            error : function() {
                            	errorMsgs += "Error occurred. Please try again.";
                                return false;
                            }
                      }); 
                    } else {
                         $.each(json, function (index) {
                             errorMsgs += "Error : " + json[index] + "\n";
                         }); 
                         return false;
                    }
            	}
            })
            if(errorMsgs != 0 ) {
            	this.updateValidation(validationEle, errorMsgs);
                return false;
            }
	        return;
	     },
         
         checkAllApprove : function(e) {
         	var key = _(e).parseActionKey();
			var principalId = key.principalId;	// get the principalId from the id of the "Select All" checkbox
			var radioCells = $("input:radio[id^=action_" + principalId + "]");
			// when checkbox is checked, select approve for all radio buttons of employee
			if($('#checkAllApprove_' + principalId).is(':checked')){
				radioCells.each(function() {
					if($(this).attr("value") == "approve") {
						$(this).attr("checked", "checked");
					}
				});
			} else {
				radioCells.each(function() {
					if($(this).attr("value") == "noAction") {
						$(this).attr("checked", "checked");
					}
				});
			}
			//disable all reason input fields, reason fields should be disabled for approve and noAction
			var reasonCells = $("input[id^=reason_" + principalId + "]");
			reasonCells.each(function() {
				$(this)
					.val('')
    				.attr("disabled", "disabled")
    				.addClass('ui-state-disabled');
			});
			
         },
         
         changeReasonStyle : function(e) {
 			var actionId = (e.target || e.srcElement).id;
 			var reasonId = actionId.replace(/action/g, "reason");
    		var radioCells = $("input:radio[id^=" + actionId + "]");
   			radioCells.each(function() {
        		if($(this).attr('checked') == "checked") {
        			var radioValue = $(this).attr('value');
		    		if(radioValue == "approve" || radioValue == "noAction") {	
		    			$('#'+reasonId)
		    				.val('')
		    				.attr("disabled", "disabled")
		    				.addClass('ui-state-disabled');
		    		} else {
		    			$('#'+reasonId)
		    				.removeAttr("disabled")
		    				.removeClass("ui-state-disabled");
		    		}
		    	}
   			});
         },
         
//       update the validation field for this employee table
         updateValidation : function(e, t) {
        	    e.text(t)
        	            .css({'color':'red','font-weight':'bold','font-size': '1.2em','text-align':'center'});
         }, 
         resetErrorFields : function(t) {
        	 $("#validation_" + t)
		            .text('')
		            .removeClass('ui-state-error');
        	 var reasonCells = $('input:text[id^="reason_" + t]');
        	 reasonCells.each(function() {
        		 $(this).removeClass('ui-state-error');
        	 });
         }
    
    });
    
    var app = new LeaveRequestApprovalView;
    _.mixin({
        /**
         * Parse the element id to get the action and principalId
         * @param event
         */
        parseActionKey : function (e) {
            var id = (e.target || e.srcElement).id;

            return {
                action : id.split("_")[0],
                principalId : id.split("_")[1]
            };
        }, 
        /**
         * Parse the element id to get the action, principalId and documentId
         * @param event
         */
        parseRadioEleKey : function (e) {
            var id = e.id;

            return {
                action : id.split("_")[0],
                principalId : id.split("_")[1],
                documentId : id.split("_")[2]
            };
        }
    });

});