package tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects;

import org.springframework.http.HttpStatus;

/**
 * Enumeration of standardized error codes for the application.
 * 
 * <p>Each error code follows the pattern: PREFIX_XXX where:
 * <ul>
 *   <li>PREFIX identifies the bounded context or category</li>
 *   <li>XXX is a sequential number</li>
 * </ul>
 * 
 * <p>These codes are used by the frontend to:
 * <ul>
 *   <li>Display user-friendly messages</li>
 *   <li>Perform specific actions based on error type</li>
 *   <li>Log and track specific error conditions</li>
 * </ul>
 */
public enum ErrorCodes {
    // ============================================
    // Employee Management (EMP)
    // ============================================
    EMP_NOT_FOUND("EMP_001", "El empleado no fue encontrado", HttpStatus.NOT_FOUND),
    EMP_ALREADY_EXISTS("EMP_002", "Ya existe un empleado vinculado a este usuario", HttpStatus.CONFLICT),
    EMP_INVALID_DATA("EMP_003", "Los datos del empleado son inválidos", HttpStatus.BAD_REQUEST),
    EMP_USER_NOT_FOUND("EMP_004", "El usuario vinculado al empleado no fue encontrado", HttpStatus.NOT_FOUND),

    // ============================================
    // User & Roles Management (USR)
    // ============================================
    USR_NOT_FOUND("USR_001", "El usuario no fue encontrado", HttpStatus.NOT_FOUND),
    USR_ALREADY_EXISTS("USR_002", "El usuario ya existe en el sistema", HttpStatus.CONFLICT),
    USR_INACTIVE("USR_003", "El usuario está inactivo", HttpStatus.BAD_REQUEST),
    USR_INVALID_CREDENTIALS("USR_004", "Credenciales inválidas", HttpStatus.UNAUTHORIZED),
    USR_INVALID_PASSWORD("USR_005", "La contraseña no cumple los requisitos de seguridad", HttpStatus.BAD_REQUEST),
    USR_APPLICATION_NOT_FOUND("USR_006", "La aplicación no fue encontrada", HttpStatus.NOT_FOUND),
    USR_CREATION_FAILED("USR_007", "Error al crear el usuario", HttpStatus.INTERNAL_SERVER_ERROR),
    USR_UPDATE_FAILED("USR_008", "Error al actualizar el usuario", HttpStatus.INTERNAL_SERVER_ERROR),
    USR_DELETE_FAILED("USR_009", "Error al eliminar el usuario", HttpStatus.INTERNAL_SERVER_ERROR),
    USR_ROLE_UPDATE_FAILED("USR_010", "Error al modificar los roles del usuario", HttpStatus.INTERNAL_SERVER_ERROR),
    USR_ROLE_NOT_FOUND("USR_011", "El rol especificado no fue encontrado en el sistema", HttpStatus.NOT_FOUND),
    USR_PASSWORD_EMPTY("USR_013", "La contraseña no puede estar vacía", HttpStatus.BAD_REQUEST),
    USR_PASSWORD_TOO_SHORT("USR_014", "La contraseña debe tener al menos {min} caracteres", HttpStatus.BAD_REQUEST),
    USR_USERNAME_EMPTY("USR_015", "El nombre de usuario no puede estar vacío", HttpStatus.BAD_REQUEST),
    USR_USERNAME_INVALID("USR_016", "El formato del nombre de usuario es inválido", HttpStatus.BAD_REQUEST),
    USR_EMAIL_INVALID("USR_017", "El formato del correo electrónico es inválido", HttpStatus.BAD_REQUEST),
    USR_EMAIL_DOMAIN_INVALID("USR_018", "El dominio del correo electrónico no es válido para este rol", HttpStatus.BAD_REQUEST),
    USR_RUC_INVALID("USR_019", "El RUC debe tener exactamente 11 dígitos numéricos", HttpStatus.BAD_REQUEST),
    USR_ROLE_REQUIRED("USR_020", "Debe especificar al menos un rol para el usuario", HttpStatus.BAD_REQUEST),

    // ============================================
    // Issuer Management (ISS)
    // ============================================
    ISS_NOT_FOUND("ISS_001", "El emisor no fue encontrado", HttpStatus.NOT_FOUND),
    ISS_ALREADY_EXISTS("ISS_002", "Ya existe un emisor con este RUC", HttpStatus.CONFLICT),
    ISS_INVALID_DATA("ISS_003", "Los datos del emisor son inválidos", HttpStatus.BAD_REQUEST),
    ISS_OPERATION_FAILED("ISS_004", "Error al procesar la operación del emisor", HttpStatus.INTERNAL_SERVER_ERROR),

    // ============================================
    // Menu Management (MNU)
    // ============================================
    MNU_PRODUCT_NOT_FOUND("MNU_001", "El producto no fue encontrado", HttpStatus.NOT_FOUND),
    MNU_PRODUCT_ALREADY_EXISTS("MNU_002", "Ya existe un producto con este código", HttpStatus.CONFLICT),
    MNU_CATEGORY_NOT_FOUND("MNU_003", "La categoría no fue encontrada", HttpStatus.NOT_FOUND),
    MNU_CATEGORY_ALREADY_EXISTS("MNU_004", "Ya existe una categoría con este nombre", HttpStatus.CONFLICT),

