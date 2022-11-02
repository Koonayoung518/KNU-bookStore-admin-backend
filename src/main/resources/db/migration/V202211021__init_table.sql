DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS bookstore_info;
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS sell;

CREATE TABLE admin (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(63),
    password VARCHAR(255),
    salt VARCHAR(63),
    refresh_token VARCHAR(255)
);

CREATE TABLE book(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    registration_date TIMESTAMP,
    isbn VARCHAR(63),
    title VARCHAR(255),
    publisher VARCHAR(255),
    author VARCHAR(255),
    price INTEGER,
    image VARCHAR(255),
    pubdate VARCHAR(63),
    stock INTEGER,
    type VARCHAR(15)
);

CREATE TABLE bookstore_info(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    operation_time VARCHAR(255),
    phone VARCHAR(255),
    location VARCHAR(255),
    notice VARCHAR(255)
);

CREATE TABLE history(
    history_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sell_date TIMESTAMP,
    total_price INTEGER,
    change_price INTEGER,
    money INTEGER,
    payment VARCHAR(15)
);

CREATE TABLE sell(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(63),
    title VARCHAR(255),
    unit_price INTEGER,
    amount INTEGER,
    total INTEGER,
    history_id BIGINT NOT NULL,
    FOREIGN KEY(history_id) REFERENCES history(history_id)
);
