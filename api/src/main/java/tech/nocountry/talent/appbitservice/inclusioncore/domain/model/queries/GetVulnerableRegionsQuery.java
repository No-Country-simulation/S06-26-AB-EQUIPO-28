package tech.nocountry.talent.appbitservice.inclusioncore.domain.model.queries;

/**
 * Query to retrieve regions with high vulnerability scores.
 *
 * <p>This query retrieves regions that meet specific vulnerability criteria,
 * useful for targeting interventions and resource allocation.</p>
 *
 * @param minVulnerabilityScore minimum vulnerability score threshold (0-100)
 * @param maxResults maximum number of results to return
 * @param poorConnectivityOnly if true, only include regions with poor connectivity
 */
public record GetVulnerableRegionsQuery(
        int minVulnerabilityScore,
        int maxResults,
        boolean poorConnectivityOnly
) {
    /**
     * Creates a query to get highly vulnerable regions.
     *
     * @param minVulnerabilityScore minimum vulnerability score (default 60)
     * @param maxResults maximum results (default 20)
     * @return new GetVulnerableRegionsQuery
     */
    public static GetVulnerableRegionsQuery highlyVulnerable(int minVulnerabilityScore, int maxResults) {
        return new GetVulnerableRegionsQuery(minVulnerabilityScore, maxResults, false);
    }

    /**
     * Creates a query to get vulnerable regions with poor connectivity.
     *
     * @param minVulnerabilityScore minimum vulnerability score (default 60)
     * @param maxResults maximum results (default 20)
     * @return new GetVulnerableRegionsQuery
     */
    public static GetVulnerableRegionsQuery vulnerableWithPoorConnectivity(int minVulnerabilityScore, int maxResults) {
        return new GetVulnerableRegionsQuery(minVulnerabilityScore, maxResults, true);
    }

    /**
     * Creates a query with default values.
     *
     * @return new GetVulnerableRegionsQuery with defaults
     */
    public static GetVulnerableRegionsQuery withDefaults() {
        return new GetVulnerableRegionsQuery(60, 20, false);
    }

    /**
     * Validates the query parameters.
     *
     * @throws IllegalArgumentException if parameters are invalid
     */
    public GetVulnerableRegionsQuery {
        if (minVulnerabilityScore < 0 || minVulnerabilityScore > 100) {
            throw new IllegalArgumentException("Minimum vulnerability score must be between 0 and 100");
        }
        if (maxResults <= 0 || maxResults > 1000) {
            throw new IllegalArgumentException("Max results must be between 1 and 1000");
        }
    }
}