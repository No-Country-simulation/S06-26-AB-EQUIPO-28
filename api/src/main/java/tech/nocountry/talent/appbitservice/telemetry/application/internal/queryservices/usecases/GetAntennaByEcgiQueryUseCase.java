package tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.telemetry.domain.exceptions.AntennaNotFoundException;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates.Antenna;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.queries.GetAntennaByEcgiQuery;
import tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories.AntennaRepository;

import java.util.Optional;

/**
 * Caso de uso para obtener una antenna por su ECGI.
 *
 * <p>Busca una antenna específica utilizando su identificador único.</p>
 */
@Service
@RequiredArgsConstructor
public class GetAntennaByEcgiQueryUseCase {
    private final AntennaRepository antennaRepository;

    /**
     * Maneja la consulta para obtener una antenna por ECGI.
     *
     * @param query query con el ECGI a buscar
     * @return la antenna encontrada
     * @throws AntennaNotFoundException si no se encuentra la antenna
     */
    @Transactional(readOnly = true)
    public Antenna handle(GetAntennaByEcgiQuery query) {
        if (query == null || query.ecgi() == null) {
            throw new IllegalArgumentException("ECGI query is required");
        }

        return antennaRepository.findByEcgiValue(query.ecgi().getValue())
                .orElseThrow(() -> new AntennaNotFoundException(query.ecgi().getValue()));
    }

    /**
     * Maneja la consulta para obtener una antenna por ECGI como string.
     *
     * @param ecgiValue valor del ECGI a buscar
     * @return Optional conteniendo la antenna si existe
     */
    @Transactional(readOnly = true)
    public Optional<Antenna> handle(String ecgiValue) {
        if (ecgiValue == null || ecgiValue.isBlank()) {
            return Optional.empty();
        }

        return antennaRepository.findByEcgiValue(ecgiValue);
    }
}