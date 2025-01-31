package com.example.schedule.service;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }


    // 각 변수의 null 체크, 값이 있다면 저장.
    @Override
    public ScheduleResponseDto saveSchedule(String todo, String name, String password) {

        nullCheckThrow(todo, "할 일");
        nullCheckThrow(name, "이름");
        nullCheckThrow(password, "비밀번호");

        Schedule schedule = new Schedule(todo, name, password, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate findtime) {

        return scheduleRepository.findAllSchedules(name, findtime);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        return scheduleRepository.findScheduleById(id);
    }

    // password null 체크, 값이 있다면 id 체크, 값이 있다면 해당 id의 password 체크.
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String todo, String name, String password) {

        nullCheckThrow(password, "비밀번호");

        scheduleRepository.findScheduleById(id);

        scheduleRepository.checkPassword(id, password);

        return scheduleRepository.updateSchedule(id, todo, name, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @Override
    public void deleteSchedule(Long id) {

        scheduleRepository.deleteSchedule(id);
    }

    // 각종 변수의 null 체크.
    private void nullCheckThrow(Object object, String fieldName){

        if (object == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + "칸을 작성해주세요");
        }
    }
}
