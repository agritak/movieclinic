CREATE TABLE IF NOT EXISTS categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    picture_url VARCHAR(250),
    UNIQUE KEY uk_categories (name)
    );