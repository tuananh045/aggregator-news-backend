package com.news.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.news.model.News;
import com.news.model.Tag;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NewsDTO extends AbstractDTO<NewsDTO>{

    private String title;

    private String slug;

    private String short_description;

    private String url;

    private String image;

    private String content;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String author_name;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String author_slug;
    private Integer display;
    private String createdDate;
    private String category_slug;
    private String category_name;
    private String source_slug;
    private String source_name;
    private List<String> tag_names;
    private List<String> tag_slugs;

    public NewsDTO(){

    }

    public NewsDTO(News entity){
        super();
        this.setId(entity.getId());
        this.title = entity.getTitle();
        this.slug = entity.getSlug();
        this.short_description = entity.getShort_description();
        this.image = entity.getImage();
        this.content = entity.getContent();
        this.author_name = entity.getAuthor().getName();
        this.author_slug = entity.getAuthor().getSlug();
        this.url = entity.getUrl();
        this.display = entity.getDisplay();
        this.category_slug = entity.getCategory().getSlug();
        this.category_name = entity.getCategory().getName();

        this.source_slug = entity.getSource().getSlug();
        this.source_name = entity.getSource().getName();

        this.tag_names = new ArrayList<>();
        this.tag_slugs = new ArrayList<>();

        for (Tag tag : entity.getTags()){
            TagDTO tagDto = new TagDTO(tag);
            this.tag_names.add(tagDto.getName());
            this.tag_slugs.add(tagDto.getSlug());
        }

        try {
            this.createdDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(
                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(entity.getCreatedDate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
