package com.example.schedule.service;

import com.example.schedule.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(String todo, String name, String password);
    List<ScheduleResponseDto> findAllSchedules(String name, LocalDate findtime);
    ScheduleResponseDto findScheduleById(Long id);
    ScheduleResponseDto updateSchedule(Long id, String todo, String name, String password);
    void deleteSchedule(Long id);
}
