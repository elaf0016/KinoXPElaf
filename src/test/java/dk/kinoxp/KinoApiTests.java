package dk.kinoxp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Tester REST API'et mod testdata fra data.sql (2 film, 2 sale, 2 forestillinger)
@SpringBootTest
@Transactional
class KinoApiTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void henterAlleForestillinger() throws Exception {
        mockMvc.perform(get("/api/forestillinger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].film.titel").value("Interstellar"));
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
                .andExpect(status().isNotFound());
    }

    @Test
    void henterForestillingerForFilm() throws Exception {
        mockMvc.perform(get("/api/film/2/forestillinger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].film.titel").value("The Dark Knight"));
    }

    @Test
    void opretterReservation() throws Exception {
        mockMvc.perform(post("/api/reservationer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"navn": "Anna", "antalBilletter": 2, "forestilling": {"id": 1}}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.navn").value("Anna"))
                .andExpect(jsonPath("$.forestilling.film.titel").value("Interstellar"));

        mockMvc.perform(get("/api/reservationer"))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void afviserReservationUdenNavn() throws Exception {
        mockMvc.perform(post("/api/reservationer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"navn": " ", "antalBilletter": 2, "forestilling": {"id": 1}}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void afviserNulBilletter() throws Exception {
        mockMvc.perform(post("/api/reservationer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"navn": "Anna", "antalBilletter": 0, "forestilling": {"id": 1}}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void afviserUkendtForestilling() throws Exception {
        mockMvc.perform(post("/api/reservationer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"navn": "Anna", "antalBilletter": 2, "forestilling": {"id": 999}}
                                """))
                .andExpect(status().isNotFound());
    }
}
