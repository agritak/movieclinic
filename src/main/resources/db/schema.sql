CREATE TABLE IF NOT EXISTS movies(
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
	description VARCHAR(500),
    year INT NOT NULL,
    picture_url VARCHAR(200),
    trailer_url VARCHAR(200),
    UNIQUE KEY uk_movies (name, year)
    );

CREATE TABLE IF NOT EXISTS categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    picture_url VARCHAR(250),
    UNIQUE KEY uk_categories (name)
    );

CREATE TABLE IF NOT EXISTS movie_category(
    movie_id INT,
    category_id INT
    );