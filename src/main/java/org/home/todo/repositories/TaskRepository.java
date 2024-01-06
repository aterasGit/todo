package org.home.todo.repositories;

import org.home.todo.models.Task;
import org.home.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(int id, User user);
}


