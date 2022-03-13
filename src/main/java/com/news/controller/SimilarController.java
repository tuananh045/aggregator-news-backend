package com.news.controller;

import com.news.dto.NewsDTO;
import com.news.model.News;
import com.news.model.Tag;
import com.news.payload.response.SimilarResponse;
import com.news.repository.NewsRepository;
import com.news.utils.ContentBased;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/similar")
public class SimilarController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/{category}/{slug}")
    public ResponseEntity<?> getSimilarListProduct(@PathVariable String category, @PathVariable String slug) {
        News entity = newsRepository.findOneBySlug(slug);

        List<List<String>> documents = new ArrayList<List<String>>();
        List<News> entities = newsRepository.getList(category, PageRequest.of(0, 100, Sort.by("createdDate").descending()));
        List<NewsDTO> dtos = new ArrayList<>();
        for (News e : entities) {
            dtos.add(new NewsDTO(e));
        }

        for (NewsDTO item : dtos) {
            List<String> listTags = new ArrayList<String>();
            for (int i = 0; i < item.getTag_slugs().size(); i++) {
                listTags.add(item.getTag_slugs().get(i));
            }
            documents.add(listTags);
        }
        List<String> tagList = new ArrayList<String>();
        List<String> tag_slugs = new ArrayList<String>();
        for (Tag tag : entity.getTags()) {
            tag_slugs.add(tag.getSlug());
        }

        for (int i = 0; i < tag_slugs.size(); i++) {
            tagList.add(tag_slugs.get(i));
        }
        List<SimilarResponse> list = ContentBased.similarByTags(tagList, documents);

        List<NewsDTO> result = new ArrayList<>();
        int result_size = list.size();
        if (result_size >= 3) {
            for (int i = 0; i < result_size; i++) {
                News p = newsRepository.getById(dtos.get(list.get(i).getIndex()).getId());
                NewsDTO pDto = new NewsDTO(p);
                result.add(pDto);
            }
            return new ResponseEntity<List<NewsDTO>>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }

}
