package dk.kinoxp.entity;

import jakarta.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String bookingKode;

    private String navn;

    private int antalBilletter;

    @ManyToOne
    private Forestilling forestilling;

    public Reservation() {}

    public Long getId() {
        return id;
    }

    public String getBookingKode() {
        return bookingKode;
    }

    public void setBookingKode(String bookingKode) {
        this.bookingKode = bookingKode;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public int getAntalBilletter() {
        return antalBilletter;
    }

    public void setAntalBilletter(int antalBilletter) {
        this.antalBilletter = antalBilletter;
    }

    public Forestilling getForestilling() {
        return forestilling;
    }

    public void setForestilling(Forestilling forestilling) {
        this.forestilling = forestilling;
    }
}
