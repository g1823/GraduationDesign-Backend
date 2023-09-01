package com.albummanagement.view.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author:gj
 * @DateTime:2023/4/10 14:56
 **/
@Data
@AllArgsConstructor
public class PageParam {
    private int current;
    private int size;

    public PageParam() {
    }
}
