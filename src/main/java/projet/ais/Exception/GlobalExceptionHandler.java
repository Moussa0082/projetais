package projet.ais.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Erreur JSON (mauvais format, etc.)
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> handleJsonError(JsonProcessingException e) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Erreur de format JSON : " + e.getOriginalMessage());
    }

    // Erreur de paramètres invalides
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return buildResponse(HttpStatus.CONFLICT, "Argument invalide : " + e.getMessage());
    }

    // Violation de contrainte Hibernate (clé étrangère, unique, etc.)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException e) {
        String constraint = e.getConstraintName() != null ? e.getConstraintName() : "Contrainte inconnue";
        return buildResponse(HttpStatus.CONFLICT,
                "Erreur de contrainte : " + constraint +
                ". Impossible de modifier ou supprimer une ressource utilisée ailleurs.");
    }

    // Violation d'intégrité native SQL
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> handleSQLIntegrityConstraint(SQLIntegrityConstraintViolationException e) {
        return buildResponse(HttpStatus.CONFLICT,
                "Impossible de modifier ou supprimer une ressource utilisée ailleurs : " );
    }

    // Erreur générique avec filtrage et reformulation
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericError(Exception e) {
        String rawMessage = e.getMessage();
        String message;

        if (rawMessage != null && rawMessage.contains("Cannot delete or update a parent row")) {
            // Message personnalisé pour erreurs de type "clé étrangère"
            message = "Opération impossible : l’élément que vous essayez de supprimer est référencé par une autre entité.";
        } else {
            // Fallback pour toute autre erreur
            message = "Erreur inattendue : ";
        }

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    // Méthode pour formater les réponses d'erreur en JSON structuré
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}