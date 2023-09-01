package com.albummanagement.view.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author:gj
 * @DateTime:2023/4/16 16:28
 **/
@Data
public class CategoryVo {

    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    private String categoryName;
    private Integer type;
    private String description;
}

