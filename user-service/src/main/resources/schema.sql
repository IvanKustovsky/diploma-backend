CREATE TABLE IF NOT EXISTS `company`
(
    `id`             BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`           VARCHAR(255) NOT NULL,
    `code`           VARCHAR(20)  NOT NULL UNIQUE,
    `contact_number` VARCHAR(20),
    `address`        VARCHAR(255) NULL,
    `created_at`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    `created_by`     VARCHAR(50)  NOT NULL DEFAULT 'SYSTEM',
    `updated_at`     TIMESTAMP             DEFAULT NULL,
    `updated_by`     VARCHAR(50)           DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `role`
(
    `id`         BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(50) NOT NULL UNIQUE,
    `created_at` TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    `created_by` VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    `updated_at` TIMESTAMP            DEFAULT NULL,
    `updated_by` VARCHAR(50)          DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS `users`
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `company_id`    BIGINT       NULL,
    `role_id`       BIGINT       NOT NULL,
    `is_company`    BOOLEAN               DEFAULT FALSE,
    `full_name`     VARCHAR(255) NOT NULL,
    `email`         VARCHAR(255) NOT NULL UNIQUE,
    `password`      VARCHAR(255) NOT NULL UNIQUE,
    `mobile_number` VARCHAR(20)  NOT NULL UNIQUE,
    `created_at`    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    `created_by`    VARCHAR(50)  NOT NULL DEFAULT 'SYSTEM',
    `updated_at`    TIMESTAMP             DEFAULT NULL,
    `updated_by`    VARCHAR(50)           DEFAULT NULL,
    CONSTRAINT `fk_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
);


