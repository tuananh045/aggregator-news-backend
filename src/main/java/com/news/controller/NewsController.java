package com.news.controller;

import com.news.dto.NewsDTO;
import com.news.dto.SearchDTO;
import com.news.model.*;
import com.news.payload.response.MessageResponse;
import com.news.repository.*;
import com.news.service.CrawlService;
import com.news.service.NewsTagService;
import com.news.utils.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/news/article")
public class NewsController {
    @Autowired
    private EntityManager manager;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsCategoryRepository newsCategoryRepository;

    @Autowired
    private NewsSourceRepository sourceRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TagsRepository tagRepository;

    @Autowired
    private CrawlService crawlService;

    @Autowired
    private NewsTagService service;

    @GetMapping("")
    public ResponseEntity<Page<NewsDTO>> getAllNews(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "24") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
        Page<News> list = newsRepository.findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
        Page<NewsDTO> result = list.map(tag -> new NewsDTO(tag));
        return new ResponseEntity<Page<NewsDTO>>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<NewsDTO>> getAllNews (
            @RequestParam(name ="page", defaultValue = "0") Integer page,
            @RequestParam(name ="limit", defaultValue = "24") Integer limit,
            @RequestParam(name ="sortBy",defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name ="category" , defaultValue = "") String category,
            @RequestParam(name ="source" , defaultValue = "") String source,
            @RequestParam(name = "display", defaultValue = "2") Integer display
    ) {

        SearchDTO dto = new SearchDTO();
        dto.setDisplay(display);
        dto.setKeyword(keyword);
        dto.setSource(source);
        dto.setCategory(category);

        Integer pageIndex = page > 0 ? page -= 1 : 0;
        String whereClause = "";
        String orderBy = " ORDER BY createdDate DESC";
        String sqlCount = "select count(entity.id) from  News as entity where (1=1) ";
        String sql = "select new com.news.dto.NewsDTO(entity) from News as entity where (1=1) ";
        if (dto.getDisplay() == 0 || dto.getDisplay() == 1) {
            whereClause += " AND ( entity.display = " + dto.getDisplay() + ")";
        } else {
            whereClause += "";
        }
        if (keyword != null && StringUtils.hasText(keyword)) {
            if (keyword.contains(" ")) {
                String[] keywords = keyword.split(" ");
                whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
                        + keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
                        + " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
                for (int i = 1; i < keywords.length; i++) {
                    whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
                            + " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
                            + "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
                }
                whereClause += " ) ";
            } else {
                whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
                        + "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
            }
        }

        if (dto.getCategory() != null && StringUtils.hasText(dto.getCategory())) {
            whereClause += " AND ( entity.category.slug = :category )";
        } else {
            whereClause += "";
        }

        if (dto.getSource() != null && StringUtils.hasText(dto.getSource())) {
            whereClause += " AND ( entity.source.slug = :source )";
        } else {
            whereClause += "";
        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, NewsDTO.class);
        Query qCount = manager.createQuery(sqlCount);

        if (keyword != null && StringUtils.hasText(keyword)) {
            if (keyword.contains(" ")) {

            } else {
                q.setParameter("text", '%' + keyword + '%');
                qCount.setParameter("text", '%' + keyword + '%');
            }

        }
        if (dto.getCategory() != null && dto.getCategory().length() > 0) {
            q.setParameter("category", dto.getCategory());
            qCount.setParameter("category", dto.getCategory());
        }

        if (dto.getSource() != null && dto.getSource().length() > 0) {
            q.setParameter("source", dto.getSource());
            qCount.setParameter("source", dto.getSource());
        }
        int startPosition = pageIndex * limit;
        q.setFirstResult(startPosition);
        q.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<NewsDTO> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, limit);

        Page<NewsDTO> result = new PageImpl<NewsDTO>(entities, pageable, count);

