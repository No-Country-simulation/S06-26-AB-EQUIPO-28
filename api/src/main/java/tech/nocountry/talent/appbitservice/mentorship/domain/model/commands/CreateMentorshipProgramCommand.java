package tech.nocountry.talent.appbitservice.mentorship.domain.model.commands;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

/**
 * Comando de creación de un programa de mentoría.
 *
 * <p>Record inmutable que encapsula los datos de entrada para el factory
 * {@code MentorshipProgram.create(CreateMentorshipProgramCommand)}. Los Value
 * Objects del agregado (focusArea, modality, targetAudience) se construyen
 * dentro del factory a partir de los strings de este comando, centralizando
 * la validación de dominio en la capa de dominio (no en la de aplicación).</p>
 *
 * <p><b>Validación Bean Validation</b>: los campos críticos
 * ({@code programId}, {@code name}, {@code clusterName}, {@code focusArea},
 * {@code modality}) son {@link NotBlank} y se validan en el perímetro REST
 * antes de llegar al caso de uso. La validación semántica de los VOs la hace
 * el factory del agregado.</p>
 *
 * @param programId         identificador de negocio único (e.g., "MPR-001")
 * @param name              nombre del programa
 * @param description       descripción del programa (nullable)
 * @param organization      organización que ofrece el programa (nullable)
 * @param focusArea         área de enfoque (TECH, EMPLOYMENT, HEALTH, CULTURE, EDUCATION, GENERAL)
 * @param modality          modalidad (REMOTE, IN_PERSON, HYBRID)
 * @param targetAudience    público objetivo (YOUNG_ADULTS, WOMEN, ELDERLY, GENERAL; nullable)
 * @param targetIncomeLevel cluster de renta objetivo (A, B, C, D, ALL; nullable)
 * @param clusterName       cluster geográfico (Visent CDRView)
 * @param totalCapacity     capacidad total de mentees (nullable = ilimitada)
 * @param startDate         fecha de inicio (nullable)
 * @param endDate           fecha de fin (nullable)
 * @param websiteUrl        URL del sitio web del programa (nullable)
 * @param contactEmail      email de contacto del programa (nullable)
 */
public record CreateMentorshipProgramCommand(
        @NotBlank(message = "programId no puede estar vacío")
        String programId,
        @NotBlank(message = "name no puede estar vacío")
        String name,
        String description,
        String organization,
        @NotBlank(message = "focusArea no puede estar vacío")
        String focusArea,
        @NotBlank(message = "modality no puede estar vacío")
        String modality,
        String targetAudience,
        String targetIncomeLevel,
        @NotBlank(message = "clusterName no puede estar vacío")
        String clusterName,
        Integer totalCapacity,
        LocalDate startDate,
        LocalDate endDate,
        String websiteUrl,
        String contactEmail
) {
    /**
     * Constructor compact: valida a nivel de contrato que los campos críticos
     * no sean blank. Esta validación es una red adicional de seguridad además
     * de la que realiza Bean Validation en el perímetro REST, porque el comando
     * puede construirse también desde otros BCs vía ACL sin pasar por un
     * controller.
     *
     * @throws IllegalArgumentException si {@code programId}, {@code name},
     *         {@code clusterName}, {@code focusArea} o {@code modality} son blank
     */
    public CreateMentorshipProgramCommand {
        requireNotBlank(programId, "programId");
        requireNotBlank(name, "name");
        requireNotBlank(clusterName, "clusterName");
        requireNotBlank(focusArea, "focusArea");
        requireNotBlank(modality, "modality");
    }

    private static void requireNotBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " no puede estar vacío");
        }
    }
}
