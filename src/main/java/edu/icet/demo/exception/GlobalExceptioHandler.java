package edu.icet.demo.exception;

import edu.icet.demo.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptioHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MissingAttributeException.class)
    ResponseEntity<ErrorResponse> handleMissingAttributeException(MissingAttributeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().errorMessage(exception.getMessage()).status("BAD_REQUEST").build());
    }

    @ExceptionHandler(DataNotFoundException.class)
    ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder().errorMessage(exception.getMessage()).status("NOT_FOUND").build());
    }
}
