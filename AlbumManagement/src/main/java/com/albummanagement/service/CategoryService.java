package com.albummanagement.service;

import com.albummanagement.dao.entity.Category;
import com.albummanagement.view.dto.CategoryDto;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【category】的数据库操作Service
* @createDate 2023-04-16 16:35:16
*/
public interface CategoryService extends IService<Category> {

    /**
     * 得到用户分类和系统分类
     * @return
     */
    Result getSystemAndUserCategories();

    /**
     * 创建新的用户分类
     * @param categoryDto
     * @return
     */
    Result creatCategory(CategoryDto categoryDto);

    /**
     * 得到用户分类
     * @return
     */
    Result getUserCategories();

    /**
     * 删除指定分类
     * @param categoryId
     * @return
     */
    Result deleteCategory(Long categoryId);
}
