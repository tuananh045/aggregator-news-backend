package com.news.dto;

import com.news.model.Source;
import lombok.Data;

@Data
public class SourceDTO extends AbstractDTO<SourceDTO> {

    private String name;
    private String slug;
    private String url_logo;

    public SourceDTO() {
        super();
    }

    public SourceDTO(Source entity) {
        super();
        this.setId(entity.getId());
        this.name = entity.getName();
        this.slug = entity.getSlug();
        this.url_logo = entity.getUrl_logo();
    }

}
