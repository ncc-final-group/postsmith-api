package com.postsmith.api.content.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postsmith.api.content.dto.RepliesManageDto;
import com.postsmith.api.content.service.RepliesManageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/replies")
public class RepliesManageController {

    private final RepliesManageService repliesManageService;
    
    public RepliesManageController(RepliesManageService repliesManageService) {
        this.repliesManageService = repliesManageService;
    }

    @GetMapping
    public List<RepliesManageDto> getReplies(@RequestParam("blogId") Long blogId) {
        return repliesManageService.getRepliesByBlogId(blogId);
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable("id") Integer id) {
        repliesManageService.deleteReplyById(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMultiple(@RequestBody List<Integer> ids) {
    	repliesManageService.deleteRepliesByIds(ids);
        return ResponseEntity.noContent().build();
    }
    
    
    
    
}
