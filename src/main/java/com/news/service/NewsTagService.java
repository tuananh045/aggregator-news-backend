package com.news.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface NewsTagService {

    @Transactional
    public Boolean deleteNews(Long newsId);

}
