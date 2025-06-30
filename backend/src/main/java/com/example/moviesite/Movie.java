package com.example.moviesite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private Long id;
    private Double rating;

    private String title;
    private String posterPath;
    private String status;

    public Movie() {}

    public Movie(String title, String posterPath) {
        this.title = title;
        this.posterPath = posterPath;
    }

    public Long getId() { return id; }


    public Double getRating() {
        return rating;
    }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setRating(Double rating) {
        this.rating = rating;
    }
}
