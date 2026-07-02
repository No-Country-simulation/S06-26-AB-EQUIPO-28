package tech.nocountry.talent.appbitservice.userandroles.domain.exceptions;

public class RoleNotFoundException extends UserDomainException {
    public RoleNotFoundException(String roleName) {
        super("El rol '" + roleName + "' no fue encontrado en el sistema");
    }
    
    public RoleNotFoundException() {
        super("El rol especificado no fue encontrado en el sistema");
    }
}