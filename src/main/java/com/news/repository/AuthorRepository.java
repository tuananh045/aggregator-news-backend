package com.news.repository;

import com.news.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    public Author findOneBySlug(String slug);

    public Author findOneByName(String name);

}
