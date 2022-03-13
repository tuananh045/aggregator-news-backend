package com.news.dto;

import lombok.Data;

@Data
public class SearchDTO extends AbstractDTO<SearchDTO> {

    private int pageIndex;
    private int pageSize;
    private String keyword;
    private String sortBy; // name
    private String tag; // tim theo tag
    private String category;
    private String source;
    private Integer display;

    public SearchDTO() {
        super();
    }

    public SearchDTO(int pageIndex, int pageSize, String keyword) {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.keyword = keyword;
    }

    public SearchDTO(int pageIndex, int pageSize, String keyword, String category, String source) {
        super();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.keyword = keyword;
        this.category = category;
        this.source = source;
    }

}
