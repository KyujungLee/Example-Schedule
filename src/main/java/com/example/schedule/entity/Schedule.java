package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    Long schedule_id;
    Long writer_id;
    String schedule_todo;
    String schedule_name;
    String schedule_password;
    LocalDateTime schedule_created_at;
    LocalDateTime schedule_updated_at;


    public Schedule(Long writer_id, String schedule_todo, String schedule_name, String schedule_password, LocalDateTime schedule_updated_at){
        this.writer_id = writer_id;
        this.schedule_todo = schedule_todo;
        this.schedule_name = schedule_name;
        this.schedule_password = schedule_password;
        this.schedule_updated_at = schedule_updated_at;
    }
    public Schedule(Long schedule_id, String schedule_password){
        this.schedule_id = schedule_id;
        this.schedule_password = schedule_password;
    }

}
