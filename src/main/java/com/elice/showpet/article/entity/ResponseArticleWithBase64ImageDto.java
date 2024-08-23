package com.elice.showpet.article.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseArticleWithBase64ImageDto {
  private Long id;
  private String title;
  private String content;
  private String image;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public ResponseArticleWithBase64ImageDto(ResponseArticleDto dto) {
    id = dto.getId();
    title = dto.getTitle();
    content = dto.getContent();
    try {
      String[] imageInfo = dto.getImage().split(","); // image[0] : mimeType, image[1] : base64 data
      image = "data:" + imageInfo[0] + ";base64," + imageInfo[1];
    } catch (Exception e) {
      image = null;
    }
    createdAt = dto.getCreatedAt();
    updatedAt = dto.getUpdatedAt();
  }
}