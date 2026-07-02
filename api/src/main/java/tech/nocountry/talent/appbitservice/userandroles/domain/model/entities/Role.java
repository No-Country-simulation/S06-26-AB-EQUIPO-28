package tech.nocountry.talent.appbitservice.userandroles.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.nocountry.talent.appbitservice.shared.domain.model.entities.AuditableModel;
import tech.nocountry.talent.appbitservice.userandroles.domain.model.valueobjects.Roles;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Roles name;

    public Role(Roles name) {
        if (name == null) {
            throw new IllegalArgumentException("El nombre del rol no puede ser null");
        }
        this.name = name;
    }

    /** Crea un rol con el nombre especificado. */
    public static Role create(Roles name) {
        return new Role(name);
    }

    public String getStringName() {
        return name.name();
    }
}
