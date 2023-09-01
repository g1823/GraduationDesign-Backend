package com.albummanagement.dao.mapper;

import com.albummanagement.dao.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author lenovo
* @description 针对表【user(用户基础信息表)】的数据库操作Mapper
* @createDate 2023-04-09 16:22:32
* @Entity com.albummanagement.dao.entity.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




