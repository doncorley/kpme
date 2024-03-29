--
-- Copyright 2004-2013 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

delete from lm_leave_document_header_t where document_id >= '5000';
delete from hr_calendar_entries_t where hr_calendar_entry_id in ('10000', '10001', '10002', '10003');
delete from hr_principal_attributes_t where principal_id like('testUser%');
delete from lm_leave_plan_t where lm_leave_plan_id >= '5000';
delete from lm_accrual_category_t where lm_accrual_category_id >= '5000';
delete from lm_accrual_category_rules_t where lm_accrual_category_rules_id >= '5000';
delete from hr_earn_code_t where hr_earn_code_id >= '5000';
delete from lm_leave_block_t where principal_id like('testUser%');

INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5000', 'testUser', '2012-03-01 00:00:00','2012-03-15 00:00:00', 'F', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '1');
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5001', 'testUser', '2012-03-15 00:00:00','2012-04-01 00:00:00', 'I', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E96', '1');

-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('10000', '2', 'BWS-CAL', '2012-03-01 00:00:00', '2012-03-15 00:00:00');
-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('10001', '2', 'BWS-CAL', '2012-03-15 00:00:00','2012-04-01 00:00:00');
-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('10002', '2', 'BWS-CAL', '2012-04-01 00:00:00', '2012-05-01 00:00:00');
-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('10003', '2', 'BWS-CAL', '2012-05-01 00:00:00', '2012-06-01 00:00:00');
-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('10004', '2', 'BWS-CAL', '2012-02-01 00:00:00', '2012-03-01 00:00:00');

# first principalHRAttributes become active on 02/01/2012, then become inactive on 05/05/2012, then active on 05/05/2012 with a different leave plan
insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5001', 'testUser', 'BWS-CAL', 'testLP', '2012-02-05', 'Y', 'Y', null, '2012-02-05', '2012-02-01 11:59:46', uuid(), '1', 'Y', 'LM');
insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5002', 'testUser', 'BWS-CAL', 'testLP', '2012-02-05', 'Y', 'Y', null, '2012-05-05', '2012-05-01 11:59:46', uuid(), '1', 'N', 'LM');
insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5003', 'testUser', 'BWS-CAL', 'testLP1', '2012-02-05', 'Y', 'Y', null, '2012-05-05','2012-05-01 13:00:00', uuid(), '1', 'Y', 'LM');

insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('5000', 'testLP', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '12');
insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('5001', 'testLP1', 'Test Leave Plan', '01/01', '2012-05-01', '', '1', 'Y', '2012-02-06 11:59:46', '12');

# for testLP
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5000', 'testAC', 'testLP', 'test', 'M', '40', '2012-02-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'Y', null, 'Y', 'Y',now(), '0.5', 'EC4', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5000', 'M', 0, 800, 8, 100.00, 'NA', 'NA', NULL, NULL, NULL, NULL, NULL, '200', NULL, '5000', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'N');
# for testLP1
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5001', 'testAC1', 'testLP1', 'test', 'M', '40', '2012-02-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'Y', null, 'Y', 'Y',now(), '0.5', 'EC4', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5001', 'M', 0, 800, 8, 100.00, 'NA', 'NA', NULL, NULL, NULL, NULL, NULL, '300', NULL, '5001', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'N');

# fmla eligible earn code
insert into hr_earn_code_t values('5000', 'EC', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'testAC', '1.5', '1.5', 'Hours', 'testLP', 'U', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I', 'N');
# fmla ineligible earn code
insert into hr_earn_code_t values('5001', 'EC1', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'testAC', '1.5', '1.5', 'Hours', 'testLP', 'U', '99', 'T', 'N', 'Y', 'Y', 'Y', 'N', 'test', null, 'N', 'I', 'N');
insert into hr_earn_code_t values('5002', 'EC2', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'testAC1', '1.5', '1.5', 'Hours', 'testLP1', 'U', '99', 'T', 'N', 'Y', 'Y', 'Y', 'N', 'test', null, 'N', 'I', 'N');

insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type`) values('5000', '2012-03-05', 'approved', 'testUser', 'EC', NULL, 'testAC', '-2', null, 'N', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', now(), 'admin', 'A', 'AS');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type` ) values('5001', '2012-03-10', 'approved', 'testUser', 'EC', NULL, 'testAC', '5', null, 'Y', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', now(), 'admin', 'A', 'AS');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type` ) values('5002', '2012-04-10', 'Send for Approval', 'testUser', 'EC1', NULL, 'testAC', '10', null, 'Y', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', now(), 'admin', 'P', 'AS');
# for EC2
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type` ) values('5003', '2012-05-10', 'Send for Approval', 'testUser', 'EC2', NULL, 'testAC1', '8', null, 'Y', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', now(), 'admin', 'P', 'AS');

