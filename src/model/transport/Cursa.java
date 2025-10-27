package model.transport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cursa {
    private int idCursa;
    private int idCerere;
    private int idSofer;
    private int idVehicul;
    private double distantaKm;
    private double durataMinute;
    private double tarif;
    private LocalDateTime dataStart;
    private LocalDateTime dataFinalizare;
    private boolean finalizata;

    public Cursa() { }

    //constructor insert
    public Cursa(int idCerere, int idSofer, int idVehicul,
                 double distantaKm, double durataMinute, double tarif) {
        this.idCerere = idCerere;
        this.idSofer = idSofer;
        this.idVehicul = idVehicul;
        this.distantaKm = distantaKm;
        this.durataMinute = durataMinute;
        this.tarif = tarif;
        this.dataStart = LocalDateTime.now();
        this.finalizata = false;
    }

    //constructor read
    public Cursa(int idCursa, int idCerere, int idSofer, int idVehicul,
                 double distantaKm, double durataMinute, double tarif,
                 LocalDateTime dataStart, LocalDateTime dataFinalizare, boolean finalizata) {
        this.idCursa = idCursa;
        this.idCerere = idCerere;
        this.idSofer = idSofer;
        this.idVehicul = idVehicul;
        this.distantaKm = distantaKm;
        this.durataMinute = durataMinute;
        this.tarif = tarif;
        this.dataStart = dataStart;
        this.dataFinalizare = dataFinalizare;
        this.finalizata = finalizata;
    }

    public int getIdCursa() { return idCursa; }
    public void setIdCursa(int idCursa) { this.idCursa = idCursa; }

    public int getIdCerere() { return idCerere; }
    public void setIdCerere(int idCerere) { this.idCerere = idCerere; }

    public int getIdSofer() { return idSofer; }
    public void setIdSofer(int idSofer) { this.idSofer = idSofer; }

    public int getIdVehicul() { return idVehicul; }
    public void setIdVehicul(int idVehicul) { this.idVehicul = idVehicul; }

    public double getDistantaKm() { return distantaKm; }
    public void setDistantaKm(double distantaKm) { this.distantaKm = distantaKm; }

    public double getDurataMinute() { return durataMinute; }
    public void setDurataMinute(double durataMinute) { this.durataMinute = durataMinute; }

    public double getTarif() { return tarif; }
    public void setTarif(double tarif) { this.tarif = tarif; }

    public LocalDateTime getDataStart() { return dataStart; }
    public void setDataStart(LocalDateTime dataStart) { this.dataStart = dataStart; }

    public LocalDateTime getDataFinalizare() { return dataFinalizare; }
    public void setDataFinalizare(LocalDateTime dataFinalizare) { this.dataFinalizare = dataFinalizare; }

    public boolean isFinalizata() { return finalizata; }
    public void setFinalizata(boolean finalizata) { this.finalizata = finalizata; }

    @Override
    public String toString() {
        return "Cursa#" + idCursa +
                " | Cerere ID: " + idCerere +
                " | Sofer ID: " + idSofer +
                " | Vehicul ID: " + idVehicul +
                " | Distanta: " + distantaKm + " km" +
                " | Durata: " + durataMinute + " min" +
                " | Tarif: " + tarif + " RON" +
                " | Start: " + dataStart.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                " | Finalizata: " + (finalizata ? "DA" : "NU");
    }
}
