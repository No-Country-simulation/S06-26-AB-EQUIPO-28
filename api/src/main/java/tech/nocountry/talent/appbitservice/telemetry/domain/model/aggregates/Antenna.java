package tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.nocountry.talent.appbitservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.valueobjects.*;

/**
 * Aggregate root que representa una antena de telecomunicación en el sistema.
 *
 * <p>Cada antenna tiene coordenadas geográficas y pertenece a un cluster y municipio.
 * Es la entidad principal del bounded context de telemetría.</p>
 */
@Getter
@Entity
@Table(name = "antennas", schema = "telemetry_schema")
@NoArgsConstructor
public class Antenna extends AuditableAbstractAggregateRoot<Antenna> {
    @Id
    private Ecgi ecgi;

    @Embedded
    private ClusterName cluster;

    @Embedded
    private MunicipalityName municipality;

    @Embedded
    private Latitude latitude;

    @Embedded
    private Longitude longitude;

    /**
     * Constructor para crear una nueva antenna.
     *
     * @param ecgi identificador único de la celda
     * @param cluster nombre del cluster geográfico
     * @param municipality nombre del municipio
     * @param latitude latitud geográfica
     * @param longitude longitud geográfica
     */
    public Antenna(Ecgi ecgi, ClusterName cluster, MunicipalityName municipality, Latitude latitude, Longitude longitude) {
        this.ecgi = ecgi;
        this.cluster = cluster;
        this.municipality = municipality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructor estático factory para crear una antenna.
     *
     * @param ecgiValue identificador único de la celda
     * @param clusterValue nombre del cluster geográfico
     * @param municipalityValue nombre del municipio
     * @param latitudeValue latitud geográfica
     * @param longitudeValue longitud geográfica
     * @return nueva instancia de Antenna
     */
    public static Antenna create(String ecgiValue, String clusterValue, String municipalityValue, Double latitudeValue, Double longitudeValue) {
        return new Antenna(
                Ecgi.of(ecgiValue),
                ClusterName.of(clusterValue),
                MunicipalityName.of(municipalityValue),
                Latitude.of(latitudeValue),
                Longitude.of(longitudeValue)
        );
    }
}