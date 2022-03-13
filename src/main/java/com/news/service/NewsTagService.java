package com.news.service;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

public interface NewsTagService {

    @Transactional
    public Boolean deleteNews(Long newsId);

}