    // ============================================
    // Branch Management (BRN)
    // ============================================
    BRN_NOT_FOUND("BRN_001", "La branch no fue encontrada", HttpStatus.NOT_FOUND),
    BRN_ALREADY_EXISTS("BRN_002", "Ya existe una branch con este código para el emisor", HttpStatus.CONFLICT),
    BRN_OPERATION_FAILED("BRN_003", "Error al procesar la operación de la branch", HttpStatus.INTERNAL_SERVER_ERROR),
    BRN_EMPLOYEES_NOT_FOUND("BRN_004", "Error al obtener los empleados de la sucursal", HttpStatus.NOT_FOUND),

    // ============================================
    // Zone Management (ZON)
    // ============================================
    ZON_NOT_FOUND("ZON_001", "La zona no fue encontrada", HttpStatus.NOT_FOUND),
    ZON_ALREADY_EXISTS("ZON_002", "Ya existe una zona con este nombre para la sucursal", HttpStatus.CONFLICT),
    ZON_OPERATION_FAILED("ZON_003", "Error al procesar la operación de la zona", HttpStatus.INTERNAL_SERVER_ERROR),

    // ============================================
    // Table Management (TBL)
    // ============================================
    TBL_NOT_FOUND("TBL_001", "La mesa no fue encontrada", HttpStatus.NOT_FOUND),
    TBL_ALREADY_EXISTS("TBL_002", "Ya existe una mesa con este código para la zona", HttpStatus.CONFLICT),
    TBL_OPERATION_FAILED("TBL_003", "Error al procesar la operación de la mesa", HttpStatus.INTERNAL_SERVER_ERROR),

    // ============================================
    // Company Management (CMP)
    // ============================================
    CMP_NOT_FOUND("CMP_001", "La empresa no fue encontrada", HttpStatus.NOT_FOUND),
    CMP_ALREADY_EXISTS("CMP_002", "Ya existe una empresa con este RUC", HttpStatus.CONFLICT),
    CMP_INVALID_DATA("CMP_003", "Los datos de la empresa son inválidos", HttpStatus.BAD_REQUEST),

    // Company Settings
    CMP_SETTINGS_NOT_FOUND("CMP_SET_001", "La configuración de la empresa no fue encontrada", HttpStatus.NOT_FOUND),

    // ============================================
    // Reports Management (RPT)
    // ============================================
    RPT_NOT_FOUND("RPT_001", "El reporte no fue encontrado", HttpStatus.NOT_FOUND),

    // Subscription Plan Management
    SUB_PLAN_NOT_FOUND("SUB_PLAN_001", "El plan de suscripción no fue encontrado", HttpStatus.NOT_FOUND),
    SUB_PLAN_ALREADY_EXISTS("SUB_PLAN_002", "Ya existe un plan de suscripción con este código", HttpStatus.CONFLICT),
    SUB_PLAN_INVALID_DATA("SUB_PLAN_003", "Los datos del plan de suscripción son inválidos", HttpStatus.BAD_REQUEST),

    // Payment Method Catalog
    PAYMENT_METHOD_NOT_FOUND("PAY_METH_001", "El metodo de pago no fue encontrado", HttpStatus.NOT_FOUND),
    PAYMENT_METHOD_ALREADY_EXISTS("PAY_METH_002", "Ya existe un metodo de pago con este nombre", HttpStatus.CONFLICT),

    // Company Subscription
    CMP_SUBSCRIPTION_NOT_FOUND("CMP_SUB_001", "La suscripción de la empresa no fue encontrada", HttpStatus.NOT_FOUND),

    // ============================================
    // Inventory Management (INV)
    // ============================================
    INV_CATEGORY_NOT_FOUND("INV_001", "La categoría de inventario no fue encontrada", HttpStatus.NOT_FOUND),
    INV_CATEGORY_ALREADY_EXISTS("INV_002", "Ya existe una categoría de inventario con este nombre", HttpStatus.CONFLICT),
    INV_PRODUCT_NOT_FOUND("INV_003", "El producto de inventario no fue encontrado", HttpStatus.NOT_FOUND),
    INV_PRODUCT_ALREADY_EXISTS("INV_004", "Ya existe un producto de inventario con este SKU", HttpStatus.CONFLICT),
    INV_MOVEMENT_NOT_FOUND("INV_005", "El movimiento de inventario no fue encontrado", HttpStatus.NOT_FOUND),
    INV_INSUFFICIENT_STOCK("INV_006", "Stock insuficiente para realizar el movimiento", HttpStatus.BAD_REQUEST),
    INV_INVALID_DATA("INV_007", "Los datos del inventario son inválidos", HttpStatus.BAD_REQUEST),

