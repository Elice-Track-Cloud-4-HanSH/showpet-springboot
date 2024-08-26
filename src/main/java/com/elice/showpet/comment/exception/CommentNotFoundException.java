package com.elice.showpet.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message){
        super(message);
    }
}
