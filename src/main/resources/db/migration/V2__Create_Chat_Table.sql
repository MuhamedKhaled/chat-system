CREATE TABLE chats (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       number INT NOT NULL,
                       application_id BIGINT NOT NULL,
                       messages_count INT DEFAULT 0,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE,
                       UNIQUE KEY application_id_and_chat_number_index (application_id, number)
)