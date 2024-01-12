package com.sky.controller;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/admin/common/upload")
public class FilesUploadController {
    @Autowired
    AliOssUtil aliOssUtil;

    @PostMapping
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            //截取原始文件名的后缀  sdgsdg.png
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新的文件名
            String objectName = UUID.randomUUID().toString() + substring;
            //文件的请求路径
            String filePat = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePat);
        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
        }
        return null;
    }
}
