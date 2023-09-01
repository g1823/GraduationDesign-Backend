package com.albummanagement.service;

import com.albummanagement.dao.entity.Comment;
import com.albummanagement.view.dto.CommentDto;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2023-05-08 21:42:04
*/
public interface CommentService extends IService<Comment> {

    /**
     * 插入评论信息
     * @param commentDto
     * @return
     */
    Result addComment(CommentDto commentDto);

    /**
     * 获取图片评论信息
     * @param id
     * @return
     */
    Result getComments(Long id);
}
