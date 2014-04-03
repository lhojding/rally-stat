create table METHOD_INFO (
    METHOD_ID number(9) not null,
    SERVICE_NAME varchar2(100) not null,
    METHOD_NAME varchar2(100) not null,
    constraint METHOD_INFO_PK primary key (METHOD_ID),
    constraint METHOD_INFO_UNIQUE unique (SERVICE_NAME, METHOD_NAME))
;
create sequence METHOD_ID_SEQ
    minvalue 0
    maxvalue 999999999
    start with 0
    increment by 1
    nocache
;
create table LAYER_INFO (
    LAYER_ID number(9) not null,
    LAYER_NAME varchar2(30) not null,
    constraint LAYER_INFO_PK primary key (LAYER_ID),
    constraint LAYER_INFO_UNIQUE unique (LAYER_NAME))
;
create sequence LAYER_ID_SEQ
    minvalue 0
    maxvalue 999999999
    start with 0
    increment by 1
    nocache
;
create table ORIGIN_INFO (
    ORIGIN_ID number(9) not null,
    ORIGIN_NAME varchar2(30) not null,
    constraint ORIGIN_INFO_PK primary key (ORIGIN_ID),
    constraint ORIGIN_INFO_UNIQUE unique (ORIGIN_NAME))
;
create sequence ORIGIN_ID_SEQ
    minvalue 0
    maxvalue 999999999
    start with 0
    increment by 1
    nocache
;
create table MEDIA_INFO (
    MEDIA_ID number(9) not null,
    MEDIA_NAME varchar2(50) not null,
    constraint MEDIA_INFO_PK primary key (MEDIA_ID),
    constraint MEDIA_INFO_UNIQUE unique (MEDIA_NAME))
;
create sequence MEDIA_ID_SEQ
    minvalue 0
    maxvalue 999999999
    start with 0
    increment by 1
    nocache
;
create table DAY_OF_WEEK (
    DAY_OF_WEEK_ID number(1) not null,
    DAY_OF_WEEK_NAME varchar2(10) not null,
    constraint DAY_OF_WEEK_PK primary key (DAY_OF_WEEK_ID),
    constraint DAY_OF_WEEK_UNIQUE unique (DAY_OF_WEEK_NAME))
;
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(0, 'Måndag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(1, 'Tisdag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(2, 'Onsdag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(3, 'Torsdag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(4, 'Fredag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(5, 'Lördag');
insert into DAY_OF_WEEK(DAY_OF_WEEK_ID, DAY_OF_WEEK_NAME) values(6, 'Söndag');
commit
;
create table CUSTOMER_STAT (
    METHOD_ID number(9) not null,
    ORIGIN_ID number(9) not null,
    MEDIA_ID number(9) not null,
    PRODUCT char(20) not null,
    CUSTOMER char(50) not null,
    STATISTICS_DATE date not null,
    DAY_OF_WEEK_ID number(1) not null,
    NUM_CORRECT_CALLS number(9) default 0 not null,
    NUM_INVALID_CALLS number(9) default 0 not null,
    NUM_FAILED_CALLS number(9) default 0 not null,
    constraint CUSTOMER_STAT_PK primary key (METHOD_ID, ORIGIN_ID, MEDIA_ID, PRODUCT, CUSTOMER, STATISTICS_DATE),
    constraint CUSTOMER_STAT_METHOD_INFO_FK foreign key (METHOD_ID) references METHOD_INFO (METHOD_ID),
    constraint CUSTOMER_STAT_ORIGIN_INFO_FK foreign key (ORIGIN_ID) references ORIGIN_INFO (ORIGIN_ID),
    constraint CUSTOMER_STAT_MEDIA_INFO_FK foreign key (MEDIA_ID) references MEDIA_INFO (MEDIA_ID),
    constraint CUSTOMER_STAT_DAY_OF_WEEK_FK foreign key (DAY_OF_WEEK_ID) references DAY_OF_WEEK (DAY_OF_WEEK_ID))
;
create table TIME_STAT (
    METHOD_ID number(9) not null,
    ORIGIN_ID number(9) not null,
    MEDIA_ID number(9) not null,
    LAYER_ID number(9) not null,
    PRODUCT char(20) not null,
    STATISTICS_TIME date not null,
    DAY_OF_WEEK_ID number(1) not null,
    HOUR_OF_DAY number(2) not null,
    NUM_CORRECT_CALLS number(9) default 0 not null,
    NUM_INVALID_CALLS number(9) default 0 not null,
    NUM_FAILED_CALLS number(9) default 0 not null,
    TOTAL_TIME_CORRECT_CALLS number(18) default 0 not null,
    TOTAL_TIME_INVALID_CALLS number(18) default 0 not null,
    TOTAL_TIME_FAILED_CALLS number(18) default 0 not null,
    NUM_10 number(9) default 0 not null,
    NUM_20 number(9) default 0 not null,
    NUM_50 number(9) default 0 not null,
    NUM_100 number(9) default 0 not null,
    NUM_200 number(9) default 0 not null,
    NUM_500 number(9) default 0 not null,
    NUM_1000 number(9) default 0 not null,
    NUM_2000 number(9) default 0 not null,
    NUM_5000 number(9) default 0 not null,
    NUM_10000 number(9) default 0 not null,
    NUM_20000 number(9) default 0 not null,
    NUM_OVER_20000 number(9) default 0 not null,
    constraint TIME_STAT_PK primary key (METHOD_ID, ORIGIN_ID, MEDIA_ID, LAYER_ID, PRODUCT, STATISTICS_TIME),
    constraint TIME_STAT_METHOD_INFO_FK foreign key (METHOD_ID) references METHOD_INFO (METHOD_ID),
    constraint TIME_STAT_ORIGIN_INFO_FK foreign key (ORIGIN_ID) references ORIGIN_INFO (ORIGIN_ID),
    constraint TIME_STAT_MEDIA_INFO_FK foreign key (MEDIA_ID) references MEDIA_INFO (MEDIA_ID),
    constraint TIME_STAT_LAYER_INFO_FK foreign key (LAYER_ID) references LAYER_INFO (LAYER_ID),
    constraint TIME_STAT_DAY_OF_WEEK_FK foreign key (DAY_OF_WEEK_ID) references DAY_OF_WEEK (DAY_OF_WEEK_ID))
;
