package com.albummanagement.service.impl;

import com.albummanagement.dao.entity.*;
import com.albummanagement.dao.mapper.CategoryMapper;
import com.albummanagement.dao.mapper.PicTagMapper;
import com.albummanagement.dao.mapper.TagMapper;
import com.albummanagement.service.PicTagService;
import com.albummanagement.utils.MyException;
import com.albummanagement.utils.QiniuUtils;
import com.albummanagement.utils.UserThreadLocal;
import com.albummanagement.view.ErrorCodeENUM;
import com.albummanagement.view.dto.PageParam;
import com.albummanagement.view.vo.HomePagePictureVo;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.albummanagement.service.PictureService;
import com.albummanagement.dao.mapper.PictureMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * @author lenovo
 * @description 针对表【picture】的数据库操作Service实现
 * @createDate 2023-04-09 18:34:16
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
        implements PictureService {

    @Autowired
    PicTagService picTagService;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    PicTagMapper picTagMapper;
    //用来存储分类名和id的对应关系
    private Map<String, Integer> categoryToId = new HashMap<>();

    public PictureServiceImpl() {
        categoryToId.put("未分类", 0);
        categoryToId.put("动物", 1);
        categoryToId.put("食物", 2);
        categoryToId.put("植物", 3);
        categoryToId.put("风景", 4);
        categoryToId.put("交通工具", 5);
        categoryToId.put("体育用具", 6);
        categoryToId.put("音乐器材", 7);
        categoryToId.put("家具", 8);
        categoryToId.put("建筑", 9);
        categoryToId.put("电子产品", 10);
        categoryToId.put("衣服装饰品", 11);
        categoryToId.put("人", 12);
    }

    /**
     * 用来分页查询首页的图片
     *
     * @return
     */
    @Override
    public Result getAllPublicPicture(PageParam pageParam) {
        Page<Picture> page = new Page<>(pageParam.getCurrent(), pageParam.getSize());
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Picture::getIsPublic, 0);
        IPage<Picture> iPage = this.page(page, lambdaQueryWrapper);//分页查询
        List<Picture> pictures = iPage.getRecords();
        Map ResultMap = new HashMap();
        ResultMap.put("list", copyList(pictures));
        ResultMap.put("total", iPage.getTotal());
        return Result.success(ResultMap);
    }

    @Override
    public Result uploadPictureNotClassify(MultipartFile file, Boolean isPublic) {
        String name = UUID.randomUUID().toString() + "."
                + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (QiniuUtils.upload(file, name)) {
            String url = QiniuUtils.url + "/" + name;
            long size = file.getSize();
            Picture picture = new Picture();
            picture.setPictureName(name);
            picture.setUrl(url);
            picture.setUserId(UserThreadLocal.getUserVo().getId());
            picture.setFileSize(size);
            if (isPublic == false) {
                picture.setIsPublic(1);
            }
            picture.setCreateTime(new Date());
            if (this.save(picture)) {
                return Result.success("success");
            } else {
                return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
            }
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result uploadPictureClassify(MultipartFile file, Boolean isPublic) {
        String name = UUID.randomUUID().toString() + "."
                + StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (QiniuUtils.upload(file, name)) {
            String url = QiniuUtils.url + "/" + name;

            Map<String, String> map = null;
            try {
                map = getCategory(url);
            } catch (IOException e) {
                e.printStackTrace();
                throw new MyException("与python交互出现问题");
            }
            Long categoryID = Long.valueOf(map.get("categoryId"));
            Long tagId = Long.valueOf(map.get("tagId"));
            String categoryName = map.get("category");
            String tagName = map.get("tag");

            long size = file.getSize();

            Picture picture = new Picture();
            picture.setPictureName(name);
            picture.setUrl(url);
            picture.setUserId(UserThreadLocal.getUserVo().getId());
            picture.setFileSize(size);
            picture.setCategoryId(categoryID);
            if (isPublic == false) {
                picture.setIsPublic(1);
            }
            picture.setCreateTime(new Date());
            if (this.save(picture)) {
                PicTag picTag = new PicTag();
                picTag.setTagId(tagId);
                picTag.setPicId(picture.getId());
                picTagService.save(picTag);

                Map<String, String> resultMap = new HashMap<>();
                resultMap.put("picId", picture.getId().toString());
                resultMap.put("category", categoryName);
                resultMap.put("categoryId", categoryID.toString());
                resultMap.put("tag", tagName);
                resultMap.put("url", url);
                resultMap.put("name", picture.getPictureName());
                resultMap.put("desc", picture.getDescription());
                return Result.success(resultMap);
            } else {
                return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
            }
        } else {
            return Result.fail(ErrorCodeENUM.SERVER_ERROR.getCode(), ErrorCodeENUM.SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getCategoryPics(String categoryId) {
        Long category_Id = Long.valueOf(categoryId);
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Picture::getCategoryId, category_Id);
        lambdaQueryWrapper.eq(Picture::getUserId, UserThreadLocal.getUserVo().getId());
        List<Picture> pictureList = this.list(lambdaQueryWrapper);
        if (pictureList.isEmpty()) {
            return Result.success("null");
        }
        Map resultMap = new HashMap();
        resultMap.put("categoryId", categoryId);
        resultMap.put("pics", copyList(pictureList));
        return Result.success(resultMap);
    }

    @Override
    public Result updateViewCount(Long id) {
        Picture picture = this.getById(id);
        picture.setViewCount(picture.getViewCount() + 1);
        this.updateById(picture);
        return Result.success(picture.getViewCount());
    }

    @Override
    public Result getHotPicturesByLike() {
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Picture::getIsPublic, 0)
                .orderBy(true, false, Picture::getLikeCount)
                .last("LIMIT " + 6);
        List<Picture> list = list(lambdaQueryWrapper);
        return Result.success(copyList(list));

    }

    @Override
    public Result getHotPicturesByComment() {
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Picture::getIsPublic, 0)
                .orderBy(true, false, Picture::getCommentCount)
                .last("LIMIT " + 6);
        List<Picture> list = list(lambdaQueryWrapper);
        return Result.success(copyList(list));
    }

    @Override
    public Result getHotPicturesByView() {
        LambdaQueryWrapper<Picture> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Picture::getIsPublic, 0)
                .orderBy(true, false, Picture::getViewCount)
                .last("LIMIT " + 6);
        List<Picture> list = list(lambdaQueryWrapper);
        return Result.success(copyList(list));
    }

    @Override
    public Result searchPics(String name) {
        Map<String, Object> result = new HashMap<>();
        //查询分类相关
        //查询相关分类id
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper
                .and(wrapper -> wrapper.eq(Category::getUserId, 0)
                .or()
                .eq(Category::getUserId, UserThreadLocal.getUserVo().getId()))
                .like(StringUtils.isNotBlank(name), Category::getCategoryName, "%" + name + "%");
        List<Category> categoryList = categoryMapper.selectList(categoryLambdaQueryWrapper);

        List<String> categoryNameList = new ArrayList<>();//分类名称列表
        List<Long> categoryIdList = new ArrayList<>();//分类id列表
        for (Category category : categoryList) {
            categoryIdList.add(category.getId());
            categoryNameList.add(category.getCategoryName());
        }
        //循环id，获取分类查询图片信息
        List<Picture> byCategoryResult = new ArrayList<>();
        for (Long categoryId : categoryIdList) {
            LambdaQueryWrapper<Picture> categoryLambda = new LambdaQueryWrapper<>();
            categoryLambda.eq(Picture::getIsPublic, 0)
                    .or()
                    .and(wrapper -> wrapper.eq(Picture::getIsPublic, 1)
                            .eq(Picture::getUserId, UserThreadLocal.getUserVo().getId()))
                    .eq(Picture::getCategoryId, categoryId);
            byCategoryResult.addAll(this.list(categoryLambda));
        }

        LambdaQueryWrapper<Picture> nameLambda = new LambdaQueryWrapper<>();
        nameLambda.eq(Picture::getUserId, UserThreadLocal.getUserVo().getId())
                .like(StringUtils.isNotBlank(name), Picture::getPictureName, "%" + name + "%");
        List<Picture> byNameResult = list(nameLambda);
        result.put("categoryName", categoryNameList);
        result.put("byCategory", byCategoryResult);
        result.put("byTag", null);
        result.put("byName", byNameResult);

        return Result.success(result);
    }

    // 得到分类
    @Transactional
    public Map<String, String> getCategory(String pic_URL) throws IOException {
        Map<String, String> result = new HashMap<>();
        String category = "未分类";
        String tagName = "未分类标签";
        String tagId = "0";
        // socket相关配置
        int port = 7777;
        InetAddress localAddress = InetAddress.getLocalHost();
        String localIP = localAddress.getHostAddress();

        Socket socket = new Socket(localIP, port); // 创建一个 socket
        PrintWriter out;
        BufferedReader in;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(socket.toString());
            System.out.println("发送数据" + pic_URL);
            // 向服务器发送图片地址
            out.println(pic_URL);

            // 从输入流中读取数据
            String message = in.readLine();

            System.out.println("接收到服务器返回的消息" + message);
            String[] parts = message.split(";");
            tagName = parts[0].split(":")[1].split("、")[0];
            tagId = parts[0].split(":")[1].split("、")[1];
            category = parts[1].split(":")[1];
            result.put("tag", tagName);
            result.put("tagId", tagId);
            result.put("category", category);
            result.put("categoryId", categoryToId.get(category).toString());
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            socket.close();
            throw new MyException("与Python交互出现问题");
        }
        return result;
    }

    public List<HomePagePictureVo> copyList(List<Picture> pictureList) {
        List<HomePagePictureVo> homePagePictureVos = new ArrayList<>();
        for (Picture picture : pictureList) {
            homePagePictureVos.add(copy(picture));
        }
        return homePagePictureVos;
    }

    public HomePagePictureVo copy(Picture picture) {
        HomePagePictureVo homePagePictureVo = new HomePagePictureVo(
                picture.getId(),
                picture.getUrl(),
                picture.getViewCount(),
                picture.getLikeCount(),
                picture.getCommentCount(),
                picture.getDownloadCount(),
                picture.getPictureName(),
                picture.getDescription()
        );
        return homePagePictureVo;
    }
}




