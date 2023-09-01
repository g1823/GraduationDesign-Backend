package com.albummanagement.controller;

import com.albummanagement.service.PicLikeService;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:gj
 * @DateTime:2023/5/14 0:14
 **/
@RestController
@RequestMapping("like")
public class LikeController {

    @Autowired
    PicLikeService likeService;

    @GetMapping("/{PicID}")
    public Result updateLikeCount(@PathVariable("PicID") String picId){
        Long id = Long.parseLong(picId);
        return likeService.updateLikeCount(id);
    }
}
