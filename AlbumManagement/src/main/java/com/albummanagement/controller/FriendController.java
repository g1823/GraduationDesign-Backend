package com.albummanagement.controller;

import com.albummanagement.service.FriendService;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:gj
 * @DateTime:2023/5/14 21:48
 **/
@RestController
@RequestMapping("friend")
public class FriendController {
    @Autowired
    FriendService friendService;
    @GetMapping("/add/{UserID}")
    public Result addFriend(@PathVariable("UserID") String UserId){
        Long id = Long.valueOf(UserId);
        return friendService.add(id);
    }

    @GetMapping("/list/apply")
    public Result getApply(){
        return friendService.getApply();
    }

    @GetMapping("/update/{applyId}")
    public Result receive(@PathVariable("applyId") String applyId){
        Long id = Long.valueOf(applyId);
        return friendService.receive(id);
    }

    @GetMapping("/list")
    public Result getFriend(){
        return friendService.getFriend();
    }

    @GetMapping("/delete/{id}")
    public Result deleteFriend(@PathVariable("id")String id){
        Long friendId = Long.valueOf(id);
        return friendService.deleteFriend(friendId);
    }
}
