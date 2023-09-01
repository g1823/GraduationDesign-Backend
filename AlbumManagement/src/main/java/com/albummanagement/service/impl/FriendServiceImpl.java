package com.albummanagement.service.impl;

import com.albummanagement.dao.entity.User;
import com.albummanagement.dao.mapper.UserMapper;
import com.albummanagement.service.UserService;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.vo.FindUserVO;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.Friend;
import com.albummanagement.service.FriendService;
import com.albummanagement.dao.mapper.FriendMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author lenovo
* @description 针对表【friend(好友表)】的数据库操作Service实现
* @createDate 2023-05-14 20:00:56
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Autowired
    UserMapper userMapper;
    @Override
    public Result add(Long id) {
        Long userId = UserThreadLocal.getUserVo().getId();
        Friend friend = new Friend();
        friend.setApplyId(userId);
        friend.setStatus(0);
        friend.setReceiveId(id);
        if(this.save(friend)){
            return Result.success("好友申请已发送");
        }else{
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getApply() {
        Long userId = UserThreadLocal.getUserVo().getId();
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Friend::getReceiveId,userId).eq(Friend::getStatus,0);
        List<Friend> list = list(lambdaQueryWrapper);
        List<User> userList = new ArrayList<>();
        for (Friend friend : list) {
            User byId = userMapper.selectById(friend.getApplyId());
            userList.add(byId);
        }
        return Result.success(copyList(userList));
    }

    public List<Long> getFriendList(Long id,int status){
        List<Long> idList = new ArrayList<>();
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Friend::getApplyId,id).eq(Friend::getStatus,status);
        List<Friend> list = this.list(lambdaQueryWrapper);
        for (Friend friend : list) {
            idList.add(friend.getReceiveId());
        }
        return idList;
    }

    @Override
    public Result receive(Long id) {
        Long myId = UserThreadLocal.getUserVo().getId();
        LambdaUpdateWrapper<Friend> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Friend::getApplyId,id).eq(Friend::getReceiveId,myId)
                .set(Friend::getStatus,1);
        if(this.update(lambdaUpdateWrapper)){
            return Result.success("success");
        }else{
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getFriend() {
        Long userId = UserThreadLocal.getUserVo().getId();
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Friend::getReceiveId,userId).eq(Friend::getStatus,1);
        List<Friend> list = list(lambdaQueryWrapper);
        List<User> userList = new ArrayList<>();
        for (Friend friend : list) {
            User byId = userMapper.selectById(friend.getApplyId());
            userList.add(byId);
        }
        return Result.success(copyList(userList));
    }

    @Override
    public Result deleteFriend(Long friendId) {
        LambdaQueryWrapper<Friend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long userId = UserThreadLocal.getUserVo().getId();
        lambdaQueryWrapper.eq(Friend::getReceiveId,userId)
                .eq(Friend::getApplyId,friendId);
        if(this.remove(lambdaQueryWrapper)){
            return Result.success("success");
        }else{
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    private List<FindUserVO> copyList(List<User> userList){
        List<FindUserVO> userVos = new ArrayList<>();
        for (User user : userList) {
            userVos.add(copy(user));
        }
        return userVos;
    }
    private FindUserVO copy(User user){
        FindUserVO userVo = new FindUserVO();
        BeanUtils.copyProperties(user,userVo);
        if(user.getSex()==1){
            userVo.setSex("男");
        }if(user.getSex()==2){
            userVo.setSex("女");
        }if(user.getSex()==0){
            userVo.setSex("保密");
        }
        return userVo;
    }
}




