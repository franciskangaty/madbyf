CREATE TABLE institutions
(
    institution_id UUID PRIMARY KEY,

    registration_number VARCHAR(100) UNIQUE NOT NULL,

    legal_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    short_name VARCHAR(100),

    ownership_type VARCHAR(50),

    incorporation_date DATE,

    country VARCHAR(100),
    region VARCHAR(100),
    city VARCHAR(100),
    address TEXT,
    postal_code VARCHAR(50),

    timezone VARCHAR(100),
    default_language VARCHAR(20),
    default_currency VARCHAR(10),

    website VARCHAR(255),

    primary_email VARCHAR(255),
    primary_phone VARCHAR(50),

    status VARCHAR(30) NOT NULL,

    registration_verified BOOLEAN NOT NULL DEFAULT FALSE,

    suspension_reason TEXT,
    suspended_until TIMESTAMP,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_institution_country
ON institutions(country);

CREATE INDEX idx_institution_city
ON institutions(city);

CREATE INDEX idx_institution_status
ON institutions(status);

CREATE INDEX idx_institution_registration
ON institutions(registration_number);

CREATE INDEX idx_institution_name
ON institutions(display_name);