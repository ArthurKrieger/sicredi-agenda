package com.sicredi.agenda.domain.exception;

public class AssociateInvalidCpfException extends AgendaException {
    private static final String ASSOCIATE_INVALID_CPF_EXCEPTION = "associate.invalid.cpf";

    public AssociateInvalidCpfException() {
        super(ASSOCIATE_INVALID_CPF_EXCEPTION);
    }

}