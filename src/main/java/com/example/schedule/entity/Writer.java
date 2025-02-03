package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Writer {

    Long writer_id;
    String writer_name;
    String writer_email;
    LocalDateTime writer_created_at;
    LocalDateTime writer_updated_at;

    public Writer (String writer_name, String writer_email, LocalDateTime writer_updated_at){
        this.writer_name = writer_name;
        this.writer_email = writer_email;
        this.writer_updated_at = writer_updated_at;
    }

}
