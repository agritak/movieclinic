CREATE TABLE movies(
	movie_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_title VARCHAR(200),
	movie_plot VARCHAR(500),
    movie_year INT,
    movie_picture_url VARCHAR(200),
    movie_trailer_url VARCHAR(200)
    );

CREATE TABLE categories(
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100),
    category_description VARCHAR(500),
    category_picture VARCHAR(250)
    );

CREATE TABLE movie_category(
    movie_id INT,
    category_id INT
    );