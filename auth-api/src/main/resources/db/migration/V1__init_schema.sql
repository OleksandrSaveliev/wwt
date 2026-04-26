CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE processing_log (
                                id UUID PRIMARY KEY,
                                user_id UUID NOT NULL REFERENCES users(id),
                                input_text TEXT NOT NULL,
                                output_text TEXT NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);