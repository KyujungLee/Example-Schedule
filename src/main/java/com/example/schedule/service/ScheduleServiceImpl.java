package com.example.schedule.service;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.Writer;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository){
        this.scheduleRepository = scheduleRepository;
    }


    // 각 변수의 null 체크, 값이 있다면 저장.
    @Override
    public ScheduleResponseDto saveSchedule(String schedule_todo, String schedule_name, String schedule_password, String writer_email) {

        nullCheckThrow(schedule_todo, "할 일");
        nullCheckThrow(schedule_name, "이름");
        nullCheckThrow(schedule_password, "비밀번호");
        nullCheckThrow(writer_email, "이메일");
        formatCheckThrow(writer_email);

        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Long writer_id = scheduleRepository.checkWriterDuplication(schedule_name, writer_email);

        if (writer_id == null){
            Writer writer = new Writer(schedule_name, writer_email, localDateTime);
            writer_id = scheduleRepository.saveWriter(writer);
        }

        Schedule schedule = new Schedule(writer_id, schedule_todo, schedule_name, schedule_password, localDateTime);

        return scheduleRepository.saveSchedule(schedule);
    }

    // 우선 검색조건별로 찾은 리스트를, 페이지 번호와 페이지 크기를 기준으로 반환
    @Override
    public List<ScheduleResponseDto> findAllSchedules(Long writer_id, String schedule_name, LocalDate findTime, Long paging_number, Long paging_size) {

        List<ScheduleResponseDto> result = scheduleRepository.findAllSchedules(writer_id, schedule_name, findTime);

        return result.stream().skip((paging_number-1) * paging_size).limit(paging_size).collect(Collectors.toList());
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long schedule_id) {

        return scheduleRepository.findScheduleById(schedule_id);
    }


    // password null 체크, 값이 있다면 id 와 password 체크. 검증 후 데이터 수정
    // 업데이트 할 데이터가 없는데 업데이트
    @Override
    public ScheduleResponseDto updateSchedule(Long schedule_id, String schedule_todo, String schedule_name, String schedule_password) {

        nullCheckThrow(schedule_password, "비밀번호");

        Schedule result = scheduleRepository.checkPassword(schedule_id, schedule_password);

        return scheduleRepository.updateSchedule(schedule_id, schedule_todo, schedule_name, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    // 비밀번호 체크 후 데이터 삭제
    @Override
    public void deleteSchedule(Long schedule_id, String schedule_password) {

        scheduleRepository.checkPassword(schedule_id, schedule_password);

        scheduleRepository.deleteSchedule(schedule_id);
    }



    // 각종 변수의 null 체크.
    private void nullCheckThrow(Object object, String fieldName){

        if (object == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + "칸을 작성해주세요");
        }
    }

    // 이메일 형식 체크.
    private void formatCheckThrow(String writer_email){

        if (    !writer_email.contains("@")
                || !writer_email.contains(".")
                || writer_email.matches(".*@.*@.*")
                || writer_email.matches(".*\\..*\\..*")
        ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 양식에 맞게 작성해주세요");
        }
    }

}
