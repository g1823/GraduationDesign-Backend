package com.albummanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.Tag;
import com.albummanagement.service.TagService;
import com.albummanagement.dao.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【tag(图片标签表)】的数据库操作Service实现
* @createDate 2023-05-06 10:47:34
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




