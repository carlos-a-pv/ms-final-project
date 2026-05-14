package co.edu.uniquindio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.net.URI;
import java.time.Instant;

public class UserEmailAlreadyExists extends ErrorResponseException {

    public UserEmailAlreadyExists(String email, String path) {
        super(HttpStatus.CONFLICT, problemDetailFrom("User with email: "+ email + " already exists", path), null);
    }

    private static ProblemDetail problemDetailFrom(String message, String path) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problemDetail.setType(URI.create("http://localhost:8080/errors/UserEmailAlreadyExists"));
        problemDetail.setTitle("User email already exists");
        problemDetail.setInstance(URI.create(path));
        problemDetail.setProperty("timestamp", Instant.now()); // additional data
        return problemDetail;
    }
}
