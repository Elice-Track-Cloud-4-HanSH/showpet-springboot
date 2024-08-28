package com.elice.showpet.aws.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.elice.showpet.common.exception.BucketFileNotDeletedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class S3BucketService {
  private final AmazonS3Client s3Client;

  @Value("${aws.s3.bucket.name}")
  private String bucketName;

  private final String bucketUrl = "https://showpet.s3.ap-northeast-2.amazonaws.com/";

  public String uploadFile(MultipartFile file) throws IOException {
    String fileName = makeHashedFileName(file);
    String fileUrl = bucketUrl + fileName;
    ObjectMetadata metadata =  createFileMetadata(file);
    try {
      s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
      return fileUrl;
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }

  public String uploadFile(MultipartFile file, String path) throws IOException {
    String fileName = (path.endsWith("/") ? path : path + "/") + makeHashedFileName(file);
    String fileUrl = bucketUrl + fileName;
    ObjectMetadata metadata =  createFileMetadata(file);
    try {
      s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
      return fileUrl;
    } catch (IOException e) {
      throw new IOException(e.getMessage());
    }
  }

  public void deleteFile(String fileName) throws BucketFileNotDeletedException {
    if (fileName.startsWith("https")) {
      fileName = fileName.substring(bucketUrl.length());
    }
    try {
      s3Client.deleteObject(bucketName, fileName);
    } catch (Exception e) {
      throw new BucketFileNotDeletedException("S3 버킷의 파일이 삭제되지 않았습니다.");
    }
  }

  private String makeHashedFileName(MultipartFile file) {
    LocalDateTime now = LocalDateTime.now();
    String fileName = file.getOriginalFilename();
    int hashed = (fileName + now.format(DateTimeFormatter.ofPattern("HHmmss"))).hashCode();
    return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + hashed;
  }

  private ObjectMetadata createFileMetadata(MultipartFile file) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(file.getContentType());
    metadata.setContentLength(file.getSize());
    return metadata;
  }
}
