package com.news.repository;

import com.news.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsSourceRepository extends JpaRepository<Source, Long>{

    public Source findOneBySlug(String slug);

    public Source findOneByName(String name);

}
