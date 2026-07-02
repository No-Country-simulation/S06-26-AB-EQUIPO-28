package tech.nocountry.talent.appbitservice.userandroles.domain.model.queries;

/**
 * Consulta para obtener todos los usuarios del sistema.
 * 
 * @param search término de búsqueda opcional (busca por userId, username o roles)
 */
public record GetAllUsersQuery(String search) {
    public GetAllUsersQuery() {
        this(null);
    }
}
