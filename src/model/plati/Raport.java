package model.plati;

import java.time.LocalDateTime;

public class Raport {
    private int idRaport;
    private int idSofer;
    private double venitTotal;
    private LocalDateTime dataGenerare;

    public Raport() { }

    public Raport(int idSofer, double venitTotal) {
        this.idSofer = idSofer;
        this.venitTotal = venitTotal;
        this.dataGenerare = LocalDateTime.now();
    }

    public Raport(int idRaport, int idSofer, double venitTotal, LocalDateTime dataGenerare) {
        this.idRaport = idRaport;
        this.idSofer = idSofer;
        this.venitTotal = venitTotal;
        this.dataGenerare = dataGenerare;
    }

    public int getIdRaport() { return idRaport; }
    public void setIdRaport(int idRaport) { this.idRaport = idRaport; }

    public int getIdSofer() { return idSofer; }
    public void setIdSofer(int idSofer) { this.idSofer = idSofer; }

    public double getVenitTotal() { return venitTotal; }
    public void setVenitTotal(double venitTotal) { this.venitTotal = venitTotal; }

    public LocalDateTime getDataGenerare() { return dataGenerare; }
    public void setDataGenerare(LocalDateTime dataGenerare) { this.dataGenerare = dataGenerare; }

    @Override
    public String toString() {
        return "Raport#" + idRaport +
                " | Sofer ID: " + idSofer +
                " | Venit Total: " + venitTotal +
                " | Data Generare: " + dataGenerare;
    }
}
