import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/booking_transport";
        String user = "root";
        String password = "RoCo5000@B!";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexiune reusita!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("Driverul nu a fost gasit!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare la conectare!");
            e.printStackTrace();
        }
    }
}