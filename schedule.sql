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

drop table schedule;

CREATE TABLE `schedule` (
                            `schedule_id`	bigint	auto_increment  primary key COMMENT '스케쥴 고유값',
                            `writer_id`	bigint	NOT NULL	COMMENT '작성자 고유값',
                            `schedule_todo`	varchar(100)	NOT NULL	COMMENT '스케쥴 할일',
                            `schedule_name`	varchar(100)	NOT NULL	COMMENT '스케쥴 작성자명',
                            `schedule_password`	varchar(100)	NOT NULL	COMMENT '스케쥴 비밀번호',
                            `schedule_created_at`	datetime	NOT NULL	COMMENT '스케쥴 작성일',
                            `schedule_updated_at`	datetime	NOT NULL	COMMENT '스케쥴 수정일'
);

CREATE TABLE `writer` (
                          `writer_id`	bigint  auto_increment  primary key COMMENT '작성자 고유값',
                          `writer_name`	varchar(100)	NOT NULL	COMMENT '작성자 작성자명',
                          `writer_email`	varchar(100)	NOT NULL	COMMENT '작성자 이메일',
                          `writer_created_at`	datetime	NOT NULL	COMMENT '작성자 작성일',
                          `writer_updated_at`	datetime	NOT NULL	COMMENT '작성자 수정일'
);

ALTER TABLE `schedule` ADD CONSTRAINT `FK_writer_TO_schedule_1` FOREIGN KEY (
                                                                             `writer_id`
    )
    REFERENCES `writer` (
                         `writer_id`
        );
