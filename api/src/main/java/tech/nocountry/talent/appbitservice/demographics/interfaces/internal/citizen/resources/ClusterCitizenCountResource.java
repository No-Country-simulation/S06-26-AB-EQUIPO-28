package tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.resources;

/**
 * Resource representing a citizen count grouped by home cluster.
 *
 * @param clusterName  the geographic cluster name
 * @param citizenCount the number of citizens in that cluster
 */
public record ClusterCitizenCountResource(
        String clusterName,
        Long citizenCount
) {}
