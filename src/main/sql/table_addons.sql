/*****
 * Copyright (c) 2023 Renat Salimov
 **/

CREATE TABLE addons (
    id SERIAL,
    url VARCHAR(1024) NOT NULL,
    createdAt TIMESTAMP NOT NULL,
    task_id INT REFERENCES tasks(id) ON DELETE CASCADE
);

