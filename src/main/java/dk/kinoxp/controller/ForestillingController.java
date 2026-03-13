package dk.kinoxp.controller;

import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.repository.ForestillingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForestillingController {

    private final ForestillingRepository forestillingRepository;

    public ForestillingController(ForestillingRepository forestillingRepository) {
        this.forestillingRepository = forestillingRepository;
    }

    @GetMapping("/api/forestillinger")
    public List<Forestilling> hentAlleForestillinger() {
        return forestillingRepository.findAll();
    }

    @GetMapping("/api/film/{filmId}/forestillinger")
    public List<Forestilling> hentForestillingerForFilm(@PathVariable Long filmId) {
        return forestillingRepository.findByFilmId(filmId);
    }
}