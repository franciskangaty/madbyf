CREATE TABLE domain_event_entry (
    globalIndex BIGSERIAL PRIMARY KEY,
    eventIdentifier VARCHAR(255) NOT NULL UNIQUE,
    aggregateIdentifier VARCHAR(255),
    sequenceNumber BIGINT NOT NULL,
    type VARCHAR(255),
    timeStamp TIMESTAMP NOT NULL,
    payloadType VARCHAR(255) NOT NULL,
    payloadRevision VARCHAR(255),
    payload BYTEA NOT NULL,
    metaData BYTEA
);

CREATE TABLE snapshot_event_entry (
    aggregateIdentifier VARCHAR(255) NOT NULL,
    sequenceNumber BIGINT NOT NULL,
    type VARCHAR(255),
    eventIdentifier VARCHAR(255) NOT NULL UNIQUE,
    timeStamp TIMESTAMP NOT NULL,
    payloadType VARCHAR(255) NOT NULL,
    payloadRevision VARCHAR(255),
    payload BYTEA NOT NULL,
    metaData BYTEA,
    PRIMARY KEY (aggregateIdentifier, sequenceNumber)
);

CREATE TABLE association_value_entry (
    id BIGSERIAL PRIMARY KEY,
    associationKey VARCHAR(255),
    associationValue VARCHAR(255),
    sagaType VARCHAR(255),
    sagaId VARCHAR(255)
);

CREATE INDEX idx_assoc_value_entry_saga_id
ON association_value_entry(sagaId);