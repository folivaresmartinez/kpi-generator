package com.santander.oasis.kpigenerator.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.santander.oasis.kpigenerator.configuration.S3Properties;
import com.santander.oasis.kpigenerator.configuration.S3Client;
import com.santander.oasis.kpigenerator.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;
    @Mock
    private S3Client s3Client;
    @Mock
    private S3Properties s3Properties;
    @Mock
    private AmazonS3 amazonS3;

    private static S3Object s3Object = new S3Object();
    private static PutObjectResult putObjectResult = new PutObjectResult();


    @BeforeEach
    public void mockProperties() {
        Mockito.lenient().when(s3Client.getProperties()).thenReturn(s3Properties);
        Mockito.lenient().when(s3Properties.getBucketName()).thenReturn(Constants.REAL_BUCKET);
    }

    @Test
    public void whenDownloadFileIsOkAndNotNull(){
        Mockito.lenient().when(amazonS3.getObject(Constants.REAL_BUCKET, Constants.FILENAME)).thenReturn(s3Object);
        S3Object anotherS3Object = s3Service.getObject(Constants.FILENAME);
        assertNotNull(anotherS3Object);
        assertEquals(s3Object, anotherS3Object);
    }

    @Test
    public void whenUploadFileIsOk(){
        Mockito.lenient().when(amazonS3.putObject(Constants.REAL_BUCKET, Constants.FILENAME, Constants.FILENAME)).thenReturn(putObjectResult);
        PutObjectResult anotherPutObjectResult = s3Service.putObject(Constants.FILENAME, Constants.FILENAME);
        assertEquals(putObjectResult, anotherPutObjectResult);
    }

    @Test
    public void whenDeleteFileIsOk(){
        s3Service.deleteObject(Constants.FILENAME);
    }


}
