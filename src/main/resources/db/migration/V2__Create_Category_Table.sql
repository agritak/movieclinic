CREATE TABLE IF NOT EXISTS categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    top INT,
    description VARCHAR(500),
    UNIQUE KEY uk_categories (name)
    );