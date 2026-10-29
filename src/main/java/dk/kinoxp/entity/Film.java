package dk.kinoxp.entity;
import jakarta.persistence.*;

@Entity
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titel;
    private int varighed;
    private int aldersGraense;
    private String plakatUrl;
    private String genre;

    @Column(length = 1000)
    private String beskrivelse;

    public Film() {}

    public Film(String titel, int varighed, int aldersGraense, String genre, String plakatUrl, String beskrivelse) {
        this.titel = titel;
        this.varighed = varighed;
        this.aldersGraense = aldersGraense;
        this.genre = genre;
        this.plakatUrl = plakatUrl;
        this.beskrivelse = beskrivelse;
    }

    public Long getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public int getVarighed() {
        return varighed;
    }

    public void setVarighed(int varighed) {
        this.varighed = varighed;
    }

    public int getAldersGraense() {
        return aldersGraense;
    }

    public void setAldersGraense(int aldersGraense) {
        this.aldersGraense = aldersGraense;
    }

    public String getPlakatUrl() {
        return plakatUrl;
    }

    public void setPlakatUrl(String plakatUrl) {
        this.plakatUrl = plakatUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }
}
