package com.albummanagement.service.impl;

import com.albummanagement.dao.entity.Picture;
import com.albummanagement.dao.mapper.PictureMapper;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.PicLike;
import com.albummanagement.service.PicLikeService;
import com.albummanagement.dao.mapper.PicLikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author lenovo
* @description 针对表【pic_like(点赞表)】的数据库操作Service实现
* @createDate 2023-05-14 10:16:04
*/
@Service
public class PicLikeServiceImpl extends ServiceImpl<PicLikeMapper, PicLike>
    implements PicLikeService{
    @Autowired
    PictureMapper pictureMapper;

    @Autowired
    PicLikeMapper likeMapper;
    @Override
    public Result updateLikeCount(Long picID) {
        PicLike like = new PicLike();
        Long userId = UserThreadLocal.getUserVo().getId();
        like.setPicId(picID);
        like.setUserId(userId);
        like.setCreateTime(new Date());
        if(this.save(like)){
            Picture picture = pictureMapper.selectById(picID);
            picture.setLikeCount(picture.getLikeCount()+1);
            pictureMapper.updateById(picture);
            return Result.success(null);
        }
        return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
    }
}




