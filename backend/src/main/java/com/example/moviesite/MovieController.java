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
    private final String test = "test";

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
    @PutMapping("/{id}/status")
    public ResponseEntity<Movie> updateStatus(@PathVariable Long id, @RequestBody String status) {
        Optional<Movie> movieOpt = movieRepository.findById(id);

        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            movie.setStatus(status.replace("\"", ""));
            movieRepository.save(movie);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<Movie> updateRating(@PathVariable Long id, @RequestBody Double rating) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            movie.setRating(rating);
            movieRepository.save(movie);
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}
