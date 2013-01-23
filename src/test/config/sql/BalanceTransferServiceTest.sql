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

delete from lm_accrual_category_rules_t where lm_accrual_category_rules_id >= 5000;
delete from lm_leave_plan_t where lm_leave_plan_id >= 80000;
delete from hr_principal_attributes_t where hr_principal_attribute_id >= 5000;
delete from lm_accrual_category_t where lm_accrual_category_id >= 5000;
delete from hr_job_t where hr_job_id >= 5000;
delete from lm_leave_document_header_t where document_id >= 5000;
delete from hr_earn_code_t where hr_earn_code_id >= 5000;
delete from hr_calendar_entries_t where hr_calendar_entry_id >= 5000;
delete from lm_leave_block_t where lm_leave_block_id >= 39430;
delete from lm_employee_override_t where lm_employee_override_id >= 3000;

insert into lm_employee_override_t values ('3000', 'testUser1', 'ye-xfer-eo', 'testLP', 'MB', 15, NULL,'Y', now(), uuid(), '1', '2012-02-03 12:10:23');
insert into lm_employee_override_t values ('3001', 'testUser1', 'ye-xfer-eo', 'testLP', 'MAC', 5, NULL,'Y', now(), uuid(), '1', '2012-02-03 12:10:23');
insert into lm_employee_override_t values ('3002', 'testUser1', 'ye-xfer-eo', 'testLP', 'MTA', 7, NULL,'Y', now(), uuid(), '1', '2012-02-03 12:10:23');

insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5000', 'M', 0, 888, 16, 15.00, 'OD', 'T', 'od-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5000', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5001', 'M', 0, 888, 16, 15.00, 'YE', 'T', 'ye-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5001', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5002', 'M', 0, 888, 16, 15.00, 'LA', 'T', 'la-xfer-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5002', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5003', 'M', 0, 888, 16, 15.00, 'OD', 'T', 'od-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5003', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5004', 'M', 0, 888, 16, 15.00, 'YE', 'T', 'ye-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5004', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5005', 'M', 0, 888, 16, 15.00, 'LA', 'T', 'la-xfer-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5005', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5006', 'M', 0, 888, 16, 15.00, 'OD', 'L', 'od-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5006', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5007', 'M', 0, 888, 16, 15.00, 'YE', 'L', 'ye-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5007', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5008', 'M', 0, 888, 16, 15.00, 'LA', 'L', 'la-lose-dep', 0.5, 10.00, NULL, NULL, NULL, NULL, '5008', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5009', 'M', 0, 888, 16, 15.00, 'OD', 'L', 'od-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5009', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5010', 'M', 0, 888, 16, 15.00, 'YE', 'L', 'ye-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5010', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');
insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5011', 'M', 0, 888, 16, 15.00, 'LA', 'L', 'la-lose-mac-dep', 0.5, 10.00, NULL, NULL, NULL, 10.00, '5011', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');

insert into lm_accrual_category_rules_t (`lm_accrual_category_rules_id`, `SERVICE_UNIT_OF_TIME`, `START_ACC`, `END_ACC`, `ACCRUAL_RATE`, `MAX_BAL`, `MAX_BAL_ACTION_FREQUENCY`, `ACTION_AT_MAX_BAL`, `MAX_BAL_TRANS_ACC_CAT`, `MAX_BAL_TRANS_CONV_FACTOR`, `MAX_TRANS_AMOUNT`, `MAX_PAYOUT_AMOUNT`, `MAX_PAYOUT_EARN_CODE`, `MAX_USAGE`, `MAX_CARRY_OVER`, `LM_ACCRUAL_CATEGORY_ID`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `MAX_BAL_FLAG`) values ('5012', 'M', 0, 888, 16, 100.00, 'YE', 'T', 'ye-xfer-eo', NULL, NULL, NULL, NULL, NULL, 10.00, '7000', 'DEDC243D-4E51-CCDE-1326-E1700B2631E1', '1', 'Y', '2012-02-03 12:10:23', 'Y');

-- setup leave plan, principal hr attributes, leave eligible jobs, leave calendar.
insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80000', 'testLP', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80001', 'ye-xfer', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80002', 'la-xfer', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80003', 'od-xfer-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80004', 'ye-xfer-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80005', 'la-xfer-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80006', 'od-lose', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80007', 'ye-lose', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80008', 'la-lose', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80009', 'od-lose-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80010', 'ye-lose-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');
-- insert into lm_leave_plan_t (`lm_leave_plan_id`, `LEAVE_PLAN`, `DESCR`, `CAL_YEAR_START`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `ACTIVE`, `TIMESTAMP`, `PLANNING_MONTHS`) values ('80011', 'la-lose-mac', 'Test Leave Plan', '02/01', '2012-02-01', '', '1', 'Y', '2012-02-06 11:59:46', '80');


insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5000', 'testUser1', 'BWS-LM', 'testLP', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'BWS-LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5001', 'testUser2', 'BWS-CAL', 'ye-xfer', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5002', 'testUser3', 'BWS-CAL', 'la-xfer', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5003', 'testUser4', 'BWS-CAL', 'od-xfer-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5004', 'testUser5', 'BWS-CAL', 'ye-xfer-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5005', 'testUser6', 'BWS-CAL', 'la-xfer-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5006', 'testUser7', 'BWS-CAL', 'od-lose', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5007', 'testUser8', 'BWS-CAL', 'ye-lose', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5008', 'testUser9', 'BWS-CAL', 'la-lose', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5009', 'testUser10', 'BWS-CAL', 'od-lose-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5010', 'testUser11', 'BWS-CAL', 'ye-lose-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');
-- insert into hr_principal_attributes_t (`hr_principal_attribute_id`, `principal_id`, `pay_calendar`, `leave_plan`, `service_date`, `fmla_eligible`, `workers_eligible`, `timezone`, `EFFDT`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `active`, `leave_calendar`) values('5011', 'testUser12', 'BWS-CAL', 'la-lose-mac', '2012-03-10', 'Y', 'Y', null, '2012-03-10', now(), uuid(), '1', 'Y', 'LM');

insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5000', 'od-xfer', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC1', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5001', 'ye-xfer', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC2', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5002', 'la-xfer', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC3', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5003', 'od-xfer-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC4', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5004', 'ye-xfer-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC5', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5005', 'la-xfer-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC6', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5006', 'od-lose', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC7', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5007', 'ye-lose', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC8', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5008', 'la-lose', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC9', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5009', 'od-lose-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC10', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5010', 'ye-lose-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC11', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('5011', 'la-lose-mac', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC12', 'Y');

insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6001', 'od-xfer-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC1a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6002', 'ye-xfer-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC2a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6003', 'la-xfer-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC3a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6004', 'od-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC4a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6005', 'ye-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC5a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6006', 'la-xfer-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC6a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6007', 'od-lose-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC7a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6008', 'ye-lose-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC8a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6009', 'la-lose-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC9a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6010', 'od-lose-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC10a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6011', 'ye-lose-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC11a', 'Y');
insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('6012', 'la-lose-mac-dep', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC12a', 'Y');

insert into lm_accrual_category_t (`lm_accrual_category_id`, `ACCRUAL_CATEGORY`, `LEAVE_PLAN`, `DESCR`, `ACCRUAL_INTERVAL_EARN`, `UNIT_OF_TIME`, `EFFDT`, `OBJ_ID`, `VER_NBR`, `PRORATION`, `DONATION`, `SHOW_ON_GRID`, `ACTIVE`, `TIMESTAMP`, `MIN_PERCENT_WORKED`, `EARN_CODE`, `HAS_RULES`) values('7000', 'ye-xfer-eo', 'testLP', 'test', 'M', '40', '2012-03-01', '8421CD29-E1F4-4B9A-AE33-F3F4752505CE', '1', 'N', null, 'Y', 'Y',now(), '0', 'EC13', 'Y');

insert into hr_job_t (`hr_job_id`, `PRINCIPAL_ID`, `JOB_NUMBER`, `EFFDT`, `dept`, `HR_SAL_GROUP`, `pay_grade`, `TIMESTAMP`, `OBJ_ID`, `VER_NBR`, `comp_rate`, `location`, `std_hours`, `hr_paytype`, `active`, `primary_indicator`, `position_nbr`, `eligible_for_leave`, `FLSA_STATUS`) values ('5000', 'testUser1', '19', '2012-03-01', 'TEST-DEPT', 'SD1', 'SD1', now(), uuid(), '1', '0.000000', 'SD1', '40.00', 'BW', 'Y',  'Y', 'N', 'Y', null);

INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5000', 'testUser1', '2012-12-01 00:00:00','2013-01-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E97', '1');
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5001', 'testUser1', '2013-01-01 00:00:00','2013-02-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E98', '1');
INSERT INTO lm_leave_document_header_t (`document_id`,`principal_id`,`begin_date`,`end_date`,`document_status`,`obj_id`,`ver_nbr`) values ('5002', 'testUser1', '2012-11-01 00:00:00','2012-12-01 00:00:00', 'S', '7EE387AB-26B0-B6A6-9C4C-5B5F687F0E96', '1');

insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5000','3','BWS-LM','2012-11-01 00:00:00','2012-12-01 00:00:00');
insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5001','3','BWS-LM','2012-12-01 00:00:00','2013-01-01 00:00:00');
insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`,`calendar_name`,`begin_period_date`,`end_period_date`) values ('5002','3','BWS-LM','2013-01-01 00:00:00','2013-02-01 00:00:00');

insert into hr_earn_code_t values('5001', 'EC1', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5002', 'EC2', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5003', 'EC3', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5004', 'EC4', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5005', 'EC5', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5006', 'EC6', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5007', 'EC7', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5008', 'EC8', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5009', 'EC9', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5010', 'EC10', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5011', 'EC11', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('5012', 'EC12', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-mac', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');

insert into hr_earn_code_t values('6001', 'EC1a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6002', 'EC2a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6003', 'EC3a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6004', 'EC4a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6005', 'EC5a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6006', 'EC6a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-xfer-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6007', 'EC7a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6008', 'EC8a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6009', 'EC9a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6010', 'EC10a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'od-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6011', 'EC11a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');
insert into hr_earn_code_t values('6012', 'EC12a', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'la-lose-mac-dep', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');

insert into hr_earn_code_t values('7000', 'EC13', 'test', '2012-02-01', 'Y', 'Y', 'B2991ADA-E866-F28C-7E95-A897AC377D0C', '1', now(), 'ye-xfer-eo', '1.5', '1.5', 'Hours', 'testLP', 'None', '99', 'T', 'N', 'Y', 'Y', 'Y', 'Y', 'test', null, 'N', 'I');