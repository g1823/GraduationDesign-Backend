package com.albummanagement.service;

import com.albummanagement.dao.entity.Picture;
import com.albummanagement.view.dto.PageParam;
import com.albummanagement.view.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author lenovo
* @description 针对表【picture】的数据库操作Service
* @createDate 2023-04-09 18:34:16
*/
public interface PictureService extends IService<Picture> {


    Result getAllPublicPicture(PageParam pageParam);

    Result uploadPictureNotClassify(MultipartFile file, Boolean isPublic);

    Result uploadPictureClassify(MultipartFile file, Boolean isPublic) throws IOException;

    Result getCategoryPics(String categoryId);

    Result updateViewCount(Long id);

    Result getHotPicturesByLike();

    Result getHotPicturesByComment();

    Result getHotPicturesByView();

    Result searchPics(String name);
}
