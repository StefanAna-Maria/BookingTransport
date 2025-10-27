package dao.jdbc;

import model.utilizatori.Sofer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoferDAO {
    private UtilizatorDAO utilDao = new UtilizatorDAO();

    // --- CREATE sofer ---
    public int create(Sofer s) throws SQLException {
        int idUtil = utilDao.create(s);
        String sql = "INSERT INTO SOFER (id_sofer) VALUES (?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtil);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in SOFER a esuat.");
            }
            return idUtil;
        }
    }

    // --- READ ALL soferi ---
    public List<Sofer> findAll() throws SQLException {
        List<Sofer> lista = new ArrayList<>();
        String sql = "SELECT U.id_utilizator, U.nume, U.email, U.telefon, U.username, U.parola " +
                "FROM UTILIZATOR U " +
                "JOIN SOFER S ON U.id_utilizator = S.id_sofer";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sofer s = new Sofer(
                        rs.getInt("id_utilizator"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("telefon"),
                        rs.getString("username"),
                        rs.getString("parola")
                );
                lista.add(s);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Sofer findById(int idSofer) throws SQLException {
        String sql = "SELECT U.id_utilizator, U.nume, U.email, U.telefon, U.username, U.parola " +
                "FROM UTILIZATOR U " +
                "JOIN SOFER S ON U.id_utilizator = S.id_sofer " +
                "WHERE U.id_utilizator = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idSofer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sofer(
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

    // --- UPDATE ---
    public boolean update(Sofer s) throws SQLException {
        return utilDao.update(s);
    }

    // --- DELETE ---
    public boolean delete(int idSofer) throws SQLException {
        return utilDao.delete(idSofer);
    }
}
