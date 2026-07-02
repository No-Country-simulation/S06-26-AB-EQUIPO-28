package tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.aggregates.Antenna;
import tech.nocountry.talent.appbitservice.telemetry.infrastructure.persistence.jpa.repositories.AntennaRepository;

/**
 * Caso de uso para obtener todas las antenas del sistema con paginación.
 *
 * <p>Utiliza el repositorio directamente para evitar dependencias circulares.
 * Soporta paginación mediante {@link Pageable}.</p>
 */
@Service
@RequiredArgsConstructor
public class GetAllAntennasQueryUseCase {
    private final AntennaRepository antennaRepository;

    /**
     * Maneja la consulta para obtener todas las antenas de forma paginada.
     *
     * @param pageable objeto con la información de paginación
     * @return página de antenas
     */
    @Transactional(readOnly = true)
    public Page<Antenna> handle(Pageable pageable) {
        return antennaRepository.findAll(pageable);
    }
}
