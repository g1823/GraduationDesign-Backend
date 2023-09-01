package com.albummanagement.service;

import com.albummanagement.dao.entity.PicLike;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【pic_like(点赞表)】的数据库操作Service
* @createDate 2023-05-14 10:16:04
*/
public interface PicLikeService extends IService<PicLike> {

    Result updateLikeCount(Long id);
}
