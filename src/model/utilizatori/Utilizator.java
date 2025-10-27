package model.utilizatori;

public class Utilizator {
    private int idUtilizator;
    private String nume;
    private String email;
    private String telefon;
    private String username;
    private String parola;
    private String rol;

    public Utilizator() {}

    public Utilizator(String nume, String email, String telefon,
                      String username, String parola, String rol) {
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
        this.username = username;
        this.parola = parola;
        this.rol = rol;
    }

    public Utilizator(int idUtilizator, String nume, String email, String telefon,
                      String username, String parola, String rol) {
        this.idUtilizator = idUtilizator;
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
        this.username = username;
        this.parola = parola;
        this.rol = rol;
    }

    private Utilizator(Builder builder) {
        this.idUtilizator = builder.idUtilizator;
        this.nume = builder.nume;
        this.email = builder.email;
        this.telefon = builder.telefon;
        this.username = builder.username;
        this.parola = builder.parola;
        this.rol = builder.rol;
    }

    // Builder Pattern:

    public static class Builder {
        private int idUtilizator;
        private String nume;
        private String email;
        private String telefon;
        private String username;
        private String parola;
        private String rol;

        public Builder idUtilizator(int idUtilizator) {
            this.idUtilizator = idUtilizator;
            return this;
        }

        public Builder nume(String nume) {
            this.nume = nume;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder telefon(String telefon) {
            this.telefon = telefon;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder parola(String parola) {
            this.parola = parola;
            return this;
        }

        public Builder rol(String rol) {
            this.rol = rol;
            return this;
        }

        public Utilizator build() {
            return new Utilizator(this);
        }
    }

    public int getIdUtilizator() { return idUtilizator; }
    public void setIdUtilizator(int idUtilizator) { this.idUtilizator = idUtilizator; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getParola() { return parola; }
    public void setParola(String parola) { this.parola = parola; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}
