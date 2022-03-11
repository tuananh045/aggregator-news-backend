package com.news.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDTO<T> {
    private Long id;
    private String createdDate = new Timestamp(new Date().getTime()).toString();
    private String updatedDate;
}
