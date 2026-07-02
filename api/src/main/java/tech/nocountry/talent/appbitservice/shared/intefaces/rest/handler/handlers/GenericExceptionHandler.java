package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Manejador generico para excepciones no especificas (fallback).
 */
@Slf4j
@Component
public class GenericExceptionHandler {
    public ProblemDetail handle(IllegalArgumentException ex, String path) {
        log.warn("Error de argumento ilegal en la ruta {}: {}", path, ex.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");

        return problemDetail;
    }

    public ProblemDetail handle(HttpMessageNotReadableException ex, String path) {
        log.warn("Error de parseo JSON en la ruta {}: {}", path, ex.getMessage());
        String message = detectParseErrorMessage(ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, message);
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");

        return problemDetail;
    }

    public ProblemDetail handle(MethodArgumentTypeMismatchException ex, String path) {
        log.warn("Error de argumento en la ruta {}: {}", path, ex.getMessage());
        String paramName = ex.getName();
        String actualValue = ex.getValue() != null ? ex.getValue().toString() : "null";
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido";

        String message = String.format("El valor '%s' no es valido para el parametro '%s'. Se esperaba un tipo '%s'.",
                actualValue, paramName, expectedType);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, message);
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");

        return problemDetail;
    }

    public ProblemDetail handleUnexpected(Exception ex, String path) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado. Por favor, intente nuevamente o contacte a soporte.");
        problemDetail.setProperty("code", "GEN_INTERNAL_ERROR");

        return problemDetail;
    }

    private String detectParseErrorMessage(String message) {
        if (message == null) {
            return "El formato de los datos es invalido";
        }

        if (message.contains("Required request body is missing")) {
            return "El cuerpo de la solicitud es requerido";
        } else if (message.contains("JSON parse error") || message.contains("Cannot deserialize")) {
            return "Error al procesar el JSON. Verifica la sintaxis o el tipo de datos enviados";
        } else if (message.contains("Invalid UTF-8")) {
            return "El texto contiene caracteres invalidos";
        }

        return "El formato de los datos es invalido. Verifica que el JSON este correctamente formateado";
    }
}