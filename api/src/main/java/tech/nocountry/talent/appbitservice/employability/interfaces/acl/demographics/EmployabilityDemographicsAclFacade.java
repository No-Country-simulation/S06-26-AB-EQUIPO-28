package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.adapter.EmployabilityDemographicsInternalClient;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.transform.EmployabilityDemographicsErrorAssembler;

import java.util.List;

/**
 * ACL Facade for the Demographics bounded context — employability side.
 *
 * <p>Implements {@link EmployabilityDemographicsAclPort} and is the physical boundary of the
 * ACL. It orchestrates the {@link EmployabilityDemographicsInternalClient} (transport layer,
 * direct in-process injection, no HTTP) and translates any failure into a
 * domain-appropriate exception via {@link EmployabilityDemographicsErrorAssembler}.</p>
 *
 * <p>This facade is the only entry point the employability use cases use to
 * reach demographic data, keeping the upstream model out of the employability
 * domain.</p>
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EmployabilityDemographicsAclFacade implements EmployabilityDemographicsAclPort {

    private final EmployabilityDemographicsInternalClient adapter;

    @Override
    public List<EmployabilityClusterCitizenSummary> getCitizenCountByCluster() {
        try {
            log.info("Fetching citizen count by cluster (no filter)");
            return adapter.getCitizenCountByCluster(null);
        } catch (Exception ex) {
            throw EmployabilityDemographicsErrorAssembler.assemble(ex, "citizen-count-by-cluster");
        }
    }

    @Override
    public List<EmployabilityClusterCitizenSummary> getCitizenCountByClusterAndIncomeLevel(String incomeLevel) {
        try {
            log.info("Fetching citizen count by cluster, incomeLevel={}", incomeLevel);
            return adapter.getCitizenCountByCluster(incomeLevel);
        } catch (Exception ex) {
            throw EmployabilityDemographicsErrorAssembler.assemble(ex, "citizen-count-by-income");
        }
    }

    @Override
    public List<EmployabilityClusterCitizenSummary> getCitizenCountByClusterAndAgeGroups(List<String> ageGroups) {
        try {
            log.info("Fetching citizen count by cluster and age groups, ageGroups={}", ageGroups);
            return adapter.getCitizenCountByClusterAndAgeGroups(ageGroups);
        } catch (Exception ex) {
            throw EmployabilityDemographicsErrorAssembler.assemble(ex, "citizen-count-by-age");
        }
    }
}