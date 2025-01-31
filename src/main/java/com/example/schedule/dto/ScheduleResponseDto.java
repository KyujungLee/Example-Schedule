package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {

    Long id;
    String todo;
    String name;
    LocalDateTime updated_at;

}
