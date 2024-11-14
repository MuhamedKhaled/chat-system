CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          number INT NOT NULL,
                          chat_id BIGINT NOT NULL,
                          body TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
                          UNIQUE KEY chat_id_and_message_number_index (chat_id, number)
)