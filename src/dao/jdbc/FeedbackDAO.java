package dao.jdbc;

import model.transport.Feedback;
import dto.ScorUtilizatorDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    // --- CREATE feedback ---
    public int create(Feedback f) throws SQLException {
        String sql = "INSERT INTO FEEDBACK (id_cursa, id_sender, id_receiver, scor, comentariu, tip) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getIdCursa());
            ps.setInt(2, f.getIdSender());
            ps.setInt(3, f.getIdReceiver());
            ps.setInt(4, f.getScor());
            ps.setString(5, f.getComentariu());
            ps.setString(6, f.getTip());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insertia in FEEDBACK a esuat.");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    f.setIdFeedback(id);
                    return id;
                } else {
                    throw new SQLException("Insertia in FEEDBACK a esuat, nu am generat ID.");
                }
            }
        }
    }

    // --- READ ALL feedback-uri ---
    public List<Feedback> findAll() throws SQLException {
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT id_feedback, id_cursa, id_sender, id_receiver, scor, comentariu, tip " +
                "FROM FEEDBACK";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Feedback f = new Feedback(
                        rs.getInt("id_feedback"),
                        rs.getInt("id_cursa"),
                        rs.getInt("id_sender"),
                        rs.getInt("id_receiver"),
                        rs.getInt("scor"),
                        rs.getString("comentariu"),
                        rs.getString("tip"),
                        LocalDateTime.now() // daca vrei sa adaugi un camp dataCreare, extinde constructorul
                );
                lista.add(f);
            }
        }
        return lista;
    }

    // --- FIND BY ID ---
    public Feedback findById(int idFeedback) throws SQLException {
        String sql = "SELECT id_feedback, id_cursa, id_sender, id_receiver, scor, comentariu, tip " +
                "FROM FEEDBACK WHERE id_feedback = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFeedback);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Feedback(
                            rs.getInt("id_feedback"),
                            rs.getInt("id_cursa"),
                            rs.getInt("id_sender"),
                            rs.getInt("id_receiver"),
                            rs.getInt("scor"),
                            rs.getString("comentariu"),
                            rs.getString("tip"),
                            LocalDateTime.now()
                    );
                } else {
                    return null;
                }
            }
        }
    }

    // --- UPDATE feedback ---
    public boolean update(Feedback f) throws SQLException {
        String sql = "UPDATE FEEDBACK SET scor = ?, comentariu = ?, tip = ? WHERE id_feedback = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, f.getScor());
            ps.setString(2, f.getComentariu());
            ps.setString(3, f.getTip());
            ps.setInt(4, f.getIdFeedback());

            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }

    // --- DELETE feedback ---
    public boolean delete(int idFeedback) throws SQLException {
        String sql = "DELETE FROM FEEDBACK WHERE id_feedback = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFeedback);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }


    // --- FIND BY ROL ---
    public List<Feedback> findByTip(String tip) throws SQLException {
        List<Feedback> lista = new ArrayList<>();
        String sql = "SELECT * FROM FEEDBACK WHERE tip = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tip);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Feedback f = new Feedback(
                            rs.getInt("id_feedback"),
                            rs.getInt("id_cursa"),
                            rs.getInt("id_sender"),
                            rs.getInt("id_receiver"),
                            rs.getInt("scor"),
                            rs.getString("comentariu"),
                            rs.getString("tip"),
                            LocalDateTime.now()
                    );
                    lista.add(f);
                }
            }
        }
        return lista;
    }

    // --- Scor Mediu ---
    public List<ScorUtilizatorDTO> getScorMediuPeUtilizator() throws SQLException {
        List<ScorUtilizatorDTO> lista = new ArrayList<>();
        String sql = """
        SELECT u.id_utilizator, u.nume, u.email, u.rol, AVG(f.scor) AS scor_mediu
        FROM utilizator u
        JOIN feedback f ON u.id_utilizator = f.id_receiver
        GROUP BY u.id_utilizator, u.nume, u.email, u.rol
        ORDER BY scor_mediu DESC
        """;

        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ScorUtilizatorDTO dto = new ScorUtilizatorDTO(
                        rs.getInt("id_utilizator"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("rol"),
                        rs.getDouble("scor_mediu")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

}
