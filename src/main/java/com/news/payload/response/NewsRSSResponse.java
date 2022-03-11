package com.news.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//RSS : Really Simple Syndication ( định dạng tài liệu , tập tin)
public class NewsRSSResponse {

    private String title;
    private String short_description;
    private String image;
    private String url;

}
