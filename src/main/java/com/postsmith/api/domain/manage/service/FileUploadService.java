package com.postsmith.api.domain.manage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    @Value("${ncp.object-storage.bucket-name}")
    private String bucketName;

    @Value("${ncp.object-storage.region}")
    private String region;

    @Value("${ncp.access-key}")
    private String accessKey;

    @Value("${ncp.secret-key}")
    private String secretKey;

    @Value("${ncp.object-storage.endpoint}")
    private String endpoint;

    private S3Client getS3Client() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFileToS3(file, "images/");
    }

    public String uploadVideo(MultipartFile file) throws IOException {
        return uploadFileToS3(file, "videos/");
    }

    public String uploadFile(MultipartFile file) throws IOException {
        return uploadFileToS3(file, "files/");
    }

    private String uploadFileToS3(MultipartFile file, String folder) throws IOException {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String key = folder + fileName;

            S3Client s3Client = getS3Client();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("파일 업로드 성공: {}", key);
            return endpoint + "/" + bucketName + "/" + key;

        } catch (S3Exception e) {
            log.error("S3 업로드 실패: {}", e.getMessage());
            throw new IOException("S3 업로드 실패: " + e.getMessage());
        }
    }

    private String generateFileName(String originalFileName) {
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}