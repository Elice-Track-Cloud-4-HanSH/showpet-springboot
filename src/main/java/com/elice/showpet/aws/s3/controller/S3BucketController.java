package com.elice.showpet.aws.s3.controller;

import com.elice.showpet.aws.s3.service.S3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3/bucket")
@RequiredArgsConstructor
public class S3BucketController {
  private final S3BucketService s3BucketService;

  @PostMapping
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      String fileUrl = s3BucketService.uploadFile(file);
      return new ResponseEntity<>(fileUrl, HttpStatus.OK);
    } catch (IOException e) {
      e.printStackTrace();
      return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{filename}")
  public ResponseEntity<String> deleteBucket(@PathVariable("filename") String filename) {
    s3BucketService.deleteFile(filename);
    return new ResponseEntity<>("Hello World!", HttpStatus.OK);
  }
}
