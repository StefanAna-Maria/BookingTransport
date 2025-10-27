package model.utilizatori;

public class Admin extends Utilizator {

    public Admin(int id, String nume, String email, String telefon, String username, String parola) {
        super(id, nume, email, telefon, username, parola, "ADMIN");
    }

    @Override
    public String getRol() {
        return "ADMIN";
    }

    @Override
    public String toString() {
        return "Admin > " + super.toString();
    }
}
