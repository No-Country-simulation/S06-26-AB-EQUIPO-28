package tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;

@Embeddable
public record Password(
        @Column(name = "password", nullable = false, length = 200)
        @Size(max = 200)
        String value
) {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 200;

    /**
     * Crea una nueva instancia de Password.
     * 
     * @param value el valor de la contraseña
     */
    public Password(String value) {
        isValid(value);
        this.value = value;
    }

    /**
     * Crea una Password a partir de un string.
     * 
     * @param password la contraseña en texto plano
     * @return una nueva instancia de Password
     */
    public static Password of(String password) {
        isValid(password);
        return new Password(password);
    }

    /**
     * Retorna el valor de la contraseña.
     * 
     * @return el valor de la contraseña
     */
    @JsonValue
    public String getValue() { return value; }

    /**
     * Valida que una contraseña cumpla con los requisitos.
     * 
     * @param password la contraseña a validar
     * @return true si la contraseña es válida
     * @throws IllegalArgumentException si la contraseña no cumple los requisitos
     */
    public static boolean isValid (String password){
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("La contraseña debe tener como máximo " + MAX_LENGTH + " caracteres");
        }
        if (password.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("La contraseña debe tener al menos " + MIN_LENGTH + " caracteres");
        }
        return true;
    }
}