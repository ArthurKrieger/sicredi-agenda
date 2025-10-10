package com.sicredi.agenda.infrastructure.database.vote;

import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class EntityToVoteConverterTest {

    @InjectMocks
    private EntityToVoteConverter entityToVoteConverter;

    @Test
    void convertsVoteEntityToVote() {
        final VoteEntity voteEntity = VoteEntity.builder()
                .id(UUID.randomUUID())
                .inFavor(true)
                .cpf("12345678900")
                .build();

        final Vote result = entityToVoteConverter.convert(voteEntity);

        assertThat(result.inFavor()).isTrue();
        assertThat(result.associate().cpf()).isEqualTo("12345678900");
    }

    @Test
    void convertsVoteEntityWithFalseVote() {
        final VoteEntity voteEntity = VoteEntity.builder()
                .id(UUID.randomUUID())
                .inFavor(false)
                .cpf("98765432100")
                .build();

        final Vote result = entityToVoteConverter.convert(voteEntity);

        assertThat(result.inFavor()).isFalse();
        assertThat(result.associate().cpf()).isEqualTo("98765432100");
    }
}
