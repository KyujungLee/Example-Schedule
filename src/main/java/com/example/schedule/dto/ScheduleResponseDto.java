package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    Long schedule_id;
    String schedule_todo;
    String schedule_name;
    LocalDateTime schedule_updated_at;

}
