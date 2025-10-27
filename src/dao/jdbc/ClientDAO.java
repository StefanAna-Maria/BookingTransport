package dao.jdbc;

import model.utilizatori.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {
    private UtilizatorDAO utilDao = new UtilizatorDAO();

    // --- CREATE client: inseram mai intai in UTILIZATOR (rol=CLIENT), apoi in CLIENT ---
    public int create(Client c) throws SQLException {
        // 1) Inseram in UTILIZATOR
        int idUtil = utilDao.create(c);

        // 2) Inseram in CLIENT folosind id-ul obtinut
        String sql = "INSERT INTO CLIENT (id_client) VALUES (?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtil);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in CLIENT a esuat.");
            }
            return idUtil;
        }
    }

    // --- READ ALL client ---
    public List<Client> findAll() throws SQLException {
        List<Client> lista = new ArrayList<>();
        String sql = "SELECT U.id_utilizator, U.nume, U.email, U.telefon, U.username, U.parola " +
                "FROM UTILIZATOR U " +
                "JOIN CLIENT C ON U.id_utilizator = C.id_client";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id_utilizator"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("telefon"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Client findById(int idClient) throws SQLException {
        String sql = "SELECT U.id_utilizator, U.nume, U.email, U.telefon, U.username, U.parola " +
                "FROM UTILIZATOR U " +
                "JOIN CLIENT C ON U.id_utilizator = C.id_client " +
                "WHERE U.id_utilizator = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Client(
                            rs.getInt("id_utilizator"),
                            rs.getString("nume"),
                            rs.getString("email"),
                            rs.getString("telefon"),
                            rs.getString("username"),
                            rs.getString("parola")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE client: actualizeaza UTILIZATOR si implicit si CLIENT ---
    public boolean update(Client c) throws SQLException {
        // UPDATE pe UTILIZATOR (rolul ramane CLIENT)
        return utilDao.update(c);
    }

    // --- DELETE client ---
    public boolean delete(int idClient) throws SQLException {
        return utilDao.delete(idClient);
    }
}
