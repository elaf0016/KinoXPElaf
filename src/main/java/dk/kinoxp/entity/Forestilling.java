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

    public Forestilling() {
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
}