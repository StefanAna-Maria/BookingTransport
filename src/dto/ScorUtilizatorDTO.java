package dto;

public class ScorUtilizatorDTO {
    private int id;
    private String nume;
    private String email;
    private String rol;
    private double scorMediu;

    public ScorUtilizatorDTO(int id, String nume, String email, String rol, double scorMediu) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.rol = rol;
        this.scorMediu = scorMediu;
    }

    public int getId() {
        return id;
    }

    public String getNume() {
        return nume;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public double getScorMediu() {
        return scorMediu;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setScorMediu(double scorMediu) {
        this.scorMediu = scorMediu;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Nume: %s | Email: %s | Rol: %s | Scor Mediu: %.2f",
                id, nume, email, rol, scorMediu);
    }
}
