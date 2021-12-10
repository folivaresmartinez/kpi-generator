package com.santander.oasis.kpigenerator.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.santander.oasis.kpigenerator.configuration.S3Client;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;
    private final AmazonS3 amazonS3;

    public S3Object getObject(String filename){
    	
    	ObjectListing objects = amazonS3.listObjects(s3Client.getProperties().getBucketName());
    	
    	S3Object s3Object= amazonS3.getObject(s3Client.getProperties().getBucketName(), filename);
    	
        return amazonS3.getObject(s3Client.getProperties().getBucketName(), filename);
    }

    public PutObjectResult putObject(String filename, String content){
        return amazonS3.putObject(s3Client.getProperties().getBucketName(), filename, content);
    }

    public void deleteObject(String filename){
        amazonS3.deleteObject(s3Client.getProperties().getBucketName(), filename);
    }

}

