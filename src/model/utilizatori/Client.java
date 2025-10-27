package model.utilizatori;

public class Client extends Utilizator {

    public Client() {
        super();
    }

    public Client(String nume, String email, String telefon,
                  String username, String parola) {
        super(nume, email, telefon, username, parola, "CLIENT");
    }

    public Client(int idUtilizator, String nume, String email, String telefon,
                  String username, String parola) {
        super(idUtilizator, nume, email, telefon, username, parola, "CLIENT");
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + getIdUtilizator() +
                ", nume='" + getNume() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefon='" + getTelefon() + '\'' +
                ", username='" + getUsername() + '\'' +
                '}';
    }
}