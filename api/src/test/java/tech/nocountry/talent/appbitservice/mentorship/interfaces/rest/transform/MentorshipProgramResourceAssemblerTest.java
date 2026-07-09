package tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates.MentorshipProgram;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;
import tech.nocountry.talent.appbitservice.mentorship.interfaces.rest.resources.MentorshipProgramResource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for the MapStruct-generated {@link MentorshipProgramResourceAssembler}.
 *
 * <p>Instantiates the assembler directly via its mapper implementation produced by
 * MapStruct during the {@code test-compile} phase (annotation processing writes the
 * impl class to {@code target/generated-sources/annotations}). No Spring context
 * is necessary for the plain attribute mappings exercised here.</p>
 */
class MentorshipProgramResourceAssemblerTest {

    private MentorshipProgramResourceAssembler assembler = new MentorshipProgramResourceAssemblerImpl();

    @Test
    @DisplayName("toResource maps programId, focusArea (TECH) and modality (REMOTE) correctly")
    void toResource_MapsCoreFields() {
        var command = new CreateMentorshipProgramCommand(
                "MPR-ASSEMB-001",
                "Tech Mentorship Program",
                "Description of the program",
                "Org X",
                "TECH",
                "REMOTE",
                "YOUNG_ADULTS",
                "D",
                "clusterA",
                10,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31),
                "https://example.org",
                "contact@example.org"
        );
        var program = MentorshipProgram.create(command);

        MentorshipProgramResource resource = assembler.toResource(program);

        assertThat(resource).isNotNull();
        assertThat(resource.programId()).isEqualTo("MPR-ASSEMB-001");
        assertThat(resource.name()).isEqualTo("Tech Mentorship Program");
        assertThat(resource.clusterName()).isEqualTo("clusterA");
        assertThat(resource.focusArea()).isEqualTo("TECH");
        assertThat(resource.modality()).isEqualTo("REMOTE");
        assertThat(resource.totalCapacity()).isEqualTo(10);
        assertThat(resource.isActive()).isTrue();
    }
}
