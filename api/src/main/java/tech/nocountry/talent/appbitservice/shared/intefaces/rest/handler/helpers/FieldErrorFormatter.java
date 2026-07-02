package tech.nocountry.talent.appbitservice.shared.intefaces.rest.handler.helpers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Formatea errores de validacion para respuestas RFC 7807.
 */
@Slf4j
@Component
public class FieldErrorFormatter {
    private static final Map<String, String> FIELD_NAME_TRANSLATIONS = Map.ofEntries(
            Map.entry("username", "nombre de usuario"),
            Map.entry("password", "contrasenia"),
            Map.entry("email", "correo electronico"),
            Map.entry("companyEmail", "correo electronico"),
            Map.entry("ruc", "RUC"),
            Map.entry("companyRuc", "RUC"),
            Map.entry("name", "nombre"),
            Map.entry("firstName", "nombre"),
            Map.entry("lastName", "apellido"),
            Map.entry("businessName", "razon social"),
            Map.entry("phone", "telefono"),
            Map.entry("phoneNumber", "telefono"),
            Map.entry("companyPhone", "telefono"),
            Map.entry("branchId", "ID de sucursal"),
            Map.entry("companyId", "ID de empresa"),
            Map.entry("roleId", "ID de rol"),
            Map.entry("employeeId", "ID de empleado"),
            Map.entry("categoryId", "ID de categoria"),
            Map.entry("productId", "ID de producto"),
            Map.entry("zoneId", "ID de zona"),
            Map.entry("tableId", "ID de mesa"),
            Map.entry("documentNumber", "numero de documento"),
            Map.entry("documentType", "tipo de documento"),
            Map.entry("companyName", "nombre de empresa"),
            Map.entry("status", "estado"),
            Map.entry("address", "direccion"),
            Map.entry("roles", "roles")
    );

    public String translateFieldName(String fieldName) {
        if (fieldName == null) {
            return "campo desconocido";
        }
        String lowerField = fieldName.toLowerCase();
        return FIELD_NAME_TRANSLATIONS.getOrDefault(lowerField, fieldName);
    }

    public List<ValidationError> formatFieldErrors(List<org.springframework.validation.FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fe -> new ValidationError(
                        translateFieldName(fe.getField()),
                        formatFieldErrorMessage(fe),
                        fe.getRejectedValue()))
                .toList();
    }

    public String formatFieldErrorMessage(org.springframework.validation.FieldError fe) {
        String fieldName = translateFieldName(fe.getField());
        String message = fe.getDefaultMessage();

        if (message == null) {
            return String.format("El campo '%s' es invalido", fieldName);
        }

        if (message.contains("must not be null") || message.contains("must not be blank")) {
            return String.format("El campo '%s' es obligatorio", fieldName);
        } else if (message.contains("must not be empty")) {
            return String.format("El campo '%s' no puede estar vacio", fieldName);
        } else if (message.contains("size must be between")) {
            return String.format("El campo '%s' debe tener entre %s caracteres",
                    fieldName, extractSizeRange(message));
        } else if (message.contains("must be a valid companyEmail")) {
            return String.format("El campo '%s' debe ser un correo valido", fieldName);
        } else if (message.contains("must match")) {
            return String.format("El campo '%s' tiene un formato invalido", fieldName);
        }

        return String.format("El campo '%s': %s", fieldName, message);
    }

    private String extractSizeRange(String message) {
        try {
            String[] parts = message.split("between ");
            if (parts.length > 1) {
                return parts[1].replaceAll("and", "y").replaceAll("[\\(\\)]", "");
            }
        } catch (Exception e) {
            log.trace("Error extracting size range: {}", e.getMessage());
        }
        return "el minimo y maximo permitido";
    }

    public String extractFieldName(String propertyPath) {
        if (propertyPath == null) {
            return "";
        }
        String[] parts = propertyPath.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : propertyPath;
    }

    public record ValidationError(String field, String message, Object rejectedValue) {}
}