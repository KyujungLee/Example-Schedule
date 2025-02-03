package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.WriterRequestDto;
import com.example.schedule.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    // 스케쥴 생성 API
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody ScheduleRequestDto scheduleRequestDto,
            @RequestBody WriterRequestDto writerRequestDto
            ) {
        return new ResponseEntity<>(scheduleService.saveSchedule(
                        scheduleRequestDto.getSchedule_todo(),
                        scheduleRequestDto.getSchedule_name(),
                        scheduleRequestDto.getSchedule_password(),
                        writerRequestDto.getWriter_email()
                ),
                HttpStatus.CREATED
        );
    }

    // 스케쥴 조건부 조회 API
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(required = false) Long writer_id,
            @RequestParam(required = false) String schedule_name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate findTime,
            @RequestParam(required = true) Long paging_number,
            @RequestParam(required = true) Long paging_size
            ){
        return new ResponseEntity<>(scheduleService.findAllSchedules(writer_id, schedule_name, findTime, paging_number, paging_size), HttpStatus.OK);
    }

    // 스케쥴 단건 조회 API
    @GetMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long schedule_id){
        return new ResponseEntity<>(scheduleService.findScheduleById(schedule_id), HttpStatus.OK);
    }

    //  스케쥴 조건부 수정 API
    @PatchMapping("/{schedule_id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long schedule_id,
            @RequestBody ScheduleRequestDto scheduleRequestDto
            ){
        return new ResponseEntity<>(scheduleService.updateSchedule(
                        schedule_id,
                        scheduleRequestDto.getSchedule_todo(),
                        scheduleRequestDto.getSchedule_name(),
                        scheduleRequestDto.getSchedule_password()
                ),
                HttpStatus.OK);
    }

    // 스케쥴 삭제 API
    @DeleteMapping("/{schedule_id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long schedule_id,
            @RequestBody String schedule_password
            ){
        scheduleService.deleteSchedule(schedule_id, schedule_password);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
