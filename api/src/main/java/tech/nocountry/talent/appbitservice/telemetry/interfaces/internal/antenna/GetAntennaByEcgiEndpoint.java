package tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases.GetAntennaByEcgiQueryUseCase;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform.AntennaResourceAssembler;

/**
 * Endpoint interno para obtener una antenna por su ECGI.
 *
 * <p>Este es un endpoint interno (@Component) que transforma entidades del dominio
 * a Resources. Consumido por el Controller REST y el ACL de otros Bounded Contexts.
 * No es un REST Controller.</p>
 *
 * <p>Sigue el patrón de Gastro Suite: el endpoint recibe datos crudos, usa el Assembler
 * para transformar a recursos del dominio, y retorna Resources listos para la capa de
 * presentación.</p>
 */
@Component
@RequiredArgsConstructor
public class GetAntennaByEcgiEndpoint {
    private final GetAntennaByEcgiQueryUseCase getAntennaByEcgiQueryUseCase;
    private final AntennaResourceAssembler assembler;

    /**
     * Obtiene una antenna por su ECGI y la transforma a Resource.
     *
     * @param ecgiValue el valor del ECGI
     * @return recurso de la antenna o null si no existe
     */
    public AntennaResource handle(String ecgiValue) {
        return getAntennaByEcgiQueryUseCase.handle(ecgiValue)
                .map(assembler::toResource)
                .orElse(null);
    }
}
