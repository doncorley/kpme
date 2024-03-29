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

delete from lm_leave_block_t where lm_leave_block_id >= '1000';
-- delete from hr_calendar_entries_t where hr_calendar_entry_id = '50001';
-- delete from hr_calendar_entries_t where hr_calendar_id = 2 and calendar_name = 'BWS-CAL' and begin_period_date = '2012-03-01 00:00:00' and end_period_date = '2012-03-15 00:00:00';

insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type` ) values('1000', '2012-03-01', 'Test Description', 'admin', 'testLC', '3000', 'testAC', '8', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin', 'P', 'LC');
insert into lm_leave_block_t (`lm_leave_block_id`, `leave_date`,`description`,  `principal_id` , `earn_code` ,`lm_sys_schd_timeoff_id` ,`accrual_category` ,`leave_amount`, `document_id`, `accrual_generated`, `block_id`,`ver_nbr`, `obj_id`, `timestamp`,`principal_id_modified`, `request_status`, `leave_block_type` ) values('1001', '2012-03-02', 'Test Description', 'admin', 'testLC', '3000', 'testAC', '8', '12546', 'A', '0','1','B2991ADA-E866-F28C-7E95-A897AC377D0C', '2010-01-01 08:08:08', 'admin', 'P', 'LC');

-- insert into hr_calendar_entries_t (`hr_calendar_entry_id`,`hr_calendar_id`, `calendar_name`, `begin_period_date`, `end_period_date`) values ('50001', '2', 'BWS-CAL', '2012-03-01 00:00:00', '2012-03-15 00:00:00');