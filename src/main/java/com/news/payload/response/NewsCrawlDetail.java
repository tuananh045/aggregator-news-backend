package com.news.payload.response;

import com.news.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class NewsCrawlDetail {

    private String title;
    private String content;
    private String short_description;
    private String author;
    private List<String> tags;

    public NewsCrawlDetail() {
        super();
    }

    public NewsCrawlDetail(String title, String content, String short_description, String author, List<String> tags) {
        super();
        this.title = title;
        this.content = content;
        this.short_description = short_description;
        this.author = author;
        this.tags = tags;
    }
}
