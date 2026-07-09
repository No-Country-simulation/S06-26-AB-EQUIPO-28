package tech.nocountry.talent.appbitservice.employability.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.domain.model.queries.EmployabilityGap;
import tech.nocountry.talent.appbitservice.employability.interfaces.rest.resources.EmployabilityGapResource;

import java.util.List;

/**
 * Manual assembler que transforma {@link EmployabilityGap} domain records en
 * {@link EmployabilityGapResource} REST DTOs.
 *
 * <p>Es un assembler manual (no MapStruct) porque {@link EmployabilityGap} es
 * un read model (record) producido por el domain service
 * {@link tech.nocountry.talent.appbitservice.employability.domain.services.EmployabilityGapCalculator}:
 * no hay agregado JPA que mapear, solo una conversión 1:1 de record a record.
 * Sigue el mismo patrón que {@code MentorshipGapResourceAssembler}.</p>
 */
@Component
public class EmployabilityGapResourceAssembler {

    /**
     * Transforma un {@link EmployabilityGap} en su resource REST.
     *
     * @param gap el read model de dominio
     * @return el resource DTO
     */
    public EmployabilityGapResource toResource(EmployabilityGap gap) {
        return new EmployabilityGapResource(
                gap.cluster(),
                gap.municipalities(),
                gap.citizenCount(),
                gap.incomeDCount(),
                gap.incomeCCount(),
                gap.youthCount18_24(),
                gap.hasTelemetryCoverage(),
                gap.daytimeAvgUsers(),
                gap.outboundTripsToHubs(),
                gap.distanceToNearestHubKm(),
                gap.mobilityIntensity(),
                gap.gapSeverity(),
                gap.gapScore(),
                gap.primaryFactors()
        );
    }

    /**
     * Transforma una lista de {@link EmployabilityGap} en una lista de
     * resources REST.
     *
     * @param gaps la lista de read models de dominio
     * @return la lista de resources DTO
     */
    public List<EmployabilityGapResource> toResourceList(List<EmployabilityGap> gaps) {
        return gaps.stream().map(this::toResource).toList();
    }
}
