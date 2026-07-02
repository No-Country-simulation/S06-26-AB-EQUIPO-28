package tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserName(
        @Column(name = "username", nullable = false, length = 100, unique = true)
        String value
) {
    private static final int MAX_LENGTH = 100;

    public UserName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        value = value.trim().toLowerCase();

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("El username no puede tener más de %d caracteres", MAX_LENGTH)
            );
        }
    }

    /**
     * Crea un UserName a partir de un string.
     * 
     * @param identifier el identificador de usuario
     * @return una nueva instancia de UserName
     */
    @JsonCreator
    public static UserName of(String identifier) {
        return new UserName(identifier);
    }

    /**
     * Retorna el valor del nombre de usuario.
     * 
     * @return el valor del username
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}