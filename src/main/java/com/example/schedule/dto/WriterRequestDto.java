package com.example.schedule.dto;

import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class WriterRequestDto {

    String writer_name;
    String writer_email;
    LocalDateTime writer_updated_at;
}
