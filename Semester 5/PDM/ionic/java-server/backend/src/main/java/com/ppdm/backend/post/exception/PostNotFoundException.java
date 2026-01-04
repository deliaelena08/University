package com.ppdm.backend.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Post Not Found");
    }
}
