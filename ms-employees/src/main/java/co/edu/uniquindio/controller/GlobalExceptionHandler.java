package co.edu.uniquindio.controller;

import co.edu.uniquindio.error.ApiError;
import co.edu.uniquindio.exception.DepartmentNotFoundException;
import co.edu.uniquindio.exception.DepartmentUnavailableException;
import jakarta.servlet.http.HttpServlet;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = handleValidationException(ex);
        return ResponseEntity.status(status.value()).body(problemDetail);
    }

    private ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String details = getErrorsDetails(ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), details);
        problemDetail.setType(URI.create("http://localhost:8080/errors/bad-request"));
        problemDetail.setTitle("Bad request");
        problemDetail.setInstance(ex.getBody().getInstance());
        problemDetail.setProperty("timestamp", Instant.now()); // additional data
        return problemDetail;
    }

    private String getErrorsDetails(MethodArgumentNotValidException ex) {
        return Optional.of(ex.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                        .filter(msg -> !ObjectUtils.isEmpty(msg))
                        .reduce("Please make sure to provide a valid request, ", (a, b) -> a + " " + b)
                )
                .orElse("").toString();
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(DepartmentNotFoundException ex, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .status(404)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DepartmentUnavailableException.class)
    public ResponseEntity<ApiError> handleUnavailable(DepartmentUnavailableException ex, HttpServletRequest request){
        ApiError error = ApiError.builder()
                .status(503)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
