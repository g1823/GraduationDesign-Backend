package com.albummanagement.view.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author:gj
 * @DateTime:2023/4/5 16:45
 **/
@Data
@AllArgsConstructor
public class UserVo {
    //由于有的文章的id是雪花算法生产的19位数字，
    //初始查询返回的json数据中id为19位,而jsNumber类型最多16位，超出的位数不保证精度。导致前端再次查询文章时的请求参数id出错。
    //可在对应Vo类的id字段上添加 @JsonSerialize(using = ToStringSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String nickname;
    private Date date;
    private String avatar;
    private String token;

    public UserVo() {
    }
}
