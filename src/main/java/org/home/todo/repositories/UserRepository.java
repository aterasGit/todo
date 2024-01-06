
package org.home.todo.repositories;

import org.home.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}

