package com.news.controller;

import com.news.dto.SourceDTO;
import com.news.model.Source;
import com.news.payload.response.MessageResponse;
import com.news.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/news/source")
public class SourceController {
    @Autowired
    private SourceRepository sourceRepository;

    @GetMapping("/{id}")
    public ResponseEntity<SourceDTO> getById(@PathVariable Long id) {
        Source news = sourceRepository.getById(id);
        SourceDTO result = new SourceDTO(news);
        return new ResponseEntity<SourceDTO>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Source>> getAll() {
        List<Source> sources = new ArrayList<>();
        sources = sourceRepository.findAll();
        return new ResponseEntity<>(sources,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> create(@RequestBody SourceDTO dto){
        Source entity = null;
        if(sourceRepository.existsBySlug(dto.getSlug())){
            return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Nguồn đã tồn tại!"),
                    HttpStatus.OK);
        }else{
            entity = new Source();
            entity.setCreatedDate(new Timestamp(new Date().getTime()).toString());
            entity.setName(dto.getName());
            entity.setSlug(dto.getSlug());
            entity.setUrl_logo(dto.getUrl_logo());

            entity = sourceRepository.save(entity);

            return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Thêm source thành công!"),
                    HttpStatus.OK);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<MessageResponse> update(@RequestBody SourceDTO dto, @PathVariable Long id) {
        Source source = null;
        Optional<Source> optionalSource = sourceRepository.findById(id);
        if(optionalSource.isPresent()){
            source = optionalSource.get();
        }
        source.setName(dto.getName());
        source.setSlug(dto.getSlug());
        source.setUrl_logo(dto.getUrl_logo());
        source = sourceRepository.save(source);

        return new ResponseEntity<MessageResponse>(new MessageResponse("SUCCESS", "Update Success fully!"),
                HttpStatus.OK);
    }
    //	@DeleteMapping(value = "/{id}")
//	public ResponseEntity<ResponseMessage> delete(@PathVariable Long id) {
//		if (id != null) {
//			Source entity = sourceRepos.getById(id);
//			if (entity.getDisplay() == 1) {
//				entity.setDisplay(0);
//			} else {
//				entity.setDisplay(1);
//			}
//			entity = sourceRepos.save(entity);
//			return new ResponseEntity<ResponseMessage>(new ResponseMessage("SUCCESS", "Xoá danh mục thành công!"),
//					HttpStatus.OK);
//		}
//		return new ResponseEntity<ResponseMessage>(new ResponseMessage("FAILURE", "Xoá danh mục không thành công!"),
//				HttpStatus.BAD_REQUEST);
//	}

}
