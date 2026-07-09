package tech.nocountry.talent.appbitservice.mentorship.domain.model.valueobjects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tech.nocountry.talent.appbitservice.mentorship.domain.exceptions.InvalidMentorshipModalityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for the {@link MentorshipModality} Value Object.
 */
class MentorshipModalityTest {

    @Test
    @DisplayName("of(REMOTE) returns REMOTE constant")
    void of_Remote_ReturnsRemoteConstant() {
        var vo = MentorshipModality.of("REMOTE");

        assertThat(vo.getValue()).isEqualTo("REMOTE");
        assertThat(vo.isRemote()).isTrue();
    }

    @Test
    @DisplayName("of(invalid) throws InvalidMentorshipModalityException")
    void of_InvalidValue_ThrowsException() {
        assertThatThrownBy(() -> MentorshipModality.of("INVALID"))
                .isInstanceOf(InvalidMentorshipModalityException.class);
    }

    @Test
    @DisplayName("of(null) throws InvalidMentorshipModalityException")
    void of_Null_ThrowsException() {
        assertThatThrownBy(() -> MentorshipModality.of(null))
                .isInstanceOf(InvalidMentorshipModalityException.class);
    }

    @Test
    @DisplayName("of(\"hybrid\") normalizes to uppercase \"HYBRID\"")
    void of_Lowercase_NormalizesToUppercase() {
        var vo = MentorshipModality.of("hybrid");

        assertThat(vo.getValue()).isEqualTo("HYBRID");
    }
}
