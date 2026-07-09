package tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.transform;

import org.mapstruct.Mapper;
import tech.nocountry.talent.appbitservice.demographics.interfaces.internal.citizen.resources.ClusterCitizenCountResource;
import tech.nocountry.talent.appbitservice.employability.interfaces.acl.demographics.resources.EmployabilityClusterCitizenCountRawResource;

import java.util.List;

/**
 * Assembler that translates the upstream demographics BC resource shape into the
 * local raw resource owned by the employability ACL.
 *
 * <p>This MapStruct mapper is the <strong>only</strong> component in the
 * employability BC that imports {@code demographics.interfaces.internal.citizen.resources.*}.
 * It is the physical boundary of the anti-corruption layer: the
 * {@code EmployabilityDemographicsInternalClient} adapter depends solely on the local raw
 * resource (and on the in-process internal endpoint), never on the upstream
 * model, so the upstream shape cannot propagate into the employability domain.</p>
 *
 * <p>Field names match between source and target, so MapStruct generates the
 * mapping code automatically.</p>
 */
@Mapper(componentModel = "spring")
public interface EmployabilityDemographicsExternalResourceAssembler {

    /**
     * Maps an upstream {@link ClusterCitizenCountResource} to the local
     * {@link EmployabilityClusterCitizenCountRawResource}.
     *
     * @param resource the upstream cluster citizen count resource (nullable)
     * @return the local raw resource, or {@code null} if the source is null
     */
    EmployabilityClusterCitizenCountRawResource toRaw(ClusterCitizenCountResource resource);

    /**
     * Maps a list of upstream {@link ClusterCitizenCountResource} to a list of
     * local {@link EmployabilityClusterCitizenCountRawResource}.
     *
     * @param resources the upstream cluster citizen count resources
     * @return the list of local raw resources (never null)
     */
    List<EmployabilityClusterCitizenCountRawResource> toRawList(List<ClusterCitizenCountResource> resources);
}