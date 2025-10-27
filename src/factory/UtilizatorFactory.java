package factory;

import model.utilizatori.Utilizator;

public interface UtilizatorFactory {
    Utilizator createUtilizator(String nume, String email, String telefon,
                                String username, String parola);
}
