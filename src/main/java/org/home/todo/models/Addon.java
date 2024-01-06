package org.home.todo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Entity
@Table(name = "addons")
@Getter
@Setter
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "url")
    private String url;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    public Addon() {}

    public Addon(String url, LocalDateTime createdAt, Task task) {
        this.url = url;
        this.createdAt = createdAt;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreated_at() {
        return createdAt;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.createdAt = created_at;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
