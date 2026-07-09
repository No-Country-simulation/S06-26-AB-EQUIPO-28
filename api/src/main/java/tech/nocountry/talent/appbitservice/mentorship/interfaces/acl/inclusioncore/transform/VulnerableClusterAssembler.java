package tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.transform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.rest.resources.VulnerableRegionResource;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.acl.inclusioncore.VulnerableClusterAclResult;

import java.util.List;

/**
 * MapStruct assembler that translates
 * {@link VulnerableRegionResource} (inclusioncore language)
 * into {@link VulnerableClusterAclResult} (mentorship language).
 *
 * <p>The only semantic divergence is the field name: upstream calls it
 * {@code regionName}, mentorship calls it {@code clusterName} — same
 * geographic concept, different bounded context language.</p>
 *
 * <p>All other fields match 1:1 by name and type, so MapStruct auto-maps
 * them without explicit {@code @Mapping} annotations.</p>
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VulnerableClusterAssembler {

    /**
     * Translates a single upstream resource into a mentorship ACL result.
     *
     * @param resource the upstream VulnerableRegionResource
     * @return the mentorship-language VulnerableClusterAclResult
     */
    @Mapping(source = "regionName", target = "clusterName")
    VulnerableClusterAclResult toAclResult(VulnerableRegionResource resource);

    /**
     * Translates a list of upstream resources into mentorship ACL results.
     *
     * @param resources the list of upstream VulnerableRegionResources
     * @return the list of mentorship-language VulnerableClusterAclResults
     */
    List<VulnerableClusterAclResult> toAclResultList(List<VulnerableRegionResource> resources);
}