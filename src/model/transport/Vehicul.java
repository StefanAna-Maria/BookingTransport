package model.transport;

public class Vehicul {
    private int idVehicul;
    private String marca;
    private String model;
    private String nrInmatriculare;
    private int capacitate;
    private double vitezaMedie;
    private int idSofer;

    public Vehicul() { }

    public Vehicul(String marca, String model, String nrInmatriculare,
                   int capacitate, double vitezaMedie, int idSofer) {
        this.marca = marca;
        this.model = model;
        this.nrInmatriculare = nrInmatriculare;
        this.capacitate = capacitate;
        this.vitezaMedie = vitezaMedie;
        this.idSofer = idSofer;
    }

    public Vehicul(int idVehicul, String marca, String model, String nrInmatriculare,
                   int capacitate, double vitezaMedie, int idSofer) {
        this.idVehicul = idVehicul;
        this.marca = marca;
        this.model = model;
        this.nrInmatriculare = nrInmatriculare;
        this.capacitate = capacitate;
        this.vitezaMedie = vitezaMedie;
        this.idSofer = idSofer;
    }

    public int getIdVehicul() { return idVehicul; }
    public void setIdVehicul(int idVehicul) { this.idVehicul = idVehicul; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getNrInmatriculare() { return nrInmatriculare; }
    public void setNrInmatriculare(String nrInmatriculare) { this.nrInmatriculare = nrInmatriculare; }

    public int getCapacitate() { return capacitate; }
    public void setCapacitate(int capacitate) { this.capacitate = capacitate; }

    public double getVitezaMedie() { return vitezaMedie; }
    public void setVitezaMedie(double vitezaMedie) { this.vitezaMedie = vitezaMedie; }

    public int getIdSofer() { return idSofer; }
    public void setIdSofer(int idSofer) { this.idSofer = idSofer; }

    @Override
    public String toString() {
        return "Vehicul{" +
                "idVehicul=" + idVehicul +
                ", marca='" + marca + '\'' +
                ", model='" + model + '\'' +
                ", nrInmatriculare='" + nrInmatriculare + '\'' +
                ", capacitate=" + capacitate +
                ", vitezaMedie=" + vitezaMedie +
                ", idSofer=" + idSofer +
                '}';
    }
}