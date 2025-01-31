package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    Long id;
    String todo;
    String name;
    String password;
    LocalDateTime created_at;
    LocalDateTime updated_at;

    public Schedule(String todo, String name, String password, LocalDateTime updated_at){
        this.todo = todo;
        this.name = name;
        this.password = password;
        this.updated_at = updated_at;
    }
    public Schedule(Long id, String password){
        this.id = id;
        this.password = password;
    }

    public Schedule(){}

}
