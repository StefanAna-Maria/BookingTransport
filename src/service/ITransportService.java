package service;

import model.plati.Raport;
import model.plati.Tarif;
import model.transport.*;
import model.utilizatori.*;

import service.exceptions.CerereNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ITransportService {

    void adaugaClient(Client c);
    void adaugaSofer(Sofer s);
    void adaugaVehicul(Vehicul v);
    Vehicul getVehiculDupaNrInmatriculare(String nr);
    void stergeVehicul(String nrInmatriculare);
    void modificaVehicul(String nrInmatriculare, Vehicul vehiculModificat);
    Map<String, Vehicul> getVehicule();

    void acceptaCerereSiCreeazaCursa(int idCerere, Sofer sofer) throws CerereNotFoundException;
    void finalizeazaCursa(int idCursa);

    void adaugaCerere(CerereTransport cerere);
    void adaugaCursa(Cursa cursa);
    List<CerereTransport> getCereri();
    List<Cursa> getCurse();

    void finalizeazaCerere(int idCerere) throws CerereNotFoundException;
    void anuleazaCerere(int idCerere) throws CerereNotFoundException;

    List<Client> getClienti();
    Set<Sofer> getSoferi();
    List<Cursa> getCurseClient(int idClient);

    void adaugaFeedbackClient(int idCursa, int scor, String comentariu);
    void adaugaFeedbackSofer(int idCursa, int scor, String comentariu);

    Raport genereazaRaportVenituri(Sofer sofer, Tarif tarif);
}
