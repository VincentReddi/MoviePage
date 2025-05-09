package com.example.moviesite;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class MovieController {

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("/movies");
    }

    @GetMapping("/movies")
    public List<Movie> getAllMovies() {
        return List.of(
                new Movie("Interstellar", "Sci-Fi", "Netflix", 9.0, "123", "https://image.tmdb.org/t/p/original/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", "gesehen"),
                new Movie("Barbie", "Comedy", "Amazon", 7.0, "456", "https://image.tmdb.org/t/p/original/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg", "geplant")
        );
    }
}
