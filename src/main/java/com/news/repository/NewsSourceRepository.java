package com.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.transform.Source;

@Repository
public interface NewsSourceRepository extends JpaRepository<Source, Long>{

    public Source findOneBySlug(String slug);

    public Source findOneByName(String name);

}
