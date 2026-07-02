package tech.nocountry.talent.appbitservice.demographics.domain.model.aggregates;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.nocountry.talent.appbitservice.demographics.domain.model.commands.UpdateCitizenProfileCommand;
import tech.nocountry.talent.appbitservice.demographics.domain.model.valueobjects.*;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.List;

/**
 * Aggregate root que representa el perfil demográfico de un ciudadano.
 *
 * <p>Cada CitizenProfile contiene información anonimizada sobre el ciudadano,
 * incluyendo nivel de ingreso, grupo de edad, patrón de movilidad y cluster de residencia.</p>
 *
 * <p>Este agregado es la entidad principal del bounded context de demographics.</p>
 */
@Getter
@Entity
@Table(name = "citizen_profiles", schema = "demographics_schema")
@NoArgsConstructor
public class CitizenProfile extends AuditableAbstractAggregateRoot<CitizenProfile> {
    @Id
    private CitizenId citizenId;

    @Embedded
    private IncomeLevel incomeLevel;

    @Embedded
    private AgeGroup ageGroup;

    @Embedded
    private MobilityPattern mobilityPattern;

    @Embedded
    private HomeCluster homeCluster;

    /**
     * Constructor para crear un nuevo perfil de ciudadano.
     *
     * @param citizenId identificador único anonimizado del ciudadano
     * @param incomeLevel nivel de ingreso económico
     * @param ageGroup grupo de edad
     * @param mobilityPattern patrón de movilidad
     * @param homeCluster cluster geográfico de residencia
     */
    public CitizenProfile(
            CitizenId citizenId,
            IncomeLevel incomeLevel,
            AgeGroup ageGroup,
            MobilityPattern mobilityPattern,
            HomeCluster homeCluster
    ) {
        this.citizenId = citizenId;
        this.incomeLevel = incomeLevel;
        this.ageGroup = ageGroup;
        this.mobilityPattern = mobilityPattern;
        this.homeCluster = homeCluster;
    }

    /**
     * Constructor estático para crear un CitizenProfile con valores String.
     *
     * @param citizenIdString identificador único anonimizado del ciudadano
     * @param incomeLevel nivel de ingreso económico
     * @param ageGroup grupo de edad
     * @param mobilityPattern patrón de movilidad
     * @param homeCluster cluster geográfico de residencia
     * @return nueva instancia de CitizenProfile
     */
    public static CitizenProfile create(
            String citizenIdString,
            String incomeLevel,
            String ageGroup,
            String mobilityPattern,
            String homeCluster) {
        return new CitizenProfile(
                CitizenId.of(citizenIdString),
                IncomeLevel.of(incomeLevel),
                AgeGroup.of(ageGroup),
                MobilityPattern.of(mobilityPattern),
                HomeCluster.of(homeCluster)
        );
    }

    /**
     * Fusiona los valores de un comando de actualización en esta entidad.
     * <p>Solo actualiza los campos no nulos del comando, permitiendo actualizaciones parciales.</p>
     *
     * @param command comando con los valores a actualizar
     */
    public void merge(UpdateCitizenProfileCommand command) {
        if (command.incomeLevel() != null) { this.incomeLevel = command.incomeLevel(); }
        if (command.ageGroup() != null) { this.ageGroup = command.ageGroup(); }
        if (command.mobilityPattern() != null) { this.mobilityPattern = command.mobilityPattern(); }
        if (command.homeCluster() != null) { this.homeCluster = command.homeCluster(); }
    }

    /**
     * Obtener todos los datos como lista.
     *
     * @return lista con todos los valores del perfil
     */
    public List<String> toList() {
        return List.of(
                citizenId.getValue(),
                incomeLevel.getValue(),
                ageGroup.getValue(),
                mobilityPattern.getValue(),
                homeCluster.getValue()
        );
    }

    /**
     * Verifica si este ciudadano pertenece a la población vulnerable.
     *
     * @return true si el incomeLevel es D
     */
    public boolean isVulnerable() {
        return incomeLevel != null && incomeLevel.isVulnerable();
    }
}