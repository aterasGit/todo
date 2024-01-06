package org.home.todo.repositories;

import org.home.todo.models.Addon;
import org.home.todo.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Repository
public interface AddonRepository extends JpaRepository<Addon, Integer> {
    void deleteByCreatedAtBefore(LocalDateTime localDateTime);
    void deleteByTask(Task task);
}
