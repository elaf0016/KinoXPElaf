package dk.kinoxp.entity;
import jakarta.persistence.*;

@Entity
public class Sal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String navn;
    private int kapacitet;

    public Sal() {
    }

    public Sal(String navn, int kapacitet) {
        this.navn = navn;
        this.kapacitet = kapacitet;
    }

    public Long getId() {
        return id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }
}
