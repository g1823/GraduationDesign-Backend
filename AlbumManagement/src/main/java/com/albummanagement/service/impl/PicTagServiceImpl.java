package com.albummanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.PicTag;
import com.albummanagement.service.PicTagService;
import com.albummanagement.dao.mapper.PicTagMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【pic_tag(图片标签对应表)】的数据库操作Service实现
* @createDate 2023-05-06 10:48:44
*/
@Service
public class PicTagServiceImpl extends ServiceImpl<PicTagMapper, PicTag>
    implements PicTagService{

}




