package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
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


    @Override
    public ScheduleResponseDto saveSchedule(Schedule schedule) {

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("schedule").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("todo", schedule.getTodo());
        parameters.put("name", schedule.getName());
        parameters.put("password", schedule.getPassword());
        parameters.put("created_at", schedule.getUpdated_at());
        parameters.put("updated_at", schedule.getUpdated_at());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        return new ScheduleResponseDto(key.longValue(), schedule.getTodo(), schedule.getName(), schedule.getUpdated_at());
    }

    // StringBuilder 를 이용하여 조건부 쿼리 작성, List<Object> 를 이용하여 적합한 파라미터 배열 부여
    @Override
    public List<ScheduleResponseDto> findAllSchedules(String name, LocalDate findtime) {

        StringBuilder sql = new StringBuilder("select id, todo, name, updated_at from schedule");
        List<Object> params = new ArrayList<>();

        if (name != null || findtime != null){
            sql.append(" where ");
        }
        if (name != null){
            sql.append(" name = ? ");
            params.add(name);
        }
        if (findtime != null){
            sql.append(", date(updated_at) = ? ");
            params.add(findtime);
        }
        sql.append(" order by updated_at desc");

        return jdbcTemplate.query(sql.toString(), scheduleRowMapper(), params.toArray());
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        List<ScheduleResponseDto> schedule = jdbcTemplate.query("select id, todo, name, updated_at from schedule where id = ? ", scheduleRowMapper(), id);

        return schedule.stream().findAny().orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id = " + id + " 가 존재하지 않습니다." ));
    }

    // StringBuilder 를 이용하여 조건부 쿼리 작성, List<Object> 를 이용하여 적합한 파라미터 배열 부여
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String todo, String name, LocalDateTime updated_at) {

        StringBuilder sql = new StringBuilder("update schedule set");
        List<Object> params = new ArrayList<>();

        boolean firstField = true;

        if (todo != null){
            sql.append(" todo = ? ");
            params.add(todo);
            firstField = false;
        }
        if (name != null){
            if (!firstField){
                sql.append(",");
            }
            sql.append(" name = ? ");
            params.add(name);
        }
        if (!firstField){
            sql.append(",");
        }
        sql.append(" updated_at = ? where id = ? ");
        params.add(updated_at);
        params.add(id);

        jdbcTemplate.update(sql.toString(), params.toArray());

        return findScheduleById(id);
    }

    @Override
    public void deleteSchedule(Long id) {

        jdbcTemplate.update("delete from schedule where id = ?", id);
    }

    @Override
    public Schedule checkPassword(Long id, String password) {

        List<Schedule> schedule = jdbcTemplate.query("select id, password from schedule where id = ? and password = ?",scheduleRowMapperPasswordCheck(), id, password);

        return schedule.stream().findAny().orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "비밀번호가 틀렸습니다" ));
    }

    public RowMapper<ScheduleResponseDto> scheduleRowMapper(){

        return new RowMapper<ScheduleResponseDto>() {
            @Override
            public ScheduleResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ScheduleResponseDto(
                        rs.getLong("id"),
                        rs.getString("todo"),
                        rs.getString("name"),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }
        };
    }

    public RowMapper<Schedule> scheduleRowMapperPasswordCheck(){

        return new RowMapper<Schedule>() {
            @Override
            public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Schedule(
                        rs.getLong("id"),
                        rs.getString("password")
                );
            }
        };
    }

}
