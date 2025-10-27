package dao.jdbc;

import model.plati.Raport;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RaportDAO {

    // --- CREATE raport ---
    public int create(Raport r) throws SQLException {
        String sql = "INSERT INTO RAPORT (id_sofer, venit_total) VALUES (?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, r.getIdSofer());
            ps.setDouble(2, r.getVenitTotal());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in RAPORT a esuat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    r.setIdRaport(id);
                    return id;
                } else {
                    throw new SQLException("Insertia in RAPORT a esuat, nu a generat ID-ul.");
                }
            }
        }
    }

    // --- READ ALL rapoarte ---
    public List<Raport> findAll() throws SQLException {
        List<Raport> lista = new ArrayList<>();
        String sql = "SELECT id_raport, id_sofer, venit_total, data_generare FROM RAPORT";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("data_generare");
                LocalDateTime dataGen = (ts != null ? ts.toLocalDateTime() : null);

                Raport r = new Raport(
                        rs.getInt("id_raport"),
                        rs.getInt("id_sofer"),
                        rs.getDouble("venit_total"),
                        dataGen
                );
                lista.add(r);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Raport findById(int idRaport) throws SQLException {
        String sql = "SELECT id_raport, id_sofer, venit_total, data_generare FROM RAPORT WHERE id_raport = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRaport);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("data_generare");
                    LocalDateTime dataGen = (ts != null ? ts.toLocalDateTime() : null);

                    return new Raport(
                            rs.getInt("id_raport"),
                            rs.getInt("id_sofer"),
                            rs.getDouble("venit_total"),
                            dataGen
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE raport ---
    public boolean update(Raport r) throws SQLException {
        String sql = "UPDATE RAPORT SET venit_total = ? WHERE id_raport = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, r.getVenitTotal());
            ps.setInt(2, r.getIdRaport());
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // --- DELETE raport ---
    public boolean delete(int idRaport) throws SQLException {
        String sql = "DELETE FROM RAPORT WHERE id_raport = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRaport);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
