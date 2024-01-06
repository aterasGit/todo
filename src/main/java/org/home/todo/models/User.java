package org.home.todo.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Size(min = 4, max = 64, message = "4-64 characters required")
    @Column(name = "username")
    private String username;

    @Size(min = 4, max = 128, message = "4-128 characters required")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Task> tasks;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != getClass()) return false;
        return Objects.equals(username, ((User) o).username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
