package com.news.dto;

import com.news.model.Tag;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class TagDTO extends AbstractDTO<TagDTO> {
    private String name;
    private String slug;
    private String createdDate;
    private String updatedDate;

    public TagDTO() {

    }

    public TagDTO(Tag entity) {
        super();
        this.setId(entity.getId());
        this.name = entity.getName();
        this.slug = entity.getSlug();
        try {
            this.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(
                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(entity.getCreatedDate()).getTime()));
            this.updatedDate = new SimpleDateFormat("dd/MM/yyyy").format(
                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(entity.getUpdatedDate()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
