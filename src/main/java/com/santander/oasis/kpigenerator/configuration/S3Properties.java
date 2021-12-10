package com.santander.oasis.kpigenerator.configuration;

import lombok.Data;

@Data
public class S3Properties {
    private String bucketName;
    private String endpoint;
    private String accessKey;
    private String secretKey;
}

