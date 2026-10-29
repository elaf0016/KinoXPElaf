package dk.kinoxp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Forestilling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTid;

    @ManyToOne
    private Film film;

    @ManyToOne
    private Sal sal;

    // Beregnes ud fra salens kapacitet og reservationerne - gemmes ikke i databasen
    @Transient
    private Integer ledigePladser;

    public Forestilling() {
    }

    public Forestilling(LocalDateTime startTid, Film film, Sal sal) {
        this.startTid = startTid;
        this.film = film;
        this.sal = sal;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTid() {
        return startTid;
    }

    public void setStartTid(LocalDateTime startTid) {
        this.startTid = startTid;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Sal getSal() {
        return sal;
    }

    public void setSal(Sal sal) {
        this.sal = sal;
    }

    public Integer getLedigePladser() {
        return ledigePladser;
    }

    public void setLedigePladser(Integer ledigePladser) {
        this.ledigePladser = ledigePladser;
    }
}
