package com.news.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlResponse {
    private String title;
    private String short_description;
    private String content;
    private String author;
    private String source;
    private String date;
    private List<String> tags;
}
