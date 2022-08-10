package com.hong.dk.bookcollect.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;

    @Bean
    public MinioClient minioClient() { // 创建 MinioClient 对象
        return MinioClient.builder()
                .endpoint(endpoint) // 设置 Minio 服务器地址
                .credentials(accessKey, secretKey) // 设置 Minio 服务器的访问凭证
                .build(); // 创建 MinioClient 对象
    }
}