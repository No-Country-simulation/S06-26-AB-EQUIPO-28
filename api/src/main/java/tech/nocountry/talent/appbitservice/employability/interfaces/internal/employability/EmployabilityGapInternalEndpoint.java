package tech.nocountry.talent.appbitservice.employability.interfaces.internal.employability;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.application.internal.queryservices.usecases.GetEmployabilityGapsUseCase;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.GetEmployabilityGapsQuery;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.EmployabilityGapResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform.EmployabilityGapResourceAssembler;

import java.util.List;

/**
 * Internal endpoint para el análisis de brechas de empleabilidad.
 *
 * <p>Orquesta el use case CORE y transforma los resultados de dominio en REST
 * resources usando el assembler. Sigue el patrón
 * {@code MentorshipGapInternalEndpoint}.</p>
 */
@Component
@RequiredArgsConstructor
public class EmployabilityGapInternalEndpoint {

    private final GetEmployabilityGapsUseCase getGapsUseCase;
    private final EmployabilityGapResourceAssembler assembler;

    /**
     * Ejecuta el análisis de brechas de empleabilidad: clusters con alta
     * densidad diurna pero baja conectividad saliente a hubs de empleo.
     *
     * @param maxResults     número máximo de clusters a devolver (1-1000)
     * @param minSeverity    severidad mínima (CRITICAL, HIGH, MODERATE, LOW, NONE); null = todas
     * @param cluster        filtro por cluster específico; null = todos
     * @param onlyBlindZones si {@code true}, solo clusters sin cobertura de telemetría
     * @return lista de gap resources ordenada por gapScore desc
     */
    public List<EmployabilityGapResource> getGaps(
            int maxResults,
            String minSeverity,
            String cluster,
            boolean onlyBlindZones) {
        var query = new GetEmployabilityGapsQuery(maxResults, minSeverity, cluster, onlyBlindZones);
        var results = getGapsUseCase.execute(query);
        return assembler.toResourceList(results);
    }
}
