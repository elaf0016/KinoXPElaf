package dk.kinoxp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

// Sender fejlbeskeden med i svaret som {"message": "..."}, så frontend kan vise den
@RestControllerAdvice
public class FejlHaandtering {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> haandterFejl(ResponseStatusException fejl) {
        return ResponseEntity.status(fejl.getStatusCode())
                .body(Map.of("message", fejl.getReason() == null ? "Der skete en fejl" : fejl.getReason()));
    }
}
