package dao.jdbc;

import model.utilizatori.Utilizator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilizatorDAO {

    // --- CREATE ---
    public int create(Utilizator u) throws SQLException {
        String sql = "INSERT INTO UTILIZATOR (nume, email, telefon, username, parola, rol) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNume());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefon());
            ps.setString(4, u.getUsername());
            ps.setString(5, u.getParola());
            ps.setString(6, u.getRol());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Crearea utilizatorului a esuat, nu a fost niciun rand inserat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    u.setIdUtilizator(id);
                    return id;
                } else {
                    throw new SQLException("Crearea utilizatorului a esuat, nu s-a putut obtine ID-ul.");
                }
            }
        }
    }

    // --- READ username (pt autentificare) ---
    public Utilizator findByUsername(String username) throws SQLException {
        String sql = "SELECT id_utilizator, nume, email, telefon, username, parola, rol " +
                "FROM UTILIZATOR WHERE username = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilizator.Builder()
                            .idUtilizator(rs.getInt("id_utilizator"))
                            .nume(rs.getString("nume"))
                            .email(rs.getString("email"))
                            .telefon(rs.getString("telefon"))
                            .username(rs.getString("username"))
                            .parola(rs.getString("parola"))
                            .rol(rs.getString("rol"))
                            .build();
                } else {
                    return null;
                }
            }
        }
    }

    // --- READ ALL ---
    public List<Utilizator> findAll() throws SQLException {
        List<Utilizator> lista = new ArrayList<>();
        String sql = "SELECT id_utilizator, nume, email, telefon, username, parola, rol FROM UTILIZATOR";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Utilizator u = new Utilizator.Builder()
                        .idUtilizator(rs.getInt("id_utilizator"))
                        .nume(rs.getString("nume"))
                        .email(rs.getString("email"))
                        .telefon(rs.getString("telefon"))
                        .username(rs.getString("username"))
                        .parola(rs.getString("parola"))
                        .rol(rs.getString("rol"))
                        .build();
                lista.add(u);
            }
        }
        return lista;
    }


    // --- FIND BY ID---

    public Utilizator findById(int id) throws SQLException {
        String sql = "SELECT * FROM UTILIZATOR WHERE id_utilizator = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Utilizator.Builder()
                            .idUtilizator(rs.getInt("id_utilizator"))
                            .nume(rs.getString("nume"))
                            .email(rs.getString("email"))
                            .telefon(rs.getString("telefon"))
                            .username(rs.getString("username"))
                            .parola(rs.getString("parola"))
                            .rol(rs.getString("rol"))
                            .build();
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE ---
    public boolean update(Utilizator u) throws SQLException {
        String sql = "UPDATE UTILIZATOR SET nume = ?, email = ?, telefon = ?, username = ?, parola = ?, rol = ? " +
                "WHERE id_utilizator = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNume());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefon());
            ps.setString(4, u.getUsername());
            ps.setString(5, u.getParola());
            ps.setString(6, u.getRol());
            ps.setInt(7, u.getIdUtilizator());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // --- DELETE ---
    public boolean delete(int idUtilizator) throws SQLException {
        String sql = "DELETE FROM UTILIZATOR WHERE id_utilizator = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUtilizator);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
}
