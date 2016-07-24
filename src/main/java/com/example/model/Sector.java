package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Model object, which represents the SECTOR database table
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/07/16
 */
public class Sector implements Serializable {

    private Long id;

    private String name;

    private List<Sector> children;

    private Timestamp created;


    public Sector() {
    }

    public Sector(Long id, String name, Sector... children) {
        this.id = id;
        this.name = name;

        if (children != null) {
            getChildren().addAll(Arrays.asList(children));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sector> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }
}
