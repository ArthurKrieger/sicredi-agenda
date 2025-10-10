package com.sicredi.agenda.domain.associate;

import lombok.Builder;
import org.springframework.util.Assert;

@Builder
public record Associate(String cpf) {

    public Associate {
        Assert.notNull(cpf, "CPF must not be null");
    }
}
