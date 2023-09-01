package com.albummanagement.service.impl;

import com.albummanagement.dao.entity.Picture;
import com.albummanagement.dao.entity.User;
import com.albummanagement.dao.mapper.PictureMapper;
import com.albummanagement.dao.mapper.UserMapper;
import com.albummanagement.service.UserService;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.dto.CommentDto;
import com.albummanagement.view.vo.CommentVo;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.Comment;
import com.albummanagement.service.CommentService;
import com.albummanagement.dao.mapper.CommentMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【comment(评论表)】的数据库操作Service实现
 * @createDate 2023-05-08 21:42:04
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {


    @Autowired
    UserService userService;
    @Autowired
    PictureMapper pictureMapper;

    @Override
    public Result addComment(CommentDto commentDto) {
        Comment comment = new Comment();
        Long userId = UserThreadLocal.getUserVo().getId();
        comment.setUserId(userId);
        comment.setContent(commentDto.getContent());
        comment.setPicId(Long.valueOf(commentDto.getPicId()));
        comment.setCreateDate(new Date());
        comment.setFatherId(Long.valueOf(commentDto.getFatherId()));
        if (this.save(comment)) {
            Picture picture = pictureMapper.selectById(Long.valueOf(commentDto.getPicId()));
            picture.setCommentCount(picture.getCommentCount()+1);
            pictureMapper.updateById(picture);
            return null;
        }
        return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
    }

    @Override
    public Result getComments(Long id) {
        List<CommentVo> commentByPicId = getCommentByPicId(id);
        return Result.success(commentByPicId);
    }

    /**
     * 通过图片id获取该图片的所有评论
     * @param picId
     * @return
     */
    public List<CommentVo> getCommentByPicId(Long picId){
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getPicId,picId);
        lambdaQueryWrapper.eq(Comment::getFatherId,0);
        List<Comment> list = this.list(lambdaQueryWrapper);
        List<CommentVo> commentVos = copyList(list);
        return commentVos;
    }

    private List<CommentVo> copyList(List<Comment> list) {
        List<CommentVo> result = new ArrayList<>();
        for (Comment comment : list) {
            result.add(copy(comment));
        }
        return result;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);

        Long userId = comment.getUserId();
        User user = userService.getById(userId);
        commentVo.setUserName(user.getNickname());
        commentVo.setAvatar(user.getAvatar());
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getFatherId,comment.getId())
                .eq(Comment::getPicId,comment.getPicId());
        List<Comment> list = list(lambdaQueryWrapper);

        //插入该评论的子评论
        List<CommentVo> children = new ArrayList<>();
        for (Comment comment1 : list) {
            CommentVo commentVo1 = new CommentVo();

            BeanUtils.copyProperties(comment1,commentVo1);
            Long userId1 = comment.getUserId();
            User user1 = userService.getById(userId1);
            commentVo1.setUserName(user1.getNickname());
            commentVo1.setAvatar(user1.getAvatar());
            children.add(commentVo1);
        }
        commentVo.setChildComment(children);
        return commentVo;
    }

}




