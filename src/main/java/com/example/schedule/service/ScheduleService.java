package com.example.schedule.service;

import com.example.schedule.dto.ScheduleResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {

    ScheduleResponseDto saveSchedule(String schedule_todo, String schedule_name, String schedule_password, String writer_email);
    List<ScheduleResponseDto> findAllSchedules(Long writer_id, String schedule_name, LocalDate findTime, Long paging_number, Long paging_size);
    ScheduleResponseDto findScheduleById(Long schedule_id);
    ScheduleResponseDto updateSchedule(Long schedule_id, String schedule_todo, String schedule_name, String schedule_password);
    void deleteSchedule(Long schedule_id, String schedule_password);
}
