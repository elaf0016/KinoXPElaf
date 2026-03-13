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

    public Film() {}

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
}
