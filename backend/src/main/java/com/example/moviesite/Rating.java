package com.example.moviesite;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Rating {

    @Id
    private String tmdbId;
    private Double value;

    public Rating() {}

    public Rating(String tmdbId, Double value) {
        this.tmdbId = tmdbId;
        this.value = value;
    }

    public String getTmdbId() { return tmdbId; }
    public void setTmdbId(String tmdbId) { this.tmdbId = tmdbId; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
}