    // ============================================
    // Order Management (ORD)
    // ============================================
    ORD_NOT_FOUND("ORD_001", "La orden no fue encontrada", HttpStatus.NOT_FOUND),
    ORD_INVALID_STATE("ORD_002", "La orden no está en el estado correcto para esta operación", HttpStatus.BAD_REQUEST),
    ORD_OPERATION_FAILED("ORD_003", "Error al procesar la operación de la orden", HttpStatus.INTERNAL_SERVER_ERROR),

    // Order Management - Station (STA)
    STA_NOT_FOUND("STA_001", "La estación de cocina no fue encontrada", HttpStatus.NOT_FOUND),
    STA_ALREADY_EXISTS("STA_002", "Ya existe una estación con este nombre para la sucursal", HttpStatus.CONFLICT),
    STA_OPERATION_FAILED("STA_003", "Error al procesar la operación de la estación", HttpStatus.INTERNAL_SERVER_ERROR),

    // Order Management - Ticket (TKT)
    TKT_NOT_FOUND("TKT_001", "El ticket de cocina no fue encontrado", HttpStatus.NOT_FOUND),
    TKT_OPERATION_FAILED("TKT_002", "Error al procesar la operación del ticket", HttpStatus.INTERNAL_SERVER_ERROR),

    // Order Management - Sale (SAL)
    SAL_NOT_FOUND("SAL_001", "La venta no fue encontrada", HttpStatus.NOT_FOUND),
    SAL_OPERATION_FAILED("SAL_002", "Error al procesar la operación de la venta", HttpStatus.INTERNAL_SERVER_ERROR),

    // ============================================
    // Billing Management (BLG)
    // ============================================
    BLG_DOCUMENT_NOT_FOUND("BLG_001", "El documento de facturación no fue encontrado", HttpStatus.NOT_FOUND),
    BLG_ISSUANCE_FAILED("BLG_002", "Error al emitir el documento de facturación", HttpStatus.INTERNAL_SERVER_ERROR),
    BLG_PROVIDER_UNAVAILABLE("BLG_003", "El proveedor de facturación no está disponible", HttpStatus.SERVICE_UNAVAILABLE),
    BLG_PROVIDER_ERROR("BLG_004", "Error en el proveedor de facturación", HttpStatus.SERVICE_UNAVAILABLE),
    BLG_INVALID_DOCUMENT_TYPE("BLG_005", "El tipo de documento no es válido", HttpStatus.BAD_REQUEST),
    BLG_EXTERNAL_API_ERROR("BLG_006", "Error en la comunicación con el servicio de facturación externo", HttpStatus.SERVICE_UNAVAILABLE),

    // ============================================
    // Authentication (AUTH)
    // ============================================
    AUTH_UNAUTHORIZED("AUTH_001", "No autorizado", HttpStatus.UNAUTHORIZED),
    AUTH_INVALID_TOKEN("AUTH_002", "Token de autenticación inválido o expirado", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_INVALID("AUTH_004", "Token de autenticación inválido o malformado", HttpStatus.UNAUTHORIZED),
    AUTH_TOKEN_EXPIRED("AUTH_005", "El token de autenticación ha expirado", HttpStatus.UNAUTHORIZED),
    AUTH_HEADER_MISSING("AUTH_006", "Falta el encabezado de autorización", HttpStatus.UNAUTHORIZED),
    AUTH_METHOD_NOT_SUPPORTED("AUTH_007", "El método de autenticación no es soportado", HttpStatus.UNAUTHORIZED),
    AUTH_FORBIDDEN("AUTH_003", "No tienes permiso para acceder a este recurso", HttpStatus.FORBIDDEN),

    // ============================================
    // AI Assistant (AIS)
    // ============================================
    AIS_DATA_UNAVAILABLE("AIS_001", "Los datos de inclusión social no están disponibles", HttpStatus.SERVICE_UNAVAILABLE),
    AIS_LLM_UNAVAILABLE("AIS_002", "El asistente de IA no está disponible en este momento", HttpStatus.SERVICE_UNAVAILABLE),

    // ============================================
    // General / Shared (GEN)
    // ============================================
    GEN_VALIDATION_ERROR("GEN_001", "Error de validación en los datos enviados", HttpStatus.BAD_REQUEST),
    GEN_RESOURCE_NOT_FOUND("GEN_002", "El recurso solicitado no fue encontrado", HttpStatus.NOT_FOUND),
    GEN_CONFLICT("GEN_003", "Conflicto con el estado actual del recurso", HttpStatus.CONFLICT),
    GEN_BAD_REQUEST("GEN_004", "Solicitud inválida", HttpStatus.BAD_REQUEST),
    GEN_INTERNAL_ERROR("GEN_999", "Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    ErrorCodes(String code, String defaultMessage, HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    /**
     * Returns the error code identifier.
     * @return the code string (e.g., "EMP_001")
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the default user-friendly message for this error.
     * @return the default message
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Returns the HTTP status associated with this error code.
     * @return the HTTP status
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Finds an ErrorCode by its code string.
     * @param code the code to find
     * @return the ErrorCode or null if not found
     */
    public static ErrorCodes fromCode(String code) {
        for (ErrorCodes ec : values()) {
            if (ec.code.equals(code)) {
                return ec;
            }
        }
        return null;
    }
}