        return new ResponseEntity<Page<NewsDTO>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Page<NewsDTO>> searchNews(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "24") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "keyword", defaultValue = "") String keyword
    ){
        Integer pageIndex = page > 0 ? page -= 1 : 0;
        String whereClause = "";
        String orderBy = " ORDER BY createdDate DESC";
        String sqlCount = "select count(entity.id) from  News as entity where (1=1) ";
        String sql = "select new com.news.dto.NewsDTO(entity) from News as entity where entity.display=1 AND (1=1) ";
        if (keyword != null && StringUtils.hasText(keyword)) {

            if (keyword.contains(" ")) {
                String[] keywords = keyword.split(" ");
                whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
                        + keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
                        + " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
                for (int i = 1; i < keywords.length; i++) {
                    whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
                            + " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
                            + "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
                }
                whereClause += " ) ";
            } else {
                whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
                        + "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
            }

        }

        sql += whereClause + orderBy;
        sqlCount += whereClause;

        Query q = manager.createQuery(sql, NewsDTO.class);
        Query qCount = manager.createQuery(sqlCount);

        if (keyword != null && StringUtils.hasText(keyword)) {
            if (keyword.contains(" ")) {

            } else {
                q.setParameter("text", '%' + keyword + '%');
                qCount.setParameter("text", '%' + keyword + '%');
            }

        }
        int startPosition = pageIndex * limit;
        q.setFirstResult(startPosition);
        q.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<NewsDTO> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, limit);
        Page<NewsDTO> result = new PageImpl<NewsDTO>(entities, pageable, count);
        return new ResponseEntity<Page<NewsDTO>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/{category}")
    public ResponseEntity<Page<NewsDTO>> getAllByCategory(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(name = "limit", defaultValue = "24") Integer limit,
                                                          @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
                                                          @RequestParam(name = "keyword", defaultValue = "") String keyword, @PathVariable String category) {
        Integer pageIndex = page > 0 ? page -= 1 : 0;
        String whereClause = "";
        String orderBy = " ORDER BY createdDate DESC";
        String sqlCount = "select count(entity.id) from  News as entity where entity.display=1 AND (1=1) ";
        String sql = "select new com.news.dto.NewsDTO(entity) from News as entity where entity.display=1 AND (1=1) ";
        if (keyword != null && StringUtils.hasText(keyword)) {
            if (keyword.contains(" ")) {
                String[] keywords = keyword.split(" ");
                whereClause += " AND ( entity.title LIKE " + "'" + keywords[0] + "'" + " OR entity.slug LIKE " + "'"
                        + keywords[0] + "'" + " OR entity.content LIKE " + "'" + keywords[0] + "'"
                        + " OR entity.short_description LIKE " + "'" + keywords[0] + "'";
                for (int i = 1; i < keywords.length; i++) {
                    whereClause += " or entity.title LIKE " + "'" + keywords[i] + "'"
                            + " OR entity.short_description LIKE " + "'" + keywords[i] + "'" + " OR entity.slug LIKE "
                            + "'" + keywords[i] + "'" + " OR entity.content LIKE " + "'" + keywords[i] + "'";
                }
                whereClause += " ) ";
            } else {
                whereClause += " AND ( entity.title LIKE :text " + "OR entity.slug LIKE :text "
                        + "OR entity.short_description LIKE :text " + "OR entity.content LIKE :text )";
            }
        }
        if (category != null && StringUtils.hasText(category)) {
            whereClause += " AND ( entity.category.slug LIKE :category )";
        }
        sql += whereClause + orderBy;
        sqlCount += whereClause;
        Query q = manager.createQuery(sql, NewsDTO.class);
        Query qCount = manager.createQuery(sqlCount);

        if (keyword != null && StringUtils.hasText(keyword)) {
            if (keyword.contains(" ")) {

            } else {
                q.setParameter("text", '%' + keyword + '%');
                qCount.setParameter("text", '%' + keyword + '%');
            }
        }
        if (category != null && StringUtils.hasText(category)) {
            q.setParameter("category", category);
            qCount.setParameter("category", category);
        }
        int startPosition = pageIndex * limit;
        q.setFirstResult(startPosition);
        q.setMaxResults(limit);

        @SuppressWarnings("unchecked")
        List<NewsDTO> entities = q.getResultList();
        long count = (long) qCount.getSingleResult();
        Pageable pageable = PageRequest.of(pageIndex, limit);
        Page<NewsDTO> result = new PageImpl<NewsDTO>(entities, pageable, count);
        return new ResponseEntity<Page<NewsDTO>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/tag/{tag}")
    public ResponseEntity<Page<NewsDTO>> getPostByTag(
            @PathVariable String tag,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "limit", defaultValue = "24") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy
    ) {
        Page<News> list = newsRepository.findByTags_slug(tag,
                PageRequest.of(page, limit, Sort.by(sortBy).descending()));
        Page<NewsDTO> result = list.map(item -> new NewsDTO(item));
        return new ResponseEntity<Page<NewsDTO>>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/slug/{slug}")
    public ResponseEntity<NewsDTO> getPostBySlug(@PathVariable String slug) {
        News entity = newsRepository.findOneBySlug(slug);
        NewsDTO result = new NewsDTO(entity);
        return new ResponseEntity<NewsDTO>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<MessageResponse> add(@RequestBody List<NewsDTO> dtos) throws IOException {
        List<News> entities = new ArrayList<>();

        if(dtos != null && dtos.size() > 0){
            for(NewsDTO dto : dtos){
                News entity = null;
                Tag tag = null;
                NewsCategory newsCategory = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
                Source source = sourceRepository.findOneBySlug(dto.getSource_slug());
                Author author = authorRepository.findOneBySlug(dto.getAuthor_slug());
                List<String> tagNames = dto.getTag_names();
                List<Tag> tags = new ArrayList<>();

                if(newsRepository.existsByUrl(dto.getUrl())){
                    entity = newsRepository.getOneByUrl(dto.getUrl());
                    return new ResponseEntity<MessageResponse>(new MessageResponse("Bài viết " + entity.getTitle() + " đã tồn tại!"), HttpStatus.OK);
                }else{
                    entity = new News();
                    entity.setTitle(dto.getTitle());
                    entity.setSlug(Slug.makeSlug(dto.getTitle()));
                    entity.setShort_description(dto.getShort_description());
                    entity.setImage(dto.getImage());
                    entity.setContent(dto.getContent());
                    entity.setUrl(dto.getUrl());
                    entity.setCategory(newsCategory);
                    entity.setSource(source);

                    if(author != null){
                        author = new Author();
                        author.setName(dto.getAuthor_name());
                        author.setSlug(Slug.makeSlug(dto.getAuthor_name()));
                        author.setDisplay(1);
                        author.setCreatedDate(new Timestamp(new Date().getTime()).toString());

                        authorRepository.save(author);

                    }
                    entity.setAuthor(author);


                    if(tagNames != null){
                        for(String tagName : tagNames){
                            tag = tagRepository.findOneByName(tagName);
                            if(tag != null){
                                tags.add(tag);
                            }else{
                                tag = new Tag();
                                tag.setName(tagName);
                                tag.setSlug(Slug.makeSlug(tagName));
                                tag.setCreatedDate(new Timestamp(new Date().getTime()).toString());
                                tag = tagRepository.save(tag);

                                tags.add(tag);
                            }
                        }
                    }
                    entity.setTags(tags);
                    entity.setDisplay(1);
                    entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());

                    entities.add(entity);
                }
            }
            newsRepository.saveAll(entities);
            if (entities != null && entities.size() > 0) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Thêm bài viết thành công!"),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Thêm bài viết không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> update(@RequestBody NewsDTO dto, @PathVariable Long id){
        dto.setId(id);
        if(dto != null){
            News entity = null;
            NewsCategory category = newsCategoryRepository.findOneBySlug(dto.getCategory_slug());
            if(dto.getId() != null){
                entity = newsRepository.getById(dto.getId());
                entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
            }

            if(entity == null){
                entity = new News();
                entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
            }
            entity.setTitle(dto.getTitle());
            entity.setShort_description(dto.getShort_description());
            entity.setCategory(category);
            entity.setDisplay(1);
            entity = newsRepository.save(entity);

            if (entity != null) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Sửa bài viết thành công!"),
                        HttpStatus.OK);
            }
        }
        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Sửa bài viết không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/hidden/{id}")
    public ResponseEntity<MessageResponse> hide(@PathVariable Long id) {
        if (id != null) {
            News entity = newsRepository.getById(id);
            if (entity.getDisplay() == 1) {
                entity.setDisplay(0);
            } else {
                entity.setDisplay(1);
            }
            entity = newsRepository.save(entity);
            return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Ẩn bài viết thành công!"),
                    HttpStatus.OK);
        }
        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Ẩn bài viết không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {

        if (id != null) {
            Boolean result = service.deleteNews(id);
            if (result) {
                return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Xoá bài viết thành công!"),
                        HttpStatus.OK);
            }
            return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Xoá bài viết không thành công!"),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<MessageResponse>(new MessageResponse("FAILURE", "Xoá bài viết không thành công!"),
                HttpStatus.BAD_REQUEST);
    }

}
