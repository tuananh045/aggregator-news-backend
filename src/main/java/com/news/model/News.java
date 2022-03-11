package com.news.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_news")
public class News extends BaseEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "slug")
    private String slug;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String short_description;

    @Column(name = "url")
    private String url;

    @Column(name = "image")
    private String image;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "display")
    private Integer display; // 1 : show, 0: hidden

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private NewsCategory category;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "news_tags",
            joinColumns = { @JoinColumn(name = "news_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") }
    )
    private Set<Tag> tags = new HashSet<>();
}
