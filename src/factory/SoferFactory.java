package factory;

import model.utilizatori.Utilizator;

public class SoferFactory implements UtilizatorFactory {
    public Utilizator createUtilizator(String nume, String email, String telefon,
                                       String username, String parola) {
        return new Utilizator.Builder()
                .nume(nume)
                .email(email)
                .telefon(telefon)
                .username(username)
                .parola(parola)
                .rol("SOFER")
                .build();
    }
}
