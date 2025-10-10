package com.sicredi.agenda.application.validation;

import com.sicredi.agenda.TestFixtures;
import com.sicredi.agenda.domain.agenda.Agenda;
import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.associate.AssociateValidator;
import com.sicredi.agenda.domain.session.SessionId;
import com.sicredi.agenda.domain.vote.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class VoteValidatorTest {

    private final AssociateValidator associateValidator1 = mock(AssociateValidator.class);

    private final AssociateValidator associateValidator2=mock(AssociateValidator.class);

    private final VoteValidator voteValidator=new VoteValidator(List.of(associateValidator1, associateValidator2));

    private final Vote vote = mock(Vote.class);
    private final Associate associate = mock(Associate.class);

    @BeforeEach
    void setUp() {
        when(vote.associate()).thenReturn(associate);
    }

    @Test
    void validationPassesWhenAllAssociateValidatorsPass() {
        voteValidator.validate(TestFixtures.AGENDA, TestFixtures.SESSION_ID, vote);

        verify(associateValidator1).validate(associate);
        verify(associateValidator2).validate(associate);
    }

    @Test
    void validationThrowsExceptionWhenAssociateValidationFails() {
        doThrow(new RuntimeException("Validation failed")).when(associateValidator1).validate(associate);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> voteValidator.validate(TestFixtures.AGENDA, TestFixtures.SESSION_ID, vote));

        verify(associateValidator1).validate(associate);
        verifyNoInteractions(associateValidator2);
    }

    @Test
    void validationPassesWithEmptyAssociateValidatorsList() {
        VoteValidator emptyValidator = new VoteValidator(List.of());

        assertThatNoException().isThrownBy(() -> emptyValidator.validate(TestFixtures.AGENDA, TestFixtures.SESSION_ID, vote));
    }
}