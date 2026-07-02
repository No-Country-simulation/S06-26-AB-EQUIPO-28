package tech.nocountry.talent.appbitservice.inclusioncore.domain.exceptions;

/**
 * Exception thrown when a region is not found in the health vulnerability index.
 */
public class RegionNotFoundException extends InclusionCoreDomainException {
    public RegionNotFoundException(String regionName) {
        super(String.format("Region not found: %s", regionName));
    }

    public RegionNotFoundException(String regionName, Throwable cause) {
        super(String.format("Region not found: %s", regionName), cause);
    }
}