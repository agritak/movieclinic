CREATE TABLE IF NOT EXISTS movies(
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
	description VARCHAR(500),
    year INT NOT NULL,
    picture_url VARCHAR(200),
    trailer_url VARCHAR(200),
    UNIQUE KEY uk_movies (name, year),
    INDEX (year)
    );
