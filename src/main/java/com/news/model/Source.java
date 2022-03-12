package com.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_news_source")
public class Source extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "url_logo")
    private String url_logo;

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<News> news = new ArrayList<>();

    public Source() {
        super();
    }

    public Source(String name, String slug, String url_logo) {
        super();
        this.name = name;
        this.slug = slug;
        this.url_logo = url_logo;
    }
}
