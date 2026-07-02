package tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.transform;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.nocountry.talent.appbitservice.demographics.interfaces.rest.resources.CitizenResource;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.DemographicsAclResult;
import tech.nocountry.talent.appbitservice.inclusioncore.interfaces.acl.demographics.resources.DemographicsCitizenResource;

import java.util.List;

/**
 * Assembler para transformar recursos crudos del upstream Demographics BC al modelo de dominio de inclusion-core.
 *
 * <p>Esta interfaz utiliza MapStruct para garantizar rendimiento ultrarrápido y seguridad de tipos
 * en tiempo de compilación. Transforma {@link DemographicsCitizenResource} (modelo externo)
 * a {@link DemographicsAclResult} (modelo ACL limpio para inclusion-core).</p>
 */
@Mapper(componentModel = "spring")
public interface DemographicsExternalResourceAssembler {

    /**
     * Transforma un recurso de ciudadano externo a una vista ACL.
     *
     * @param resource el recurso externo del upstream Demographics BC
     * @return la vista ACL del dominio de inclusion-core
     */
    DemographicsAclResult toAclView(DemographicsCitizenResource resource);

    /**
     * Transforma una lista de recursos de ciudadanos externos a una lista de vistas ACL.
     *
     * @param resources la lista de recursos externos
     * @return la lista de vistas ACL del dominio de inclusion-core
     */
    List<DemographicsAclResult> toAclViewList(List<DemographicsCitizenResource> resources);

    /**
     * Transforma un CitizenResource (REST DTO del demographics BC) directamente a la vista ACL.
     * <p>Usado cuando el ACL consume los endpoints internos por invocación directa (sin HTTP).</p>
     *
     * @param resource el recurso REST del demographics BC
     * @return la vista ACL del dominio de inclusion-core
     */
    @Mapping(target = "citizenHash", source = "citizenHash")
    @Mapping(target = "mobilityPattern", source = "mobilityPattern")
    DemographicsAclResult fromCitizenResource(CitizenResource resource);

    /**
     * Transforma una lista de CitizenResource a vistas ACL.
     *
     * @param resources la lista de recursos REST del demographics BC
     * @return la lista de vistas ACL del dominio de inclusion-core
     */
    List<DemographicsAclResult> fromCitizenResourceList(List<CitizenResource> resources);
}