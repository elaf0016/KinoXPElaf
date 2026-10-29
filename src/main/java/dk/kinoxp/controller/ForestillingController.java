package dk.kinoxp.controller;

import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.service.ForestillingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ForestillingController {

    private final ForestillingService forestillingService;

    public ForestillingController(ForestillingService forestillingService) {
        this.forestillingService = forestillingService;
    }

    @GetMapping("/api/forestillinger")
    public List<Forestilling> hentKommendeForestillinger() {
        return forestillingService.kommendeForestillinger();
    }

    @GetMapping("/api/film/{filmId}/forestillinger")
    public List<Forestilling> hentForestillingerForFilm(@PathVariable Long filmId) {
        return forestillingService.kommendeForestillingerForFilm(filmId);
    }
}
