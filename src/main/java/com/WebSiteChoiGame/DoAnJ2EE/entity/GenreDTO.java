package com.WebSiteChoiGame.DoAnJ2EE.entity;

public class GenreDTO {
    private Integer genreID;
    private String name;

    public GenreDTO(Integer genreID, String name) {
        this.genreID = genreID;
        this.name = name;
    }

    public Integer getGenreID() { return genreID; }
    public void setGenreID(Integer genreID) { this.genreID = genreID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 