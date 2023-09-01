package com.albummanagement.controller;

import com.albummanagement.service.CommentService;
import com.albummanagement.view.dto.CommentDto;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:gj
 * @DateTime:2023/5/8 21:43
 **/
@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping
    public Result addComment(@RequestBody CommentDto commentDto){
        return commentService.addComment(commentDto);
    }

    @GetMapping("/list/{PicID}")
    public Result getComment(@PathVariable("PicID") String picId){
        Long id = Long.parseLong(picId);
        return commentService.getComments(id);
    }
}
