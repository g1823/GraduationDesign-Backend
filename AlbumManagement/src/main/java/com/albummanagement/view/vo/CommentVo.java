package com.albummanagement.view.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author:gj
 * @DateTime:2023/5/13 0:27
 **/
@Data
public class CommentVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    private List<CommentVo> childComment;

    private Date createDate;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String userName;

    private String avatar;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long fatherId;
}
