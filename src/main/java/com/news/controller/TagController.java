package com.news.controller;

import com.news.dto.TagDTO;
import com.news.model.Tag;
import com.news.payload.response.MessageResponse;
import com.news.repository.TagsRepository;
import com.news.utils.Slug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.sql.Timestamp;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/news/tag")
public class TagController {
    @Autowired
    private TagsRepository tagRepository;

    @GetMapping("")
    public ResponseEntity<Page<TagDTO>> getAllNewsTag(
            @RequestParam(name="page",defaultValue = "0") Integer page ,
            @RequestParam(name="limit",defaultValue = "24") Integer limit,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy
    ){
        Page<Tag> list = tagRepository.getList(PageRequest.of(page, limit, Sort.by(sortBy).descending()));
        Page<TagDTO> tagDTO = list.map(tag -> new TagDTO(tag));

        return new ResponseEntity<Page<TagDTO>>(tagDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable Long id) {
        Tag tag = tagRepository.getById(id);
        TagDTO result = new TagDTO(tag);
        return new ResponseEntity<TagDTO>(result, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody TagDTO dto) {
        Tag entity = null;
        if (dto.getId() != null) {
            entity = tagRepository.getById(dto.getId());
        }
        if (entity == null) {
            entity = new Tag();
            entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
        }

        entity.setName(dto.getName());
        entity.setSlug(Slug.makeCode(dto.getName()));
        entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
        entity = tagRepository.save(entity);


        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Thêm tag thành công!"),
                HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@RequestBody TagDTO dto, @PathVariable Long id) {
        dto.setId(id);
        Tag entity = null;
        if (dto.getId() != null) {
            entity = tagRepository.getById(dto.getId());
            entity.setUpdatedDate(new Timestamp(new Date().getTime()).toString());
        }
        if (entity == null) {
            entity = new Tag();
            entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
        }

        entity.setName(dto.getName());
        entity.setSlug(Slug.makeCode(dto.getName()));
        entity = tagRepository.save(entity);


        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Update tag thành công!"),
                HttpStatus.OK);
    }

    //	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<MessageResponse> delete(@PathVariable Long id) {
//			Tag entity = tagRepository.getById(id);
//			if (entity.getDisplay() == 1) {
//				entity.setDisplay(0);
//			} else {
//				entity.setDisplay(1);
//			}
//			entity = newsGenreRepository.save(entity);
//			return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Xoá thể loại thành công!"),
//					HttpStatus.OK);

//	}


}
