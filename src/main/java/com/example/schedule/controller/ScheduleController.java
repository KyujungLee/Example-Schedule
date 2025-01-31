package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
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
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(
                scheduleService.saveSchedule(dto.getTodo(),dto.getName(),dto.getPassword()),
                HttpStatus.CREATED
        );
    }

    // 스케쥴 조건부 조회 API
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate findtime
            ){
        return new ResponseEntity<>(scheduleService.findAllSchedules(name, findtime), HttpStatus.OK);
    }

    // 스케쥴 단건 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    //  스케쥴 조건부 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
            ){
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto.getTodo(), dto.getName(), dto.getPassword()), HttpStatus.OK);
    }

    // 스케쥴 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody String password
            ){
        scheduleService.deleteSchedule(id, password);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
