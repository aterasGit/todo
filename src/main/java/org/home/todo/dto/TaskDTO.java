package org.home.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/*****
 * Copyright (c) 2023 Renat Salimov
 **/

public class TaskDTO extends RepresentationModel<TaskDTO> {

    private int id;

    @Size(min = 4, max = 256, message = "4-256 characters required")
    @NotEmpty(message = "4-256 characters required")
    private String header;

    @Size(max = 1024, message = "0-1024 characters required")
    private String info;

    @JsonFormat(pattern="dd-MM-yyyy HH:mm")
    private LocalDateTime dateTime;

    private String ownedBy;

    private List<String> addons;

    public TaskDTO() {}

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

    public List<String> getAddons() {
        return addons;
    }

    public void setAddons(List<String> addons) {
        this.addons = addons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
        this.ownedBy = ownedBy;
    }
}
