package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources;

/**
 * Local raw resource mirroring the {@code AntennaResource} of the upstream
 * telemetry BC, restricted to the fields the employability ACL needs.
 *
 * <p>This record belongs to the <strong>employability</strong> bounded context:
 * it speaks the consumer's language and must NOT be confused with the upstream
 * telemetry resource. The translation from the upstream shape happens exclusively
 * in {@link tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform.EmployabilityTelemetryExternalResourceAssembler}.</p>
 *
 * <p>It is the only antenna shape the {@code EmployabilityTelemetryInternalClient} adapter
 * manipulates at runtime, so the upstream model never leaks beyond the
 * assembler's generated code.</p>
 *
 * @param ecgi         the antenna identifier
 * @param cluster      the geographic cluster name
 * @param municipality the municipality name
 * @param latitude     the antenna latitude
 * @param longitude    the antenna longitude
 */
public record EmployabilityAntennaRawResource(
        String ecgi,
        String cluster,
        String municipality,
        Double latitude,
        Double longitude
) {}