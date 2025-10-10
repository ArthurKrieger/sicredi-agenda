CREATE TABLE event (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       occurred_on TIMESTAMP NOT NULL DEFAULT now(),
                       payload TEXT,
                       published BOOLEAN NOT NULL DEFAULT false
);