package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.resources;

/**
 * Local raw resource mirroring the {@code ClusterCitizenCountResource} of the
 * upstream demographics BC, restricted to the fields the employability ACL
 * needs to build {@link tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.EmployabilityClusterCitizenSummary}.
 *
 * <p>This record belongs to the <strong>employability</strong> bounded context:
 * it speaks the consumer's language and must NOT be confused with the upstream
 * demographics resource. The translation from the upstream shape happens
 * exclusively in {@link tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.transform.EmployabilityDemographicsExternalResourceAssembler}.</p>
 *
 * <p>It is the only citizen-count shape the
 * {@code EmployabilityDemographicsInternalClient} adapter manipulates at runtime, so the
 * upstream model never leaks beyond the assembler's generated code.</p>
 *
 * @param clusterName  the geographic cluster name
 * @param citizenCount the number of citizens in that cluster (nullable)
 */
public record EmployabilityClusterCitizenCountRawResource(
        String clusterName,
        Long citizenCount
) {}