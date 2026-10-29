package dk.kinoxp.controller;

import dk.kinoxp.entity.Film;
import dk.kinoxp.repository.FilmRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping("/api/film")
    public List<Film> hentAlleFilm() {
        return filmRepository.findAll();
    }

    @GetMapping("/api/film/{id}")
    public Film hentFilm(@PathVariable Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filmen findes ikke"));
    }
}
