CREATE TABLE movies(
	id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
	plot VARCHAR(500),
    year INT,
    picture_url VARCHAR(200),
    trailer_url VARCHAR(200)
    );

CREATE TABLE categories(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(500),
    picture VARCHAR(250)
    );

CREATE TABLE movie_category(
    movie_id INT,
    category_id INT
    );