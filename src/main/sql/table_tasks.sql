/*****
 * Copyright (c) 2023 Renat Salimov
 **/

CREATE TABLE tasks
(
    id SERIAL PRIMARY KEY,
    header VARCHAR(246) NOT NULL,
    info VARCHAR(1024),
    datetime TIMESTAMP NOT NULL,
    user_id INT REFERENCES users(id) ON DELETE CASCADE
);
