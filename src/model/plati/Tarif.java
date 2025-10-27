package model.plati;

import java.util.*;

public class Tarif {
    private int id;
    private double perKm;
    private double perMinut;
    private Date valabilDeLa;

    public Tarif(int id, double perKm, double perMinut, Date valabilDeLa) {
        this.id = id;
        this.perKm = perKm;
        this.perMinut = perMinut;
        this.valabilDeLa = valabilDeLa;
    }

    public int getId() { return id; }
    public double getPerKm() { return perKm; }
    public double getPerMinut() { return perMinut; }
    public Date getValabilDeLa() { return valabilDeLa; }
}