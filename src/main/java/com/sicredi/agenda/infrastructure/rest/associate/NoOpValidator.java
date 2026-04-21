package com.sicredi.agenda.infrastructure.rest.associate;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.associate.AssociateValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoOpValidator implements AssociateValidator {

    //TODO: Update to call real service once its deployed in k8s
    @Override
    public void validate(Associate associate) {
        return;
    }

}

