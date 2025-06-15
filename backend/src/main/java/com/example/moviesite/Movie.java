package com.example.moviesite;

public class Movie {
    private String title;
    private String genre;
    private String platform;
    private Double personalRating;
    private String tmdbId;
    private String posterUrl;
    private String status;

    public Movie() {}

    public Movie(String title, String genre, String platform, Double personalRating, String tmdbId, String posterUrl, String status) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.personalRating = personalRating;
        this.tmdbId = tmdbId;
        this.posterUrl = posterUrl;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getPlatform() { return platform; }
    public Double getPersonalRating() { return personalRating; }
    public String getTmdbId() { return tmdbId; }
    public String getPosterUrl() { return posterUrl; }
    public String getStatus() { return status; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPlatform(String platform) { this.platform = platform; }
    public void setPersonalRating(Double personalRating) { this.personalRating = personalRating; }
    public void setTmdbId(String tmdbId) { this.tmdbId = tmdbId; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public void setStatus(String status) { this.status = status; }
}
