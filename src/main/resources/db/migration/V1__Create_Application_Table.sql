CREATE TABLE applications (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             token BINARY(16) NOT NULL UNIQUE,
                             name VARCHAR(255) NOT NULL,
                             chats_count INT DEFAULT 0,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             UNIQUE KEY application_token_index (token)
);