CREATE TABLE IF NOT EXISTS `company`
(
    `id`                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    `companyName`          VARCHAR(255) NOT NULL,
    `companyCode`          VARCHAR(20)  NOT NULL UNIQUE,
    `companyContactNumber` VARCHAR(20),
    `address`              VARCHAR(255) NULL,
    `created_at`           TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    `created_by`           VARCHAR(50)  NOT NULL DEFAULT 'SYSTEM',
    `updated_at`           TIMESTAMP             DEFAULT NULL,
    `updated_by`           VARCHAR(50)           DEFAULT NULL
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

CREATE TABLE IF NOT EXISTS `user`
(
    `id`               BIGINT AUTO_INCREMENT PRIMARY KEY,
    `companyId`        BIGINT       NULL,
    `roleId`           BIGINT       NOT NULL,
    `isCompany`        BOOLEAN               DEFAULT FALSE,
    `fullName`         VARCHAR(255) NOT NULL,
    `email`            VARCHAR(255) NOT NULL UNIQUE,
    `password`         VARCHAR(255) NOT NULL UNIQUE,
    `userMobileNumber` VARCHAR(20)  NOT NULL UNIQUE,
    `created_at`       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    `created_by`       VARCHAR(50)  NOT NULL DEFAULT 'SYSTEM',
    `updated_at`       TIMESTAMP             DEFAULT NULL,
    `updated_by`       VARCHAR(50)           DEFAULT NULL,
    CONSTRAINT `fk_company` FOREIGN KEY (`companyId`) REFERENCES `company` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT `fk_role` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
);


