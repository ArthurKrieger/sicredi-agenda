package com.sicredi.agenda.infrastructure.database.vote;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VoteToEntityConverterTest {

    @InjectMocks
    private VoteToEntityConverter voteToEntityConverter;

    @Test
    void convertsVoteToVoteEntity() {
        final Associate associate = Associate.builder()
                .cpf("12345678900")
                .build();

        final Vote vote = Vote.builder()
                .inFavor(true)
                .associate(associate)
                .build();

        final VoteEntity result = voteToEntityConverter.convert(vote);

        assertThat(result.inFavor()).isTrue();
        assertThat(result.cpf()).isEqualTo("12345678900");
        assertThat(result.id()).isNull();
    }

    @Test
    void convertsVoteWithFalseVote() {
        final Associate associate = Associate.builder()
                .cpf("98765432100")
                .build();

        final Vote vote = Vote.builder()
                .inFavor(false)
                .associate(associate)
                .build();

        final VoteEntity result = voteToEntityConverter.convert(vote);

        assertThat(result.inFavor()).isFalse();
        assertThat(result.cpf()).isEqualTo("98765432100");
        assertThat(result.id()).isNull();
    }
}