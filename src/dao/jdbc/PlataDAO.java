package dao.jdbc;

import model.plati.Plata;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlataDAO {

    // --- CREATE plata ---
    public int create(Plata p) throws SQLException {
        String sql = "INSERT INTO PLATA (id_cursa, suma, metoda_plata) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdCursa());
            ps.setDouble(2, p.getSuma());
            ps.setString(3, p.getMetodaPlata());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in PLATA a esuat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    p.setIdPlata(id);
                    return id;
                } else {
                    throw new SQLException("Insertia in PLATA a esuat, nu s-a generat ID-ul.");
                }
            }
        }
    }

    // --- READ ALL plati ---
    public List<Plata> findAll() throws SQLException {
        List<Plata> lista = new ArrayList<>();
        String sql = "SELECT id_plata, id_cursa, suma, metoda_plata, data_plata FROM PLATA";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("data_plata");
                LocalDateTime dataPlata = (ts != null ? ts.toLocalDateTime() : null);

                Plata p = new Plata(
                        rs.getInt("id_plata"),
                        rs.getInt("id_cursa"),
                        rs.getDouble("suma"),
                        rs.getString("metoda_plata"),
                        dataPlata
                );
                lista.add(p);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Plata findById(int idPlata) throws SQLException {
        String sql = "SELECT id_plata, id_cursa, suma, metoda_plata, data_plata FROM PLATA WHERE id_plata = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPlata);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("data_plata");
                    LocalDateTime dataPlata = (ts != null ? ts.toLocalDateTime() : null);

                    return new Plata(
                            rs.getInt("id_plata"),
                            rs.getInt("id_cursa"),
                            rs.getDouble("suma"),
                            rs.getString("metoda_plata"),
                            dataPlata
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE plata ---
    public boolean update(Plata p) throws SQLException {
        String sql = "UPDATE PLATA SET suma = ?, metoda_plata = ? WHERE id_plata = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, p.getSuma());
            ps.setString(2, p.getMetodaPlata());
            ps.setInt(3, p.getIdPlata());
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // --- DELETE plata ---
    public boolean delete(int idPlata) throws SQLException {
        String sql = "DELETE FROM PLATA WHERE id_plata = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPlata);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
