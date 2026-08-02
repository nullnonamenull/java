CREATE TABLE outbox_message
(
    id              UUID PRIMARY KEY,
    aggregate_type  VARCHAR(255) NOT NULL,
    aggregate_id    VARCHAR(255) NOT NULL,
    event_type      VARCHAR(255) NOT NULL,
    payload         JSONB        NOT NULL,
    headers         JSONB        NOT NULL DEFAULT '{}'::jsonb,
    status          VARCHAR(16)  NOT NULL DEFAULT 'NEW',
    attempts        INTEGER      NOT NULL DEFAULT 0,
    last_error      TEXT,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    next_attempt_at TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT outbox_message_status_chk
        CHECK (status IN ('NEW', 'FAILED'))
);

CREATE INDEX idx_outbox_pending
    ON outbox_message (next_attempt_at, created_at)
    WHERE status = 'NEW';