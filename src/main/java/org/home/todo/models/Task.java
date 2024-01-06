package org.home.todo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "header")
    @Size(min = 4, max = 256, message = "4-256 characters required")
    @NotEmpty(message = "4-256 characters required")
    private String header;

    @Column(name = "info")
    @Size(max = 1024, message = "0-1024 characters required")
    private String info;

    @Column(name = "datetime")
    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "task")
    private List<Addon> addons;

    public Task() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public void setAddons(List<Addon> addons) {
        this.addons = addons;
    }
}
