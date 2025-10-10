package com.sicredi.agenda.infrastructure.rest.associate;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.associate.AssociateValidator;
import com.sicredi.agenda.domain.exception.AssociateInvalidCpfException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class RestBasedCpfValidator implements AssociateValidator {

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    private static final String USERS_PATH = "users";

    private final RestTemplate restTemplate;

    @Value("${sicredi.endpoints.associate-host}")
    private String associateHost;

    @Override
    public void validate(Associate associate) {
        CpfResponse response = restTemplate.getForObject(buildUserUrl(associate.cpf()), CpfResponse.class);

        if (response == null || !ABLE_TO_VOTE.equals(response.status())) {
            throw new AssociateInvalidCpfException();
        }
    }

    private String buildUserUrl(String cpf) {
        return UriComponentsBuilder
                .fromUriString(associateHost)
                .pathSegment(USERS_PATH, cpf)
                .toUriString();
    }

    protected record CpfResponse(String status) {
    }
}