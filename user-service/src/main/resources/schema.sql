CREATE TABLE IF NOT EXISTS company
(
    company_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    code       VARCHAR(20)  NOT NULL UNIQUE,
    address    VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS role
(
    role_id    SMALLINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id    BIGINT,
    full_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by    VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at    TIMESTAMP,
    updated_by    VARCHAR(50),
    CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company (company_id) ON DELETE SET NULL ON UPDATE CASCADE
);
