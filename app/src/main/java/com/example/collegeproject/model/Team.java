package com.example.collegeproject.model;

public class Team {
    long id;
    String name;

    public Team(){}

    public Team(String name) {
        this.name = name;
    }

    public Team(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
