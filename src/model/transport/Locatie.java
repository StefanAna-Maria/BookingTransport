package model.transport;

public class Locatie {
    private String nume;

    public Locatie(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locatie)) return false;
        Locatie locatie = (Locatie) o;
        return nume.equalsIgnoreCase(locatie.nume);
    }

    @Override
    public int hashCode() {
        return nume.toLowerCase().hashCode();
    }
}
