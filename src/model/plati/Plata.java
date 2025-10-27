package model.plati;

import java.time.LocalDateTime;

public class Plata {
    private int idPlata;
    private int idCursa;
    private double suma;
    private String metodaPlata;
    private LocalDateTime dataPlata;

    public Plata() { }

    //constructor pt insert
    public Plata(int idCursa, double suma, String metodaPlata) {
        this.idCursa = idCursa;
        this.suma = suma;
        this.metodaPlata = metodaPlata;
        this.dataPlata = LocalDateTime.now();
    }

    //constructor pt read
    public Plata(int idPlata, int idCursa, double suma, String metodaPlata, LocalDateTime dataPlata) {
        this.idPlata = idPlata;
        this.idCursa = idCursa;
        this.suma = suma;
        this.metodaPlata = metodaPlata;
        this.dataPlata = dataPlata;
    }


    public int getIdPlata() { return idPlata; }
    public void setIdPlata(int idPlata) { this.idPlata = idPlata; }

    public int getIdCursa() { return idCursa; }
    public void setIdCursa(int idCursa) { this.idCursa = idCursa; }

    public double getSuma() { return suma; }
    public void setSuma(double suma) { this.suma = suma; }

    public String getMetodaPlata() { return metodaPlata; }
    public void setMetodaPlata(String metodaPlata) { this.metodaPlata = metodaPlata; }

    public LocalDateTime getDataPlata() { return dataPlata; }
    public void setDataPlata(LocalDateTime dataPlata) { this.dataPlata = dataPlata; }

    @Override
    public String toString() {
        return "Plata#" + idPlata +
                " | Cursa ID: " + idCursa +
                " | Suma: " + suma +
                " | Metoda: " + metodaPlata +
                " | Data: " + dataPlata;
    }
}
