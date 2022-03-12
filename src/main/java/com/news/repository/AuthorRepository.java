package com.news.repository;

import com.news.dto.AuthorDTO;
import com.news.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    public Author findOneBySlug(String slug);

    public Author findOneByName(String name);

}
