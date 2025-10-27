package dao.jdbc;

import model.transport.Vehicul;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculDAO {

    // --- CREATE vehicul ---
    public int create(Vehicul v) throws SQLException {
        String sql = "INSERT INTO VEHICUL (marca, model, nr_inmatriculare, capacitate, viteza_medie, id_sofer) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModel());
            ps.setString(3, v.getNrInmatriculare());
            ps.setInt(4, v.getCapacitate());
            ps.setDouble(5, v.getVitezaMedie());
            ps.setInt(6, v.getIdSofer());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in VEHICUL a esuat, nu s-a creat niciun rand.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    v.setIdVehicul(id);
                    return id;
                } else {
                    throw new SQLException("Insertia in VEHICUL a esuat, nu am putut obtine ID-ul.");
                }
            }
        }
    }

    // --- READ ALL vehicule ---
    public List<Vehicul> findAll() throws SQLException {
        List<Vehicul> lista = new ArrayList<>();
        String sql = "SELECT id_vehicul, marca, model, nr_inmatriculare, capacitate, viteza_medie, id_sofer " +
                "FROM VEHICUL";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicul v = new Vehicul(
                        rs.getInt("id_vehicul"),
                        rs.getString("marca"),
                        rs.getString("model"),
                        rs.getString("nr_inmatriculare"),
                        rs.getInt("capacitate"),
                        rs.getDouble("viteza_medie"),
                        rs.getInt("id_sofer")
                );
                lista.add(v);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Vehicul findById(int idVehicul) throws SQLException {
        String sql = "SELECT id_vehicul, marca, model, nr_inmatriculare, capacitate, viteza_medie, id_sofer " +
                "FROM VEHICUL WHERE id_vehicul = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehicul);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehicul(
                            rs.getInt("id_vehicul"),
                            rs.getString("marca"),
                            rs.getString("model"),
                            rs.getString("nr_inmatriculare"),
                            rs.getInt("capacitate"),
                            rs.getDouble("viteza_medie"),
                            rs.getInt("id_sofer")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE ---
    public boolean update(Vehicul v) throws SQLException {
        String sql = "UPDATE VEHICUL SET marca = ?, model = ?, nr_inmatriculare = ?, capacitate = ?, viteza_medie = ?, id_sofer = ? " +
                "WHERE id_vehicul = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getMarca());
            ps.setString(2, v.getModel());
            ps.setString(3, v.getNrInmatriculare());
            ps.setInt(4, v.getCapacitate());
            ps.setDouble(5, v.getVitezaMedie());
            ps.setInt(6, v.getIdSofer());
            ps.setInt(7, v.getIdVehicul());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // --- DELETE ---
    public boolean delete(int idVehicul) throws SQLException {
        String sql = "DELETE FROM VEHICUL WHERE id_vehicul = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehicul);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
}
