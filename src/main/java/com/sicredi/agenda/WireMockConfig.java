package com.sicredi.agenda;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Configuration
public class WireMockConfig {

    private static WireMockServer wireMockServer = null;
    @Value("${wiremock.standalone.port}")
    private int wiremockPort;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer wireMockServer() {
        return Objects.requireNonNullElseGet(wireMockServer, this::configureWireMock);

    }

    private WireMockServer configureWireMock() {
        wireMockServer = new WireMockServer(wiremockPort);
        configureStubs(wireMockServer);
        return wireMockServer;
    }

    private void configureStubs(WireMockServer wireMockServer) {
        wireMockServer.stubFor(get(urlPathMatching("/users/12345678900"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\": \"ABLE_TO_VOTE\"}")));

        wireMockServer.stubFor(get(urlPathMatching("/users/98765432100"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\": \"UNABLE_TO_VOTE\"}")));

        wireMockServer.stubFor(get(urlPathMatching("/users/00000000000"))
                .willReturn(aResponse().withStatus(404)));
    }

}