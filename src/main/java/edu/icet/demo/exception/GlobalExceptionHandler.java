package edu.icet.demo.exception;

import edu.icet.demo.dto.response.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String BAD_REQUEST = "BAD_REQUEST";

    @ExceptionHandler(DataNotFoundException.class)
    ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder().errorMessage(exception.getMessage()).status("NOT_FOUND").build());
    }

    @ExceptionHandler(MissingAttributeException.class)
    ResponseEntity<ErrorResponse> handleMissingAttributeException(MissingAttributeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().errorMessage(exception.getMessage()).status(BAD_REQUEST).build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
        String errorMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorMessage(errorMessage)
                        .status(BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(DataMisMatchException.class)
    ResponseEntity<ErrorResponse> handleDataMisMatchException(DataMisMatchException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .errorMessage(exception.getMessage())
                        .status(BAD_REQUEST)
                        .build());
    }

    @ExceptionHandler(DataDuplicateException.class)
    ResponseEntity<ErrorResponse> handleDataDuplicateException(DataDuplicateException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .errorMessage(exception.getMessage())
                        .status("CONFLICT")
                        .build());
    }

    @ExceptionHandler(DeletionException.class)
    ResponseEntity<ErrorResponse> handleDeletionException(DeletionException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.builder()
                        .errorMessage(exception.getMessage())
                        .status("CONFLICT")
                        .build());
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .errorMessage(exception.getMessage())
                        .status("UNAUTHORIZED")
                        .build());
    }
}