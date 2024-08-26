package com.elice.showpet.comment.mapper;

import com.elice.showpet.comment.dto.CommentRequestDto;
import com.elice.showpet.comment.dto.CommentResponseDto;
import com.elice.showpet.comment.entity.Comment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-26T20:16:31+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 22.0.1 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto) {
        if ( commentRequestDto == null ) {
            return null;
        }

        Comment.CommentBuilder comment = Comment.builder();

        comment.content( commentRequestDto.getContent() );

        return comment.build();
    }

    @Override
    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDto commentResponseDto = new CommentResponseDto();

        commentResponseDto.setId( comment.getId() );
        commentResponseDto.setContent( comment.getContent() );
        commentResponseDto.setCreatedAt( comment.getCreatedAt() );
        commentResponseDto.setUpdatedAt( comment.getUpdatedAt() );

        return commentResponseDto;
    }
}
