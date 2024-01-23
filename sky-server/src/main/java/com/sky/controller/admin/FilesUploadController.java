package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//文件上传
@RestController
@Slf4j
@RequestMapping("/admin/common/upload")
public class FilesUploadController {
    @Autowired
    AliOssUtil aliOssUtil;

    //文件上传
    @PostMapping
    public Result<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传：{}", file);
            //文件的请求路径
            String filePat = aliOssUtil.upload(file);
            return Result.success(filePat);
    }
}
