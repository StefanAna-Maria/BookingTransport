package model.utilizatori;

public class Sofer extends Utilizator {
    public Sofer() {
        super();
    }

    public Sofer(String nume, String email, String telefon,
                 String username, String parola) {
        super(nume, email, telefon, username, parola, "SOFER");
    }

    public Sofer(int idUtilizator, String nume, String email, String telefon,
                 String username, String parola) {
        super(idUtilizator, nume, email, telefon, username, parola, "SOFER");
    }

    @Override
    public String toString() {
        return "Sofer{" +
                "id=" + getIdUtilizator() +
                ", nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefon='" + getTelefon() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}