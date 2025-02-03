package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.dto.WriterResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.entity.Writer;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateScheduleRepository implements ScheduleRepository{

    JdbcTemplate jdbcTemplate;

    public JdbcTemplateScheduleRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    // 스케쥴 생성
    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("schedule_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer_id", schedule.getWriter_id());
        parameters.put("schedule_todo", schedule.getSchedule_todo());
        parameters.put("schedule_name", schedule.getSchedule_name());
        parameters.put("schedule_password", schedule.getSchedule_password());
        parameters.put("schedule_created_at", schedule.getSchedule_updated_at());
        parameters.put("schedule_updated_at", schedule.getSchedule_updated_at());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getSchedule_todo(), schedule.getSchedule_name(), schedule.getSchedule_updated_at() );
    }

    // 작성자 정보 생성
    @Override
    public Long saveWriter(Writer writer) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("writer").usingGeneratedKeyColumns("writer_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("writer_name", writer.getWriter_name());
        parameters.put("writer_email", writer.getWriter_email());
        parameters.put("writer_created_at", writer.getWriter_updated_at());
        parameters.put("writer_updated_at", writer.getWriter_updated_at());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return key.longValue();
    }

    // 작성자 중복 체크 (스케쥴 생성 시)
    @Override
    public Long checkWriterDuplication(String writer_name, String writer_email) {

        List<WriterResponseDto> writer = jdbcTemplate.query(
                "select writer_id from writer where writer_name = ? and writer_email = ? ",
                writerRowMapperCheckDuplication(),
                writer_name,
                writer_email
        );

        return writer.get(0).getWriter_id();
    }

    // StringBuilder 를 이용하여 조건부 쿼리 작성, List<Object> 를 이용하여 적합한 파라미터 배열 부여
    @Override
    public List<ScheduleResponseDto> findAllSchedules(Long writer_id, String schedule_name, LocalDate findTime) {

        StringBuilder sql = new StringBuilder("select schedule_id, schedule_todo, schedule_name, schedule_updated_at from schedule");
        List<Object> params = new ArrayList<>();

        if (writer_id != null || schedule_name != null || findTime != null){
            sql.append(" where ");

            boolean firstField = true;

            if (writer_id != null){
                sql.append(" writer_id = ? ");
                params.add(writer_id);
                firstField = false;
            }
            if (schedule_name != null){
                if (!firstField){
                    sql.append(",");
                }
                sql.append(" name = ? ");
                params.add(schedule_name);
                firstField = false;
            }
            if (findTime != null){
                if (!firstField){
                    sql.append(",");
                }
                sql.append("date(updated_at) = ? ");
                params.add(findTime);
            }

        }
        sql.append(" order by updated_at desc");

        return jdbcTemplate.query(sql.toString(), scheduleRowMapper(), params.toArray());
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long schedule_id) {

        List<ScheduleResponseDto> schedule = jdbcTemplate.query(
                "select schedule_id, schedule_todo, schedule_name, schedule_updated_at from schedule where schedule_id = ? ",
                scheduleRowMapper(),
                schedule_id
        );

        return schedule.stream().findAny().orElseThrow( () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "id = " + schedule_id + " 가 존재하지 않습니다."
        ));
    }

    // StringBuilder 를 이용하여 조건부 쿼리 작성, List<Object> 를 이용하여 적합한 파라미터 배열 부여
    @Override
    public ScheduleResponseDto updateSchedule(Long schedule_id, String schedule_todo, String schedule_name, LocalDateTime schedule_updated_at) {

        StringBuilder sql = new StringBuilder("update schedule set");
        List<Object> params = new ArrayList<>();

        boolean firstField = true;

        if (schedule_todo != null){
            sql.append(" schedule_todo = ? ");
            params.add(schedule_todo);
            firstField = false;
        }
        if (schedule_name != null){
            if (!firstField){
                sql.append(",");
            }
            sql.append(" schedule_name = ? ");
            params.add(schedule_name);
            firstField = false;
        }
        if (!firstField){
            sql.append(",");
        }
        sql.append(" schedule_updated_at = ? where schedule_id = ? ");
        params.add(schedule_updated_at);
        params.add(schedule_id);

        jdbcTemplate.update(sql.toString(), params.toArray());

        return findScheduleById(schedule_id);
    }

    @Override
    public void deleteSchedule(Long schedule_id) {

        jdbcTemplate.update("delete from schedule where schedule_id = ?", schedule_id);
    }

    @Override
    public Schedule checkPassword(Long schedule_id, String schedule_password) {

        List<Schedule> schedule = jdbcTemplate.query(
                "select schedule_id, schedule_password, schedule_todo, schedule_name from schedule where schedule_id = ? and schedule_password = ?",
                scheduleRowMapperCheckPassword(),
                schedule_id,
                schedule_password
        );

        return schedule.stream().findAny().orElseThrow( () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "id = " + schedule_id + " 가 존재하지 않거나, 비밀번호가 틀렸습니다"
        ));
    }

    public RowMapper<ScheduleResponseDto> scheduleRowMapper(){

        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("schedule_id"),
                        rs.getString("schedule_todo"),
                        rs.getString("schedule_name"),
                        rs.getTimestamp("schedule_updated_at").toLocalDateTime()
                );
            }
        };
    }

    public RowMapper<Schedule> scheduleRowMapperCheckPassword(){

        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("schedule_id"),
                        rs.getString("schedule_password"),
                        rs.getString("schedule_todo"),
                        rs.getString("schedule_name")
                );
            }
        };
    }

    public RowMapper<WriterResponseDto> writerRowMapperCheckDuplication(){

        return new RowMapper<WriterResponseDto>() {
            @Override
            public WriterResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new WriterResponseDto(
                        rs.getLong("writer_id"),
                        rs.getString("writer_name"),
                        rs.getString("writer_email")
                );
            }
        };
    }

}
