package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics;

/**
 * ACL result representing citizen count grouped by home cluster.
 *
 * @param clusterName  the geographic cluster name
 * @param citizenCount the number of citizens in that cluster
 */
public record ClusterCountAclResult(
        String clusterName,
        Long citizenCount
) {}
