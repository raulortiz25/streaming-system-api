package com.Streaming.StreamingSystem.Exception;

import com.Streaming.StreamingSystem.Exception.Custom.BaseException;
import com.Streaming.StreamingSystem.Exception.DtoException.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse>handleBaseException(BaseException ex, HttpServletRequest request){

        log.error("Excepcion ocurrida: {} - URL: {}", ex, request.getRequestURI());

        HttpStatus status = ex.getHttpStatus();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, status);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request ){

        log.warn("Error de validacion en la peticon: {}", ex.getMessage(), request.getRequestURI());

        Map<String, String> error = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()){
            error.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "Error de validacion en los datos de entrada",
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, status);

    }


}
