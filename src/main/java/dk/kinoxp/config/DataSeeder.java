package dk.kinoxp.config;

import dk.kinoxp.entity.Film;
import dk.kinoxp.entity.Forestilling;
import dk.kinoxp.entity.Sal;
import dk.kinoxp.repository.FilmRepository;
import dk.kinoxp.repository.ForestillingRepository;
import dk.kinoxp.repository.SalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Indsætter testdata ved opstart. Forestillingerne lægges altid de næste 7 dage,
// så der er noget at reservere, uanset hvornår programmet startes.
@Component
public class DataSeeder implements CommandLineRunner {

    private static final String PLAKAT = "https://image.tmdb.org/t/p/w500/";
    private static final List<LocalTime> STARTTIDER =
            List.of(LocalTime.of(17, 0), LocalTime.of(19, 30), LocalTime.of(21, 45));

    private final FilmRepository filmRepository;
    private final SalRepository salRepository;
    private final ForestillingRepository forestillingRepository;

    public DataSeeder(FilmRepository filmRepository, SalRepository salRepository,
                      ForestillingRepository forestillingRepository) {
        this.filmRepository = filmRepository;
        this.salRepository = salRepository;
        this.forestillingRepository = forestillingRepository;
    }

    @Override
    public void run(String... args) {
        if (filmRepository.count() > 0) {
            return;
        }

        List<Film> film = filmRepository.saveAll(List.of(
                new Film("Interstellar", 169, 11, "Sci-fi", PLAKAT + "gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                        "Jorden er ved at blive ubeboelig, og en gruppe astronauter rejser gennem et ormehul "
                                + "for at finde et nyt hjem til menneskeheden."),
                new Film("The Dark Knight", 152, 15, "Action", PLAKAT + "qJ2tW6WMUDux911r6m7haRef0WH.jpg",
                        "Batman møder sin hidtil farligste modstander, Jokeren, der vil kaste Gotham ud i kaos."),
                new Film("Dune: Part Two", 166, 15, "Sci-fi", PLAKAT + "eAiKuHifVXObxAmjcuq15l2O6SN.jpg",
                        "Paul Atreides slår sig sammen med ørkenfolket Fremen for at hævne sin familie "
                                + "og standse en krig om planeten Arrakis."),
                new Film("Oppenheimer", 180, 15, "Drama", PLAKAT + "913IzxpCeL2mgH2l22fSOiM96cA.jpg",
                        "Historien om fysikeren J. Robert Oppenheimer og udviklingen af atombomben "
                                + "under Anden Verdenskrig."),
                new Film("Inception", 148, 11, "Thriller", PLAKAT + "xlaY2zyzMfkhk0HSC5VUwzoZPU1.jpg",
                        "En tyv, der stjæler hemmeligheder fra folks drømme, får til opgave at plante "
                                + "en idé i stedet."),
                new Film("Spider-Man: Across the Spider-Verse", 140, 7, "Animation",
                        PLAKAT + "igFOJA8TY1K7l8KwSq75yXK8N6L.jpg",
                        "Miles Morales kastes ud på et eventyr på tværs af multiverset og møder "
                                + "et helt hold af Spider-folk."),
                new Film("Inside Out 2", 96, 0, "Familie", PLAKAT + "yVyz6VW78JRucHa8a0Pf4RqnAL4.jpg",
                        "Riley er blevet teenager, og nye følelser flytter ind i hovedkvarteret."),
                new Film("Top Gun: Maverick", 131, 11, "Action", PLAKAT + "z5DrWMX1GIxiCmwDe1FVHAAZiQr.jpg",
                        "Efter 30 år som pilot skal Maverick træne en ny generation til en farlig mission.")
        ));

        List<Sal> sale = salRepository.saveAll(List.of(
                new Sal("Sal 1", 120),
                new Sal("Sal 2", 80),
                new Sal("Sal 3", 40)
        ));

        // Hver film vises én gang om dagen. Kombinationen af starttid og sal er forskellig
        // for hver film samme dag, så to film aldrig bruger samme sal på samme tid.
        LocalDate iMorgen = LocalDate.now().plusDays(1);
        for (int dag = 0; dag < 7; dag++) {
            for (int i = 0; i < film.size(); i++) {
                int kombination = (i + dag) % (STARTTIDER.size() * sale.size());
                LocalTime tid = STARTTIDER.get(kombination / sale.size());
                Sal sal = sale.get(kombination % sale.size());
                forestillingRepository.save(new Forestilling(iMorgen.plusDays(dag).atTime(tid), film.get(i), sal));
            }
        }
    }
}
