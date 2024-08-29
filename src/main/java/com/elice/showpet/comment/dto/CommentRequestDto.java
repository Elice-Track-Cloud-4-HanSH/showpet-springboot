package com.elice.showpet.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentRequestDto {
    @NotBlank(message = "댓글을 입력해 주세요.")
    @Size(max = 1000, message = "1,000자까지 입력 가능합니다.")
    private String content;

    private String password;
}
