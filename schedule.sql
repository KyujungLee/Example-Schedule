use schedule;

CREATE TABLE schedule (
    id			bigint auto_increment primary key	comment '고유값',
    todo 		varchar(100) not null 				comment '할일',
    name 		varchar(100) not null 				comment '작성자명',
    password 	varchar(100) not null 				comment '비밀번호',
    create_At 	datetime not null					comment '작성일',
    update_At   datetime not null 					comment '수정일'
    );
show tables ;
show databases ;
show schemas;
alter table schedule change column update_At updated_at datetime not null comment '수정일';
alter table schedule change column create_At created_at datetime not null comment '작성일';
select * from schedule;