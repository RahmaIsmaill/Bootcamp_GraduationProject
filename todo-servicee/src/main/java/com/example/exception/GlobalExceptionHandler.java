package com.example.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, GlobalException.class})
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception exception) {

        ErrorResponse errorResponse = new ErrorResponse();


        if (exception instanceof MethodArgumentNotValidException validationException)  {
            List<FieldError> fieldErrors = validationException.getBindingResult().getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                errorResponse.addError(fieldError.getField(), fieldError.getDefaultMessage());
            }
        }

        if (exception instanceof GlobalException GlobalException) {
            GlobalException.getErrors().forEach(errorResponse::addError);
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception exception) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addError("Server Error", exception.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}