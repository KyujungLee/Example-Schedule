package com.example.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class ScheduleRequestDto {

    String todo;
    String name;
    String password;
    LocalDateTime updated_at;

}
