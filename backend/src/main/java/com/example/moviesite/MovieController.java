package com.example.moviesite;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        if (movieRepository.findAll().isEmpty()) {
            Movie example = new Movie(
                    "Interstellar", "Sci-Fi", "Netflix",
                    "Ein episches Sci-Fi-Abenteuer über Zeit und Raum.",
                    "157336",  // TMDb-ID
                    "https://image.tmdb.org/t/p/original/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                    "Geplant"
            );
            movieRepository.save(example);
        }
    }


    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie saved = movieRepository.save(movie);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Movie> getMovies() {
        List<Movie> movies = movieRepository.findAll();

        for (Movie movie : movies) {
            ratingRepository.findById(movie.getTmdbId())
                    .ifPresent(r -> movie.setPersonalRating(r.getValue()));
        }

        System.out.println("Filme: " + movieRepository.findAll().size());


        return movies;
    }


    @PutMapping("/{tmdbId}/rating")
    public Rating updateRating(@PathVariable String tmdbId, @RequestBody Double ratingValue) {
        Rating rating = new Rating(tmdbId, ratingValue);
        return ratingRepository.save(rating);
    }


}
