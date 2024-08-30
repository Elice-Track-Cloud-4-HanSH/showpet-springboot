package com.elice.showpet.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String content;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
