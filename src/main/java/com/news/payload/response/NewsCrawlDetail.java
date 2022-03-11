package com.news.payload.response;

import com.news.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsCrawlDetail {

    private String title;
    private String content;
    private String short_description;
    private String author;
    private List<Tag> tags;
}
