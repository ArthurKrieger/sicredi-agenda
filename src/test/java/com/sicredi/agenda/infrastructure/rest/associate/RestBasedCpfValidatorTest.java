package com.sicredi.agenda.infrastructure.rest.associate;

import com.sicredi.agenda.domain.associate.Associate;
import com.sicredi.agenda.domain.exception.AssociateInvalidCpfException;
import com.sicredi.agenda.infrastructure.rest.RestTemplateConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest
@ContextConfiguration(classes = {RestBasedCpfValidator.class, RestTemplateConfig.class})
class RestBasedCpfValidatorTest {

    @Autowired
    private RestBasedCpfValidator cpfValidator;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${sicredi.endpoints.associate-host}")
    private String associateHost;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Nested
    class Validate {

        @Test
        void validatesSuccessfullyWhenCpfIsAbleToVote() {
            final Associate associate = Associate.builder().cpf("12345678900").build();
            final String responseJson = """
                    {"status": "ABLE_TO_VOTE"}
                    """;

            mockServer.expect(requestTo(associateHost + "/users/12345678900"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

            assertThatNoException().isThrownBy(() -> cpfValidator.validate(associate));

            mockServer.verify();
        }

        @Test
        void throwsExceptionWhenCpfIsUnableToVote() {
            final Associate associate = Associate.builder().cpf("98765432100").build();
            final String responseJson = """
                    {"status": "UNABLE_TO_VOTE"}
                    """;

            mockServer.expect(requestTo(associateHost + "/users/98765432100"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> cpfValidator.validate(associate))
                    .isInstanceOf(AssociateInvalidCpfException.class);

            mockServer.verify();
        }

        @Test
        void throwsExceptionWhenResponseIsNull() {
            final Associate associate = Associate.builder().cpf("11111111111").build();

            mockServer.expect(requestTo(associateHost + "/users/11111111111"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

            assertThatThrownBy(() -> cpfValidator.validate(associate))
                    .isInstanceOf(AssociateInvalidCpfException.class);

            mockServer.verify();
        }
    }
}