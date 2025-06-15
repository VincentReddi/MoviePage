package com.example.moviesite;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final RatingRepository ratingRepository;

    public MovieController(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @GetMapping
    public List<Movie> getMovies() {
        List<Movie> movies = List.of(
                new Movie("Interstellar", "Sci-Fi", "Netflix", null, "123", "https://image.tmdb.org/t/p/original/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", "Watched"),
                new Movie("Barbie", "Comedy", "Prime", null, "456", "https://image.tmdb.org/t/p/original/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg", "Watched")
        );

        for (Movie movie : movies) {
            ratingRepository.findById(movie.getTmdbId())
                    .ifPresent(r -> movie.setPersonalRating(r.getValue()));
        }

        return movies;
    }

    @PutMapping("/{tmdbId}/rating")
    public Rating updateRating(@PathVariable String tmdbId, @RequestBody Double ratingValue) {
        Rating rating = new Rating(tmdbId, ratingValue);
        return ratingRepository.save(rating);
    }
}
