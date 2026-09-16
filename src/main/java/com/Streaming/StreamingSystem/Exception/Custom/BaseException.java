package com.Streaming.StreamingSystem.Exception.Custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private HttpStatus httpStatus;

    public BaseException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BaseException(String message,Throwable cause,  HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;

    }

}
