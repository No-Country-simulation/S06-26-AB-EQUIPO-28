package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.helpers.FieldErrorFormatter;

import java.util.List;

/**
 * Manejador para excepciones de validacion.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationExceptionHandler {
    private final FieldErrorFormatter fieldErrorFormatter;

    public ProblemDetail handle(MethodArgumentNotValidException ex, String path) {
        List<FieldErrorFormatter.ValidationError> errors =
                fieldErrorFormatter.formatFieldErrors(ex.getBindingResult().getFieldErrors());
        log.warn("Error de validación en la ruta {}: {} errores", path, errors.size());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Los datos de la peticion contienen errores de validacion.");
        problemDetail.setProperty("code", "GEN_VALIDATION_ERROR");
        problemDetail.setProperty("invalid_params", errors);

        return problemDetail;
    }

    public ProblemDetail handle(ConstraintViolationException ex, String path) {
        log.warn("Error de validación en la ruta {}: {} errores", path, ex.getConstraintViolations().size());
        List<FieldErrorFormatter.ValidationError> errors = ex.getConstraintViolations()
                .stream()
                .map(v -> new FieldErrorFormatter.ValidationError(
                        fieldErrorFormatter.extractFieldName(v.getPropertyPath().toString()),
                        v.getMessage(),
                        null))
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Los datos no cumplen con las restricciones requeridas.");
        problemDetail.setProperty("code", "GEN_VALIDATION_ERROR");
        problemDetail.setProperty("invalid_params", errors);

        return problemDetail;
    }
}