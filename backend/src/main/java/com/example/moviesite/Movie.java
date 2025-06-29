package com.example.moviesite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String genre;
    private String platform;
    private String description;
    private String tmdbId;
    private String posterPath;
    private String status;

    @Transient
    private Double personalRating;

    // Konstruktoren
    public Movie() {}

    public Movie(String title, String genre, String platform, String description, String tmdbId, String posterPath, String status) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.description = description;
        this.tmdbId = tmdbId;
        this.posterPath = posterPath;
        this.status = status;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTmdbId() { return tmdbId; }
    public void setTmdbId(String tmdbId) { this.tmdbId = tmdbId; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getPersonalRating() { return personalRating; }
    public void setPersonalRating(Double personalRating) { this.personalRating = personalRating; }
}
