package dao.jdbc;

import model.transport.CerereTransport;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CerereTransportDAO {

    // --- CREATE cerere ---
    public int create(CerereTransport ct) throws SQLException {
        String sql = "INSERT INTO CERERE_TRANSPORT (id_client, destinatie, distanta_km, status) " +
                "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, ct.getIdClient());
            ps.setString(2, ct.getDestinatie());
            ps.setDouble(3, ct.getDistantaKm());
            ps.setString(4, ct.getStatus());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in CERERE_TRANSPORT a esuat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ct.setIdCerere(id);
                    return id;
                } else {
                    throw new SQLException("Insertia in CERERE_TRANSPORT a esuat, fara generarea ID-ului.");
                }
            }
        }
    }

    // --- READ ALL cereri ---
    public List<CerereTransport> findAll() throws SQLException {
        List<CerereTransport> lista = new ArrayList<>();
        String sql = "SELECT id_cerere, id_client, destinatie, distanta_km, data_creare, status " +
                "FROM CERERE_TRANSPORT";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("data_creare");
                LocalDateTime dataCreare = ts.toLocalDateTime();

                CerereTransport ct = new CerereTransport(
                        rs.getInt("id_cerere"),
                        rs.getInt("id_client"),
                        rs.getString("destinatie"),
                        rs.getDouble("distanta_km"),
                        dataCreare,
                        rs.getString("status")
                );
                lista.add(ct);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public CerereTransport findById(int idCerere) throws SQLException {
        String sql = "SELECT id_cerere, id_client, destinatie, distanta_km, data_creare, status " +
                "FROM CERERE_TRANSPORT WHERE id_cerere = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCerere);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("data_creare");
                    LocalDateTime dataCreare = ts.toLocalDateTime();
                    return new CerereTransport(
                            rs.getInt("id_cerere"),
                            rs.getInt("id_client"),
                            rs.getString("destinatie"),
                            rs.getDouble("distanta_km"),
                            dataCreare,
                            rs.getString("status")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE ---
    public boolean update(CerereTransport ct) throws SQLException {
        String sql = "UPDATE CERERE_TRANSPORT SET destinatie = ?, distanta_km = ?, status = ? " +
                "WHERE id_cerere = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ct.getDestinatie());
            ps.setDouble(2, ct.getDistantaKm());
            ps.setString(3, ct.getStatus());
            ps.setInt(4, ct.getIdCerere());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // --- DELETE ---
    public boolean delete(int idCerere) throws SQLException {
        String sql = "DELETE FROM CERERE_TRANSPORT WHERE id_cerere = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCerere);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
