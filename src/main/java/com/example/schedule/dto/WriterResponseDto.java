package com.example.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class WriterResponseDto {

    Long writer_id;
    String writer_name;
    String writer_email;
    LocalDateTime writer_updated_at;

    public WriterResponseDto(Long writer_id, String writer_name, String writer_email) {
        this.writer_id = writer_id;
        this.writer_name = writer_name;
        this.writer_email = writer_email;
    }

}
