package com.albummanagement.controller;

import com.albummanagement.service.CategoryService;
import com.albummanagement.view.dto.CategoryDto;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 处理与图片分类有关的操作
 *
 * @Author:gj
 * @DateTime:2023/4/16 16:16
 **/
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 得到系统分类以及自定义分类
     *
     * @return
     */
    @GetMapping()
    public Result getCategories(@RequestParam("isGetAll") Boolean isGetAll) {
        if (isGetAll) {
            return categoryService.getSystemAndUserCategories();
        } else {
            return categoryService.getUserCategories();
        }

    }

    /**
     * 创建新的分类
     *
     * @param categoryDto
     * @return
     */
    @PostMapping
    public Result creatCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.creatCategory(categoryDto);
    }

    @GetMapping("/delete")
    public Result deleteCategory(@RequestParam("categoryId") String categoryId) {
        return categoryService.deleteCategory(Long.valueOf(categoryId));
    }

}
