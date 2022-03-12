package com.news.controller;

import com.news.dto.AuthorDTO;
import com.news.model.Author;
import java.sql.Timestamp;
import java.util.*;
import javax.validation.Valid;
import com.news.payload.response.MessageResponse;
import com.news.repository.AuthorRepository;
import com.news.utils.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/post/author")
public class AuthorController {
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("")
    public ResponseEntity<List<AuthorDTO>> getAllAuthor() {
        List<AuthorDTO> result = new ArrayList<>();
        List<Author> authors = authorRepository.findAll();
        for (Author author : authors) {
            AuthorDTO dto = new AuthorDTO(author);
            result.add(dto);
        }

        return new ResponseEntity<List<AuthorDTO>>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AuthorDTO>> getAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "limit", defaultValue = "24") Integer limit,
                                                  @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy) {
        Page<Author> list = authorRepository.findAll(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
        Page<AuthorDTO> result = list.map(tag -> new AuthorDTO(tag));
        return new ResponseEntity<Page<AuthorDTO>>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getById(@PathVariable Long id) {
        Author author = authorRepository.getById(id);
        AuthorDTO result = new AuthorDTO(author);
        return new ResponseEntity<AuthorDTO>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<AuthorDTO> getByIdDetail(@PathVariable Long id) {
        Author author = authorRepository.getById(id);
        AuthorDTO result = new AuthorDTO(author);
        return new ResponseEntity<AuthorDTO>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@Valid @RequestBody AuthorDTO dto) {
        Author author = null;
        if (dto.getId() != null) {
            author = authorRepository.getById(dto.getId());
            author.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
        }
        if (author == null) {
            author = new Author();
            author.setCreatedDate(new Timestamp(new Date().getTime()).toString());
        }

        author.setName(dto.getName());
        author.setSlug(Slug.makeCode(dto.getSlug()));
        author.setDisplay(1);

        author = authorRepository.save(author);

        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Thêm tác giả thành công!"),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> update(@Valid @RequestBody AuthorDTO dto, @PathVariable Long id) {
        dto.setId(id);
        Author entity = null;
        if (dto.getId() != null) {
            entity = authorRepository.getById(dto.getId());
            entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
        }
        if (entity == null) {
            entity = new Author();
            entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
        }

        entity.setName(dto.getName());
        entity.setSlug(Slug.makeCode(dto.getName()));
        entity.setDisplay(1);
        entity = authorRepository.save(entity);

        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Sửa tác giả thành công!"),
                    HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
        Author entity = authorRepository.getById(id);
        if (entity.getDisplay() == 1) {
            entity.setDisplay(0);
        } else {
            entity.setDisplay(1);
        }
        entity = authorRepository.save(entity);
        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Xoá tác giả thành công!"),
                HttpStatus.OK);
    }
}
