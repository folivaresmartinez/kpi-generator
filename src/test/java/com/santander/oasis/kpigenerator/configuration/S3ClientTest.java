package com.santander.oasis.kpigenerator.configuration;

import com.amazonaws.services.s3.model.Bucket;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.santander.oasis.kpigenerator.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class S3ClientTest {

    @InjectMocks
    private S3Client s3Client;
    @Mock
    private S3Properties s3Properties;

    @BeforeEach
    public void mockProperties() {
        Mockito.lenient().when(s3Properties.getAccessKey()).thenReturn(Constants.ACCESS_KEY);
        Mockito.lenient().when(s3Properties.getSecretKey()).thenReturn(Constants.SECRET_KEY);
        Mockito.lenient().when(s3Properties.getEndpoint()).thenReturn(Constants.ENDPOINT);
    }

    @Test
    public void whenRequestTheListOfAvailableBucketsIsNotEmptyAndOurBucketIsPresent() {
        List<String> buckets = s3Client.amazonS3().listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        assertTrue(buckets != null);
        assertTrue(buckets.size() != 0);
    }

    @Test
    public void whenCheckIfBucketExistReturnTrue() {
        List<String> buckets = s3Client.amazonS3().listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        assertTrue(buckets != null);
        assertTrue(buckets.contains(Constants.REAL_BUCKET));
    }

    @Test
    public void whenCheckIfFakeBucketExistReturnFalse() {
        List<String> buckets = s3Client.amazonS3().listBuckets().stream().map(Bucket::getName).collect(Collectors.toList());
        assertTrue(buckets != null);
        assertFalse(buckets.contains(Constants.FAKE_BUCKET));
    }

    @Test
    public void whenGetRegionNameIsNotNull() {
        assertNotNull(s3Client.amazonS3().getRegionName());
    }

    @Test
    public void whenGetRegionNameReturnTheCorrectRegionName() {
        assertEquals(Constants.REGION_NAME, s3Client.amazonS3().getRegionName());
    }

    @Test
    public void whenUploadFileThenCanDownloadItAndDeleteIt(){
        PutObjectResult putObjectResult = s3Client.amazonS3().putObject(Constants.REAL_BUCKET, Constants.FILE_TO_UPLOAD, Constants.FILE_CONTENT);
        assertTrue(putObjectResult != null);
        S3Object s3Object = s3Client.amazonS3().getObject(Constants.REAL_BUCKET, Constants.FILE_TO_UPLOAD);
        assertTrue(s3Object != null);
        assertTrue(s3Object.getObjectContent() != null);
        s3Client.amazonS3().deleteObject(Constants.REAL_BUCKET, Constants.FILE_TO_UPLOAD);
    }
}
