CREATE TABLE agenda
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    description VARCHAR(255) NOT NULL
);

CREATE TABLE session
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    agenda_id  UUID      NOT NULL,
    agenda_key INTEGER   NOT NULL,
    start_time TIMESTAMP NOT NULL,
    duration   BIGINT    NOT NULL,
    CONSTRAINT fk_session_agenda FOREIGN KEY (agenda_id) REFERENCES agenda (id)
);

CREATE TABLE vote
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id  UUID        NOT NULL,
    session_key INTEGER     NOT NULL,
    cpf         VARCHAR(36) NOT NULL,
    in_favor    BOOLEAN     NOT NULL,
    CONSTRAINT fk_vote_session FOREIGN KEY (session_id) REFERENCES session (id)
);
