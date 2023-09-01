package com.albummanagement.view.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author:gj
 * @DateTime:2023/5/8 21:49
 **/
@Data
public class CommentDto {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String content;

    private String picId;

    private String fatherId;

}
