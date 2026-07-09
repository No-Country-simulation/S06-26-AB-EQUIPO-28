package tech.nocountry.talent.appbitservice.mentorship.domain.model.aggregates;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipFocusAreaException;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipModalityException;
import tech.nocountry.talent.appbitservice.mentorship.domain.model.commands.CreateMentorshipProgramCommand;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link MentorshipProgram} aggregate root.
 *
 * <p>These tests exercise the domain factory and the state-transition methods
 * (capacity, enrollment, activation) without any Spring/JPA wiring — pure domain
 * logic. Uses JUnit 5 + AssertJ as declared in {@code pom.xml} via
 * {@code spring-boot-starter-test}.</p>
 */
class MentorshipProgramTest {

    private static CreateMentorshipProgramCommand validCommand() {
        return new CreateMentorshipProgramCommand(
                "MPR-TEST-001",
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
    }

    @Test
    @DisplayName("create with valid command returns aggregate with default state")
    void create_ValidCommand_ReturnsAggregate() {
        var command = validCommand();

        var program = MentorshipProgram.create(command);

        assertThat(program).isNotNull();
        assertThat(program.getProgramId()).isEqualTo("MPR-TEST-001");
        assertThat(program.getName()).isEqualTo("Tech Mentorship Program");
        assertThat(program.getFocusArea()).isNotNull();
        assertThat(program.getFocusArea().getValue()).isEqualTo("TECH");
        assertThat(program.getModality()).isNotNull();
        assertThat(program.getModality().getValue()).isEqualTo("REMOTE");
        assertThat(program.getClusterName()).isEqualTo("clusterA");
        assertThat(program.getActiveMentees()).isZero();
        assertThat(program.isActive()).isTrue();
        assertThat(program.getTotalCapacity()).isEqualTo(10);
    }

    @Test
    @DisplayName("create with invalid focusArea throws InvalidMentorshipFocusAreaException")
    void create_InvalidFocusArea_ThrowsException() {
        var command = new CreateMentorshipProgramCommand(
                "MPR-TEST-002",
                "Program",
                null,
                null,
                "INVALID",
                "REMOTE",
                null,
                null,
                "clusterA",
                10,
                null,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> MentorshipProgram.create(command))
                .isInstanceOf(InvalidMentorshipFocusAreaException.class);
    }

    @Test
    @DisplayName("create with invalid modality throws InvalidMentorshipModalityException")
    void create_InvalidModality_ThrowsException() {
        var command = new CreateMentorshipProgramCommand(
                "MPR-TEST-003",
                "Program",
                null,
                null,
                "TECH",
                "INVALID",
                null,
                null,
                "clusterA",
                10,
                null,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> MentorshipProgram.create(command))
                .isInstanceOf(InvalidMentorshipModalityException.class);
    }

    @Test
    @DisplayName("enrollMentee on a program with available capacity increments activeMentees")
    void enrollMentee_ValidCapacity_IncrementsActiveMentees() {
        var command = validCommand();
        var program = MentorshipProgram.create(command);
        assertThat(program.getActiveMentees()).isZero();

        program.enrollMentee();
        program.enrollMentee();

        assertThat(program.getActiveMentees()).isEqualTo(2);
    }

    @Test
    @DisplayName("deactivate sets isActive=false and records endDate")
    void deactivate_SetsInactiveAndEndDate() {
        var program = MentorshipProgram.create(validCommand());
        assertThat(program.isActive()).isTrue();
        assertThat(program.getEndDate()).isNotNull();

        program.deactivate();

        assertThat(program.isActive()).isFalse();
        assertThat(program.getEndDate()).isNotNull();
    }
}
