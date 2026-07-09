package tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources.EmployabilityAntennaRawResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.telemetry.resources.EmployabilityConcentrationRawResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.AntennaResource;
import tech.nocountry.talent.appbitservice.telemetry.interfaces.rest.resources.ConcentrationResource;

import java.util.List;

/**
 * Assembler that translates the upstream telemetry BC resource shapes into the
 * local raw resources owned by the employability ACL.
 *
 * <p>This MapStruct mapper is the <strong>only</strong> component in the
 * employability BC that imports {@code telemetry.interfaces.rest.resources.*}.
 * It is the physical boundary of the anti-corruption layer: the
 * {@code EmployabilityTelemetryInternalClient} adapter depends solely on the local raw
 * resources (and on the in-process internal endpoints), never on the upstream
 * model, so the upstream shape cannot propagate into the employability domain.</p>
 *
 * <p>Field names match between source and target, so MapStruct generates the
 * mapping code automatically. The upstream {@code SessionPeriod} enum is mapped
 * to {@code String} via its {@code name()}, removing the dependency on the
 * telemetry domain model in the consumer.</p>
 */
@Mapper(componentModel = "spring")
public interface EmployabilityTelemetryExternalResourceAssembler {

    /**
     * Maps an upstream {@link AntennaResource} to the local
     * {@link EmployabilityAntennaRawResource}.
     *
     * @param resource the upstream antenna resource (nullable)
     * @return the local raw antenna resource, or {@code null} if the source is null
     */
    EmployabilityAntennaRawResource toAntennaRaw(AntennaResource resource);

    /**
     * Maps a list of upstream {@link AntennaResource} to a list of local
     * {@link EmployabilityAntennaRawResource}.
     *
     * @param resources the upstream antenna resources
     * @return the list of local raw antenna resources (never null)
     */
    List<EmployabilityAntennaRawResource> toAntennaRawList(List<AntennaResource> resources);

    /**
     * Maps an upstream {@link ConcentrationResource} to the local
     * {@link EmployabilityConcentrationRawResource}, flattening the {@code SessionPeriod}
     * enum to its {@code name()}.
     *
     * @param resource the upstream concentration resource (nullable)
     * @return the local raw concentration resource, or {@code null} if the source is null
     */
    EmployabilityConcentrationRawResource toConcentrationRaw(ConcentrationResource resource);

    /**
     * Maps a list of upstream {@link ConcentrationResource} to a list of
     * local {@link EmployabilityConcentrationRawResource}.
     *
     * @param resources the upstream concentration resources
     * @return the list of local raw concentration resources (never null)
     */
    List<EmployabilityConcentrationRawResource> toConcentrationRawList(List<ConcentrationResource> resources);
}