package com.albummanagement.controller;

import com.albummanagement.service.PictureService;
import com.albummanagement.view.dto.PageParam;
import com.albummanagement.view.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author:gj
 * @DateTime:2023/4/9 16:54
 **/
@RestController
@RequestMapping("pictures")
public class PictureController {

    PictureService pictureService;

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    //首页分页查询
    @GetMapping
    public Result getAllPublicPicture(@RequestParam(value = "current") Integer current, @RequestParam(value = "size") Integer size) {
        PageParam pageParam = new PageParam(current, size);
        return pictureService.getAllPublicPicture(pageParam);
    }


    @PostMapping
    public Result uploadPicture(@RequestParam("file") MultipartFile file, @RequestParam("autoClassify") String autoClassify, @RequestParam("isPublic") Boolean isPublic) throws IOException {
        if (autoClassify.equals("false")) {
            return pictureService.uploadPictureNotClassify(file,isPublic);
        } else {
            return pictureService.uploadPictureClassify(file, isPublic);
        }
    }

    @GetMapping("/updateView/{PicID}")
    public Result uploadPictureView(@PathVariable("PicID") String picId) {
        Long id = Long.parseLong(picId);
        return pictureService.updateViewCount(id);
    }

    /**
     * 获取某一分类的所有图片
     * @param categoryId
     * @return
     */
    @GetMapping("/getCategoryPics")
    public Result getCategoryPics(@RequestParam(value = "id") String categoryId){
        return pictureService.getCategoryPics(categoryId);
    }

    @GetMapping("hot")
    public Result getHotPicturesByLike(){
        return pictureService.getHotPicturesByLike();
    }

    @GetMapping("Comment")
    public Result getHotPicturesByComment(){
        return pictureService.getHotPicturesByComment();
    }

    @GetMapping("view")
    public Result getHotPicturesByView(){
        return pictureService.getHotPicturesByView();
    }

    @GetMapping("/search/{name}")
    public Result searchPics(@PathVariable("name") String name){
        return pictureService.searchPics(name);
    }

//     上传本地文件测试七牛云服务器
//    @PostMapping
//    public Result test() throws IOException {
//        File file = new File("D:/tu/S005Sayathefox/NO.02/图宫社0108.jpg");
//        FileInputStream inputStream = new FileInputStream(file);
//        MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);
//        QiniuUtils.upload(multipartFile, file.getName());
//        return Result.success(QiniuUtils.url + "/" + file.getName());
//    }


}
