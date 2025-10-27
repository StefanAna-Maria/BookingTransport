package model.transport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CerereTransport {
    private int idCerere;
    private int idClient;
    private String destinatie;
    private double distantaKm;
    private LocalDateTime dataCreare;
    private String status;

    public CerereTransport() { }

    //constructor pt insert
    public CerereTransport(int idClient, String destinatie,
                           double distantaKm, String status) {
        this.idClient = idClient;
        this.destinatie = destinatie;
        this.distantaKm = distantaKm;
        this.status = status;
        this.dataCreare = LocalDateTime.now();
    }

    //constructor pt read
    public CerereTransport(int idCerere, int idClient, String destinatie,
                           double distantaKm, LocalDateTime dataCreare, String status) {
        this.idCerere = idCerere;
        this.idClient = idClient;
        this.destinatie = destinatie;
        this.distantaKm = distantaKm;
        this.dataCreare = dataCreare;
        this.status = status;
    }


    public int getIdCerere() { return idCerere; }
    public void setIdCerere(int idCerere) { this.idCerere = idCerere; }

    public int getIdClient() { return idClient; }
    public void setIdClient(int idClient) { this.idClient = idClient; }

    public String getDestinatie() { return destinatie; }
    public void setDestinatie(String destinatie) { this.destinatie = destinatie; }

    public double getDistantaKm() { return distantaKm; }
    public void setDistantaKm(double distantaKm) { this.distantaKm = distantaKm; }

    public LocalDateTime getDataCreare() { return dataCreare; }
    public void setDataCreare(LocalDateTime dataCreare) { this.dataCreare = dataCreare; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "CerereTransport#" + idCerere +
                " | Client ID: " + idClient +
                " | Destinatie: " + destinatie +
                " | Distanta: " + distantaKm +
                " km | Data: " + dataCreare.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) +
                " | Status: " + status;
    }
}
