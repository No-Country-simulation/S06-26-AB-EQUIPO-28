package tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipFocusAreaException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link MentorshipFocusArea} Value Object.
 */
class MentorshipFocusAreaTest {

    @Test
    @DisplayName("of(TECH) returns TECH constant")
    void of_Tech_ReturnsTechConstant() {
        var vo = MentorshipFocusArea.of("TECH");

        assertThat(vo.getValue()).isEqualTo("TECH");
        assertThat(vo.isTech()).isTrue();
    }

    @Test
    @DisplayName("of(invalid) throws InvalidMentorshipFocusAreaException")
    void of_InvalidValue_ThrowsException() {
        assertThatThrownBy(() -> MentorshipFocusArea.of("INVALID"))
                .isInstanceOf(InvalidMentorshipFocusAreaException.class);
    }

    @Test
    @DisplayName("of(null) throws InvalidMentorshipFocusAreaException")
    void of_Null_ThrowsException() {
        assertThatThrownBy(() -> MentorshipFocusArea.of(null))
                .isInstanceOf(InvalidMentorshipFocusAreaException.class);
    }

    @Test
    @DisplayName("of(\"tech\") normalizes to uppercase \"TECH\"")
    void of_Lowercase_NormalizesToUppercase() {
        var vo = MentorshipFocusArea.of("tech");

        assertThat(vo.getValue()).isEqualTo("TECH");
    }
}
