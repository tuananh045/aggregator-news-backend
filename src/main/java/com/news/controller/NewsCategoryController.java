package com.news.controller;

import com.news.dto.NewsCategoryDTO;
import com.news.model.NewsCategory;
import com.news.payload.response.MessageResponse;
import com.news.repository.NewsCategoryRepository;
import com.news.utils.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/news/category")
public class NewsCategoryController {

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @GetMapping("")
    public ResponseEntity<List<NewsCategoryDTO>> getAllNewsCategory() {
        List<NewsCategoryDTO> dto = new ArrayList<>();
        List<NewsCategory> newsCategory = newsCategoryRepository.findAll();
        for (NewsCategory item : newsCategory) {
            NewsCategoryDTO newsCategoryDTO = new NewsCategoryDTO(item);
            dto.add(newsCategoryDTO);
        }
        return new ResponseEntity<List<NewsCategoryDTO>>(dto, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<NewsCategoryDTO>> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "24") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {

        Page<NewsCategory> list = newsCategoryRepository
                .findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
        Page<NewsCategoryDTO> dto = list.map(category -> new NewsCategoryDTO(category));

        return new ResponseEntity<Page<NewsCategoryDTO>>(dto, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<NewsCategoryDTO> getById(@PathVariable Long id) {
        NewsCategory news = newsCategoryRepository.getById(id);
        NewsCategoryDTO result = new NewsCategoryDTO(news);
        return new ResponseEntity<NewsCategoryDTO>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody NewsCategoryDTO dto) {
        if (dto != null) {
            NewsCategory entity = null;
            if (dto.getId() != null) {
                entity = newsCategoryRepository.getById(dto.getId());
                entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
            }
            if (entity == null) {
                entity = new NewsCategory();
                entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
            }

            entity.setName(dto.getName());
            entity.setSlug(Slug.makeCode(dto.getName()));
            entity.setDisplay(1);
            entity = newsCategoryRepository.save(entity);

            if (entity != null) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Thêm danh mục thành công!"),
                        HttpStatus.OK);
            }
        }

        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Thêm danh mục không thành công!"),
                HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long id, @RequestBody NewsCategoryDTO dto) {
        dto.setId(id);
        if (dto != null) {
            NewsCategory entity = null;
            if (dto.getId() != null) {
                entity = newsCategoryRepository.getById(dto.getId());
                entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
            }
            if (entity == null) {
                entity = new NewsCategory();
                entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
            }

            entity.setName(dto.getName());
            entity.setSlug(Slug.makeCode(dto.getName()));
            entity.setDisplay(1);
            entity = newsCategoryRepository.save(entity);

            if (entity != null) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Sửa danh mục thành công!"),
                        HttpStatus.OK);
            }
        }

        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Sửa danh mục không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        if (id != null) {
            NewsCategory entity = newsCategoryRepository.getById(id);
            if (entity.getDisplay() == 1) {
                entity.setDisplay(0);
            } else {
                entity.setDisplay(1);
            }
            entity = newsCategoryRepository.save(entity);
            return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Xoá danh mục thành công!"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Xoá danh mục không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

}
