package dk.kinoxp;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Tester REST API'et mod testdata fra DataSeeder:
// 8 film, 3 sale (120, 80 og 40 pladser) og 7 forestillinger pr. film de næste 7 dage
@SpringBootTest
@Transactional
class KinoApiTests {

    // Forestilling 1 er Interstellar i Sal 1 (120 pladser), forestilling 3 er i Sal 3 (40 pladser)
    private static final long FORESTILLING_I_SAL_1 = 1;
    private static final long FORESTILLING_I_SAL_3 = 3;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void henterAlleFilm() throws Exception {
        mockMvc.perform(get("/api/film"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8));
    }

    @Test
    void henterEnFilm() throws Exception {
        mockMvc.perform(get("/api/film/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titel").value("Interstellar"));
    }

    @Test
    void ukendtFilmGiver404() throws Exception {
        mockMvc.perform(get("/api/film/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void henterKommendeForestillingerMedLedigePladser() throws Exception {
        mockMvc.perform(get("/api/forestillinger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(56))
                .andExpect(jsonPath("$[0].ledigePladser").isNumber());
    }

    @Test
    void henterForestillingerForFilm() throws Exception {
        mockMvc.perform(get("/api/film/2/forestillinger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$[*].film.titel", everyItem(is("The Dark Knight"))));
    }

    @Test
    void opretterReservationMedBookingKode() throws Exception {
        String svar = reserver("Anna", 2, FORESTILLING_I_SAL_1)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.navn").value("Anna"))
                .andExpect(jsonPath("$.bookingKode", matchesPattern("KXP-[A-Z2-9]{6}")))
                .andExpect(jsonPath("$.forestilling.film.titel").value("Interstellar"))
                .andReturn().getResponse().getContentAsString();

        String kode = JsonPath.read(svar, "$.bookingKode");

        mockMvc.perform(get("/api/reservationer/" + kode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.navn").value("Anna"));

        // Bookingkoden skal også kunne findes med små bogstaver
        mockMvc.perform(get("/api/reservationer/" + kode.toLowerCase()))
                .andExpect(status().isOk());
    }

    @Test
    void reservationTraekkerFraLedigePladser() throws Exception {
        reserver("Anna", 2, FORESTILLING_I_SAL_1).andExpect(status().isCreated());

        mockMvc.perform(get("/api/film/1/forestillinger"))
                .andExpect(jsonPath("$[0].id").value(FORESTILLING_I_SAL_1))
                .andExpect(jsonPath("$[0].ledigePladser").value(118));
    }

    @Test
    void afviserNaarForestillingenErUdsolgt() throws Exception {
        for (int i = 0; i < 4; i++) {
            reserver("Gruppe " + i, 10, FORESTILLING_I_SAL_3).andExpect(status().isCreated());
        }

        reserver("Sidste", 1, FORESTILLING_I_SAL_3)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Der er kun 0 ledige pladser til denne forestilling"));
    }

    @Test
    void afviserReservationUdenNavn() throws Exception {
        reserver(" ", 2, FORESTILLING_I_SAL_1)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Navn skal udfyldes"));
    }

    @Test
    void afviserNulBilletter() throws Exception {
        reserver("Anna", 0, FORESTILLING_I_SAL_1)
                .andExpect(status().isBadRequest());
    }

    @Test
    void afviserForMangeBilletter() throws Exception {
        reserver("Anna", 11, FORESTILLING_I_SAL_1)
                .andExpect(status().isBadRequest());
    }

    @Test
    void afviserUkendtForestilling() throws Exception {
        reserver("Anna", 2, 999)
                .andExpect(status().isNotFound());
    }

    @Test
    void ukendtBookingKodeGiver404() throws Exception {
        mockMvc.perform(get("/api/reservationer/KXP-FINDES"))
                .andExpect(status().isNotFound());
    }

    private ResultActions reserver(String navn, int antal, long forestillingId) throws Exception {
        return mockMvc.perform(post("/api/reservationer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"navn": "%s", "antalBilletter": %d, "forestilling": {"id": %d}}
                        """.formatted(navn, antal, forestillingId)));
    }
}
