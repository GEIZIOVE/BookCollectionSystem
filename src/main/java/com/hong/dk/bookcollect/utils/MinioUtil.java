package com.hong.dk.bookcollect.utils;


import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class MinioUtil {

    @Resource
    private MinioClient minioClient;

    /**
     * 查看存储bucket是否存在
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()); //BucketExistsArgs是一个构造器类，用来构造BucketExistsArgs对象
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     */
    public void makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public  String upload(MultipartFile file ,String bucketName,String userId) {
        String originalFilename = file.getOriginalFilename();
        //判断文件名是否为空
        if (originalFilename == null || "".equals(originalFilename)) {
           throw new RuntimeException("文件名不能为空");
        }
        //判断是否存在bucket
        if (!bucketExists(bucketName)) {
            makeBucket(bucketName);
        }
        //获取文件后缀名
        String fileName = userId+originalFilename.substring(originalFilename.lastIndexOf("."));
        //获取当前日期，格式为yyyyMMdd
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String s = dateFormat.format(date);

        String objectName = s + "/" + fileName;
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder() //PutObjectArgs是一个构造器类，用来构造PutObjectArgs对象
                    .bucket(bucketName) //bucket名称
                    .object(objectName) //object名称
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()) //文件类型
                    .build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }





}