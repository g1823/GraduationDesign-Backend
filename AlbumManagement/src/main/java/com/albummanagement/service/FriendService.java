package com.albummanagement.service;

import com.albummanagement.dao.entity.Friend;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【friend(好友表)】的数据库操作Service
* @createDate 2023-05-14 20:00:56
*/
public interface FriendService extends IService<Friend> {

    /**
     * 添加好友
     * @param id
     * @return
     */
    Result add(Long id);

    /**
     * 得到申请信息
     * @return
     */
    Result getApply();

    /**
     * 查询本人好友
     * @param id
     * @return
     */
    List<Long> getFriendList(Long id,int status);

    /**
     * 接受申请
     * @param id
     * @return
     */
    Result receive(Long id);

    /**
     * 查找好友
     * @return
     */
    Result getFriend();

    /**
     * 删除好友
     * @param friendId
     * @return
     */
    Result deleteFriend(Long friendId);
}
