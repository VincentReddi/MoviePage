package com.example.moviesite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;

    public MovieController(RatingRepository ratingRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getMovies() {
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            ratingRepository.findById(movie.getTmdbId())
                    .ifPresent(r -> movie.setPersonalRating(r.getValue()));
        }

        return movies;
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie saved = movieRepository.save(movie);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{tmdbId}/rating")
    public Rating updateRating(@PathVariable String tmdbId, @RequestBody Double ratingValue) {
        Rating rating = new Rating(tmdbId, ratingValue);
        return ratingRepository.save(rating);
    }
}
