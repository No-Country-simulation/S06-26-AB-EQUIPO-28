package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources;

/**
 * Local raw resource mirroring the {@code ConcentrationResource} of the
 * upstream telemetry BC, restricted to the fields the employability ACL needs
 * for the daytime aggregation (ecgi lookup + period filter + user count).
 *
 * <p>This record belongs to the <strong>employability</strong> bounded context:
 * it speaks the consumer's language and must NOT be confused with the upstream
 * telemetry resource. In particular, the upstream {@code SessionPeriod} enum is
 * flattened to a plain {@code String} here so the consumer never depends on the
 * telemetry domain model. The translation from the upstream shape happens
 * exclusively in {@link tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform.EmployabilityTelemetryExternalResourceAssembler}.</p>
 *
 * <p>It is the only concentration shape the {@code EmployabilityTelemetryInternalClient}
 * adapter manipulates at runtime, so the upstream model never leaks beyond the
 * assembler's generated code.</p>
 *
 * @param ecgi      the antenna identifier used to resolve the owning cluster
 * @param period    the session period as a string (e.g. "MORNING", "AFTERNOON", "EVENING")
 * @param userCount the measured user count (nullable)
 */
public record EmployabilityConcentrationRawResource(
        String ecgi,
        String period,
        Integer userCount
) {}