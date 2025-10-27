package dao.jdbc;

import model.transport.Cursa;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CursaDAO {

    // --- CREATE cursa ---
    public int create(Cursa c) throws SQLException {
        String sql = "INSERT INTO CURSA " +
                "(id_cerere, id_sofer, id_vehicul, distanta_km, durata_minute, tarif, finalizata) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getIdCerere());
            ps.setInt(2, c.getIdSofer());
            ps.setInt(3, c.getIdVehicul());
            ps.setDouble(4, c.getDistantaKm());
            ps.setDouble(5, c.getDurataMinute());
            ps.setDouble(6, c.getTarif());
            ps.setBoolean(7, c.isFinalizata());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Crearea cursei a esuat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    c.setIdCursa(id);
                    return id;
                } else {
                    throw new SQLException("Crearea cursei a esuat, nu s-a generat ID-ul.");
                }
            }
        }
    }

    // --- READ ALL curse ---
    public List<Cursa> findAll() throws SQLException {
        List<Cursa> lista = new ArrayList<>();
        String sql = "SELECT id_cursa, id_cerere, id_sofer, id_vehicul, distanta_km, durata_minute, tarif, data_start, data_finalizare, finalizata " +
                "FROM CURSA";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp tsStart = rs.getTimestamp("data_start");
                LocalDateTime dataStart = tsStart.toLocalDateTime();

                Timestamp tsFinal = rs.getTimestamp("data_finalizare");
                LocalDateTime dataFinal = (tsFinal != null ? tsFinal.toLocalDateTime() : null);

                Cursa c = new Cursa(
                        rs.getInt("id_cursa"),
                        rs.getInt("id_cerere"),
                        rs.getInt("id_sofer"),
                        rs.getInt("id_vehicul"),
                        rs.getDouble("distanta_km"),
                        rs.getDouble("durata_minute"),
                        rs.getDouble("tarif"),
                        dataStart,
                        dataFinal,
                        rs.getBoolean("finalizata")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    // --- FIND BY CLIENT ID ---
    public List<Cursa> findByClientId(int idClient) throws SQLException {
        List<Cursa> lista = new ArrayList<>();
        String sql = """
        SELECT cu.*
        FROM CURSA cu
        JOIN CERERE_TRANSPORT ct ON cu.id_cerere = ct.id_cerere
        WHERE ct.id_client = ?
    """;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idClient);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp tsStart = rs.getTimestamp("data_start");
                    LocalDateTime dataStart = tsStart.toLocalDateTime();

                    Timestamp tsFinal = rs.getTimestamp("data_finalizare");
                    LocalDateTime dataFinal = (tsFinal != null ? tsFinal.toLocalDateTime() : null);

                    Cursa c = new Cursa(
                            rs.getInt("id_cursa"),
                            rs.getInt("id_cerere"),
                            rs.getInt("id_sofer"),
                            rs.getInt("id_vehicul"),
                            rs.getDouble("distanta_km"),
                            rs.getDouble("durata_minute"),
                            rs.getDouble("tarif"),
                            dataStart,
                            dataFinal,
                            rs.getBoolean("finalizata")
                    );
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Cursa findById(int idCursa) throws SQLException {
        String sql = "SELECT id_cursa, id_cerere, id_sofer, id_vehicul, distanta_km, durata_minute, tarif, data_start, data_finalizare, finalizata " +
                "FROM CURSA WHERE id_cursa = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCursa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp tsStart = rs.getTimestamp("data_start");
                    LocalDateTime dataStart = tsStart.toLocalDateTime();

                    Timestamp tsFinal = rs.getTimestamp("data_finalizare");
                    LocalDateTime dataFinal = (tsFinal != null ? tsFinal.toLocalDateTime() : null);

                    return new Cursa(
                            rs.getInt("id_cursa"),
                            rs.getInt("id_cerere"),
                            rs.getInt("id_sofer"),
                            rs.getInt("id_vehicul"),
                            rs.getDouble("distanta_km"),
                            rs.getDouble("durata_minute"),
                            rs.getDouble("tarif"),
                            dataStart,
                            dataFinal,
                            rs.getBoolean("finalizata")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE cursa ---
    public boolean update(Cursa c) throws SQLException {
        String sql = "UPDATE CURSA SET id_sofer = ?, id_vehicul = ?, distanta_km = ?, durata_minute = ?, tarif = ?, data_finalizare = ?, finalizata = ? " +
                "WHERE id_cursa = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, c.getIdSofer());
            ps.setInt(2, c.getIdVehicul());
            ps.setDouble(3, c.getDistantaKm());
            ps.setDouble(4, c.getDurataMinute());
            ps.setDouble(5, c.getTarif());
            if (c.getDataFinalizare() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(c.getDataFinalizare()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            ps.setBoolean(7, c.isFinalizata());
            ps.setInt(8, c.getIdCursa());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // --- DELETE cursa ---
    public boolean delete(int idCursa) throws SQLException {
        String sql = "DELETE FROM CURSA WHERE id_cursa = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCursa);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
