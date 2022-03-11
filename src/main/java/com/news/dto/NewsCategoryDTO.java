package com.news.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.news.model.NewsCategory;
import lombok.Data;

@Data
public class NewsCategoryDTO extends AbstractDTO<NewsCategoryDTO> {
    private String name;
    private String slug;
    private String createdDate;
    private String updatedDate;
    private Integer display;

    public NewsCategoryDTO() {
    }

    public NewsCategoryDTO(NewsCategory entity) {
        super();
        this.setId(entity.getId());
        this.name = entity.getName();
        this.slug = entity.getSlug();
        this.display = entity.getDisplay();
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
