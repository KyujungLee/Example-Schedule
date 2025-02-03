package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.WriterResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.Writer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);
    Long saveWriter(Writer writer);
    Long checkWriterDuplication(String writer_name, String writer_email);
    List<ScheduleResponseDto> findAllSchedules(Long writer_id, String schedule_name, LocalDate findTime);
    ScheduleResponseDto findScheduleById(Long schedule_id);
    ScheduleResponseDto updateSchedule(Long schedule_id, String schedule_todo, String schedule_name, LocalDateTime schedule_updated_at);
    void deleteSchedule(Long schedule_id);
    Schedule checkPassword(Long schedule_id, String schedule_password);

}
