package com.example.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleRequestDto {

    String schedule_todo;
    String schedule_name;
    String schedule_password;
    LocalDateTime schedule_updated_at;
    String writer_email;

}
