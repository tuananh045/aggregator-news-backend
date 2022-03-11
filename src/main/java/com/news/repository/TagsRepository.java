package com.news.repository;

import com.news.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagsRepository extends JpaRepository<Tag, Long> {

    public Tag findOneBySlug(String slug);

    public Tag findOneByName(String name);

    @Query("select entity from Tag entity")
    public Page<Tag> getList(Pageable pageable);

}
