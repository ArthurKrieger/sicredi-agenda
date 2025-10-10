package com.sicredi.agenda.presentation.rest.vote;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VoteConverterTest {

    private final VoteConverter converter = new VoteConverter();

    @Nested
    class Convert {

        @Test
        void shouldConvertVoteDtoWithSimToVoteInFavor() {
            final VoteDto dto = VoteDto.builder()
                    .cpf("12345678901")
                    .vote(VoteValue.SIM)
                    .build();

            final Vote result = converter.convert(dto);

            assertThat(result).isNotNull();
            assertThat(result.inFavor()).isTrue();
            assertThat(result.associate())
                    .extracting(Associate::cpf)
                    .isEqualTo("12345678901");
        }

        @Test
        void shouldConvertVoteDtoWithNaoToVoteAgainst() {
            final VoteDto dto = VoteDto.builder()
                    .cpf("98765432109")
                    .vote(VoteValue.NAO)
                    .build();

            final Vote result = converter.convert(dto);

            assertThat(result).isNotNull();
            assertThat(result.inFavor()).isFalse();
            assertThat(result.associate())
                    .extracting(Associate::cpf)
                    .isEqualTo("98765432109");
        }
    }
}