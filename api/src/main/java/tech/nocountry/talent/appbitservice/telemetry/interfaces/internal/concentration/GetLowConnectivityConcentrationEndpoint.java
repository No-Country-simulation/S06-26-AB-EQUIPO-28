package tech.nocountry.talent.appbitservice.telemetry.interfaces.internal.concentration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.telemetry.application.internal.queryservices.usecases.GetConcentrationFilteredQueryUseCase;
import tech.nocountry.talent.appbitservice.telemetry.domain.model.queries.GetConcentrationFilteredQuery;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.transform.ConcentrationResourceAssembler;

import java.util.List;

/**
 * Endpoint interno para obtener las métricas de concentración con baja conectividad.
 *
 * <p>Este es un endpoint interno ({@code @Component}) que transforma entidades del dominio
 * a Resources. Retorna las métricas de concentración donde el porcentaje de drop
 * es superior al umbral especificado.</p>
 *
 * <p>Sigue el patrón de Gastro Suite: el endpoint usa el Assembler para transformar
 * entidades del dominio a Resources listos para la capa de presentación.</p>
 */
@Component
@RequiredArgsConstructor
public class GetLowConnectivityConcentrationEndpoint {
    private final GetConcentrationFilteredQueryUseCase useCase;
    private final ConcentrationResourceAssembler assembler;

    /**
     * Obtiene las métricas de concentración con baja conectividad.
     *
     * @param dropPctThreshold umbral mínimo de drop (e.g., 0.05 para 5%)
     * @return lista de recursos de concentración con baja conectividad
     */
    public List<ConcentrationResource> handle(double dropPctThreshold) {
        var query = new GetConcentrationFilteredQuery(null, null, null, null, dropPctThreshold, 0, 1000);
        var result = useCase.handle(query);
        return assembler.toResourceList(result.getContent());
    }
}
