package tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.antenna;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases.GetAllAntennasQueryUseCase;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaPaginatedResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform.AntennaResourceAssembler;

/**
 * Endpoint interno para obtener todas las antenas del sistema con paginación.
 *
 * <p>Este es un endpoint interno ({@code @Component}) que ejecuta el caso de uso
 * y transforma el resultado paginado a Resource. Consumido por el Controller REST
 * y el ACL de otros Bounded Contexts. No es un REST Controller.</p>
 *
 * <p>Sigue el patrón de Gastro Suite: el endpoint recibe datos crudos, usa el Assembler
 * para transformar a recursos del dominio, y retorna Resources listos para la capa de
 * presentación.</p>
 */
@Component
@RequiredArgsConstructor
public class GetAllAntennasEndpoint {
    private final GetAllAntennasQueryUseCase getAllAntennasQueryUseCase;
    private final AntennaResourceAssembler assembler;

    /**
     * Obtiene todas las antenas del sistema de forma paginada.
     *
     * @param page número de página (0-based)
     * @param size tamaño de página
     * @return recurso paginado de antenas
     */
    public AntennaPaginatedResource handle(int page, int size) {
        var pageable = Pageable.ofSize(size).withPage(page);
        var result = getAllAntennasQueryUseCase.handle(pageable);
        return assembler.toPaginatedResource(result);
    }

    /**
     * Obtiene todas las antenas del sistema sin límite de paginación.
     *
     * @return recurso paginado con todos los resultados
     */
    public AntennaPaginatedResource handleAll() {
        var result = getAllAntennasQueryUseCase.handle(Pageable.unpaged());
        return assembler.toPaginatedResource(result);
    }
}
