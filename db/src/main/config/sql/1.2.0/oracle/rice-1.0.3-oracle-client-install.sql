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

-----------------------------------------------------------------------------
-- KRNS_ADHOC_RTE_ACTN_RECIP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_ADHOC_RTE_ACTN_RECIP_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T
(
      RECIP_TYP_CD NUMBER(1)
        , ACTN_RQST_CD VARCHAR2(30)
        , ACTN_RQST_RECIP_ID VARCHAR2(70)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_HDR_ID VARCHAR2(14)
    
    , CONSTRAINT KRNS_ADHOC_RTE_ACTN_RECIP_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T
    ADD CONSTRAINT KRNS_ADHOC_RTE_ACTN_RECIP_TP1
PRIMARY KEY (RECIP_TYP_CD,ACTN_RQST_CD,ACTN_RQST_RECIP_ID,DOC_HDR_ID)
/


CREATE INDEX KRNS_ADHOC_RTE_ACTN_RECIP_T2 
  ON KRNS_ADHOC_RTE_ACTN_RECIP_T 
  (DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_ATT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_ATT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_ATT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_ATT_T
(
      NTE_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , MIME_TYP VARCHAR2(255)
        , FILE_NM VARCHAR2(250)
        , ATT_ID VARCHAR2(36)
        , FILE_SZ NUMBER(14)
        , ATT_TYP_CD VARCHAR2(2)
    
    , CONSTRAINT KRNS_ATT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_ATT_T
    ADD CONSTRAINT KRNS_ATT_TP1
PRIMARY KEY (NTE_ID)
/







-----------------------------------------------------------------------------
-- KRNS_DOC_HDR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_DOC_HDR_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_DOC_HDR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_DOC_HDR_T
(
      DOC_HDR_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , FDOC_DESC VARCHAR2(40)
        , ORG_DOC_HDR_ID VARCHAR2(10)
        , TMPL_DOC_HDR_ID VARCHAR2(14)
        , EXPLANATION VARCHAR2(400)
    
    , CONSTRAINT KRNS_DOC_HDR_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_DOC_HDR_T
    ADD CONSTRAINT KRNS_DOC_HDR_TP1
PRIMARY KEY (DOC_HDR_ID)
/


CREATE INDEX KRNS_DOC_HDR_TI3 
  ON KRNS_DOC_HDR_T 
  (ORG_DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_LOOKUP_RSLT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_LOOKUP_RSLT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_LOOKUP_RSLT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_LOOKUP_RSLT_T
(
      LOOKUP_RSLT_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
        , LOOKUP_DT DATE NOT NULL
        , SERIALZD_RSLTS CLOB
    
    , CONSTRAINT KRNS_LOOKUP_RSLT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_LOOKUP_RSLT_T
    ADD CONSTRAINT KRNS_LOOKUP_RSLT_TP1
PRIMARY KEY (LOOKUP_RSLT_ID)
/







-----------------------------------------------------------------------------
-- KRNS_LOOKUP_SEL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_LOOKUP_SEL_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_LOOKUP_SEL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_LOOKUP_SEL_T
(
      LOOKUP_RSLT_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
        , LOOKUP_DT DATE NOT NULL
        , SEL_OBJ_IDS CLOB
    
    , CONSTRAINT KRNS_LOOKUP_SEL_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_LOOKUP_SEL_T
    ADD CONSTRAINT KRNS_LOOKUP_SEL_TP1
PRIMARY KEY (LOOKUP_RSLT_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_DOC_ATT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_DOC_ATT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_DOC_ATT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_DOC_ATT_T
(
      DOC_HDR_ID VARCHAR2(14)
        , ATT_CNTNT BLOB NOT NULL
        , FILE_NM VARCHAR2(150)
        , CNTNT_TYP VARCHAR2(255)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT KRNS_MAINT_DOC_ATT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_DOC_ATT_T
    ADD CONSTRAINT KRNS_MAINT_DOC_ATT_TP1
PRIMARY KEY (DOC_HDR_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_DOC_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_DOC_T
(
      DOC_HDR_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_CNTNT CLOB
    
    , CONSTRAINT KRNS_MAINT_DOC_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_DOC_T
    ADD CONSTRAINT KRNS_MAINT_DOC_TP1
PRIMARY KEY (DOC_HDR_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_LOCK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_LOCK_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_LOCK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_LOCK_T
(
      MAINT_LOCK_REP_TXT VARCHAR2(500)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_HDR_ID VARCHAR2(14) NOT NULL
        , MAINT_LOCK_ID VARCHAR2(14)
    
    , CONSTRAINT KRNS_MAINT_LOCK_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_LOCK_T
    ADD CONSTRAINT KRNS_MAINT_LOCK_TP1
PRIMARY KEY (MAINT_LOCK_ID)
/


CREATE INDEX KRNS_MAINT_LOCK_TI2 
  ON KRNS_MAINT_LOCK_T 
  (DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_NTE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_NTE_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_NTE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_NTE_T
(
      NTE_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , RMT_OBJ_ID VARCHAR2(36) NOT NULL
        , AUTH_PRNCPL_ID VARCHAR2(40) NOT NULL
        , POST_TS DATE NOT NULL
        , NTE_TYP_CD VARCHAR2(4) NOT NULL
        , TXT VARCHAR2(800)
        , PRG_CD VARCHAR2(1)
        , TPC_TXT VARCHAR2(40)
    
    , CONSTRAINT KRNS_NTE_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_NTE_T
    ADD CONSTRAINT KRNS_NTE_TP1
PRIMARY KEY (NTE_ID)
/







-----------------------------------------------------------------------------
-- KRNS_NTE_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_NTE_TYP_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_NTE_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_NTE_TYP_T
(
      NTE_TYP_CD VARCHAR2(4)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , TYP_DESC_TXT VARCHAR2(100)
        , ACTV_IND VARCHAR2(1)
    
    , CONSTRAINT KRNS_NTE_TYP_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_NTE_TYP_T
    ADD CONSTRAINT KRNS_NTE_TYP_TP1
PRIMARY KEY (NTE_TYP_CD)
/







-----------------------------------------------------------------------------
-- KRNS_PESSIMISTIC_LOCK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_PESSIMISTIC_LOCK_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_PESSIMISTIC_LOCK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_PESSIMISTIC_LOCK_T
(
      PESSIMISTIC_LOCK_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOCK_DESC_TXT VARCHAR2(4000)
        , DOC_HDR_ID VARCHAR2(14) NOT NULL
        , GNRT_DT DATE NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
    
    , CONSTRAINT KRNS_PESSIMISTIC_LOCK_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_PESSIMISTIC_LOCK_T
    ADD CONSTRAINT KRNS_PESSIMISTIC_LOCK_TP1
PRIMARY KEY (PESSIMISTIC_LOCK_ID)
/


CREATE INDEX KRNS_PESSIMISTIC_LOCK_TI1 
  ON KRNS_PESSIMISTIC_LOCK_T 
  (DOC_HDR_ID)
/
CREATE INDEX KRNS_PESSIMISTIC_LOCK_TI2 
  ON KRNS_PESSIMISTIC_LOCK_T 
  (PRNCPL_ID)
/





-----------------------------------------------------------------------------
-- KRNS_SESN_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_SESN_DOC_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_SESN_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_SESN_DOC_T
(
      SESN_DOC_ID VARCHAR2(40)
        , DOC_HDR_ID VARCHAR2(14)
        , PRNCPL_ID VARCHAR2(40)
        , IP_ADDR VARCHAR2(60)
        , SERIALZD_DOC_FRM BLOB
        , LAST_UPDT_DT DATE
        , CONTENT_ENCRYPTED_IND CHAR(1) default 'N'
    

)
/

ALTER TABLE KRNS_SESN_DOC_T
    ADD CONSTRAINT KRNS_SESN_DOC_TP1
PRIMARY KEY (SESN_DOC_ID,DOC_HDR_ID,PRNCPL_ID,IP_ADDR)
/


CREATE INDEX KRNS_SESN_DOC_TI1 
  ON KRNS_SESN_DOC_T 
  (LAST_UPDT_DT)
/





-----------------------------------------------------------------------------
-- KRSB_BAM_PARM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_BAM_PARM_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_BAM_PARM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_BAM_PARM_T
(
      BAM_PARM_ID NUMBER(14)
        , BAM_ID NUMBER(14) NOT NULL
        , PARM CLOB NOT NULL
    

)
/

ALTER TABLE KRSB_BAM_PARM_T
    ADD CONSTRAINT KRSB_BAM_PARM_TP1
PRIMARY KEY (BAM_PARM_ID)
/


CREATE INDEX KREW_BAM_PARM_TI1 
  ON KRSB_BAM_PARM_T 
  (BAM_ID)
/





-----------------------------------------------------------------------------
-- KRSB_BAM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_BAM_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_BAM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_BAM_T
(
      BAM_ID NUMBER(14)
        , SVC_NM VARCHAR2(255) NOT NULL
        , SVC_URL VARCHAR2(500) NOT NULL
        , MTHD_NM VARCHAR2(2000) NOT NULL
        , THRD_NM VARCHAR2(500) NOT NULL
        , CALL_DT DATE NOT NULL
        , TGT_TO_STR VARCHAR2(2000) NOT NULL
        , SRVR_IND NUMBER(1) NOT NULL
        , EXCPN_TO_STR VARCHAR2(2000)
        , EXCPN_MSG CLOB
    

)
/

ALTER TABLE KRSB_BAM_T
    ADD CONSTRAINT KRSB_BAM_TP1
PRIMARY KEY (BAM_ID)
/


CREATE INDEX KRSB_BAM_TI1 
  ON KRSB_BAM_T 
  (SVC_NM, MTHD_NM)
/
CREATE INDEX KRSB_BAM_TI2 
  ON KRSB_BAM_T 
  (SVC_NM)
/





-----------------------------------------------------------------------------
-- KRSB_MSG_PYLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_MSG_PYLD_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_MSG_PYLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_MSG_PYLD_T
(
      MSG_QUE_ID NUMBER(14)
        , MSG_PYLD CLOB NOT NULL
    

)
/

ALTER TABLE KRSB_MSG_PYLD_T
    ADD CONSTRAINT KRSB_MSG_PYLD_TP1
PRIMARY KEY (MSG_QUE_ID)
/







-----------------------------------------------------------------------------
-- KRSB_MSG_QUE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_MSG_QUE_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_MSG_QUE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_MSG_QUE_T
(
      MSG_QUE_ID NUMBER(14)
        , DT DATE NOT NULL
        , EXP_DT DATE
        , PRIO NUMBER(8) NOT NULL
        , STAT_CD CHAR(1) NOT NULL
        , RTRY_CNT NUMBER(8) NOT NULL
        , IP_NBR VARCHAR2(2000) NOT NULL
        , SVC_NM VARCHAR2(255)
        , SVC_NMSPC VARCHAR2(255) NOT NULL
        , SVC_MTHD_NM VARCHAR2(2000)
        , APP_VAL_ONE VARCHAR2(2000)
        , APP_VAL_TWO VARCHAR2(2000)
        , VER_NBR NUMBER(8) default 0
    

)
/

ALTER TABLE KRSB_MSG_QUE_T
    ADD CONSTRAINT KRSB_MSG_QUE_TP1
PRIMARY KEY (MSG_QUE_ID)
/


CREATE INDEX KRSB_MSG_QUE_TI1 
  ON KRSB_MSG_QUE_T 
  (SVC_NM, SVC_MTHD_NM)
/
CREATE INDEX KRSB_MSG_QUE_TI2 
  ON KRSB_MSG_QUE_T 
  (SVC_NMSPC, STAT_CD, IP_NBR, DT)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_BLOB_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_BLOB_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_BLOB_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_BLOB_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , BLOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_BLOB_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_BLOB_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_CALENDARS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_CALENDARS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_CALENDARS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_CALENDARS
(
      CALENDAR_NAME VARCHAR2(80)
        , CALENDAR BLOB NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_CALENDARS
    ADD CONSTRAINT KRSB_QRTZ_CALENDARSP1
PRIMARY KEY (CALENDAR_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_CRON_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_CRON_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_CRON_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_CRON_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , CRON_EXPRESSION VARCHAR2(80) NOT NULL
        , TIME_ZONE_ID VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_CRON_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_CRON_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_FIRED_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_FIRED_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_FIRED_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_FIRED_TRIGGERS
(
      ENTRY_ID VARCHAR2(95)
        , TRIGGER_NAME VARCHAR2(80) NOT NULL
        , TRIGGER_GROUP VARCHAR2(80) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , INSTANCE_NAME VARCHAR2(80) NOT NULL
        , FIRED_TIME NUMBER(13) NOT NULL
        , PRIORITY NUMBER(13) NOT NULL
        , STATE VARCHAR2(16) NOT NULL
        , JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , IS_STATEFUL VARCHAR2(1)
        , REQUESTS_RECOVERY VARCHAR2(1)
    

)
/

ALTER TABLE KRSB_QRTZ_FIRED_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_FIRED_TRIGGERSP1
PRIMARY KEY (ENTRY_ID)
/


CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI1 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (JOB_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI2 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (JOB_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI3 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (REQUESTS_RECOVERY)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI4 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (IS_STATEFUL)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI5 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI6 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (INSTANCE_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI7 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI8 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_NAME, TRIGGER_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI9 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (IS_VOLATILE)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_JOB_DETAILS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_JOB_DETAILS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_JOB_DETAILS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_JOB_DETAILS
(
      JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , DESCRIPTION VARCHAR2(120)
        , JOB_CLASS_NAME VARCHAR2(128) NOT NULL
        , IS_DURABLE VARCHAR2(1) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , IS_STATEFUL VARCHAR2(1) NOT NULL
        , REQUESTS_RECOVERY VARCHAR2(1) NOT NULL
        , JOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_JOB_DETAILS
    ADD CONSTRAINT KRSB_QRTZ_JOB_DETAILSP1
PRIMARY KEY (JOB_NAME,JOB_GROUP)
/


CREATE INDEX KRSB_QRTZ_JOB_DETAILS_TI1 
  ON KRSB_QRTZ_JOB_DETAILS 
  (REQUESTS_RECOVERY)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_JOB_LISTENERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_JOB_LISTENERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_JOB_LISTENERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_JOB_LISTENERS
(
      JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , JOB_LISTENER VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_JOB_LISTENERS
    ADD CONSTRAINT KRSB_QRTZ_JOB_LISTENERSP1
PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_LOCKS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_LOCKS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_LOCKS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_LOCKS
(
      LOCK_NAME VARCHAR2(40)
    

)
/

ALTER TABLE KRSB_QRTZ_LOCKS
    ADD CONSTRAINT KRSB_QRTZ_LOCKSP1
PRIMARY KEY (LOCK_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_PAUSED_TRIGGER_GRPS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_PAUSED_TRIGGER_GRPS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS
(
      TRIGGER_GROUP VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS
    ADD CONSTRAINT KRSB_QRTZ_PAUSED_TRIGGER_GRP1
PRIMARY KEY (TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_SCHEDULER_STATE
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_SCHEDULER_STATE';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_SCHEDULER_STATE CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_SCHEDULER_STATE
(
      INSTANCE_NAME VARCHAR2(80)
        , LAST_CHECKIN_TIME NUMBER(13) NOT NULL
        , CHECKIN_INTERVAL NUMBER(13) NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_SCHEDULER_STATE
    ADD CONSTRAINT KRSB_QRTZ_SCHEDULER_STATEP1
PRIMARY KEY (INSTANCE_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_SIMPLE_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_SIMPLE_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_SIMPLE_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_SIMPLE_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , REPEAT_COUNT NUMBER(7) NOT NULL
        , REPEAT_INTERVAL NUMBER(12) NOT NULL
        , TIMES_TRIGGERED NUMBER(7) NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_SIMPLE_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_SIMPLE_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , JOB_NAME VARCHAR2(80) NOT NULL
        , JOB_GROUP VARCHAR2(80) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , DESCRIPTION VARCHAR2(120)
        , NEXT_FIRE_TIME NUMBER(13)
        , PREV_FIRE_TIME NUMBER(13)
        , PRIORITY NUMBER(13)
        , TRIGGER_STATE VARCHAR2(16) NOT NULL
        , TRIGGER_TYPE VARCHAR2(8) NOT NULL
        , START_TIME NUMBER(13) NOT NULL
        , END_TIME NUMBER(13)
        , CALENDAR_NAME VARCHAR2(80)
        , MISFIRE_INSTR NUMBER(2)
        , JOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/


CREATE INDEX KRSB_QRTZ_TRIGGERS_TI1 
  ON KRSB_QRTZ_TRIGGERS 
  (NEXT_FIRE_TIME)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI2 
  ON KRSB_QRTZ_TRIGGERS 
  (NEXT_FIRE_TIME, TRIGGER_STATE)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI3 
  ON KRSB_QRTZ_TRIGGERS 
  (TRIGGER_STATE)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI4 
  ON KRSB_QRTZ_TRIGGERS 
  (IS_VOLATILE)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_TRIGGER_LISTENERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_TRIGGER_LISTENERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_TRIGGER_LISTENERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_TRIGGER_LISTENERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , TRIGGER_LISTENER VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_TRIGGER_LISTENERS
    ADD CONSTRAINT KRSB_QRTZ_TRIGGER_LISTENERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER)
/







-----------------------------------------------------------------------------
-- KR_QRTZ_PAUSED_TRIGGERS_GRPS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KR_QRTZ_PAUSED_TRIGGERS_GRPS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS
(
      TRIGGER_GROUP VARCHAR2(80)
    

)
/

ALTER TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS
    ADD CONSTRAINT KR_QRTZ_PAUSED_TRIGGERS_GRPP1
PRIMARY KEY (TRIGGER_GROUP)
/






DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_LOCK_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_LOCK_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_LOCK_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_LOOKUP_RSLT_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_LOOKUP_RSLT_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_LOOKUP_RSLT_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_MAINT_LOCK_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_MAINT_LOCK_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_MAINT_LOCK_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_NTE_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_NTE_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_NTE_S INCREMENT BY 1 START WITH 2020 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_BAM_PARM_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_BAM_PARM_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_BAM_PARM_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_BAM_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_BAM_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_BAM_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_MSG_QUE_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_MSG_QUE_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_MSG_QUE_S INCREMENT BY 1 START WITH 121 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'RECIPES_SEQ';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE RECIPES_SEQ'; END IF;
END;
/

--
-- KRNS_NTE_TYP_T
--


INSERT INTO KRNS_NTE_TYP_T (ACTV_IND,NTE_TYP_CD,OBJ_ID,TYP_DESC_TXT,VER_NBR)
  VALUES ('Y','BO','53680C68F5A9AD9BE0404F8189D80A6C','DOCUMENT BUSINESS OBJECT',1)
/
INSERT INTO KRNS_NTE_TYP_T (ACTV_IND,NTE_TYP_CD,OBJ_ID,TYP_DESC_TXT,VER_NBR)
  VALUES ('Y','DH','53680C68F5AAAD9BE0404F8189D80A6C','DOCUMENT HEADER',1)
/

--
-- KRSB_QRTZ_LOCKS
--


INSERT INTO KRSB_QRTZ_LOCKS (LOCK_NAME)
  VALUES ('CALENDAR_ACCESS')
/
INSERT INTO KRSB_QRTZ_LOCKS (LOCK_NAME)
  VALUES ('JOB_ACCESS')
/
INSERT INTO KRSB_QRTZ_LOCKS (LOCK_NAME)
  VALUES ('MISFIRE_ACCESS')
/
INSERT INTO KRSB_QRTZ_LOCKS (LOCK_NAME)
  VALUES ('STATE_ACCESS')
/
INSERT INTO KRSB_QRTZ_LOCKS (LOCK_NAME)
  VALUES ('TRIGGER_ACCESS')
/

-----------------------------------------------------------------------------
-- KRNS_ADHOC_RTE_ACTN_RECIP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_ADHOC_RTE_ACTN_RECIP_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T
(
      RECIP_TYP_CD NUMBER(1)
        , ACTN_RQST_CD VARCHAR2(30)
        , ACTN_RQST_RECIP_ID VARCHAR2(70)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_HDR_ID VARCHAR2(14)
    
    , CONSTRAINT KRNS_ADHOC_RTE_ACTN_RECIP_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_ADHOC_RTE_ACTN_RECIP_T
    ADD CONSTRAINT KRNS_ADHOC_RTE_ACTN_RECIP_TP1
PRIMARY KEY (RECIP_TYP_CD,ACTN_RQST_CD,ACTN_RQST_RECIP_ID,DOC_HDR_ID)
/


CREATE INDEX KRNS_ADHOC_RTE_ACTN_RECIP_T2 
  ON KRNS_ADHOC_RTE_ACTN_RECIP_T 
  (DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_ATT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_ATT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_ATT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_ATT_T
(
      NTE_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , MIME_TYP VARCHAR2(255)
        , FILE_NM VARCHAR2(250)
        , ATT_ID VARCHAR2(36)
        , FILE_SZ NUMBER(14)
        , ATT_TYP_CD VARCHAR2(2)
    
    , CONSTRAINT KRNS_ATT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_ATT_T
    ADD CONSTRAINT KRNS_ATT_TP1
PRIMARY KEY (NTE_ID)
/







-----------------------------------------------------------------------------
-- KRNS_DOC_HDR_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_DOC_HDR_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_DOC_HDR_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_DOC_HDR_T
(
      DOC_HDR_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , FDOC_DESC VARCHAR2(40)
        , ORG_DOC_HDR_ID VARCHAR2(10)
        , TMPL_DOC_HDR_ID VARCHAR2(14)
        , EXPLANATION VARCHAR2(400)
    
    , CONSTRAINT KRNS_DOC_HDR_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_DOC_HDR_T
    ADD CONSTRAINT KRNS_DOC_HDR_TP1
PRIMARY KEY (DOC_HDR_ID)
/


CREATE INDEX KRNS_DOC_HDR_TI3 
  ON KRNS_DOC_HDR_T 
  (ORG_DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_LOOKUP_RSLT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_LOOKUP_RSLT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_LOOKUP_RSLT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_LOOKUP_RSLT_T
(
      LOOKUP_RSLT_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
        , LOOKUP_DT DATE NOT NULL
        , SERIALZD_RSLTS CLOB
    
    , CONSTRAINT KRNS_LOOKUP_RSLT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_LOOKUP_RSLT_T
    ADD CONSTRAINT KRNS_LOOKUP_RSLT_TP1
PRIMARY KEY (LOOKUP_RSLT_ID)
/







-----------------------------------------------------------------------------
-- KRNS_LOOKUP_SEL_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_LOOKUP_SEL_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_LOOKUP_SEL_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_LOOKUP_SEL_T
(
      LOOKUP_RSLT_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
        , LOOKUP_DT DATE NOT NULL
        , SEL_OBJ_IDS CLOB
    
    , CONSTRAINT KRNS_LOOKUP_SEL_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_LOOKUP_SEL_T
    ADD CONSTRAINT KRNS_LOOKUP_SEL_TP1
PRIMARY KEY (LOOKUP_RSLT_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_DOC_ATT_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_DOC_ATT_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_DOC_ATT_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_DOC_ATT_T
(
      DOC_HDR_ID VARCHAR2(14)
        , ATT_CNTNT BLOB NOT NULL
        , FILE_NM VARCHAR2(150)
        , CNTNT_TYP VARCHAR2(255)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
    
    , CONSTRAINT KRNS_MAINT_DOC_ATT_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_DOC_ATT_T
    ADD CONSTRAINT KRNS_MAINT_DOC_ATT_TP1
PRIMARY KEY (DOC_HDR_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_DOC_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_DOC_T
(
      DOC_HDR_ID VARCHAR2(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_CNTNT CLOB
    
    , CONSTRAINT KRNS_MAINT_DOC_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_DOC_T
    ADD CONSTRAINT KRNS_MAINT_DOC_TP1
PRIMARY KEY (DOC_HDR_ID)
/







-----------------------------------------------------------------------------
-- KRNS_MAINT_LOCK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_MAINT_LOCK_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_MAINT_LOCK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_MAINT_LOCK_T
(
      MAINT_LOCK_REP_TXT VARCHAR2(500)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , DOC_HDR_ID VARCHAR2(14) NOT NULL
        , MAINT_LOCK_ID VARCHAR2(14)
    
    , CONSTRAINT KRNS_MAINT_LOCK_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_MAINT_LOCK_T
    ADD CONSTRAINT KRNS_MAINT_LOCK_TP1
PRIMARY KEY (MAINT_LOCK_ID)
/


CREATE INDEX KRNS_MAINT_LOCK_TI2 
  ON KRNS_MAINT_LOCK_T 
  (DOC_HDR_ID)
/





-----------------------------------------------------------------------------
-- KRNS_NTE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_NTE_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_NTE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_NTE_T
(
      NTE_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , RMT_OBJ_ID VARCHAR2(36) NOT NULL
        , AUTH_PRNCPL_ID VARCHAR2(40) NOT NULL
        , POST_TS DATE NOT NULL
        , NTE_TYP_CD VARCHAR2(4) NOT NULL
        , TXT VARCHAR2(800)
        , PRG_CD VARCHAR2(1)
        , TPC_TXT VARCHAR2(40)
    
    , CONSTRAINT KRNS_NTE_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_NTE_T
    ADD CONSTRAINT KRNS_NTE_TP1
PRIMARY KEY (NTE_ID)
/







-----------------------------------------------------------------------------
-- KRNS_NTE_TYP_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_NTE_TYP_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_NTE_TYP_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_NTE_TYP_T
(
      NTE_TYP_CD VARCHAR2(4)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , TYP_DESC_TXT VARCHAR2(100)
        , ACTV_IND VARCHAR2(1)
    
    , CONSTRAINT KRNS_NTE_TYP_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_NTE_TYP_T
    ADD CONSTRAINT KRNS_NTE_TYP_TP1
PRIMARY KEY (NTE_TYP_CD)
/







-----------------------------------------------------------------------------
-- KRNS_PESSIMISTIC_LOCK_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_PESSIMISTIC_LOCK_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_PESSIMISTIC_LOCK_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_PESSIMISTIC_LOCK_T
(
      PESSIMISTIC_LOCK_ID NUMBER(14)
        , OBJ_ID VARCHAR2(36) default SYS_GUID() NOT NULL
        , VER_NBR NUMBER(8) default 1 NOT NULL
        , LOCK_DESC_TXT VARCHAR2(4000)
        , DOC_HDR_ID VARCHAR2(14) NOT NULL
        , GNRT_DT DATE NOT NULL
        , PRNCPL_ID VARCHAR2(40) NOT NULL
    
    , CONSTRAINT KRNS_PESSIMISTIC_LOCK_TC0 UNIQUE (OBJ_ID)

)
/

ALTER TABLE KRNS_PESSIMISTIC_LOCK_T
    ADD CONSTRAINT KRNS_PESSIMISTIC_LOCK_TP1
PRIMARY KEY (PESSIMISTIC_LOCK_ID)
/


CREATE INDEX KRNS_PESSIMISTIC_LOCK_TI1 
  ON KRNS_PESSIMISTIC_LOCK_T 
  (DOC_HDR_ID)
/
CREATE INDEX KRNS_PESSIMISTIC_LOCK_TI2 
  ON KRNS_PESSIMISTIC_LOCK_T 
  (PRNCPL_ID)
/





-----------------------------------------------------------------------------
-- KRNS_SESN_DOC_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRNS_SESN_DOC_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRNS_SESN_DOC_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRNS_SESN_DOC_T
(
      SESN_DOC_ID VARCHAR2(40)
        , DOC_HDR_ID VARCHAR2(14)
        , PRNCPL_ID VARCHAR2(40)
        , IP_ADDR VARCHAR2(60)
        , SERIALZD_DOC_FRM BLOB
        , LAST_UPDT_DT DATE
        , CONTENT_ENCRYPTED_IND CHAR(1) default 'N'
    

)
/

ALTER TABLE KRNS_SESN_DOC_T
    ADD CONSTRAINT KRNS_SESN_DOC_TP1
PRIMARY KEY (SESN_DOC_ID,DOC_HDR_ID,PRNCPL_ID,IP_ADDR)
/


CREATE INDEX KRNS_SESN_DOC_TI1 
  ON KRNS_SESN_DOC_T 
  (LAST_UPDT_DT)
/





-----------------------------------------------------------------------------
-- KRSB_BAM_PARM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_BAM_PARM_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_BAM_PARM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_BAM_PARM_T
(
      BAM_PARM_ID NUMBER(14)
        , BAM_ID NUMBER(14) NOT NULL
        , PARM CLOB NOT NULL
    

)
/

ALTER TABLE KRSB_BAM_PARM_T
    ADD CONSTRAINT KRSB_BAM_PARM_TP1
PRIMARY KEY (BAM_PARM_ID)
/


CREATE INDEX KREW_BAM_PARM_TI1 
  ON KRSB_BAM_PARM_T 
  (BAM_ID)
/





-----------------------------------------------------------------------------
-- KRSB_BAM_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_BAM_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_BAM_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_BAM_T
(
      BAM_ID NUMBER(14)
        , SVC_NM VARCHAR2(255) NOT NULL
        , SVC_URL VARCHAR2(500) NOT NULL
        , MTHD_NM VARCHAR2(2000) NOT NULL
        , THRD_NM VARCHAR2(500) NOT NULL
        , CALL_DT DATE NOT NULL
        , TGT_TO_STR VARCHAR2(2000) NOT NULL
        , SRVR_IND NUMBER(1) NOT NULL
        , EXCPN_TO_STR VARCHAR2(2000)
        , EXCPN_MSG CLOB
    

)
/

ALTER TABLE KRSB_BAM_T
    ADD CONSTRAINT KRSB_BAM_TP1
PRIMARY KEY (BAM_ID)
/


CREATE INDEX KRSB_BAM_TI1 
  ON KRSB_BAM_T 
  (SVC_NM, MTHD_NM)
/
CREATE INDEX KRSB_BAM_TI2 
  ON KRSB_BAM_T 
  (SVC_NM)
/





-----------------------------------------------------------------------------
-- KRSB_MSG_PYLD_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_MSG_PYLD_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_MSG_PYLD_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_MSG_PYLD_T
(
      MSG_QUE_ID NUMBER(14)
        , MSG_PYLD CLOB NOT NULL
    

)
/

ALTER TABLE KRSB_MSG_PYLD_T
    ADD CONSTRAINT KRSB_MSG_PYLD_TP1
PRIMARY KEY (MSG_QUE_ID)
/







-----------------------------------------------------------------------------
-- KRSB_MSG_QUE_T
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_MSG_QUE_T';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_MSG_QUE_T CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_MSG_QUE_T
(
      MSG_QUE_ID NUMBER(14)
        , DT DATE NOT NULL
        , EXP_DT DATE
        , PRIO NUMBER(8) NOT NULL
        , STAT_CD CHAR(1) NOT NULL
        , RTRY_CNT NUMBER(8) NOT NULL
        , IP_NBR VARCHAR2(2000) NOT NULL
        , SVC_NM VARCHAR2(255)
        , SVC_NMSPC VARCHAR2(255) NOT NULL
        , SVC_MTHD_NM VARCHAR2(2000)
        , APP_VAL_ONE VARCHAR2(2000)
        , APP_VAL_TWO VARCHAR2(2000)
        , VER_NBR NUMBER(8) default 0
    

)
/

ALTER TABLE KRSB_MSG_QUE_T
    ADD CONSTRAINT KRSB_MSG_QUE_TP1
PRIMARY KEY (MSG_QUE_ID)
/


CREATE INDEX KRSB_MSG_QUE_TI1 
  ON KRSB_MSG_QUE_T 
  (SVC_NM, SVC_MTHD_NM)
/
CREATE INDEX KRSB_MSG_QUE_TI2 
  ON KRSB_MSG_QUE_T 
  (SVC_NMSPC, STAT_CD, IP_NBR, DT)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_BLOB_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_BLOB_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_BLOB_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_BLOB_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , BLOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_BLOB_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_BLOB_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_CALENDARS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_CALENDARS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_CALENDARS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_CALENDARS
(
      CALENDAR_NAME VARCHAR2(80)
        , CALENDAR BLOB NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_CALENDARS
    ADD CONSTRAINT KRSB_QRTZ_CALENDARSP1
PRIMARY KEY (CALENDAR_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_CRON_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_CRON_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_CRON_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_CRON_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , CRON_EXPRESSION VARCHAR2(80) NOT NULL
        , TIME_ZONE_ID VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_CRON_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_CRON_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_FIRED_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_FIRED_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_FIRED_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_FIRED_TRIGGERS
(
      ENTRY_ID VARCHAR2(95)
        , TRIGGER_NAME VARCHAR2(80) NOT NULL
        , TRIGGER_GROUP VARCHAR2(80) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , INSTANCE_NAME VARCHAR2(80) NOT NULL
        , FIRED_TIME NUMBER(13) NOT NULL
        , PRIORITY NUMBER(13) NOT NULL
        , STATE VARCHAR2(16) NOT NULL
        , JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , IS_STATEFUL VARCHAR2(1)
        , REQUESTS_RECOVERY VARCHAR2(1)
    

)
/

ALTER TABLE KRSB_QRTZ_FIRED_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_FIRED_TRIGGERSP1
PRIMARY KEY (ENTRY_ID)
/


CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI1 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (JOB_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI2 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (JOB_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI3 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (REQUESTS_RECOVERY)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI4 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (IS_STATEFUL)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI5 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI6 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (INSTANCE_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI7 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_NAME)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI8 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (TRIGGER_NAME, TRIGGER_GROUP)
/
CREATE INDEX KRSB_QRTZ_FIRED_TRIGGERS_TI9 
  ON KRSB_QRTZ_FIRED_TRIGGERS 
  (IS_VOLATILE)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_JOB_DETAILS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_JOB_DETAILS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_JOB_DETAILS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_JOB_DETAILS
(
      JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , DESCRIPTION VARCHAR2(120)
        , JOB_CLASS_NAME VARCHAR2(128) NOT NULL
        , IS_DURABLE VARCHAR2(1) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , IS_STATEFUL VARCHAR2(1) NOT NULL
        , REQUESTS_RECOVERY VARCHAR2(1) NOT NULL
        , JOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_JOB_DETAILS
    ADD CONSTRAINT KRSB_QRTZ_JOB_DETAILSP1
PRIMARY KEY (JOB_NAME,JOB_GROUP)
/


CREATE INDEX KRSB_QRTZ_JOB_DETAILS_TI1 
  ON KRSB_QRTZ_JOB_DETAILS 
  (REQUESTS_RECOVERY)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_JOB_LISTENERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_JOB_LISTENERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_JOB_LISTENERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_JOB_LISTENERS
(
      JOB_NAME VARCHAR2(80)
        , JOB_GROUP VARCHAR2(80)
        , JOB_LISTENER VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_JOB_LISTENERS
    ADD CONSTRAINT KRSB_QRTZ_JOB_LISTENERSP1
PRIMARY KEY (JOB_NAME,JOB_GROUP,JOB_LISTENER)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_LOCKS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_LOCKS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_LOCKS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_LOCKS
(
      LOCK_NAME VARCHAR2(40)
    

)
/

ALTER TABLE KRSB_QRTZ_LOCKS
    ADD CONSTRAINT KRSB_QRTZ_LOCKSP1
PRIMARY KEY (LOCK_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_PAUSED_TRIGGER_GRPS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_PAUSED_TRIGGER_GRPS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS
(
      TRIGGER_GROUP VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_PAUSED_TRIGGER_GRPS
    ADD CONSTRAINT KRSB_QRTZ_PAUSED_TRIGGER_GRP1
PRIMARY KEY (TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_SCHEDULER_STATE
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_SCHEDULER_STATE';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_SCHEDULER_STATE CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_SCHEDULER_STATE
(
      INSTANCE_NAME VARCHAR2(80)
        , LAST_CHECKIN_TIME NUMBER(13) NOT NULL
        , CHECKIN_INTERVAL NUMBER(13) NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_SCHEDULER_STATE
    ADD CONSTRAINT KRSB_QRTZ_SCHEDULER_STATEP1
PRIMARY KEY (INSTANCE_NAME)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_SIMPLE_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_SIMPLE_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_SIMPLE_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_SIMPLE_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , REPEAT_COUNT NUMBER(7) NOT NULL
        , REPEAT_INTERVAL NUMBER(12) NOT NULL
        , TIMES_TRIGGERED NUMBER(7) NOT NULL
    

)
/

ALTER TABLE KRSB_QRTZ_SIMPLE_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_SIMPLE_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/







-----------------------------------------------------------------------------
-- KRSB_QRTZ_TRIGGERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_TRIGGERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_TRIGGERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_TRIGGERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , JOB_NAME VARCHAR2(80) NOT NULL
        , JOB_GROUP VARCHAR2(80) NOT NULL
        , IS_VOLATILE VARCHAR2(1) NOT NULL
        , DESCRIPTION VARCHAR2(120)
        , NEXT_FIRE_TIME NUMBER(13)
        , PREV_FIRE_TIME NUMBER(13)
        , PRIORITY NUMBER(13)
        , TRIGGER_STATE VARCHAR2(16) NOT NULL
        , TRIGGER_TYPE VARCHAR2(8) NOT NULL
        , START_TIME NUMBER(13) NOT NULL
        , END_TIME NUMBER(13)
        , CALENDAR_NAME VARCHAR2(80)
        , MISFIRE_INSTR NUMBER(2)
        , JOB_DATA BLOB
    

)
/

ALTER TABLE KRSB_QRTZ_TRIGGERS
    ADD CONSTRAINT KRSB_QRTZ_TRIGGERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP)
/


CREATE INDEX KRSB_QRTZ_TRIGGERS_TI1 
  ON KRSB_QRTZ_TRIGGERS 
  (NEXT_FIRE_TIME)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI2 
  ON KRSB_QRTZ_TRIGGERS 
  (NEXT_FIRE_TIME, TRIGGER_STATE)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI3 
  ON KRSB_QRTZ_TRIGGERS 
  (TRIGGER_STATE)
/
CREATE INDEX KRSB_QRTZ_TRIGGERS_TI4 
  ON KRSB_QRTZ_TRIGGERS 
  (IS_VOLATILE)
/





-----------------------------------------------------------------------------
-- KRSB_QRTZ_TRIGGER_LISTENERS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KRSB_QRTZ_TRIGGER_LISTENERS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KRSB_QRTZ_TRIGGER_LISTENERS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KRSB_QRTZ_TRIGGER_LISTENERS
(
      TRIGGER_NAME VARCHAR2(80)
        , TRIGGER_GROUP VARCHAR2(80)
        , TRIGGER_LISTENER VARCHAR2(80)
    

)
/

ALTER TABLE KRSB_QRTZ_TRIGGER_LISTENERS
    ADD CONSTRAINT KRSB_QRTZ_TRIGGER_LISTENERSP1
PRIMARY KEY (TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_LISTENER)
/







-----------------------------------------------------------------------------
-- KR_QRTZ_PAUSED_TRIGGERS_GRPS
-----------------------------------------------------------------------------
DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_tables WHERE table_name = 'KR_QRTZ_PAUSED_TRIGGERS_GRPS';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS CASCADE CONSTRAINTS PURGE'; END IF;
END;
/

CREATE TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS
(
      TRIGGER_GROUP VARCHAR2(80)
    

)
/

ALTER TABLE KR_QRTZ_PAUSED_TRIGGERS_GRPS
    ADD CONSTRAINT KR_QRTZ_PAUSED_TRIGGERS_GRPP1
PRIMARY KEY (TRIGGER_GROUP)
/






DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_LOCK_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_LOCK_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_LOCK_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_LOOKUP_RSLT_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_LOOKUP_RSLT_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_LOOKUP_RSLT_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_MAINT_LOCK_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_MAINT_LOCK_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_MAINT_LOCK_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRNS_NTE_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRNS_NTE_S'; END IF;
END;
/

CREATE SEQUENCE KRNS_NTE_S INCREMENT BY 1 START WITH 2020 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_BAM_PARM_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_BAM_PARM_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_BAM_PARM_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_BAM_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_BAM_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_BAM_S INCREMENT BY 1 START WITH 2000 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'KRSB_MSG_QUE_S';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE KRSB_MSG_QUE_S'; END IF;
END;
/

CREATE SEQUENCE KRSB_MSG_QUE_S INCREMENT BY 1 START WITH 121 NOMAXVALUE NOCYCLE NOCACHE ORDER
/

DECLARE temp NUMBER;
BEGIN
    SELECT COUNT(*) INTO temp FROM user_sequences WHERE sequence_name = 'RECIPES_SEQ';
    IF temp > 0 THEN EXECUTE IMMEDIATE 'DROP SEQUENCE RECIPES_SEQ'; END IF;
END;
/
