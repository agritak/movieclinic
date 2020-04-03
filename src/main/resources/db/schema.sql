CREATE TABLE movies(
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200),
	description VARCHAR(500),
    year INT,
    picture_url VARCHAR(200),
    trailer_url VARCHAR(200)
    );

CREATE TABLE categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(500),
    picture_url VARCHAR(250)
    );

CREATE TABLE movie_category(
    movie_id INT NOT NULL,
    category_id INT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
    );