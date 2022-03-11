package com.news.service;

import com.news.model.News;
import com.news.model.Tag;
import com.news.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsTagServiceImpl implements NewsTagService{

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Boolean deleteNews(Long newsId) {
        if(newsId != null){
            News news = newsRepository.getById(newsId);
            List<Tag> tags = news.getTags();
            for(Tag tag : tags){
                tag.getNews().remove(news);
            }
            newsRepository.deleteById(newsId);
            return true;
        }
        return false;
    }
}
