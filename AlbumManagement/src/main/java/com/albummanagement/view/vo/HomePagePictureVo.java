package com.albummanagement.view.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author:gj
 * @DateTime:2023/4/10 9:21
 **/
@Data
@AllArgsConstructor
public class HomePagePictureVo {
    //由于有的文章的id是雪花算法生产的19位数字，
    //初始查询返回的json数据中id为19位,而jsNumber类型最多16位，超出的位数不保证精度。导致前端再次查询文章时的请求参数id出错。
    //可在对应Vo类的id字段上添加 @JsonSerialize(using = ToStringSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String url;

    private Integer viewCount;

    private Integer likeCount;

    private Integer commentCount;

    private Integer downloadCount;

    private String pictureName;

    private String description;

}
