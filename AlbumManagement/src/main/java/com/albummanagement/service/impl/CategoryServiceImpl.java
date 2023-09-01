package com.albummanagement.service.impl;

import com.albummanagement.utils.MyException;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.dto.CategoryDto;
import com.albummanagement.view.vo.CategoryVo;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.dao.entity.Category;
import com.albummanagement.service.CategoryService;
import com.albummanagement.dao.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author lenovo
 * @description 针对表【category】的数据库操作Service实现
 * @createDate 2023-04-16 16:35:16
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public Result getSystemAndUserCategories() {
        Map resultMap = new HashMap();
        //获取系统分类
        LambdaQueryWrapper<Category> lambdaQueryWrapperSystem = new LambdaQueryWrapper<>();
        lambdaQueryWrapperSystem.eq(Category::getType, 0);
        List<Category> categoryListSystem = this.list(lambdaQueryWrapperSystem);
        resultMap.put("systemCategory", copyList(categoryListSystem));
        //获取用户分类
        LambdaQueryWrapper<Category> lambdaQueryWrapperUser = new LambdaQueryWrapper<>();
        lambdaQueryWrapperUser.eq(Category::getUserId, UserThreadLocal.getUserVo().getId());
        List<Category> categoryListUser = this.list(lambdaQueryWrapperUser);
        resultMap.put("userCategory", copyList(categoryListUser));
        return Result.success(resultMap);
    }

    /**
     * 创建新的用户分类
     *
     * @param categoryDto
     * @return
     */
    @Override
    public Result creatCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setCategoryName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setCreateTime(new Date());
        category.setType(1);
        category.setUserId(UserThreadLocal.getUserVo().getId());
        if (this.save(category)) {
            return Result.success("success");
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    /**
     * 得到用户分类
     *
     * @return
     */
    @Override
    public Result getUserCategories() {
        LambdaQueryWrapper<Category> lambdaQueryWrapperUser = new LambdaQueryWrapper<>();
        lambdaQueryWrapperUser.eq(Category::getUserId, UserThreadLocal.getUserVo().getId());
        List<Category> categoryListUser = this.list(lambdaQueryWrapperUser);
        return Result.success(categoryListUser);
    }

    @Override
    public Result deleteCategory(Long categoryId) {
        if (this.removeById(categoryId)) {
            return getUserCategories();
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }

    }

    public List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVos = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVos.add(copyToCategoryVo(category));
        }
        return categoryVos;
    }

    public CategoryVo copyToCategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setCategoryName(category.getCategoryName());
        categoryVo.setId(category.getId());
        categoryVo.setType(category.getType());
        categoryVo.setDescription(category.getDescription());
        return categoryVo;
    }


}




