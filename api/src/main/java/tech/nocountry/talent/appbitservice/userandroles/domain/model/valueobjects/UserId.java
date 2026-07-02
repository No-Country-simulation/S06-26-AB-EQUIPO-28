package tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import tech.nocountry.talent.appbitservice.shared.domain.model.valueobjects.UuidV7Generator;

import java.util.UUID;

@Embeddable
public record UserId(
        @Column(name = "user_id")
        UUID value
) {
    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }
    }

    /**
     * Genera un nuevo UserId con un UUID aleatorio.
     * 
     * @return un nuevo UserId
     */
    public static UserId generate() {
        return new UserId(UuidV7Generator.generate());
    }

    /**
     * Crea un UserId a partir de un UUID.
     * 
     * @param uuid el UUID del usuario
     * @return una nueva instancia de UserId
     */
    @JsonCreator
    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }

    /**
     * Retorna el valor del UUID.
     * 
     * @return el UUID del usuario
     */
    @JsonValue
    public UUID getValue() {
        return value;
    }
}
