ALTER TABLE `hr_job_t` ADD COLUMN `eligible_for_leave` VARCHAR(1)  DEFAULT 'N' AFTER `position_nbr`;