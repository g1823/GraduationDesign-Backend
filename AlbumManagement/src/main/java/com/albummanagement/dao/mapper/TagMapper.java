package com.albummanagement.dao.mapper;

import com.albummanagement.dao.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author lenovo
* @description 针对表【tag(图片标签表)】的数据库操作Mapper
* @createDate 2023-05-06 10:47:34
* @Entity com.albummanagement.dao.entity.Tag
*/
@Repository
public interface TagMapper extends BaseMapper<Tag> {

}




