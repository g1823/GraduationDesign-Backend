package com.albummanagement.service;

import com.albummanagement.dao.entity.User;
import com.albummanagement.view.dto.FindUserDTO;
import com.albummanagement.view.dto.PasswordDto;
import com.albummanagement.view.dto.UserDetailDto;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author lenovo
* @description 针对表【user(用户基础信息表)】的数据库操作Service
* @createDate 2023-04-09 16:22:32
*/
public interface UserService extends IService<User> {

    /**
     * 获取用户的详细信息
     * @return
     */
    Result getUserDetail();

    /**
     * 修改用户头像
     * @param file
     * @return
     */
    Result changeAvatar(MultipartFile file);

    /**
     * 修改密码
     * @param newPasswordDto
     * @return
     */
    Result changePassword(PasswordDto newPasswordDto);

    /**
     * 修改用户基础信息
     * @param userDetailDto
     * @return
     */
    Result changeUserMessage(UserDetailDto userDetailDto);

    /**
     * 通过username或nickname查询用户
     * @param findUserDTO
     * @return
     */
    Result findUser(FindUserDTO findUserDTO);
}
