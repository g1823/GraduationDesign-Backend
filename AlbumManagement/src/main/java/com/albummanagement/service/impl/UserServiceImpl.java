package com.albummanagement.service.impl;

import com.albummanagement.service.FriendService;
import com.albummanagement.utils.JWTUtils;
import com.albummanagement.utils.QiniuUtils;
import com.albummanagement.utils.RedisUtils;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.dto.FindUserDTO;
import com.albummanagement.view.dto.PasswordDto;
import com.albummanagement.view.dto.UserDetailDto;
import com.albummanagement.view.vo.FindUserVO;
import com.albummanagement.view.vo.Result;
import com.albummanagement.view.vo.UserDetailVo;
import com.albummanagement.view.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.User;
import com.albummanagement.service.UserService;
import com.albummanagement.dao.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author lenovo
 * @description 针对表【user(用户基础信息表)】的数据库操作Service实现
 * @createDate 2023-04-09 16:22:32
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    FriendService friendService;
    /**
     * 获取用户详细信息
     *
     * @return
     */
    @Override
    public Result getUserDetail() {
        Long id = UserThreadLocal.getUserVo().getId();
        User user = this.getById(id);
        UserDetailVo userDetailVo = new UserDetailVo();
        userDetailVo.setUsername(user.getUsername());
        userDetailVo.setPassword(user.getPassword());
        userDetailVo.setNickname(user.getNickname());
        userDetailVo.setAge(user.getAge());
        userDetailVo.setSex(user.getSex());
        userDetailVo.setAvatar(user.getAvatar());
        userDetailVo.setIntroduction(user.getIntroduction());
        userDetailVo.setCreateTime(user.getCreateTime());
        userDetailVo.setUpdateTime(user.getUpdateTime());
        return Result.success(userDetailVo);

    }

    @Override
    public Result changeAvatar(MultipartFile file) {
        String name = UUID.randomUUID().toString() + "."
                + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (QiniuUtils.upload(file, name)) {
            String url = QiniuUtils.url + "/" + name;
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(User::getId, UserThreadLocal.getUserVo().getId())
                    .eq(User::getUsername, UserThreadLocal.getUserVo().getUsername())
                    .set(User::getAvatar, url);

            if (this.update(lambdaUpdateWrapper)) {
                //更新redis中存储的头像
                String token = JWTUtils.createToken(UserThreadLocal.getUserVo().getUsername());
                UserVo userVo = RedisUtils.getUserByToken(token);
                userVo.setAvatar(url);
                RedisUtils.setToken(token, userVo);//存入redis

                Map<String, String> map = new HashMap<>();
                map.put("url", url);
                return Result.success(url);
            } else {
                return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
            }
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result changePassword(PasswordDto passwordDto) {
        if (StringUtils.isBlank(passwordDto.getNewPassword()) && StringUtils.isBlank(passwordDto.getOldPassword())) {
            return Result.fail(ErrorCodeENUM.EMPTY_PARAMS.getCode(), ErrorCodeENUM.EMPTY_PARAMS.getMsg());
        }
        String oldPassword = DigestUtils.md5Hex(passwordDto.getOldPassword());
        String username = UserThreadLocal.getUserVo().getUsername();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);

        User user = this.getOne(lambdaQueryWrapper);
        if (user.getPassword().equals(oldPassword)) {
            LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(User::getId, UserThreadLocal.getUserVo().getId())
                    .eq(User::getUsername, UserThreadLocal.getUserVo().getUsername())
                    .set(User::getPassword, DigestUtils.md5Hex(passwordDto.getNewPassword()));
            if (this.update(lambdaUpdateWrapper)) {
                return Result.success("success");
            } else {
                return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
            }
        } else {
            return Result.fail(ErrorCodeENUM.ERROR_PASSWORD.getCode(), ErrorCodeENUM.ERROR_PASSWORD.getMsg());
        }
    }

    @Override
    public Result changeUserMessage(UserDetailDto userDetailDto) {
        String username = userDetailDto.getUsername();
        String token = JWTUtils.createToken(username);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUsername, username)
                .set(User::getAge, userDetailDto.getAge())
                .set(User::getNickname, userDetailDto.getNickname())
                .set(User::getSex, userDetailDto.getSex())
                .set(User::getIntroduction, userDetailDto.getIntroduction());
        if (this.update(lambdaUpdateWrapper)) {
            UserVo userVo = RedisUtils.getUserByToken(token);
            userVo.setNickname(userDetailDto.getNickname());
            RedisUtils.setToken(token, userVo);//存入redis
            return Result.success("success");
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }

    }

    @Override
    public Result findUser(FindUserDTO findUserDTO) {

        if(findUserDTO.getByUsername()){
            return Result.success(findByUsername(findUserDTO.getKeyWord()));
        }else{
            return Result.success(findByNickname(findUserDTO.getKeyWord()));
        }
    }

    private List<FindUserVO> findByUsername(String username){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotBlank(username), User::getUsername, "%" + username + "%");
        List<User> list = this.list(lambdaQueryWrapper);
        return copyList(list);
    }

    private List<FindUserVO> findByNickname(String nickname){
        LambdaQueryWrapper<User> lambdaUpdateWrapper = new LambdaQueryWrapper<>();
        lambdaUpdateWrapper.like(StringUtils.isNotBlank(nickname), User::getNickname, "%" + nickname + "%");
        List<User> list = this.list(lambdaUpdateWrapper);
        return copyList(list);
    }
    private List<FindUserVO> copyList(List<User> userList){
        List<FindUserVO> userVos = new ArrayList<>();
        List<Long> friendList = friendService.getFriendList(UserThreadLocal.getUserVo().getId(), 0);
        for (User user : userList) {
            if(user.getId()==UserThreadLocal.getUserVo().getId()){
                break;
            }
            if(friendList.contains(user.getId())){
                break;
            }
            userVos.add(copy(user));
        }
        return userVos;
    }

    private FindUserVO copy(User user){
        FindUserVO userVo = new FindUserVO();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}




