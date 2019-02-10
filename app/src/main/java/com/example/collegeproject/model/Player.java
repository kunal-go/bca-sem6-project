package com.example.collegeproject.model;

public class Player {
    long id;
    long team;
    String players;

    public Player(){}

    public Player(long team, String players) {
        this.team = team;
        this.players = players;
    }

    public Player(long id, long team, String players) {
        this.id = id;
        this.team = team;
        this.players = players;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeam() {
        return team;
    }

    public void setTeam(long team) {
        this.team = team;
    }

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }
}
