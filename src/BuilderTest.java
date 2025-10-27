import model.utilizatori.Utilizator;

public class BuilderTest {
    public static void main(String[] args) {
        Utilizator utilizator = new Utilizator.Builder()
                .idUtilizator(101)
                .nume("Stoica Ilinca")
                .email("ilinca@exemplu.com")
                .telefon("0712345678")
                .username("ilinca123")
                .parola("parola123")
                .rol("ADMIN")
                .build();

        System.out.println("=== TEST BUILDER ===");
        System.out.println("ID: " + utilizator.getIdUtilizator());
        System.out.println("Nume: " + utilizator.getNume());
        System.out.println("Email: " + utilizator.getEmail());
        System.out.println("Telefon: " + utilizator.getTelefon());
        System.out.println("Username: " + utilizator.getUsername());
        System.out.println("Parola: " + utilizator.getParola());
        System.out.println("Rol: " + utilizator.getRol());
    }
}
