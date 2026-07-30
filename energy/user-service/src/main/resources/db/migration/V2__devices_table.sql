CREATE TABLE `devices`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`     VARCHAR(100) NOT NULL,
    `type`     VARCHAR(100) NOT NULL,
    `location` VARCHAR(100),
    `user_id`  BIGINT       NOT NULL,
    KEY        `idx_user_id` (`user_id`),
    CONSTRAINT `fk_user_id`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
            ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;