package com.news.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.news.model.Author;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class AuthorDTO extends AbstractDTO<AuthorDTO>{

    @NotBlank
    private String name;

    @NotBlank
    private String slug;

    private Integer display;
    private String createdDate;
    private String updatedDate;

    public AuthorDTO(){

    }

    public AuthorDTO(Author author){
        this.setId(author.getId());
        this.name = author.getName();
        this.slug = author.getSlug();
        this.display = author.getDisplay();

        try {
            this.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(
                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(author.getCreatedDate()).getTime()));

            this.updatedDate = new SimpleDateFormat("dd/MM/yyyy").format(
                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(author.getCreatedDate()).getTime()));

            //            this.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").format(
//                    new Date(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(author.getCreatedDate()).getTime())));

        }catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
