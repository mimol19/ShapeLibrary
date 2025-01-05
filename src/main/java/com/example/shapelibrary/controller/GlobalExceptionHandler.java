package com.example.shapelibrary.controller;
//
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.MethodArgumentTypeMismatchException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.time.LocalDateTime;
//import java.util.NoSuchElementException;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
//        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), "Entity not found", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
//        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), "Invalid argument type", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
//        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), "Element not found", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
//        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), "Invalid input", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
//    }
//
//    @ExceptionHandler(Exception.class)  // Obsługa wszystkich innych wyjątków
//    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
//        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), "Internal Server Error", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
//}
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Entity Not Found");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Input");
        return problemDetail;
    }
}
