/*****
 * Copyright (c) 2023 Renat Salimov
 **/

CREATE TABLE users
(
    id SERIAL PRIMARY KEY,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL
);
