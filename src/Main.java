import model.transport.*;
import model.utilizatori.*;
import model.plati.*;
import service.*;
import factory.*;
import dto.ScorUtilizatorDTO;
import dao.jdbc.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Comparator;


public class Main {
    private static TransportService service = new TransportService();
    private static Scanner scanner = new Scanner(System.in);
    private static final AuditService audit = new AuditService();

//    //TEST BUILDER:
//    Utilizator u = new Utilizator.Builder()
//            .idUtilizator(10)
//            .nume("Ana Popescu")
//            .email("ana@popescu.ro")
//            .username("anapopescu")
//            .parola("pa55")
//            .rol("CLIENT")
//            .build();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Aplicatie Booking Transport ===");
            System.out.println("1. Inregistrare Utilizator");
            System.out.println("2. Autentificare Utilizator");
            System.out.println("0. Ieşire");
            System.out.print("Alege optiunea: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    inregistreazaUtilizator();
                    break;
                case "2":
                    autentificare();
                    break;
                case "0":
                    running = false;
                    System.out.println("La revedere!");
                    break;
                default:
                    System.out.println("Optiune invalida. incearca din nou.");
            }
        }
        dao.jdbc.DBManager.closeConnection();
    }

    //Metode pentru menu principal

    private static void inregistreazaUtilizator() {
        try {
            System.out.println("\n--- inregistrare Utilizator ---");
            System.out.print("Nume: ");
            String nume = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefon: ");
            String telefon = scanner.nextLine();
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Parola: ");
            String parola = scanner.nextLine();
            System.out.print("Rol (CLIENT / SOFER / ADMIN): ");
            String rol = scanner.nextLine().toUpperCase();

            // Factory -> FactorySelector pentru ROL
            UtilizatorFactory factory = FactorySelector.getFactory(rol);
            Utilizator u = factory.createUtilizator(nume, email, telefon, username, parola);

            int id = service.inregistreazaUtilizator(u);
            System.out.println("Utilizator inregistrat cu ID = " + id);
        } catch (IllegalArgumentException e) {
            System.err.println("Rol invalid: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Eroare la inregistrare: " + e.getMessage());
        }
    }


    private static void autentificare() {
        try {
            System.out.println("\n--- Autentificare ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Parola: ");
            String parola = scanner.nextLine();

            Utilizator u = service.autentificare(username, parola);
            if (u == null) {
                System.out.println("Datele de autentificare sunt incorecte.");
                return;
            }
            System.out.println("Autentificat cu succes ca " + u.getRol() + " [" + u.getNume() + "]");
            meniuPeRol(u);
        } catch (SQLException e) {
            System.err.println("Eroare la autentificare: " + e.getMessage());
        }
    }

    private static void meniuPeRol(Utilizator u) {
        switch (u.getRol()) {
            case "CLIENT":
                meniuClient(u);
                break;
            case "SOFER":
                meniuSofer(u);
                break;
            case "ADMIN":
                meniuAdmin(u);
                break;
            default:
                System.out.println("Rol necunoscut. Iesire.");
        }
    }

    //                  Meniu CLIENT

    private static void meniuClient(Utilizator u) {
        int idClient = u.getIdUtilizator();
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Meniu Client ---");
            System.out.println("1. Plaseaza cerere de transport");
            System.out.println("2. Trimite feedback catre sofer");
            System.out.println("3. Istoric curse");
            System.out.println("4. Anuleaza cerere");
            System.out.println("5. Modifica cerere");
            System.out.println("0. Back");
            System.out.print("Optiune: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    adaugaCerere(idClient);
                    break;
                case "2":
                    feedbackSofer(idClient);
                    break;
                case "3":
                    istoricCurseClient(idClient);
                    break;
                case "4":
                    anuleazaCerere(idClient);
                    break;
                case "5":
                    modificaCerere(idClient);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void adaugaCerere(int idClient) {
        try {
            System.out.println("\n--- Adauga Cerere ---");
            System.out.print("Destinatie: ");
            String destinatie = scanner.nextLine();
            System.out.print("Distanta (km): ");
            double dist = Double.parseDouble(scanner.nextLine());

            CerereTransport ct = new CerereTransport(idClient, destinatie, dist, "IN_ASTEPTARE");
            int idCerere = service.adaugaCerere(ct);
            System.out.println("Cerere creata cu ID = " + idCerere);
        } catch (SQLException e) {
            System.err.println("Eroare la adaugare cerere: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Valoare de distanta invalida.");
        }
        audit.logAction("'Adaugare cerere transport'");
    }

    private static void feedbackSofer(int idClient) {
        try {
            System.out.println("\n--- Feedback sofer ---");
            System.out.print("ID Cursa: ");
            int idCursa = Integer.parseInt(scanner.nextLine());

            Cursa cursa = service.findByIdCursa(idCursa);
            if (cursa == null) {
                System.out.println("Cursa nu a fost gasita.");
                return;
            }
            if (!cursa.isFinalizata()) {
                System.out.println("Nu puteti trimite feedback decat pentru curse finalizate.");
                return;
            }

            List<Feedback> allFb = service.getFeedbackuri();
            boolean deja = allFb.stream().anyMatch(f ->
                    f.getIdCursa() == idCursa &&
                            f.getIdSender() == idClient &&
                            f.getTip().equals("CLIENT_CATRE_SOFER")
            );
            if (deja) {
                System.out.println("Feedback-ul dumneavoastra a fost deja inregistrat.");
                return;
            }

            System.out.print("Scor (1–5): ");
            int scor = Integer.parseInt(scanner.nextLine());
            System.out.print("Comentariu: ");
            String comentariu = scanner.nextLine();

            Feedback f = new Feedback(idCursa, idClient, cursa.getIdSofer(), scor, comentariu, "CLIENT_CATRE_SOFER");
            service.adaugaFeedback(f);
            System.out.println("Feedback trimis cu succes.");
        } catch (SQLException e) {
            System.err.println("Eroare la inserare feedback: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID sau scor invalid.");
        }
        audit.logAction("'Transmitere feedback catre sofer'");
    }

    private static void istoricCurseClient(int idClient) {
        audit.logAction("'Vizualizare istoric curse client'");
        try {
            System.out.println("\n--- Istoric Curse Client ---");

            List<Cursa> curseClient = service.getCurse().stream()
                    .filter(c -> {
                        try {
                            CerereTransport cerere = service.findByIdCerere(c.getIdCerere());
                            return cerere != null && cerere.getIdClient() == idClient;
                        } catch (SQLException e) {
                            return false;
                        }
                    })
                    .toList();

            if (curseClient.isEmpty()) {
                System.out.println("Nu aveti nicio cursa.");
                return;
            }

            for (Cursa cursa : curseClient) {
                System.out.println(cursa);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea curselor: " + e.getMessage());
        }
    }


    private static void anuleazaCerere(int idClient) {
        try {
            System.out.println("\n--- Anuleaza Cerere ---");
            System.out.print("ID Cerere de anulat: ");
            int idCer = Integer.parseInt(scanner.nextLine());

            CerereTransport ct = service.findByIdCerere(idCer);
            if (ct == null || ct.getIdClient() != idClient) {
                System.out.println("Cererea nu exista sau nu va apartine.");
                return;
            }
            if (!"IN_ASTEPTARE".equals(ct.getStatus())) {
                System.out.println("Puteti anula doar cererile in asteptare.");
                return;
            }
            ct.setStatus("ANULATA");
            service.updateCerere(ct);
            System.out.println("Cerere anulata.");
        } catch (SQLException e) {
            System.err.println("Eroare la anulare cerere: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID Cerere invalid.");
        }
        audit.logAction("'Anulare cerere transport'");
    }

    private static void modificaCerere(int idClient) {
        try {
            System.out.println("\n--- Modifica Cerere ---");
            System.out.print("ID Cerere de modificat: ");
            int idCer = Integer.parseInt(scanner.nextLine());

            CerereTransport ct = service.findByIdCerere(idCer);
            if (ct == null || ct.getIdClient() != idClient) {
                System.out.println("Cererea nu exista sau nu va apartine.");
                return;
            }
            if (!"IN_ASTEPTARE".equals(ct.getStatus())) {
                System.out.println("Puteti modifica doar cererile in asteptare.");
                return;
            }
            System.out.print("Noua destinatie: ");
            String nouaDest = scanner.nextLine();
            System.out.print("Noua distanta (km): ");
            double nouaDist = Double.parseDouble(scanner.nextLine());

            ct.setDestinatie(nouaDest);
            ct.setDistantaKm(nouaDist);
            service.updateCerere(ct);
            System.out.println("Cerere modificata.");
        } catch (SQLException e) {
            System.err.println("Eroare la modificare cerere: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Valori invalide.");
        }
        audit.logAction("'Modificare cerere transport'");
    }


    //                  Meniu SOFER

    private static void meniuSofer(Utilizator u) {
        int idSofer = u.getIdUtilizator();
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Meniu sofer ---");
            System.out.println("1. Inregistreaza vehicul");
            System.out.println("2. Accepta cerere");
            System.out.println("3. Finalizeaza cursa");
            System.out.println("4. Trimite feedback catre client");
            System.out.println("5. Afiseaza venit propriu");
            System.out.println("6. Sterge vehicul");
            System.out.println("7. Actualizeaza vehicul");
            System.out.println("0. Back");
            System.out.print("Optiune: ");
            String opt = scanner.nextLine();

            switch (opt) {
                case "1":
                    adaugaVehiculSofer(idSofer);
                    break;
                case "2":
                    acceptaCerereSofer(idSofer);
                    break;
                case "3":
                    finalizeazaCursaSofer(idSofer);
                    break;
                case "4":
                    feedbackClientSofer(idSofer);
                    break;
                case "5":
                    afiseazaVenitSofer(idSofer);
                    break;
                case "6":
                    stergeVehiculSofer(idSofer);
                    break;
                case "7":
                    modificaVehiculSofer(idSofer);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void adaugaVehiculSofer(int idSofer) {
        try {
            System.out.println("\n--- Adauga Vehicul ---");
            System.out.print("Marca: ");
            String marca = scanner.nextLine();
            System.out.print("Model: ");
            String model = scanner.nextLine();
            System.out.print("Numar inmatriculare: ");
            String nr = scanner.nextLine();
            System.out.print("Capacitate: ");
            int cap = Integer.parseInt(scanner.nextLine());
            System.out.print("Viteza medie (km/h): ");
            double vm = Double.parseDouble(scanner.nextLine());

            Vehicul v = new Vehicul(marca, model, nr, cap, vm, idSofer);
            int idVeh = service.adaugaVehicul(v);
            System.out.println("Vehicul creat cu ID = " + idVeh);
        } catch (SQLException e) {
            System.err.println("Eroare la adaugare vehicul: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Valori invalide pentru capacitate sau viteza.");
        }
        audit.logAction("'Inregistrare vehicul'");
    }

    private static void acceptaCerereSofer(int idSofer) {
        try {
            System.out.println("\n--- Accepta Cerere ---");
            System.out.print("ID Cerere de acceptat: ");
            int idCer = Integer.parseInt(scanner.nextLine());

            CerereTransport ct = service.findByIdCerere(idCer);
            if (ct == null || !"IN_ASTEPTARE".equals(ct.getStatus())) {
                System.out.println("Cererea nu exista sau nu mai poate fi acceptata.");
                return;
            }

            List<Vehicul> vehicule = service.getVehicule().stream()
                    .filter(v -> v.getIdSofer() == idSofer)
                    .toList();
            if (vehicule.isEmpty()) {
                System.out.println("Nu exista vehicul asociat. Cererea nu poate fi acceptata.");
                return;
            }
            Vehicul veh = vehicule.get(0);

            ct.setStatus("ACCEPTATA");
            service.updateCerere(ct);

            double dist = ct.getDistantaKm();
            double viteza = veh.getVitezaMedie();
            double durata = (dist / viteza) * 60;

            // preturi prestabilite
            double pretPeKm = 5.0;
            double pretPeMinut = 1.0;

            double tarif = pretPeKm * dist + pretPeMinut * durata;

            Cursa cursa = new Cursa(idCer, idSofer, veh.getIdVehicul(), dist, durata, tarif);
            int idCursa = service.creazaCursa(cursa);
            System.out.println("Cerere acceptata. Cursa creata cu ID = " + idCursa);
        } catch (SQLException e) {
            System.err.println("Eroare la acceptarea cererii: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID Cerere invalid.");
        }
        audit.logAction("'Acceptare cerere'");
    }


    private static void finalizeazaCursaSofer(int idSofer) {
        try {
            System.out.println("\n--- Finalizeaza Cursa ---");
            System.out.print("ID Cursa de finalizat: ");
            int idCu = Integer.parseInt(scanner.nextLine());

            Cursa cursa = service.findByIdCursa(idCu);
            if (cursa == null || cursa.getIdSofer() != idSofer) {
                System.out.println("Cursa nu exista sau nu va apartine.");
                return;
            }
            if (cursa.isFinalizata()) {
                System.out.println("Cursa este deja finalizata.");
                return;
            }
            cursa.setFinalizata(true);
            cursa.setDataFinalizare(LocalDateTime.now());
            service.updateCursa(cursa);
            System.out.println("Cursa finalizata.");
        } catch (SQLException e) {
            System.err.println("Eroare la finalizarea cursei: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID Cursa invalid.");
        }
        audit.logAction("'Finalizare cursa'");
    }

    private static void feedbackClientSofer(int idSofer) {
        try {
            System.out.println("\n--- Feedback Client ---");
            System.out.print("ID Client (receiver): ");
            int idClient = Integer.parseInt(scanner.nextLine());
            System.out.print("ID Cursa: ");
            int idCursa = Integer.parseInt(scanner.nextLine());

            Cursa cursa = service.findByIdCursa(idCursa);
            if (cursa == null || cursa.getIdSofer() != idSofer) {
                System.out.println("Cursa nu exista sau nu va apartine.");
                return;
            }
            if (!cursa.isFinalizata()) {
                System.out.println("Puteti da feedback doar pentru curse finalizate.");
                return;
            }

            List<Feedback> allFb = service.getFeedbackuri();
            boolean deja = allFb.stream().anyMatch(f ->
                    f.getIdCursa() == idCursa &&
                            f.getIdSender() == idSofer &&
                            f.getTip().equals("SOFER_CATRE_CLIENT")
            );
            if (deja) {
                System.out.println("Feedback-ul dumneavoastra a fost deja inregistrat.");
                return;
            }

            System.out.print("Scor (1–5): ");
            int scor = Integer.parseInt(scanner.nextLine());
            System.out.print("Comentariu: ");
            String comentariu = scanner.nextLine();

            Feedback f = new Feedback(idCursa, idSofer, idClient, scor, comentariu, "SOFER_CATRE_CLIENT");
            service.adaugaFeedback(f);
            System.out.println("Feedback trimis cu succes.");
        } catch (SQLException e) {
            System.err.println("Eroare la inserare feedback: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Valori invalide.");
        }
        audit.logAction("'Transmitere feedback catre client'");
    }

    private static void afiseazaVenitSofer(int idSofer) {
        audit.logAction("'Afisare venit propriu'");
        try {
            System.out.println("\n--- Afiseaza Venit sofer ---");
            double totalVenit = service.getCurse().stream()
                    .filter(c -> c.getIdSofer() == idSofer && c.isFinalizata())
                    .mapToDouble(Cursa::getTarif)
                    .sum();
            System.out.println("Venit total (curse finalizate): " + totalVenit + " RON");
        } catch (SQLException e) {
            System.err.println("Eroare la calcul venit: " + e.getMessage());
        }
    }

    private static void stergeVehiculSofer(int idSofer) {
        try {
            System.out.println("\n--- sterge Vehicul ---");
            System.out.print("ID Vehicul de sters: ");
            int idVeh = Integer.parseInt(scanner.nextLine());

            Vehicul veh = service.findByIdVehicul(idVeh);
            if (veh == null || veh.getIdSofer() != idSofer) {
                System.out.println("Vehiculul nu exista sau nu va apartine.");
                return;
            }
            service.stergeVehicul(idVeh);
            System.out.println("Vehicul sters.");
        } catch (SQLException e) {
            System.err.println("Eroare la stergere vehicul: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID Vehicul invalid.");
        }
        audit.logAction("'Stergere vehicul'");
    }

    private static void modificaVehiculSofer(int idSofer) {
        try {
            System.out.println("\n--- Modifica Vehicul ---");
            System.out.print("ID Vehicul de modificat: ");
            int idVeh = Integer.parseInt(scanner.nextLine());

            Vehicul veh = service.findByIdVehicul(idVeh);
            if (veh == null || veh.getIdSofer() != idSofer) {
                System.out.println("Vehiculul nu exista sau nu va apartine.");
                return;
            }
            System.out.print("Noua marca: ");
            String nouaMarca = scanner.nextLine();
            System.out.print("Noul model: ");
            String noulModel = scanner.nextLine();
            System.out.print("Nou nr. inmatriculare: ");
            String nouNr = scanner.nextLine();
            System.out.print("Noua capacitate: ");
            int nouaCap = Integer.parseInt(scanner.nextLine());
            System.out.print("Noua viteza medie (km/h): ");
            double nouaViteza = Double.parseDouble(scanner.nextLine());

            veh.setMarca(nouaMarca);
            veh.setModel(noulModel);
            veh.setNrInmatriculare(nouNr);
            veh.setCapacitate(nouaCap);
            veh.setVitezaMedie(nouaViteza);

            service.updateVehicul(veh);
            System.out.println("Vehicul modificat.");
        } catch (SQLException e) {
            System.err.println("Eroare la modificare vehicul: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("Valori invalide.");
        }
        audit.logAction("'Modificare vehicul'");
    }

    //                  Meniu ADMIN

    private static void meniuAdmin(Utilizator u) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Meniu Admin ---");
            System.out.println("1. Afiseaza clienti");
            System.out.println("2. Afiseaza soferi");
            System.out.println("3. Sterge utilizator");
            System.out.println("4. Actualizeaza utilizator");
            System.out.println("5. Afiseaza curse");
            System.out.println("6. Sterge curse");
            System.out.println("7. Actualizeaza curse");
            System.out.println("8. Afiseaza vehicule");
            System.out.println("9. Curse active/sofer");
            System.out.println("10. Curse cu tarife");
            System.out.println("11. Genereaza raport venit/sofer");
            System.out.println("12. Afiseaza toate rapoartele");
            System.out.println("13. Sterge raport");
            System.out.println("14. Actualizeaza raport");
            System.out.println("15. Istoric curse client");
            System.out.println("16. Status cereri");
            System.out.println("17. Vizualizeaza feedback-urile clientilor");
            System.out.println("18. Vizualizeaza feedback-urile soferilor");
            System.out.println("19. Vizualizeaza scorul utilizatorilor");
            System.out.println("0. Back");
            System.out.print("Optiune: ");

            String opt = scanner.nextLine();
            switch (opt) {
                case "1":
                    afiseazaClienti();
                    break;
                case "2":
                    afiseazaSoferi();
                    break;
                case "3":
                    stergeUtilizator();
                    break;
                case "4":
                    actualizeazaUtilizator();
                    break;
                case "5":
                    afiseazaCurse();
                    break;
                case "6":
                    stergeCursa();
                    break;
                case "7":
                    actualizeazaCursa();
                    break;
                case "8":
                    afiseazaVehicule();
                    break;
                case "9":
                    curseActiveSofer();
                    break;
                case "10":
                    curseCuTarife();
                    break;
                case "11":
                    genereazaRaport();
                    break;
                case "12":
                    afiseazaRapoarte();
                    break;
                case "13":
                    stergeRaport();
                    break;
                case "14":
                    actualizeazaRaport();
                    break;
                case "15":
                    istoricCurseClientAdmin();
                    break;
                case "16":
                    statusCereri();
                    break;
                case "17":
                    try {
                        afiseazaFeedbackuri("SOFER_CATRE_CLIENT");
                    } catch (SQLException e) {
                        System.err.println("Eroare la afisarea feedback-urilor: " + e.getMessage());
                    }
                    break;
                case "18":
                    try {
                        afiseazaFeedbackuri("CLIENT_CATRE_SOFER");
                    } catch (SQLException e) {
                        System.err.println("Eroare la afisarea feedback-urilor: " + e.getMessage());
                    }
                    break;
                case "19":
                    afiseazaScorMediu();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Optiune invalida.");
            }
        }
    }

    private static void afiseazaClienti() {
        audit.logAction("'Afisare Clienti'");
        try {
            System.out.println("\n--- Lista Clienti ---");
            List<Client> clienti = service.getClienti();
            if (clienti.isEmpty()) {
                System.out.println("Nu exista clienti in sistem.");
            } else {
                clienti.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea clientilor: " + e.getMessage());
        }
    }

    private static void afiseazaSoferi() {
        audit.logAction("'Afisare Soferi'");
        try {
            System.out.println("\n--- Lista soferi ---");
            List<Sofer> soferi = service.getSoferi();
            if (soferi.isEmpty()) {
                System.out.println("Nu exista soferi in sistem.");
            } else {
                soferi.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea soferilor: " + e.getMessage());
        }
    }

    private static void stergeUtilizator() {
        try {
            System.out.println("\n--- sterge Utilizator ---");
            System.out.print("ID utilizator de sters: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean success = service.stergeUtilizator(id);
            if (success) {
                System.out.println("Utilizatorul a fost sters cu succes.");
            } else {
                System.out.println("Utilizatorul nu a fost gasit sau nu s-a putut sterge.");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea utilizatorului: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID invalid.");
        }
        audit.logAction("'Stergere Utilizator'");
    }

    private static void actualizeazaUtilizator() {
        try {
            System.out.println("\n--- Actualizare Utilizator ---");
            System.out.print("ID utilizator: ");
            int id = Integer.parseInt(scanner.nextLine());

            Utilizator u = service.findByIdUtilizator(id);
            if (u == null) {
                System.out.println("Utilizator inexistent.");
                return;
            }

            System.out.print("Nume nou (" + u.getNume() + "): ");
            String nume = scanner.nextLine();
            if (!nume.isEmpty()) u.setNume(nume);

            System.out.print("Email nou (" + u.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.isEmpty()) u.setEmail(email);

            System.out.print("Telefon nou (" + u.getTelefon() + "): ");
            String telefon = scanner.nextLine();
            if (!telefon.isEmpty()) u.setTelefon(telefon);

            System.out.print("Username nou (" + u.getUsername() + "): ");
            String username = scanner.nextLine();
            if (!username.isEmpty()) u.setUsername(username);

            System.out.print("Parola noua (se va inlocui): ");
            String parola = scanner.nextLine();
            if (!parola.isEmpty()) u.setParola(parola);

            System.out.print("Rol nou (CLIENT / SOFER / ADMIN) [" + u.getRol() + "]: ");
            String rol = scanner.nextLine();
            if (!rol.isEmpty()) u.setRol(rol.toUpperCase());

            boolean ok = service.actualizeazaUtilizator(u);
            System.out.println(ok ? "Utilizator actualizat cu succes." : "Actualizarea a esuat.");
        } catch (SQLException e) {
            System.err.println("Eroare la actualizare: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("ID invalid.");
        }
        audit.logAction("'Actualizare Utilizator'");
    }

    private static void afiseazaCurse() {
        audit.logAction("'Afisare Curse'");
        try {
            System.out.println("\n--- Lista Curse ---");
            List<Cursa> curse = service.getCurse();
            if (curse.isEmpty()) {
                System.out.println("Nu exista curse in sistem.");
            } else {
                curse.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea curselor: " + e.getMessage());
        }
    }

    private static void stergeCursa() {
        try {
            System.out.print("\nID Cursa de sters: ");
            int id = Integer.parseInt(scanner.nextLine());
            Cursa c = service.findByIdCursa(id);
            if (c == null) {
                System.out.println("Cursa nu exista.");
                return;
            }
            boolean deleted = service.stergeCursa(id);
            System.out.println(deleted ? "Cursa a fost stearsa." : "Eroare la stergere.");
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        }
        audit.logAction("'Stergere Cursa'");
    }

    private static void actualizeazaCursa() {
        try {
            System.out.print("\nID Cursa de modificat: ");
            int id = Integer.parseInt(scanner.nextLine());
            Cursa c = service.findByIdCursa(id);
            if (c == null) {
                System.out.println("Cursa nu exista.");
                return;
            }

            System.out.println("Introduceti noile valori (sau ENTER pentru a pastra cele vechi):");

            System.out.print("ID sofer (" + c.getIdSofer() + "): ");
            String sofer = scanner.nextLine();
            if (!sofer.isBlank()) c.setIdSofer(Integer.parseInt(sofer));

            System.out.print("ID Vehicul (" + c.getIdVehicul() + "): ");
            String vehicul = scanner.nextLine();
            if (!vehicul.isBlank()) c.setIdVehicul(Integer.parseInt(vehicul));

            System.out.print("Distanta (km) (" + c.getDistantaKm() + "): ");
            String dist = scanner.nextLine();
            if (!dist.isBlank()) c.setDistantaKm(Double.parseDouble(dist));

            System.out.print("Durata (minute) (" + c.getDurataMinute() + "): ");
            String dur = scanner.nextLine();
            if (!dur.isBlank()) c.setDurataMinute(Double.parseDouble(dur));

            System.out.print("Tarif (" + c.getTarif() + "): ");
            String tarif = scanner.nextLine();
            if (!tarif.isBlank()) c.setTarif(Double.parseDouble(tarif));

            System.out.print("Finalizata (true/false) (" + c.isFinalizata() + "): ");
            String fin = scanner.nextLine();
            if (!fin.isBlank()) c.setFinalizata(Boolean.parseBoolean(fin));

            if (c.isFinalizata()) {
                c.setDataFinalizare(LocalDateTime.now());
            }

            boolean updated = service.updateCursa(c);
            System.out.println(updated ? "Cursa actualizata cu succes." : "Actualizarea a esuat.");
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        }
        audit.logAction("'Actualizare Cursa'");
    }

    private static void afiseazaVehicule() {
        audit.logAction("'Afisare Vehicule'");
        try {
            System.out.println("\n--- Lista Vehicule ---");
            List<Vehicul> vehicule = service.getVehicule();
            if (vehicule.isEmpty()) {
                System.out.println("Nu exista vehicule in sistem.");
            } else {
                vehicule.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea vehiculelor: " + e.getMessage());
        }
    }

    private static void curseActiveSofer() {
        audit.logAction("'Afisare curse active/sofer'");
        try {
            System.out.println("\n--- Curse Active pe sofer ---");
            System.out.print("ID sofer: ");
            int idSofer = Integer.parseInt(scanner.nextLine());

            Sofer sofer = service.findSoferById(idSofer);
            if (sofer == null) {
                System.out.println("sofer inexistent.");
                return;
            }

            List<Cursa> curse = service.getCurse().stream()
                    .filter(c -> c.getIdSofer() == idSofer && !c.isFinalizata())
                    .toList();

            if (curse.isEmpty()) {
                System.out.println("Nu exista curse active pentru acest sofer.");
            } else {
                System.out.println("Curse active pentru soferul cu ID " + idSofer + ":");
                curse.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea curselor active: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID invalid.");
        }
    }


    private static void curseCuTarife() {
        audit.logAction("'Afisare curse cu tarife'");
        try {
            System.out.println("\n--- Curse cu Tarife ---");
            List<Cursa> curse = service.getCurse();
            if (curse.isEmpty()) {
                System.out.println("Nu exista curse in sistem.");
                return;
            }
            for (Cursa c : curse) {
                System.out.println("Cursa ID " + c.getIdCursa() + " – Tarif: " + c.getTarif() + " RON");
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea curselor cu tarife: " + e.getMessage());
        }
    }

    private static void genereazaRaport() {
        try {
            System.out.println("\n--- Genereaza Raport ---");
            System.out.print("ID sofer pentru raport: ");
            int idSofer = Integer.parseInt(scanner.nextLine());

            Sofer sofer = service.findSoferById(idSofer);
            if (sofer == null) {
                System.out.println("Sofer inexistent.");
                return;
            }

            double totalVenit = service.getCurse().stream()
                    .filter(c -> c.getIdSofer() == idSofer && c.isFinalizata())
                    .mapToDouble(Cursa::getTarif)
                    .sum();

            Raport r = new Raport(idSofer, totalVenit);
            service.adaugaRaport(r);
            System.out.println("Raport generat pentru sofer ID " + idSofer +
                    " cu venit total " + totalVenit + " RON");
        } catch (SQLException e) {
            System.err.println("Eroare la generarea raportului: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID sofer invalid.");
        }
        audit.logAction("'Generare raport venit/sofer'");
    }


    private static void afiseazaRapoarte() {
        audit.logAction("'Afisare rapoarte'");
        try {
            System.out.println("\n--- Lista Rapoarte ---");
            List<Raport> rapoarte = service.getRapoarte();
            if (rapoarte.isEmpty()) {
                System.out.println("Nu exista rapoarte in sistem.");
            } else {
                rapoarte.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea rapoartelor: " + e.getMessage());
        }
    }

    private static void stergeRaport() {
        try {
            System.out.print("\nID Raport de sters: ");
            int id = Integer.parseInt(scanner.nextLine());

            Raport r = service.findByIdRaport(id);
            if (r == null) {
                System.out.println("Raportul nu exista.");
                return;
            }

            boolean deleted = service.stergeRaport(id);
            System.out.println(deleted ? "Raport sters cu succes." : "Eroare la stergere.");
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        }
        audit.logAction("'Stergere raport'");
    }

    private static void actualizeazaRaport() {
        try {
            System.out.print("\nID Raport de modificat: ");
            int id = Integer.parseInt(scanner.nextLine());

            Raport r = service.findByIdRaport(id);
            if (r == null) {
                System.out.println("Raportul nu exista.");
                return;
            }

            System.out.print("Venit total nou (" + r.getVenitTotal() + "): ");
            String venitNou = scanner.nextLine();
            if (!venitNou.isBlank()) {
                r.setVenitTotal(Double.parseDouble(venitNou));
            }

            boolean updated = service.updateRaport(r);
            System.out.println(updated ? "Raport actualizat cu succes." : "Actualizarea a esuat.");
        } catch (Exception e) {
            System.err.println("Eroare: " + e.getMessage());
        }
        audit.logAction("'Actualizare raport'");
    }

    private static void istoricCurseClientAdmin() {
        audit.logAction("'Vizualizare istoric curse client'");
        try {
            System.out.println("\n--- Istoric Curse Client (Admin) ---");
            System.out.print("ID Client pentru istoric: ");
            int idClient = Integer.parseInt(scanner.nextLine());

            List<Cursa> curseClient = service.getCurse().stream()
                    .filter(c -> {
                        try {
                            CerereTransport cerere = service.findByIdCerere(c.getIdCerere());
                            return cerere != null && cerere.getIdClient() == idClient;
                        } catch (SQLException e) {
                            return false;
                        }
                    })
                    .toList();

            if (curseClient.isEmpty()) {
                System.out.println("Clientul nu are curse.");
                return;
            }

            for (Cursa cursa : curseClient) {
                System.out.println(cursa);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la istoric curse client: " + e.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("ID Client invalid.");
        }
    }


    private static void statusCereri() {
        audit.logAction("'Vizualizare status cereri'");
        try {
            System.out.println("\n--- Status Cereri ---");
            List<CerereTransport> cereri = service.getCereri();
            if (cereri.isEmpty()) {
                System.out.println("Nu exista cereri in sistem.");
            } else {
                cereri.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea statusului cererilor: " + e.getMessage());
        }
    }

    private static void afiseazaFeedbackuri(String tip) throws SQLException {
        List<Feedback> lista = service.getFeedbackuri().stream()
                .filter(f -> f.getTip().equalsIgnoreCase(tip))
                .toList();

        if (lista.isEmpty()) {
            System.out.println("Nu exista feedback-uri pentru tipul: " + tip);
            return;
        }

        for (Feedback f : lista) {
            Utilizator sender = service.findByIdUtilizator(f.getIdSender());
            Utilizator receiver = service.findByIdUtilizator(f.getIdReceiver());

            String numeSender = sender != null ? sender.getNume() : "Necunoscut";
            String numeReceiver = receiver != null ? receiver.getNume() : "Necunoscut";

            System.out.printf("ID: %d | Cursa: %d | De la: %s → Catre: %s | Scor: %d | Comentariu: %s\n",
                    f.getIdFeedback(), f.getIdCursa(), numeSender, numeReceiver, f.getScor(), f.getComentariu());
        }
    }

    private static void afiseazaScorMediu() {
        audit.logAction("'Vizualizare scor utilizatori'");
        try {
            List<ScorUtilizatorDTO> lista = service.getScorMediuPeUtilizator();

            lista.sort(Comparator.comparingDouble(ScorUtilizatorDTO::getScorMediu).reversed());

            for (ScorUtilizatorDTO dto : lista) {
                System.out.printf("ID: %d | Nume: %s | Email: %s | Rol: %s | Scor Mediu: %.2f\n",
                        dto.getId(), dto.getNume(), dto.getEmail(), dto.getRol(), dto.getScorMediu());
            }
        } catch (SQLException e) {
            System.err.println("Eroare la afisarea scorurilor medii: " + e.getMessage());
        }
    }

}
