CREATE TABLE IF NOT EXISTS country (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    code VARCHAR(3) NOT NULL,
    coordinates TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE UNIQUE INDEX IF NOT EXISTS uk_country_code ON country(code);
CREATE INDEX IF NOT EXISTS idx_country_code ON country(code);