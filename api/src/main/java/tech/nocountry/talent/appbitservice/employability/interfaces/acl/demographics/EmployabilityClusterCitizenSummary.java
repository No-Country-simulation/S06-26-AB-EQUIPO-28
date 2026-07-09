package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics;

/**
 * ACL result representing a citizen count grouped by home cluster, expressed in
 * the employability bounded context's ubiquitous language.
 *
 * <p>This is the anti-corruption translation of the upstream
 * {@code ClusterCitizenCountResource} from the demographics BC. The
 * employability BC never sees the upstream type: it only interacts with this
 * record, which uses the employability vocabulary ({@code citizenCount} as a
 * primitive {@code long}).</p>
 *
 * @param clusterName  the geographic cluster name
 * @param citizenCount the number of citizens in that cluster
 */
public record EmployabilityClusterCitizenSummary(
        String clusterName,
        long citizenCount
) {}