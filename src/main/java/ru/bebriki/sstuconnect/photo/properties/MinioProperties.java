package ru.bebriki.sstuconnect.photo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String bucket;
    private String accessKey;
    private String secretKey;
    private String url;
}


