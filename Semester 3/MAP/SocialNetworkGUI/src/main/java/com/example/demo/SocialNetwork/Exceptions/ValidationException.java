package com.example.demo.SocialNetwork.Exceptions;

public class ValidationException extends RuntimeException {
    /*
    * Exceptiile care vor fi aruncate in cazul in care datele transmise nu sunt valide
    * */
    public ValidationException(String message) {
        super(message);
    }

}