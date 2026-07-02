package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.adapter.DemographicsInternalClient;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.transform.DemographicsErrorAssembler;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.transform.DemographicsExternalResourceAssembler;

import java.util.List;

/**
 * ACL Facade para el contexto Demográfico.
 *
 * <p>Simplifica la interacción con el Bounded Context de Demografía.
 * Es el límite físico del ACL y el orquestador principal. Aísla el código complejo
 * y delega el transporte al Adapter.</p>
 *
 * <p>Implementa {@link DemographicsAclPort} y es inyectada en los casos de uso de inclusion-core.</p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DemographicsAclFacade implements DemographicsAclPort {
    private final DemographicsInternalClient adapter;
    private final DemographicsExternalResourceAssembler assembler;

    @Override
    public List<DemographicsAclResult> getVulnerableCitizens() {
        try {
            log.info("Obteniendo ciudadanos vulnerables");
            var externalResources = adapter.getVulnerableCitizens();
            return assembler.toAclViewList(externalResources);
        } catch (Exception ex) {
            throw DemographicsErrorAssembler.assemble(ex, "vulnerable-citizens");
        }
    }

    @Override
    public List<ClusterCountAclResult> getCitizenCountByCluster(String incomeLevel) {
        try {
            log.info("Obteniendo conteo de ciudadanos por cluster, incomeLevel={}", incomeLevel);
            return adapter.getCitizenCountByCluster(incomeLevel);
        } catch (Exception ex) {
            throw DemographicsErrorAssembler.assemble(ex, "citizen-count-by-cluster");
        }
    }

    @Override
    public List<ClusterCountAclResult> getCitizenCountByClusterAndMobilityPattern(String pattern) {
        try {
            log.info("Obteniendo conteo de ciudadanos por cluster y patrón de movilidad, pattern={}", pattern);
            return adapter.getCitizenCountByClusterAndMobilityPattern(pattern);
        } catch (Exception ex) {
            throw DemographicsErrorAssembler.assemble(ex, "citizen-count-by-mobility");
        }
    }

    @Override
    public List<ClusterCountAclResult> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups) {
        try {
            log.info("Obteniendo conteo de ciudadanos por cluster y grupos etarios, ageGroups={}", ageGroups);
            return adapter.getCitizenCountByClusterAndAgeGroups(ageGroups);
        } catch (Exception ex) {
            throw DemographicsErrorAssembler.assemble(ex, "citizen-count-by-age");
        }
    }
}
