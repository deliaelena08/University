package com.ppdm.backend.post.exception;

public class PostAlreadyExistException extends RuntimeException {
    public PostAlreadyExistException() {
        super("Post Already Exist");
    }
}
