package dk.kinoxp.entity;
import jakarta.persistence.*;

@Entity
public class Sal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String navn;

    public Sal() {
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
}
