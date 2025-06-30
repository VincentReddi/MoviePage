package com.example.moviesite;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GetMapping
    public List<Movie> getAll() {
        return movieRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok(movieRepository.save(movie));
    }

    @DeleteMapping
    public void deleteAllMovies() {
        movieRepository.deleteAll();
    }

    // Neuer Endpunkt zum Aktualisieren des Status
    @PutMapping("/{tmdbId}/status")
    public ResponseEntity<Movie> updateStatus(@PathVariable String tmdbId, @RequestBody String status) {
        Optional<Movie> movieOpt = movieRepository.findAll().stream()
                .filter(m -> tmdbId.equals(m.getTmdbId()))
                .findFirst();

        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            movie.setStatus(status.replace("\"", "")); // Quotes aus JSON-String entfernen
            movieRepository.save(movie);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
