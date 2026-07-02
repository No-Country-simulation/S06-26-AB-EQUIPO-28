package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import tech.nocountry.talent.appbitservice.shared.domain.exceptions.BaseException;
import tech.nocountry.talent.appbitservice.shared.domain.exceptions.ResourceNotFoundException;
import tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.handlers.*;
import tech.nocountry.talent.appbitservice.userandroles.domain.exceptions.UserDomainException;

/**
 * Manejador centralizado de excepciones HTTP.
 * Delega a manejadores especificos para cada tipo de excepcion.
 *
 * <p>NOTA: Usamos {@code @Order(HIGHEST_PRECEDENCE)} para que este handler
 * se ejecute ANTES que el {@code ProblemDetailsExceptionHandler} de Spring Boot
 * (order=0) cuando {@code spring.mvc.problemdetails.enabled=true}.
 * Sin esto, Spring maneja las excepciones MVC con mensajes en inglés y sin logs.</p>
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
@Import(ExceptionHandlerConfig.class)
public class GlobalExceptionHandler {
    private final BaseExceptionHandler baseExceptionHandler;
    private final ValidationExceptionHandler validationExceptionHandler;
    private final DatabaseExceptionHandler databaseExceptionHandler;
    private final AuthenticationExceptionHandler authenticationExceptionHandler;
    private final GenericExceptionHandler genericExceptionHandler;

    @ExceptionHandler(BaseException.class)
    public ProblemDetail handleBaseException(BaseException ex, HttpServletRequest request) {
        return baseExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(UserDomainException.class)
    public ProblemDetail handleUserDomainException(UserDomainException ex, HttpServletRequest request) {
        log.warn("[USER_DOMAIN_ERROR] {}: {}", ex.getClass().getSimpleName(), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setProperty("code", "USR_ERROR");

        return problemDetail;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("[GEN_NOT_FOUND] Recurso no encontrado: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setProperty("code", "NOT_FOUND");

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        return validationExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return validationExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        return databaseExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return genericExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return genericExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return genericExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        return authenticationExceptionHandler.handle(ex, request.getRequestURI());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        log.warn("[HTTP_METHOD_NOT_ALLOWED] Método HTTP no soportado: {} | Path: {}",
                ex.getMethod(), request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.METHOD_NOT_ALLOWED, "El método HTTP " + ex.getMethod() + " no está soportado para esta ruta");
        problemDetail.setProperty("code", "HTTP_METHOD_NOT_ALLOWED");

        return problemDetail;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ProblemDetail handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        log.warn("[MEDIA_TYPE_NOT_SUPPORTED] Tipo de contenido no soportado: {} | Path: {}",
                ex.getContentType(), request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "El tipo de contenido " + ex.getContentType() + " no es soportado. Tipos soportados: " + ex.getSupportedMediaTypes());
        problemDetail.setProperty("code", "MEDIA_TYPE_NOT_SUPPORTED");

        return problemDetail;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ProblemDetail handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
        log.warn("[MEDIA_TYPE_NOT_ACCEPTABLE] Tipo de respuesta no aceptable: {} | Path: {}",
                ex.getMessage(), request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_ACCEPTABLE,
                "El tipo de medio solicitado no está disponible");
        problemDetail.setProperty("code", "MEDIA_TYPE_NOT_ACCEPTABLE");

        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.warn("[MISSING_PARAM] Falta el parámetro requerido '{}' de tipo '{}' | Path: {}",
                ex.getParameterName(), ex.getParameterType(), request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Falta el parámetro requerido: " + ex.getParameterName());
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");
        problemDetail.setProperty("parameter", ex.getParameterName());

        return problemDetail;
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ProblemDetail handleMissingServletRequestPart(
            MissingServletRequestPartException ex, HttpServletRequest request) {
        log.warn("[MISSING_PART] Falta la parte requerida '{}' en la solicitud multipart | Path: {}",
                ex.getRequestPartName(), request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Falta la parte requerida en la solicitud: " + ex.getRequestPartName());
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");
        problemDetail.setProperty("requestPart", ex.getRequestPartName());

        return problemDetail;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.warn("[MAX_UPLOAD_EXCEEDED] Tamaño de archivo excede el máximo permitido | Path: {}",
                request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONTENT_TOO_LARGE,
                "El tamaño del archivo excede el máximo permitido");
        problemDetail.setProperty("code", "GEN_BAD_REQUEST");

        return problemDetail;
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ProblemDetail handleAsyncRequestTimeout(
            AsyncRequestTimeoutException ex, HttpServletRequest request) {
        log.error("[ASYNC_TIMEOUT] Tiempo de espera de la solicitud asíncrona agotado | Path: {}",
                request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "La solicitud asíncrona excedió el tiempo de espera");
        problemDetail.setProperty("code", "GEN_INTERNAL_ERROR");

        return problemDetail;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        log.warn("[ACCESS_DENIED] Acceso denegado a: {}", request.getRequestURI());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "No tienes permiso para acceder a este recurso");
        problemDetail.setProperty("code", "AUTH_FORBIDDEN");

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex, HttpServletRequest request) {
        log.error("[UNHANDLED_EXCEPTION] {}: {} | Path: {}",
                ex.getClass().getSimpleName(), ex.getMessage(), request.getRequestURI(), ex);

        return genericExceptionHandler.handleUnexpected(ex, request.getRequestURI());
    }
}