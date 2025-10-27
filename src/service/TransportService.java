package service;

import dao.jdbc.*;
import model.transport.CerereTransport;
import model.transport.Cursa;
import model.transport.Feedback;
import model.transport.Vehicul;
import model.plati.Plata;
import dto.ScorUtilizatorDTO;

import model.plati.Raport;
import model.utilizatori.Client;
import model.utilizatori.Sofer;
import model.utilizatori.Utilizator;

import java.sql.SQLException;
import java.util.List;

public class TransportService {

    private UtilizatorDAO utilizatorDAO = new UtilizatorDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private SoferDAO soferDAO = new SoferDAO();
    private VehiculDAO vehiculDAO = new VehiculDAO();
    private CerereTransportDAO cerereDAO = new CerereTransportDAO();
    private CursaDAO cursaDAO = new CursaDAO();
    private FeedbackDAO feedbackDAO = new FeedbackDAO();
    private PlataDAO plataDAO = new PlataDAO();
    private RaportDAO raportDAO = new RaportDAO();

    // ---------------- Utilizator / Autentificare ----------------

    public Utilizator autentificare(String username, String parola) throws SQLException {
        Utilizator u = utilizatorDAO.findByUsername(username);
        if (u != null && u.getParola().equals(parola)) {
            return u;
        }
        return null;
    }


    public int inregistreazaUtilizator(Utilizator u) throws SQLException {
        if ("CLIENT".equalsIgnoreCase(u.getRol())) {
            Client c = new Client(
                    u.getNume(),
                    u.getEmail(),
                    u.getTelefon(),
                    u.getUsername(),
                    u.getParola()
            );
            return clientDAO.create(c);
        }
        else if ("SOFER".equalsIgnoreCase(u.getRol())) {
            Sofer s = new Sofer(
                    u.getNume(),
                    u.getEmail(),
                    u.getTelefon(),
                    u.getUsername(),
                    u.getParola()
            );
            return soferDAO.create(s);
        }
        else {
            return utilizatorDAO.create(u);
        }
    }

    public List<Client> getClienti() throws SQLException {
        return clientDAO.findAll();
    }

    public List<Sofer> getSoferi() throws SQLException {
        return soferDAO.findAll();
    }

    public CerereTransport findByIdCerere(int idCerere) throws SQLException {
        return cerereDAO.findById(idCerere);
    }

    public Cursa findByIdCursa(int idCursa) throws SQLException {
        return cursaDAO.findById(idCursa);
    }

    public Vehicul findByIdVehicul(int idVehicul) throws SQLException {
        return vehiculDAO.findById(idVehicul);
    }

    // ---------------- Client / Cerere Transport ----------------

    public int adaugaCerere(CerereTransport ct) throws SQLException {
        return cerereDAO.create(ct);
    }

    public List<CerereTransport> getCereri() throws SQLException {
        return cerereDAO.findAll();
    }

    public boolean updateCerere(CerereTransport ct) throws SQLException {
        return cerereDAO.update(ct);
    }

    public boolean stergeCerere(int idCerere) throws SQLException {
        return cerereDAO.delete(idCerere);
    }

    // ---------------- Sofer / Vehicul ----------------

    public int adaugaVehicul(Vehicul v) throws SQLException {
        return vehiculDAO.create(v);
    }

    public List<Vehicul> getVehicule() throws SQLException {
        return vehiculDAO.findAll();
    }

    public boolean updateVehicul(Vehicul v) throws SQLException {
        return vehiculDAO.update(v);
    }

    public boolean stergeVehicul(int idVehicul) throws SQLException {
        return vehiculDAO.delete(idVehicul);
    }

    // ---------------- Sofer / Cursa ----------------

    public int creazaCursa(Cursa c) throws SQLException {
        return cursaDAO.create(c);
    }

    public List<Cursa> getCurse() throws SQLException {
        return cursaDAO.findAll();
    }

    public boolean updateCursa(Cursa c) throws SQLException {
        return cursaDAO.update(c);
    }

    public boolean stergeCursa(int idCursa) throws SQLException {
        return cursaDAO.delete(idCursa);
    }

    // ---------------- Sofer ---------------------

    public Sofer findSoferById(int id) throws SQLException {
        return soferDAO.findById(id);
    }

    // ---------------- Utilizator ----------------

    public boolean stergeUtilizator(int idUtilizator) throws SQLException {
        return utilizatorDAO.delete(idUtilizator);
    }

    public boolean actualizeazaUtilizator(Utilizator u) throws SQLException {
        return utilizatorDAO.update(u);
    }

    public Utilizator findByIdUtilizator(int idUtilizator) throws SQLException {
        return utilizatorDAO.findById(idUtilizator);
    }

    //----------------- Client / Cursa ----------------

    public List<Cursa> getCurseClient(int idClient) throws SQLException {
        return cursaDAO.findByClientId(idClient);
    }

    // ---------------- Feedback ----------------

    public int adaugaFeedback(Feedback f) throws SQLException {
        return feedbackDAO.create(f);
    }

    public List<Feedback> getFeedbackuri() throws SQLException {
        return feedbackDAO.findAll();
    }

    public List<ScorUtilizatorDTO> getScorMediuPeUtilizator() throws SQLException {
        return feedbackDAO.getScorMediuPeUtilizator();
    }

    // ---------------- Plata ----------------

    public int adaugaPlata(Plata p) throws SQLException {
        return plataDAO.create(p);
    }

    public List<Plata> getPlati() throws SQLException {
        return plataDAO.findAll();
    }

    // ---------------- Raport ----------------

    public int adaugaRaport(Raport r) throws SQLException {
        return raportDAO.create(r);
    }

    public List<Raport> getRapoarte() throws SQLException {
        return raportDAO.findAll();
    }

    public Raport findByIdRaport(int idRaport) throws SQLException {
        return raportDAO.findById(idRaport);
    }

    public boolean updateRaport(Raport r) throws SQLException {
        return raportDAO.update(r);
    }

    public boolean stergeRaport(int idRaport) throws SQLException {
        return raportDAO.delete(idRaport);
    }

}
